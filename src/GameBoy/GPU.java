package GameBoy;

import javax.swing.*;

import static GameBoy.Emulator.mmu;

/**
 * Author: Benjamin Baird
 * Date: 2019-01-16
 * Date Created: 2019-01-03
 * Description: The GPU of the GameBoy handles all processing of graphics.
 * Resolution of 160 x 144, with 144 visible scanlines and 8 invisible scanlines used during the V-Blank period.
 * The current scanline is stored in adr 0xFF44.
 * It takes 456 cpu clock cycles to draw one scanline
 * TODO Change Tile/Sprites to load only the needed row of it's bitmap, for better efficiency.
 */
public class GPU {
    JFrame window = new JFrame("SwoleBoy");

    Screen screen = new Screen(160, 144);

    int lcdc = 0xFF40; /* LCD control register
                    Bit 0:
                    CGB Mode: BG display always on
                    DMG Mode:   0: BG display off
                                1: BG display on
                    Bit 1:
                    OBJ On Flag
                                0: Off
                                1: On
                    Bit 2:
                    OBJ Block Composition Selection Flag
                                0: 8 x 8 dots
                                1: 8 x 16 dots
                    Bit 3:
                    BG Code Area Selection Flag
                                0: 9800h-9BFFh
                                1: 9C00h-9FFFh
                    Bit 4:
                    BG Character Data Selection Flag
                                0: 8800h-97FFh
                                1: 8000h-8FFFh
                    Bit 5:
                    Windowing On Flag
                                0: Off
                                1: On
                    Bit 6:
                    Window Code Area Selection Flag
                                0: 9800h-9BFFh
                                1: 9C00h-9FFFh
                    Bit 7:
                    LCD Controller Operation Stop Flag
                                0: LCDC Off (OFF during v-blank)
                                1: LCDC On
                     */
    int stat = 0xFF41; /* LCD Status flag
                    Bit 0, 1: Mode Flag
                            00: Enable CPU Access to all Display RAM (H-Blank period)
                            01: In vertical blanking period (V-Blank period)
                            10: Searching OAM RAM (OAM being used by LCD controller, inaccessible to CPU)
                            11: Transferring data to LCD Driver  (LCD is using 0AM [FE00 - FE90] and [8000-9FFF], CPU cannot access these areas)

                    Bit 2: Match Flag
                            0: LYC = LCDC LY
                            1: LYC = LCDC LY

                    Bit 3, 4, 5, 6: Interrupt Selection According to LCD Status
                            Mode 00 Selection
                            Mode 01 Selection, 0: not selected
                            Mode 10 Selection, 1: selected
                            LYC = LY matching selection
                     */
    int scroll_y = 0xFF42; // Scroll Y (00 - FF) top location of window on background map
    int scroll_x = 0xFF43; // Scroll X (00 - FF) left location of window on background map
    int ly = 0xFF44; // LCDC y-coordinate. 0 - 153 (144 - 153 represent V-Blank period) (Current scanline)

    // Writing a value of 0 to bit 7 of the CDC reg when its value is 1 stops the LCD controller and LY becomes 0
    int lyc = 0xFF45; // Register LYC is compared with register ly. If they match, the matchflag of the stat register is set.
    int bgp = 0xFF47; /* BG Palette Data
                                Bit 0,1: Data for dot data 00
                                Bit 2,3: Data for dot data 01
                                Bit 4,5: Data for dot data 10
                                Bit 6,7: Data for dot data 11
                                */
    int obp0 = 0xFF48; // OBG Palette Data 0 (bit usage same as bgp), when value of OAM palette selection flag is 0
    int obp1 = 0xFF49; // OBJ Palette Data 1 (bit usage same as bgp), when value of OAM palette selection flag is 1

    int wy = 0xFF4A; // Window y-coordinate 0 <= WY <= 143, window is displayed from the top edge
    int wx = 0xFF4B; // Window x-coordinate 7 <= WX <= 166, window is displayed from the left edge

    int dma = 0xFF46; // DMA Transfer and starting address

    int bg_data_0 = 0x9800; // ($9800 - $9BFF) for BG map 0
    int bg_data_1 = 0x9C00; // ($9C00 - $9FFF) for Bg map 1

    Pixel[][] mainScreenPixels = new Pixel[144][160];


    public GPU() {
        window.getContentPane().add(screen);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(160, 144);
        window.setVisible(true);

        for (int i = 0; i < 144; i++) {
            for (int j = 0; j < 160; j++) {
                mainScreenPixels[i][j] = new Pixel();
            }
        }
    }

