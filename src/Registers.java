/**
 * Author: Benjamin Baird
 * Created on: 2018-08-28
 * Last Updated on: 2018-08-28
 * Filename: Registers
 * Description: GameBoy registers (excluding flags) and functions to manage them.
 */
public class Registers {
    private int a, b, c, d, e, f, h, l; // Registers A, B, C, D, E, F, H, L (8 bit)
    // AF, BC, DE, HL pairings enable 16bit registers
    private int sp, PC;          // SP, PC (16 bit) registers
    private int program_counter = 0x100;    // Used by program instructions

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getProgram_counter() {
        return program_counter;
    }

    public void setProgram_counter(int program_counter) {
        this.program_counter = program_counter;
    }
}
