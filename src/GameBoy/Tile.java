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
        palette = (short) 0xFF48;
    }

    public Tile(int CHR_CODE) {
        this.CHR_CODE = CHR_CODE;
        this.bitmap = new int[height][width];
        palette = (short) 0xFF48;
    }

    /**
     * Takes in an array of bytes and pairs them to create colour codes
     * @param data An even sized array of bytes
     */
    public void setBitmap(byte [] data) {
        for (int i = 0; i < data.length; i+=2) {
            for (int j = 0; j < 8; j++) {
                bitmap[i / 2][j] = ((data[i + 1] >> (j - 1)) & 0b10); // Set upper bit of colour
                bitmap[i / 2][j] += ((data[i] >> j) & 0b01);    // Set lower bit of colour
            }

        }
    }

    public byte getPixel(int row, int col){
        return (byte)(bitmap[row][col]);
    }

}