    int clockCounter = 0; // Keeps track of the number of CPU cycles passed, to keep CPU/GPU in sync

    public boolean isLCDEnabled() {
        return BitUtils.testBit(mmu.getMemVal(this.lcdc), 7);
    }

    /**
     * Performs a batch update of the graphics after every 456 CPU cycles.
     *
     * @param cycles The number of CPU cycles of the last OP code performed
     */
    public void updateGraphics(int cycles) {
        updateLCDStatus();

        if (isLCDEnabled()) {
            clockCounter += cycles;
        } else {
            // No need to do anything if LCD isn't enabled
            return;
        }

        // It takes the GPU 456 CPU cycles to draw one scanline. Don't draw anything if "GPU" isn't ready.
        if (clockCounter >= 456) {
            clockCounter = 0;

            // Scanline increments before drawing a scanline
            mmu.incScanline();

            // Move to next scanline
            int curScanline = mmu.getMemVal(this.ly) & 0xFF;

            // V-Blank period, send interrupt
            if (curScanline == 144) {
                Interrupts.requestInterrupt(mmu, new Interrupt("V-Blank", "GPU", 0));

            } else if (curScanline > 153) {
                // Finished all scanlines, reset
                mmu.setMemVal(this.ly, (byte) 0);

            } else if (curScanline < 144) {
                // Draw visible scanline
                drawScanline();
            }
        }
    }

    /**
     * Sets the mode of LCD status register to val, if this changes the mode
     * an interrupt will be attempted.
     *
     * @param mode Mode to change the lcd to ( 0 - 3)
     */
    public void setLCDMode(byte mode) {
        byte lcdStatus = mmu.getMemVal(this.stat);
        byte lcdMode = (byte) (lcdStatus & 0x3);

        if (lcdMode != mode) {
            // When LCD status changes to 0, 1, or 2 an LCD interrupt Request can happen, set bits in LCD stat
            if (mode == 0) {
                if (BitUtils.testBit(lcdStatus, 3)) {
                    Interrupts.requestInterrupt(mmu, new Interrupt("LCD Interrupt", "Switched to mode 0 (H-Blank)", 1));
                }
                mmu.setMemVal(this.stat, (byte) (((lcdStatus & 0x4) + 0xB)));
            } else if (mode == 1) {
                if (BitUtils.testBit(lcdStatus, 4)) {
                    Interrupts.requestInterrupt(mmu, new Interrupt("LCD Interrupt", "Switched to mode 1 (V-Blank)", 1));
                }
                mmu.setMemVal(this.stat, (byte) (((lcdStatus & 0x4) + 0x11)));
            } else if (mode == 2) {
                if (BitUtils.testBit(lcdStatus, 5)) {
                    Interrupts.requestInterrupt(mmu, new Interrupt("LCD Interrupt", "Switched to Mode 2 (OAM)", 1));
                }
                mmu.setMemVal(this.stat, (byte) (((lcdStatus & 0x4) + 0x23)));
            } else if (mode == 3) {
                mmu.setMemVal(this.stat, (byte) (lcdStatus & 0xFC + 3));
            }
        }
    }

    /**
     * Updates the LCD status register and requests interrupts if applicable.
     *
     */
    private void updateLCDStatus() {
        // If LCD is disabled, mode must be 1, clock cycle counter and current scanline must be reset
        if (!isLCDEnabled()) {
            setLCDMode((byte) 1);
            clockCounter = 0;
            mmu.setMemVal(this.ly, (byte) 0);
            return;
        }
        int curScanline = (mmu.getMemVal(ly) & 0xFF);
        if ( curScanline >= 144) {
            setLCDMode((byte) 1);
        }

        // Sets the mode of the LCD status register
        int mode2Length = 80;
        int mode3Length = 172;
        if (clockCounter < mode2Length) {
            // Searching OAM RAM (OAM being used by LCD controller, inaccessible to CPU) (Mode 2)
            setLCDMode((byte) 2);
        } else if (clockCounter < (mode2Length + mode3Length)) {
            // Transferring data to LCD driver. (Mode 3)
            setLCDMode((byte) 3);
        } else {
            // Enable CPU access to all display RAM (H-Blank period)
            setLCDMode((byte) 0);
        }

        // Perform coincidence check (ly = lyc)
        LCDCoincidenceCheck();
    }

