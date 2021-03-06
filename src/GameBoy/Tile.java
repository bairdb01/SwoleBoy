package GameBoy;

import java.util.concurrent.ExecutionException;

/**
 * Author: Benjamin Baird
 * Created on: 2019-02-18
 * Last Updated on: 2019-02-18
 * Filename: Tile
 * Description: This class represents a background/window tile from VRAM.
 */
public class Tile {
    int height = 8;
    int width = 8;
    int CHR_CODE;
    int [][] bitmap;
    short palette;

    public Tile(){
        this.CHR_CODE = -1;
        this.bitmap = new int[height][width];
        palette = (short) 0xFF47;
    }

    public Tile(int CHR_CODE) {
        this.CHR_CODE = CHR_CODE;
        this.bitmap = new int[height][width];
        palette = (short) 0xFF47;
    }

    /**
     * Takes in an array of bytes and pairs them to create colour codes
     * Left most pixel pair is stored in left most bits.
     *
     * @param data An even sized array of bytes
     */
    public void setBitmap(byte [] data) {
        for (int i = 0; i < data.length; i+=2) {
            int row = i / 2;
            for (int j = 0; j < 8; j++) {
                int shift = (7 - j);
                bitmap[row][j] = ((data[i] >> (shift)) & 0b1) << 1;
                bitmap[row][j] += ((data[i + 1] >> shift) & 0b1);
            }
        }
    }

    public byte getPixel(int row, int col){
        return (byte)(bitmap[row][col]);
    }

}