    /**
     * Checks registers 0xFF44(ly) and 0xFF45(lyc) to see if they are the same.
     * lyc is the scanline the game is interested in and ly is the current scanline.
     * When they are the same, special effects
     * could be performed by the game.
     * Sets corresponding bit in lcd status register.
     * Interrupt request if applicable.
     *
     */
    private void LCDCoincidenceCheck() {
        byte lcdStatus = mmu.getMemVal(this.stat);

        // Bit 2 of stat is set to 1 if 0xFF44 == 0xFF45, else set to 0
        byte curScanline = mmu.getMemVal(this.ly);
        boolean coincidenceFlag = (curScanline == mmu.getMemVal(this.lyc));

        if (coincidenceFlag) {
            // Request interrupt
            mmu.setMemVal(this.stat, (byte) ((lcdStatus & 0xFB) + 4));
            if (BitUtils.testBit(lcdStatus, 6)) {
                Interrupts.requestInterrupt(mmu, new Interrupt("LCD Interrupt", "Coincidence (0xFF44 == 0xFF45)", 1));
            }
        } else {
            // Clear coincidence flag
            mmu.setMemVal(this.stat, (byte) ((lcdStatus & 0xFB)));
        }
    }

    /**
     * Draws one row of pixels to the screen.
     *
     */
    public void drawScanline() {
        byte lcdControl = mmu.getMemVal(this.lcdc);

        // Draw background/window tiles
        if (BitUtils.testBit(lcdControl, 0)) {
            renderTiles(lcdControl);
        }

        // Draw sprites
        if (BitUtils.testBit(lcdControl, 1)) {
            renderSprites(lcdControl);
        }
        int curScanline = mmu.getMemVal(this.ly) & 0xFF;
        screen.renderScreen(mainScreenPixels[curScanline], curScanline);
    }

    /**
     * Renders the background/window tiles (8x8 pixels).
     * TODO: Take into account window_y and window_x.
     *
     * @param lcdControl The LCD control register's value
     */
    public void renderTiles(byte lcdControl) {

        // Upper left starting position of the background to be displayed
        byte scrollX = mmu.getMemVal(this.scroll_x);
        byte scrollY = mmu.getMemVal(this.scroll_y);

        // X,Y positions of the window area to start drawing the window from
        byte windowY = mmu.getMemVal(this.wy);
        byte windowX = (byte)(mmu.getMemVal(this.wx) - 7);      // value of wx is offset by 7 to allow scrolling in, 7 <= windowX <=166

        // Grab the colour palette
        byte bgPalette = mmu.getMemVal(this.bgp);
        byte[] palette = new byte[4];
        for (int i = 0; i <= 6; i += 2) {
            // Shift over i bits and read 2 bits for colour
            int colourBits = bgPalette >> i;
            palette[i / 2] = (byte) (colourBits & 0x3);
        }

        // Check if loading normal Background Tiles or window background tiles
        int bgDataAdr;
        int winDataAdr = bg_data_0;
        boolean usingWindow = false;
        if (BitUtils.testBit(lcdControl, 5)) {
            usingWindow = true;
            // Window tiles needed
            if (BitUtils.testBit(lcdControl, 6)) {
                winDataAdr = bg_data_1;
            } else {
                winDataAdr = bg_data_0;
            }
        }

        // Only Background tiles needed
        if (BitUtils.testBit(lcdControl, 3)) {
            bgDataAdr = bg_data_1;
        } else {
            bgDataAdr = bg_data_0;
        }

        // Check where the Bitmap/tile data is located
        int tileMapAdr;
        boolean signed = false;
        if (BitUtils.testBit(lcdControl, 4)) {
            tileMapAdr = 0x8000;
        } else {
            tileMapAdr = 0x8800;
            signed = true;
        }

        // Draw tiles at the current scanline (LY)
        Tile tile;
        int curScanline = mmu.getMemVal(this.ly) & 0xFF;
        int curRow = (curScanline) / 8; // Account for the block size of 8 (18 blocks)
        for (int curCol = 0; curCol < 20; curCol++) {
            // Find current block/tile
            int blockX = (scrollX / 8 + curCol) % 32;
            int blockY = (32 * (scrollY / 8 + curRow));

            // Check if current pixel is for a window
            int tileDataAdr;
            if (usingWindow && curScanline >= windowY && (windowX) >= curCol * 8) {
                // Within window area
//                blockX = curCol*8 - windowX;
//                blockY = (curScanline - windowY)/8;
                tileDataAdr = winDataAdr;
            } else {
                tileDataAdr = bgDataAdr;
            }

            int blockNum = blockY + blockX;

            // Load CHR_CODE
            int chrCode = mmu.getMemVal(tileDataAdr + blockNum) & 0xFF;
            tile = new Tile(chrCode);

            // Check if CHR_CODE will be signed
            if (signed) {
                chrCode += 128;
            }
            int tileAdr = (tileMapAdr + chrCode * 16);

            // Load Bitmap from tile address
            byte[] bitmap = new byte[16];  // 16 because 2 bytes create 1 row. 16/2 = 8 rows
            for (int i = 0; i < 16; i++) {
                bitmap[i] = mmu.getMemVal((tileAdr + i));
            }
            tile.setBitmap(bitmap);

            // Tile is ready to be drawn in it's 8x8 location
            drawTile(tile, curScanline, curCol * 8, palette, false);
        }
    }

    /**
     * Renders a row of sprites onto the LCD screen.
     * TODO: Horizontal and Vertical flip flags
     * @param lcdControl The LCDC register's value.
     */
    public void renderSprites(byte lcdControl) {
        int height = BitUtils.testBit(lcdControl, 2)? 16 : 8;   // LCDC Bit 2 - OBJ (Sprite) Size (0=8x8, 1=8x16)
        byte curScanline = mmu.getMemVal(this.ly);

        for (int spriteNumber = 0; spriteNumber < 40; spriteNumber++) {
            // All 40 sprites are stored at 0xFE00 to 0xFE9F and consist of 4 bytes
            int spriteAdr = 0xFE00 + (spriteNumber * 4);

            // Each sprite consists of 4 bytes
            byte y_coord = mmu.getMemVal((spriteAdr));                               // Byte 1: LCD y_coordinate
            byte x_coord = mmu.getMemVal((spriteAdr + 1));                           // Byte 2: LCD x_coordinate
            byte chr_code = (byte) ((mmu.getMemVal((spriteAdr + 2)) >> 1) << 1);    // Byte 3: CHR_CODE or tile code. Odd CHR_CODES get rounded down. 1->0. 3->2.
            byte attributes = mmu.getMemVal((spriteAdr + 3));                        // Byte 4: Attribute flag - Palette, Horizontal/Vertical Flip Flag, and Priority
            int paletteSelection = (BitUtils.testBit(attributes, 4)) ? this.obp1 : this.obp0;
            // Check for vertical flip
            boolean yFlip = (BitUtils.testBit(attributes, 6));

            // Check for horizontal flip
            boolean xFlip = (BitUtils.testBit(attributes, 5));

            // Grab the colour palette
            byte objPalette = mmu.getMemVal(paletteSelection);
            byte[] palette = new byte[4];
            for (int i = 0; i <= 6; i += 2) {
                // Shift over i bits and read 2 bits for colour
                int colourBits = objPalette >> i;
                palette[i / 2] = (byte) (colourBits & 0x3);
            }

            // Load Bitmap from sprite address, CHR_CODE, and flip flags

            byte [] bitmap = new byte[height * 2];
            int tileAdr = (0x8000 + chr_code * 16);
            for (int i = 0; i < (2 * height); i++) { // Account of 8x8 or 8x16 sprites
                int row = i;
                if (yFlip) {
                    row = height - i;
                }

                bitmap[i] = mmu.getMemVal((tileAdr + row));

                if (xFlip) {
                    bitmap[i] = BitUtils.reverseByte(bitmap[i]);
                }
            }

            Sprite sprite = new Sprite(height, y_coord, x_coord, chr_code, attributes);
            sprite.setBitmap(bitmap);

            // Draw sprite
            if (y_coord <= curScanline && (y_coord + height) > curScanline) {
                drawTile(sprite, curScanline, x_coord, palette, true);
            }
        }
    }

    /**
     * Draws a tile to specified row.
     *
     * @param t The tile to draw a row from.
     * @param row The row to draw to
     * @param col The position of the tile within the row.
     */
    public void drawTile(Tile t, int row, int col, byte[] palette, boolean isSprite) {
        for (int i = 0; i < 8; i++) {
            byte colour = palette[t.getPixel(row % t.height, i)];
            if (isSprite && colour == 0) {
                continue;
            }
            mainScreenPixels[row][col + i].colour = colour;
        }
    }

}

