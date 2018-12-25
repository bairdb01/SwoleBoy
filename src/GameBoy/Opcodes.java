package GameBoy;

/**
 * Author: Benjamin Baird
 * Created on: 2018-08-28
 * Last Updated on: 2018-08-28
 * Filename: OPCODES
 * Description: Various GameBoy.Commands and their related OPCODES
 * Each Opcode corresponds to a specific instruction.
 * TODO: MISSING FUNCTIONS
 * TODO: FLAG FUNCTIONS
 * TODO: WRITE DEDICATED XOR, OR, AND functions
 * TODO: Figure out how to handle CB prefix opcode. (Solution? - Bitclear the CB out OPCODE &= ~(CB << 2) and use as index)
 * TODO: Lookup how results are stored for opcodes. Probably in the stack.
 */
public class Opcodes {

    final private Instructions[] std_opcodes = new Instructions[0x100];  // 0xFF is highest opcode.
    // Using array because we know the exact Opcode,
    // therefore faster random access.

    final private Instructions[] cb_opcodes = new Instructions[0x100]; // GameBoy.Opcodes which have the CB prefix
    // CB prefix just means to use this table, then is thrown out

    public Opcodes() {
        /***
         * 8-BIT LOADS
         */
        setOpCode(std_opcodes, "LD B,n", 0x06, 8, (regs, memory, args) -> regs.setB((byte) args[0]));
        setOpCode(std_opcodes, "LD C,n", 0x0E, 8, (regs, memory, args) -> regs.setC((byte) args[0]));
        setOpCode(std_opcodes, "LD D,n", 0x16, 8, (regs, memory, args) -> regs.setD((byte) args[0]));
        setOpCode(std_opcodes, "LD E,n", 0x1E, 8, (regs, memory, args) -> regs.setE((byte) args[0]));
        setOpCode(std_opcodes, "LD H,n", 0x26, 8, (regs, memory, args) -> regs.setH((byte) args[0]));
        setOpCode(std_opcodes, "LD L,n", 0x2E, 8, (regs, memory, args) -> regs.setL((byte) args[0]));

        //LD INTO A
        setOpCode(std_opcodes, "LD A,A", 0x7F, 4, (regs, memory, args) -> regs.setA(regs.getA()));
        setOpCode(std_opcodes, "LD A,B", 0x78, 4, (regs, memory, args) -> regs.setA(regs.getB()));
        setOpCode(std_opcodes, "LD A,C", 0x79, 4, (regs, memory, args) -> regs.setA(regs.getC()));
        setOpCode(std_opcodes, "LD A,D", 0x7A, 4, (regs, memory, args) -> regs.setA(regs.getD()));
        setOpCode(std_opcodes, "LD A,E", 0x7B, 4, (regs, memory, args) -> regs.setA(regs.getE()));
        setOpCode(std_opcodes, "LD A,H", 0x7C, 4, (regs, memory, args) -> regs.setA(regs.getH()));
        setOpCode(std_opcodes, "LD A,L", 0x7D, 4, (regs, memory, args) -> regs.setA(regs.getL()));
        setOpCode(std_opcodes, "LD A,(BC)", 0x0A, 8, (regs, memory, args) -> regs.setA(memory.getMemVal(regs.getBC())));
        setOpCode(std_opcodes, "LD A,(DE)", 0x1A, 8, (regs, memory, args) -> regs.setA(memory.getMemVal(regs.getDE())));
        setOpCode(std_opcodes, "LD A,(HL)", 0x7E, 8, (regs, memory, args) -> regs.setA(memory.getMemVal(regs.getHL())));
        setOpCode(std_opcodes, "LD A,(nn)", 0xFA, 16, (regs, memory, args) -> regs.setA(memory.getMemVal(args[0])));
        setOpCode(std_opcodes, "LD A,#", 0x3E, 8, (regs, memory, args) -> regs.setA((byte) args[0]));
        setOpCode(std_opcodes, "LD A,(C)", 0xF2, 8, (regs, memory, args) -> regs.setA(memory.getMemVal((short) (0xFF00 + regs.getC()))));
//        setOpCode(std_opcodes, "LDD A,(HL)", 0x3A, 8, (regs, memory, args) -> {
//            regs.setA(regs.getHL());
//            regs.decHL();
//        });
//        setOpCode(std_opcodes, "LDI A,(HL)", 0x2A, 8, (regs, memory, args) -> {
//            regs.setA(regs.getHL());
//            regs.incHL();
//        });
        setOpCode(std_opcodes, "LDD A,(n)", 0xF0, 12, (regs, memory, args) -> regs.setA(memory.getMemVal((short) (0xFF00 + args[0]))));

        //LD INTO B
        setOpCode(std_opcodes, "LD B,A", 0x47, 4, (regs, memory, args) -> regs.setB(regs.getA()));
        setOpCode(std_opcodes, "LD B,B", 0x40, 4, (regs, memory, args) -> regs.setB(regs.getB()));
        setOpCode(std_opcodes, "LD B,C", 0x41, 4, (regs, memory, args) -> regs.setB(regs.getC()));
        setOpCode(std_opcodes, "LD B,D", 0x42, 4, (regs, memory, args) -> regs.setB(regs.getD()));
        setOpCode(std_opcodes, "LD B,E", 0x43, 4, (regs, memory, args) -> regs.setB(regs.getE()));
        setOpCode(std_opcodes, "LD B,H", 0x44, 4, (regs, memory, args) -> regs.setB(regs.getH()));
        setOpCode(std_opcodes, "LD B,L", 0x45, 4, (regs, memory, args) -> regs.setB(regs.getL()));
//        setOpCode(std_opcodes, "LD B,(HL)", 0x46, 8, (regs, memory, args) -> regs.setB(regs.getHL()));

        //LD INTO C
        setOpCode(std_opcodes, "LD C,A", 0x4F, 4, (regs, memory, args) -> regs.setC(regs.getA()));
        setOpCode(std_opcodes, "LD C,B", 0x48, 4, (regs, memory, args) -> regs.setC(regs.getB()));
        setOpCode(std_opcodes, "LD C,C", 0x49, 4, (regs, memory, args) -> regs.setC(regs.getC()));
        setOpCode(std_opcodes, "LD C,D", 0x4A, 4, (regs, memory, args) -> regs.setC(regs.getD()));
        setOpCode(std_opcodes, "LD C,E", 0x4B, 4, (regs, memory, args) -> regs.setC(regs.getE()));
        setOpCode(std_opcodes, "LD C,H", 0x4C, 4, (regs, memory, args) -> regs.setC(regs.getH()));
        setOpCode(std_opcodes, "LD C,L", 0x4D, 4, (regs, memory, args) -> regs.setC(regs.getL()));
        setOpCode(std_opcodes, "LD C,(HL)", 0x4E, 8, (regs, memory, args) -> regs.setC(memory.getMemVal(regs.getHL())));
        setOpCode(std_opcodes, "LD (C),A", 0xE2, 8, (regs, memory, args) -> memory.setMemVal(regs.getC(), regs.getA())); // Check if (C) includes adding 0xFF

        //LD INTO D
        setOpCode(std_opcodes, "LD L,A", 0x57, 4, (regs, memory, args) -> regs.setD(regs.getA()));
        setOpCode(std_opcodes, "LD D,B", 0x50, 4, (regs, memory, args) -> regs.setD(regs.getB()));
        setOpCode(std_opcodes, "LD D,C", 0x51, 4, (regs, memory, args) -> regs.setD(regs.getC()));
        setOpCode(std_opcodes, "LD D,D", 0x52, 4, (regs, memory, args) -> regs.setD(regs.getD()));
        setOpCode(std_opcodes, "LD D,E", 0x53, 4, (regs, memory, args) -> regs.setD(regs.getE()));
        setOpCode(std_opcodes, "LD D,H", 0x54, 4, (regs, memory, args) -> regs.setD(regs.getH()));
        setOpCode(std_opcodes, "LD D,L", 0x55, 4, (regs, memory, args) -> regs.setD(regs.getL()));
        setOpCode(std_opcodes, "LD D,(HL)", 0x56, 8, (regs, memory, args) -> regs.setD(memory.getMemVal(regs.getHL())));

        //LD INTO E
        setOpCode(std_opcodes, "LD E,A", 0x5F, 4, (regs, memory, args) -> regs.setE(regs.getA()));
        setOpCode(std_opcodes, "LD E,B", 0x58, 4, (regs, memory, args) -> regs.setE(regs.getB()));
        setOpCode(std_opcodes, "LD E,C", 0x59, 4, (regs, memory, args) -> regs.setE(regs.getC()));
        setOpCode(std_opcodes, "LD E,D", 0x5A, 4, (regs, memory, args) -> regs.setE(regs.getD()));
        setOpCode(std_opcodes, "LD E,E", 0x5B, 4, (regs, memory, args) -> regs.setE(regs.getE()));
        setOpCode(std_opcodes, "LD E,H", 0x5C, 4, (regs, memory, args) -> regs.setE(regs.getH()));
        setOpCode(std_opcodes, "LD E,L", 0x5D, 4, (regs, memory, args) -> regs.setE(regs.getL()));
//        setOpCode(std_opcodes, "LD E,(HL)", 0x5E, 8, (regs, memory, args) -> regs.setE(regs.getHL()));

        //LD INTO H
        setOpCode(std_opcodes, "LD H,A", 0x67, 4, (regs, memory, args) -> regs.setH(regs.getA()));
        setOpCode(std_opcodes, "LD H,B", 0x60, 4, (regs, memory, args) -> regs.setH(regs.getB()));
        setOpCode(std_opcodes, "LD H,C", 0x61, 4, (regs, memory, args) -> regs.setH(regs.getC()));
        setOpCode(std_opcodes, "LD H,D", 0x62, 4, (regs, memory, args) -> regs.setH(regs.getD()));
        setOpCode(std_opcodes, "LD H,E", 0x63, 4, (regs, memory, args) -> regs.setH(regs.getE()));
        setOpCode(std_opcodes, "LD H,H", 0x64, 4, (regs, memory, args) -> regs.setH(regs.getH()));
        setOpCode(std_opcodes, "LD H,L", 0x65, 4, (regs, memory, args) -> regs.setH(regs.getL()));
        setOpCode(std_opcodes, "LD H,(HL)", 0x66, 8, (regs, memory, args) -> regs.setH(memory.getMemVal(regs.getHL())));

        //LD INTO L
        setOpCode(std_opcodes, "LD L,A", 0x6F, 4, (regs, memory, args) -> regs.setL(regs.getA()));
        setOpCode(std_opcodes, "LD L,B", 0x68, 4, (regs, memory, args) -> regs.setL(regs.getB()));
        setOpCode(std_opcodes, "LD L,C", 0x69, 4, (regs, memory, args) -> regs.setL(regs.getC()));
        setOpCode(std_opcodes, "LD L,D", 0x6A, 4, (regs, memory, args) -> regs.setL(regs.getD()));
        setOpCode(std_opcodes, "LD L,E", 0x6B, 4, (regs, memory, args) -> regs.setL(regs.getE()));
        setOpCode(std_opcodes, "LD L,H", 0x6C, 4, (regs, memory, args) -> regs.setL(regs.getH()));
        setOpCode(std_opcodes, "LD L,L", 0x6D, 4, (regs, memory, args) -> regs.setL(regs.getL()));
        setOpCode(std_opcodes, "LD L,(HL)", 0x6E, 8, (regs, memory, args) -> regs.setL(memory.getMemVal(regs.getHL())));

        //LD INTO address at HL
        setOpCode(std_opcodes, "LD (HL),A", 0x77, 8, (regs, memory, args) -> memory.setMemVal(regs.getHL(), regs.getA()));
        setOpCode(std_opcodes, "LD (HL),B", 0x70, 8, (regs, memory, args) -> memory.setMemVal(regs.getHL(), regs.getB()));
        setOpCode(std_opcodes, "LD (HL),C", 0x71, 8, (regs, memory, args) -> memory.setMemVal(regs.getHL(), regs.getC()));
        setOpCode(std_opcodes, "LD (HL),D", 0x72, 8, (regs, memory, args) -> memory.setMemVal(regs.getHL(), regs.getD()));
        setOpCode(std_opcodes, "LD (HL),E", 0x73, 8, (regs, memory, args) -> memory.setMemVal(regs.getHL(), regs.getE()));
        setOpCode(std_opcodes, "LD (HL),H", 0x74, 8, (regs, memory, args) -> memory.setMemVal(regs.getHL(), regs.getH()));
        setOpCode(std_opcodes, "LD (HL),L", 0x75, 8, (regs, memory, args) -> memory.setMemVal(regs.getHL(), regs.getL()));
        setOpCode(std_opcodes, "LD (HL),n", 0x36, 12, (regs, memory, args) -> memory.setMemVal(regs.getHL(), (byte) args[0]));    // Maybe
//        setOpCode(std_opcodes, "LDD (HL),A", 0x32, 8, (regs, memory, args) -> {
//            regs.setHL(regs.getA());
//            regs.dec8Bit(regs.getHL());
//        });
//        setOpCode(std_opcodes, "LDI (HL),A", 0x22, 8, (regs, memory, args) -> {
//            regs.setHL(regs.getA());
//            regs.inc8Bit(regs.getHL());
//        });
//


        /**
         * 16-bit Loads
         */
//        // See GameBoy.CPU book for flags
//        setOpCode(std_opcodes, "LDHL SP,n", 0xF8, 12, (regs, memory, args) -> {
//            regs.setHL(memory.getValue(regs.getSP() + args[0]));
//            regs.clearZ();
//            regs.clearN();
//            regs.setH();
//            regs.setC();
//        });
        setOpCode(std_opcodes, "LD HL,nn", 0x21, 12, (regs, memory, args) -> regs.setHL(args[0]));
//
//        // LD INTO BC
        setOpCode(std_opcodes, "LD (BC),nn", 0x01, 12, (regs, memory, args) -> regs.setBC(args[0]));
        setOpCode(std_opcodes, "LD (BC),A", 0x02, 8, (regs, memory, args) -> regs.setBC(regs.getA()));
//
//        // LD INTO DE
        setOpCode(std_opcodes, "LD DE,nn", 0x11, 12, (regs, memory, args) -> regs.setDE(args[0]));
        setOpCode(std_opcodes, "LD (DE),A", 0x12, 8, (regs, memory, args) -> regs.setDE(regs.getA()));
//
//        // LD INTO SP
        setOpCode(std_opcodes, "LD SP,nn", 0x31, 12, (regs, memory, args) -> regs.setSP(args[0]));
        setOpCode(std_opcodes, "LD SP,HL", 0xF9, 8, (regs, memory, args) -> regs.setSP(regs.getHL()));
//
//        setOpCode(std_opcodes, "LD (NN),A", 0xEA, 8, (regs, memory, args) -> memory.setMemVal( args[0], regs.getA()));
//        setOpCode(std_opcodes, "LD (NN),SP", 0x08, 20, (regs, memory, args) -> memory.setMemVal(args[0], regs.getSP()));
//
//
//        setOpCode(std_opcodes, "LDh (n),A", 0xE0, 12, (regs, memory, args) -> regs.setN(0xFF00 + args[0], regs.getA()));    // Double check if n is different from nn, probably just use memory.setVal(adr,val)
//
//
//        // PUSH REGISTER PAIR ONTO STACK; DECREMENT SP
//        setOpCode(std_opcodes, "PUSH AF", 0xF5, 16, (regs, memory, args) -> memory.push(regs.getSP(), regs.getAF()));
//        setOpCode(std_opcodes, "PUSH BC", 0xC5, 16, (regs, memory, args) -> memory.push(regs.getSP(), regs.getBC()));
//        setOpCode(std_opcodes, "PUSH DE", 0xD5, 16, (regs, memory, args) -> memory.push(regs.getSP(), regs.getDE()));
//        setOpCode(std_opcodes, "PUSH HL", 0xE5, 16, (regs, memory, args) -> memory.push(regs.getSP(), regs.getHL()));
//
//        // POP OFF STACK AND STORE IN REGISTER PAIR ; INCREMENT SP
//        setOpCode(std_opcodes, "POP AF", 0xF1, 12, (regs, memory, args) -> regs.setAF(memory.pop(regs.getSP(), regs.getAF())));
//        setOpCode(std_opcodes, "POP BC", 0xC1, 12, (regs, memory, args) -> regs.setBC(memory.pop(regs.getSP(), regs.getBC())));
//        setOpCode(std_opcodes, "POP DE", 0xD1, 12, (regs, memory, args) -> regs.setDE(memory.pop(regs.getSP(), regs.getDE())));
//        setOpCode(std_opcodes, "POP HL", 0xE1, 12, (regs, memory, args) -> regs.setHL(memory.pop(regs.getSP(), regs.getHL())));
//
//
//        /****
//         * 8-Bit ALU
//         */
//
//        // ADD TO A; FLAGS AFFECTED;
//        setOpCode(std_opcodes, "ADD A,A", 0x87, 4, (regs, memory, args) -> regs.addA(regs.getA()));
//        setOpCode(std_opcodes, "ADD A,B", 0x80, 4, (regs, memory, args) -> regs.addA(regs.getB()));
//        setOpCode(std_opcodes, "ADD A,C", 0x81, 4, (regs, memory, args) -> regs.addA(regs.getC()));
//        setOpCode(std_opcodes, "ADD A,D", 0x82, 4, (regs, memory, args) -> regs.addA(regs.getD()));
//        setOpCode(std_opcodes, "ADD A,E", 0x83, 4, (regs, memory, args) -> regs.addA(regs.getE()));
//        setOpCode(std_opcodes, "ADD A,H", 0x84, 4, (regs, memory, args) -> regs.addA(regs.getH()));
//        setOpCode(std_opcodes, "ADD A,L", 0x85, 4, (regs, memory, args) -> regs.addA(regs.getL()));
//        setOpCode(std_opcodes, "ADD A,(HL)", 0x86, 8, (regs, memory, args) -> regs.addA(regs.getHL()));
//        setOpCode(std_opcodes, "ADD A,#", 0xC6, 8, (regs, memory, args) -> regs.addA(args[0]));
//
//        // Add register + carry flag to A; FLAGS AFFECTED
//        setOpCode(std_opcodes, "ADD A,A", 0x8F, 4, (regs, memory, args) -> regs.addA(regs.getA() +));
//        setOpCode(std_opcodes, "ADD A,B", 0x88, 4, (regs, memory, args) -> regs.addA(regs.getB()) +);
//        setOpCode(std_opcodes, "ADD A,C", 0x89, 4, (regs, memory, args) -> regs.addA(regs.getC()) +);
//        setOpCode(std_opcodes, "ADD A,D", 0x8A, 4, (regs, memory, args) -> regs.addA(regs.getD()) +);
//        setOpCode(std_opcodes, "ADD A,E", 0x8B, 4, (regs, memory, args) -> regs.addA(regs.getE()) +);
//        setOpCode(std_opcodes, "ADD A,H", 0x8C, 4, (regs, memory, args) -> regs.addA(regs.getH()) +);
//        setOpCode(std_opcodes, "ADD A,L", 0x8D, 4, (regs, memory, args) -> regs.addA(regs.getL()) +);
//        setOpCode(std_opcodes, "ADD A,(HL)", 0x8E, 8, (regs, memory, args) -> regs.addA(regs.getHL()) +);
//        setOpCode(std_opcodes, "ADD A,#", 0xCE, 8, (regs, memory, args) -> regs.addA(args[0]) +);
//
//        // SUBTRACT N FROM A; FLAGS AFFECTED
//        setOpCode(std_opcodes, "SUB A", 0x97, 4, (regs, memory, args) -> regs.subA(regs.getA()));
//        setOpCode(std_opcodes, "SUB B", 0x90, 4, (regs, memory, args) -> regs.subA(regs.getB()));
//        setOpCode(std_opcodes, "SUB C", 0x91, 4, (regs, memory, args) -> regs.subA(regs.getC()));
//        setOpCode(std_opcodes, "SUB D", 0x92, 4, (regs, memory, args) -> regs.subA(regs.getD()));
//        setOpCode(std_opcodes, "SUB E", 0x93, 4, (regs, memory, args) -> regs.subA(regs.getE()));
//        setOpCode(std_opcodes, "SUB H", 0x94, 4, (regs, memory, args) -> regs.subA(regs.getH()));
//        setOpCode(std_opcodes, "SUB L", 0x95, 4, (regs, memory, args) -> regs.subA(regs.getL()));
//        setOpCode(std_opcodes, "SUB (HL)", 0x96, 8, (regs, memory, args) -> regs.subA(regs.getHL()));
//        setOpCode(std_opcodes, "SUB #", 0xD6, 8, (regs, memory, args) -> regs.subA(args[0]));
//
//        // SUBTRACT + CARRY FLAG FROM A
//        setOpCode(std_opcodes, "SBC A", 0x9F, 4, (regs, memory, args) -> regs.sbcA(regs.getA()));
//        setOpCode(std_opcodes, "SBC B", 0x98, 4, (regs, memory, args) -> regs.sbcA(regs.getB()));
//        setOpCode(std_opcodes, "SBC C", 0x99, 4, (regs, memory, args) -> regs.sbcA(regs.getC()));
//        setOpCode(std_opcodes, "SBC D", 0x9A, 4, (regs, memory, args) -> regs.sbcA(regs.getD()));
//        setOpCode(std_opcodes, "SBC E", 0x9B, 4, (regs, memory, args) -> regs.sbcA(regs.getE()));
//        setOpCode(std_opcodes, "SBC H", 0x9C, 4, (regs, memory, args) -> regs.sbcA(regs.getH()));
//        setOpCode(std_opcodes, "SBC L", 0x9D, 4, (regs, memory, args) -> regs.sbcA(regs.getL()));
//        setOpCode(std_opcodes, "SBC (HL)", 0x9E, 8, (regs, memory, args) -> regs.sbcA(regs.getHL()));
//        setOpCode(std_opcodes, "SBC #", -1, -0, (regs, memory, args) -> regs.sbcA(args[0])); // MISSING OP CODE + CLOCKS
//
//        // LOGICAL AND A & N STORED IN A; FLAG AFFECTED
//        setOpCode(std_opcodes, "AND A", 0xA7, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND B", 0xA0, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() & regs.getB());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND C", 0xA1, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() & regs.getC());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND D", 0xA2, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() & regs.getD());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND E", 0xA3, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() & regs.getE());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND H", 0xA4, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() & regs.getH());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND L", 0xA5, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() & regs.getHL());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND (HL)", 0xA6, 8, (regs, memory, args) -> {
//            regs.setA(regs.getA() & regs.getHL());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "AND #", 0xE6, 8, (regs, memory, args) -> {
//            regs.setA(regs.getA() & args[0]);
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.setH();
//            regs.flags.clearC();
//        });
//
//        // LOGICAL OR A & N STORED IN A; FLAG AFFECTED
//        setOpCode(std_opcodes, "OR A", 0xB7, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getA());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR B", 0xB0, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getB());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR C", 0xB1, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getC());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR D", 0xB2, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getD());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR E", 0xB3, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getE());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR H", 0xB4, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getH());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR L", 0xB5, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getHL());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR (HL)", 0xB6, 8, (regs, memory, args) -> {
//            regs.setA(regs.getA() | regs.getHL());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "OR #", 0xF6, 8, (regs, memory, args) -> {
//            regs.setA(regs.getA() | args[0]);
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//
//        // LOGICAL OR A & N STORED IN A; FLAG AFFECTED
//        setOpCode(std_opcodes, "XOR A", 0xAF, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getA());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR B", 0xA8, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getB());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR C", 0xA9, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getC());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR D", 0xAA, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getD());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR E", 0xAB, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getE());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR H", 0xAC, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getH());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR L", 0xAD, 4, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getHL());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR (HL)", 0xAE, 8, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ regs.getHL());
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//        setOpCode(std_opcodes, "XOR #", 0xAE, 8, (regs, memory, args) -> {
//            regs.setA(regs.getA() ^ args[0]);
//            if (regs.getA() == 0) regs.flags.setZ();
//            regs.flags.clearN();
//            regs.flags.clearH();
//            regs.flags.clearC();
//        });
//
//        // COMPARE A with N. BASICALLY AN A - N SUBTRACTION, WITH THE RESULTS THROWN AWAY; FLAGS AFFECTED.
//        setOpCode(std_opcodes, "CP A", 0xBF, 4, (regs, memory, args) -> regs.cp(regs.getA(), regs.getA());
//        setOpCode(std_opcodes, "CP B", 0xB8, 4, (regs, memory, args) -> regs.cp(regs.getA(), regs.getB());
//        setOpCode(std_opcodes, "CP C", 0xB9, 4, (regs, memory, args) -> regs.cp(regs.getA(), regs.getC());
//        setOpCode(std_opcodes, "CP D", 0xBA, 4, (regs, memory, args) -> regs.cp(regs.getA(), regs.getD());
//        setOpCode(std_opcodes, "CP E", 0xBB, 4, (regs, memory, args) -> regs.cp(regs.getA(), regs.getE());
//        setOpCode(std_opcodes, "CP H", 0xBC, 4, (regs, memory, args) -> regs.cp(regs.getA(), regs.getH());
//        setOpCode(std_opcodes, "CP L", 0xBD, 4, (regs, memory, args) -> regs.cp(regs.getA(), regs.getL());
//        setOpCode(std_opcodes, "CP (HL)", 0xBE, 8, (regs, memory, args) -> regs.cp(regs.getA(), regs.getHL());
//        setOpCode(std_opcodes, "CP #", 0xFE, 8, (regs, memory, args) -> regs.cp(regs.getA(), memory.getValue(args[0]));
//
        // INCREMENT REGISTER N; FLAGS AFFECTED
        setOpCode(std_opcodes, "INC A", 0x3C, 4, (regs, memory, args) -> regs.setA(Commands.inc8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "INC B", 0x04, 4, (regs, memory, args) -> regs.setB(Commands.inc8Bit(regs, regs.getB())));
        setOpCode(std_opcodes, "INC C", 0x0C, 4, (regs, memory, args) -> regs.setC(Commands.inc8Bit(regs, regs.getC())));
        setOpCode(std_opcodes, "INC D", 0x14, 4, (regs, memory, args) -> regs.setD(Commands.inc8Bit(regs, regs.getD())));
        setOpCode(std_opcodes, "INC E", 0x1C, 4, (regs, memory, args) -> regs.setE(Commands.inc8Bit(regs, regs.getE())));
        setOpCode(std_opcodes, "INC H", 0x24, 4, (regs, memory, args) -> regs.setH(Commands.inc8Bit(regs, regs.getH())));
        setOpCode(std_opcodes, "INC L", 0x2C, 4, (regs, memory, args) -> regs.setL(Commands.inc8Bit(regs, regs.getL())));
        setOpCode(std_opcodes, "INC (HL)", 0x34, 12, (regs, memory, args) -> memory.setMemVal(regs.getHL(), Commands.inc8Bit(regs, memory.getMemVal(regs.getHL()))));

        // DECREMENT REGISTER N; FLAGS AFFECTED
        setOpCode(std_opcodes, "DEC A", 0x3D, 4, (regs, memory, args) -> regs.setA(Commands.dec8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "DEC B", 0x05, 4, (regs, memory, args) -> regs.setA(Commands.dec8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "DEC C", 0x0D, 4, (regs, memory, args) -> regs.setA(Commands.dec8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "DEC D", 0x15, 4, (regs, memory, args) -> regs.setA(Commands.dec8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "DEC E", 0x1D, 4, (regs, memory, args) -> regs.setA(Commands.dec8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "DEC H", 0x25, 4, (regs, memory, args) -> regs.setA(Commands.dec8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "DEC L", 0x2D, 4, (regs, memory, args) -> regs.setA(Commands.dec8Bit(regs, regs.getA())));
        setOpCode(std_opcodes, "DEC (HL)", 0x35, 12, (regs, memory, args) -> memory.setMemVal(regs.getHL(), Commands.dec8Bit(regs, memory.getMemVal(regs.getHL()))));
//
//
//        /**
//         * 16-Bit Arithmetic
//         */
//        // ADD TO HL
//        setOpCode(std_opcodes, "ADD HL,BC", 0x09, 8, (regs, memory, args) -> regs.addToRegPair(regs.getH(), regs.getL(), regs.getBC()));
//        setOpCode(std_opcodes, "ADD HL,DE", 0x19, 8, (regs, memory, args) -> regs.addToRegPair(regs.getH(), regs.getL(), regs.getDE()));
//        setOpCode(std_opcodes, "ADD HL,HL", 0x29, 8, (regs, memory, args) -> regs.addToRegPair(regs.getH(), regs.getL(), regs.getHL()));
//        setOpCode(std_opcodes, "ADD HL,SP", 0x39, 8, (regs, memory, args) -> regs.addToRegPair(regs.getH(), regs.getL(), regs.getSP()));
//
//        // ADD TO STACK POINTER
//        setOpCode(std_opcodes, "ADD SP,n", 0xE8, 16, (regs, memory, args) -> regs.addToReg(regs.getSP(), args[0]));
//
        // INCREMENT REGISTER PAIR
//        setOpCode(std_opcodes, "INC BC", 0x03, 8, (regs, memory, args) -> regs.incRegPair(regs.getB(), regs.getC()));
//        setOpCode(std_opcodes, "INC DE", 0x13, 8, (regs, memory, args) -> regs.incRegPair(regs.getD(), regs.getE()));
//        setOpCode(std_opcodes, "INC HL", 0x23, 8, (regs, memory, args) -> regs.incRegPair(regs.getH(), regs.getL()));
//        setOpCode(std_opcodes, "INC SP", 0x33, 8, (regs, memory, args) -> regs.inc8Bit(regs.getSP()));
//
//        //  DECREMENT REGISTER PAIR
//        setOpCode(std_opcodes, "DEC BC", 0x0B, 8, (regs, memory, args) -> regs.decRegPair(regs.getB(), regs.getC()));
//        setOpCode(std_opcodes, "DEC DE", 0x1B, 8, (regs, memory, args) -> regs.decRegPair(regs.getD(), regs.getE());
//        setOpCode(std_opcodes, "DEC HL", 0x2B, 8, (regs, memory, args) -> regs.decRegPair(regs.getH(), regs.getL());
//        setOpCode(std_opcodes, "DEC SP", 0x3B, 8, (regs, memory, args) -> regs.dec8Bit(regs.getSP()));


        /**
         * Misc.
         */
        // Swap upper and lower nibbles of n
        setOpCode(cb_opcodes, "SWAP A", 0xCB37, 8, (regs, memory, args) -> regs.setA(Commands.swap(regs.getA())));
        setOpCode(cb_opcodes, "SWAP B", 0xCB30, 8, (regs, memory, args) -> regs.setB(Commands.swap(regs.getB())));
        setOpCode(cb_opcodes, "SWAP C", 0xCB31, 8, (regs, memory, args) -> regs.setC(Commands.swap(regs.getC())));
        setOpCode(cb_opcodes, "SWAP D", 0xCB32, 8, (regs, memory, args) -> regs.setD(Commands.swap(regs.getD())));
        setOpCode(cb_opcodes, "SWAP E", 0xCB33, 8, (regs, memory, args) -> regs.setE(Commands.swap(regs.getE())));
        setOpCode(cb_opcodes, "SWAP H", 0xCB34, 8, (regs, memory, args) -> regs.setH(Commands.swap(regs.getH())));
        setOpCode(cb_opcodes, "SWAP L", 0xCB35, 8, (regs, memory, args) -> regs.setL(Commands.swap(regs.getL())));
        setOpCode(cb_opcodes, "SWAP (HL)", 0xCB36, 16, (regs, memory, args) -> memory.setMemVal(regs.getHL(), Commands.swap(memory.getMemVal(regs.getHL()))));

//        // Decimal adjust register A
//        setOpCode(std_opcodes, "DAA", 27, 4, (regs, memory, args) -> regs.daa());
//
//        // Complement A register
//        setOpCode(std_opcodes, "CPL", 2F, 4, (regs, memory, args) -> regs.cpl());
//
//        // Complement carry flag
//        setOpCode(std_opcodes, "CCF", 3F, 4, (regs, memory, args) -> regs.ccf());
//
//        // Set carry flag
//        setOpCode(std_opcodes, "SCF", 37, 4, (regs, memory, args) -> regs.setC());
//
//
//        setOpCode(std_opcodes, "NOP", 00, 4, (regs, memory, args) -> regs.nop());
//        setOpCode(std_opcodes, "HALT", 76, 4, (regs, memory, args) -> regs.halt());
//        setOpCode(std_opcodes, "STOP", 0x1000, 4, (regs, memory, args) -> regs.stop());
//
//        // Disable Interrupt
//        setOpCode(std_opcodes, "DI", 0xF3, 4, (regs, memory, args) -> regs.disableInterrupts());
//
//        // Enable Interrupts
//        setOpCode(std_opcodes, "DI", 0xFB, 4, (regs, memory, args) -> regs.enableInterrupts());
//
//
//
//        /**
//         * Rotates & Shifts
//         */
//        // Rotate A left. Old bit 7 to Carry flag. FLAGS AFFECTED
//        setOpCode(std_opcodes, "RLCA", 0x07, 4, (regs, memory, args) -> regs.rlc(regs.getA(), regs.getFlag()));
//
//        // Rotate A left through Carry flag.
//        setOpCode(std_opcodes, "RLA", 0x17, 4, (regs, memory, args) -> regs.rla());
//
//        // Rotate A right through Old 0 bit to Carry flag.
//        setOpCode(std_opcodes, "RRCA", 0x0F, 4, (regs, memory, args) -> regs.rrca());
//
//        // Rotate A right through Carry flag.
//        setOpCode(std_opcodes, "RRA", 0x1F, 4, (regs, memory, args) -> regs.rra());
//
//        // Rotate n left. Old bit 7 to carry flag. Flag affected
//        setOpCode(cb_opcodes, "RLC A", 0xCB07, 8, (regs, memory, args) -> regs.rlc(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RLC B", 0xCB00, 8, (regs, memory, args) -> regs.rlc(regs.getB(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RLC C", 0xCB01, 8, (regs, memory, args) -> regs.rlc(regs.getC(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RLC D", 0xCB02, 8, (regs, memory, args) -> regs.rlc(regs.getD(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RLC E", 0xCB03, 8, (regs, memory, args) -> regs.rlc(regs.getE(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RLC H", 0xCB04, 8, (regs, memory, args) -> regs.rlc(regs.getH(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RLC L", 0xCB05, 8, (regs, memory, args) -> regs.rlc(regs.getL(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RLC (HL)", 0xCB06, 16, (regs, memory, args) -> regs.rlcRegPair(regs.getH(), regs.getL(), regs.getFlag()));
//
//        // Rotate n left through carry flag. Flag affected
//        setOpCode(cb_opcodes, "RL A", 0xCB17, 8, (regs, memory, args) -> regs.rl(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RL B", 0xCB10, 8, (regs, memory, args) -> regs.rl(regs.getB(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RL C", 0xCB11, 8, (regs, memory, args) -> regs.rl(regs.getC(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RL D", 0xCB12, 8, (regs, memory, args) -> regs.rl(regs.getD(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RL E", 0xCB13, 8, (regs, memory, args) -> regs.rl(regs.getE(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RL H", 0xCB14, 8, (regs, memory, args) -> regs.rl(regs.getH(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RL L", 0xCB15, 8, (regs, memory, args) -> regs.rl(regs.getL(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RL (HL)", 0xCB16, 16, (regs, memory, args) -> regs.rlRegPair(regs.getH(), regs.getL(), regs.getFlag()));
//
//        // Rotate n right. Old bit 0 to Carry flag. Flag affected
//        setOpCode(cb_opcodes, "RRC A", 0xCB0F, 8, (regs, memory, args) -> regs.rrc(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RRC B", 0xCB08, 8, (regs, memory, args) -> regs.rrc(regs.getB(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RRC C", 0xCB09, 8, (regs, memory, args) -> regs.rrc(regs.getC(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RRC D", 0xCB0A, 8, (regs, memory, args) -> regs.rrc(regs.getD(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RRC E", 0xCB0B, 8, (regs, memory, args) -> regs.rrc(regs.getE(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RRC H", 0xCB0C, 8, (regs, memory, args) -> regs.rrc(regs.getH(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RRC L", 0xCB0D, 8, (regs, memory, args) -> regs.rrc(regs.getL(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RRC (HL)", 0xCB0E, 16, (regs, memory, args) -> regs.rrcRegPair(regs.getH(), regs.getL(), regs.getFlag()));
//
//        // Rotate n right through Carry flag. GameBoy.Flags affected
//        setOpCode(cb_opcodes, "RR A", 0xCB1F, 8, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RR B", 0xCB18, 8, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RR C", 0xCB19, 8, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RR D", 0xCB1A, 8, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RR E", 0xCB1B, 8, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RR H", 0xCB1C, 8, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RR L", 0xCB1D, 8, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "RR (HL)", 0xCB1E, 16, (regs, memory, args) -> regs.rr(regs.getA(), regs.getFlag()));
//
//        // Shift n left into Carry. LSB of n set to 0. FLAGS AFFECTED
//        setOpCode(cb_opcodes, "SLA A", 0xCB27, 8, (regs, memory, args) -> regs.sla(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SLA B", 0xCB20, 8, (regs, memory, args) -> regs.sla(regs.getB(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SLA C", 0xCB21, 8, (regs, memory, args) -> regs.sla(regs.getC(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SLA D", 0xCB22, 8, (regs, memory, args) -> regs.sla(regs.getD(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SLA E", 0xCB23, 8, (regs, memory, args) -> regs.sla(regs.getE(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SLA H", 0xCB24, 8, (regs, memory, args) -> regs.sla(regs.getH(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SLA L", 0xCB25, 8, (regs, memory, args) -> regs.sla(regs.getL(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SLA (HL)", 0xCB26, 16, (regs, memory, args) -> regs.slaRegPair(regs.getH(), regs.getL(), regs.getFlag()));
//
//        // Shift n right into Carry. MSB doesn't change.
//        setOpCode(cb_opcodes, "SRA A", 0xCB2F, 8, (regs, memory, args) -> regs.sra(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRA B", 0xCB28, 8, (regs, memory, args) -> regs.sra(regs.getB(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRA C", 0xCB29, 8, (regs, memory, args) -> regs.sra(regs.getC(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRA D", 0xCB2A, 8, (regs, memory, args) -> regs.sra(regs.getD(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRA E", 0xCB2B, 8, (regs, memory, args) -> regs.sra(regs.getE(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRA H", 0xCB2C, 8, (regs, memory, args) -> regs.sra(regs.getH(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRA L", 0xCB2D, 8, (regs, memory, args) -> regs.sra(regs.getL(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRA (HL)", 0xCB2E, 16, (regs, memory, args) -> regs.sraRegPair(regs.getH(), regs.getL(), regs.getFlag()));
//
//        // Shift n right into Carry. MSB set to 0.
//        setOpCode(cb_opcodes, "SRL A", 0xCB3F, 8, (regs, memory, args) -> regs.srl(regs.getA(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRL B", 0xCB38, 8, (regs, memory, args) -> regs.srl(regs.getB(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRL C", 0xCB39, 8, (regs, memory, args) -> regs.srl(regs.getC(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRL D", 0xCB3A, 8, (regs, memory, args) -> regs.srl(regs.getD(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRL E", 0xCB3B, 8, (regs, memory, args) -> regs.srl(regs.getE(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRL H", 0xCB3C, 8, (regs, memory, args) -> regs.srl(regs.getH(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRL L", 0xCB3D, 8, (regs, memory, args) -> regs.srl(regs.getL(), regs.getFlag()));
//        setOpCode(cb_opcodes, "SRL (HL)", 0xCB3E, 16, (regs, memory, args) -> regs.srlRegPair(regs.getH(), regs.getL(), regs.getFlag()));
//
//
//        /**
//         * Bit GameBoy.Opcodes
//         */
//        // Test bit b in register r. GameBoy.Flags affected
//        setOpCode(cb_opcodes, "BIT b,A", 0xCB47, 8, (regs, memory, args) -> regs.testBit(regs.getA(), args[0], regs.getFlag()));
//        setOpCode(cb_opcodes, "BIT b,B", 0xCB40, 8, (regs, memory, args) -> regs.testBit(regs.getB(), args[0], regs.getFlag()));
//        setOpCode(cb_opcodes, "BIT b,C", 0xCB41, 8, (regs, memory, args) -> regs.testBit(regs.getC(), args[0], regs.getFlag()));
//        setOpCode(cb_opcodes, "BIT b,D", 0xCB42, 8, (regs, memory, args) -> regs.testBit(regs.getD(), args[0], regs.getFlag()));
//        setOpCode(cb_opcodes, "BIT b,E", 0xCB43, 8, (regs, memory, args) -> regs.testBit(regs.getE(), args[0], regs.getFlag()));
//        setOpCode(cb_opcodes, "BIT b,H", 0xCB44, 8, (regs, memory, args) -> regs.testBit(regs.getH(), args[0], regs.getFlag()));
//        setOpCode(cb_opcodes, "BIT b,L", 0xCB45, 8, (regs, memory, args) -> regs.testBit(regs.getL(), args[0], regs.getFlag()));
//        setOpCode(cb_opcodes, "BIT b,(HL)", 0xCB46, 16, (regs, memory, args) -> regs.testBit(regs.getHL(), args[0], regs.getFlag()));
//
//        // Set bit b in register r.
//        setOpCode(cb_opcodes, "SET b,A", 0xCBC7, 8, (regs, memory, args) -> regs.setBit(regs.getA(), args[0]));
//        setOpCode(cb_opcodes, "SET b,B", 0xCBC0, 8, (regs, memory, args) -> regs.setBit(regs.getB(), args[0]));
//        setOpCode(cb_opcodes, "SET b,C", 0xCBC1, 8, (regs, memory, args) -> regs.setBit(regs.getC(), args[0]));
//        setOpCode(cb_opcodes, "SET b,D", 0xCBC2, 8, (regs, memory, args) -> regs.setBit(regs.getD(), args[0]));
//        setOpCode(cb_opcodes, "SET b,E", 0xCBC3, 8, (regs, memory, args) -> regs.setBit(regs.getE(), args[0]));
//        setOpCode(cb_opcodes, "SET b,H", 0xCBC4, 8, (regs, memory, args) -> regs.setBit(regs.getH(), args[0]));
//        setOpCode(cb_opcodes, "SET b,L", 0xCBC5, 8, (regs, memory, args) -> regs.setBit(regs.getL(), args[0]));
//        setOpCode(cb_opcodes, "SET b,(HL)", 0xCBC6, 16, (regs, memory, args) -> regs.setBit(regs.getHL(), args[0]));
//
//        // RESET BIT B IN REGISTER r
//        setOpCode(cb_opcodes, "RES b,A", 0xCB87, 8, (regs, memory, args) -> regs.clearBit(regs.getA(), args[0]));
//        setOpCode(cb_opcodes, "RES b,B", 0xCB80, 8, (regs, memory, args) -> regs.clearBit(regs.getB(), args[0]));
//        setOpCode(cb_opcodes, "RES b,C", 0xCB81, 8, (regs, memory, args) -> regs.clearBit(regs.getC(), args[0]));
//        setOpCode(cb_opcodes, "RES b,D", 0xCB82, 8, (regs, memory, args) -> regs.clearBit(regs.getD(), args[0]));
//        setOpCode(cb_opcodes, "RES b,E", 0xCB83, 8, (regs, memory, args) -> regs.clearBit(regs.getE(), args[0]));
//        setOpCode(cb_opcodes, "RES b,H", 0xCB84, 8, (regs, memory, args) -> regs.clearBit(regs.getH(), args[0]));
//        setOpCode(cb_opcodes, "RES b,L", 0xCB85, 8, (regs, memory, args) -> regs.clearBit(regs.getL(), args[0]));
//        setOpCode(cb_opcodes, "RES b,(HL)", 0xCB86, 16, (regs, memory, args) -> regs.clearBit(regs.getHL(), args[0]));
//
//
//
//        /**
//         * Jumps
//         */
//        //  Jump to address nn
//        setOpCode(std_opcodes, "JP NN", 0xC3, 12, (regs, memory, args) -> regs.jp(args[0]));
//
//
//        // Jump to address n if following condition is true
//        setOpCode(std_opcodes, "JP NZ,NN", 0xC2, 12, (regs, memory, args) -> regs.jpIf(args[0]));
//        setOpCode(std_opcodes, "JP Z,NN", 0xCA, 12, (regs, memory, args) -> regs.jpIf(args[0]));
//        setOpCode(std_opcodes, "JP NC,NN", 0xD2, 12, (regs, memory, args) -> regs.jpIf(args[0]));
//        setOpCode(std_opcodes, "JP C,NN", 0xDA, 12, (regs, memory, args) -> regs.jpIf(args[0]));
//
//        // JUMP TO ADDRESS HL
//        setOpCode(std_opcodes, "JP (HL)", 0xE9, 4, (regs, memory, args) -> regs.jp(regs.getHL()));
//
//        // Add n to current address and jump to it
//        setOpCode(std_opcodes, "JR n", 0x18, 8, (regs, memory, args) -> regs.jr(args[0])); // Fix function
//
//        // Conditional jump + add
//        setOpCode(std_opcodes, "JR NZ, *", 0x20, 8, (regs, memory, args) -> regs.jrIf(args[0]));
//        setOpCode(std_opcodes, "JR Z,*", 0x28, 8, (regs, memory, args) -> regs.jrIf(args[0]));
//        setOpCode(std_opcodes, "JR NC, *", 0x30, 8, (regs, memory, args) -> regs.jrIf(args[0]));
//        setOpCode(std_opcodes, "JR C,*", 0x38, 8, (regs, memory, args) -> regs.jrIf(args[0]));
//
//
//
//        /**
//         * Calls
//         */
//        // Push address of next instruction onto stack and then jump to address nn
//        setOpCode(std_opcodes, "CALL nn", 0xCD, 12, (regs, memory, args) -> regs.call(args[0]));
//
//        // Call adr if
//        setOpCode(std_opcodes, "CALL NZ,nn", 0xC4, 12, (regs, memory, args) -> regs.callIf(args[0]));
//        setOpCode(std_opcodes, "CALL Z,nn", 0xCC, 12, (regs, memory, args) -> regs.callIf(args[0]));
//        setOpCode(std_opcodes, "CALL NC,nn", 0xD4, 12, (regs, memory, args) -> regs.callIf(args[0]));
//        setOpCode(std_opcodes, "CALL C,nn", 0xDC, 12, (regs, memory, args) -> regs.callIf(args[0]));
//
//
//        /**
//         * Restarts
//         */
//        // Jump to address $0000 + n. 0x00, 0x08, ...
//        setOpCode(std_opcodes, "RST 00H", 0xC7, 32, (regs, memory, args) -> Commands.restart(args[0]));
//        setOpCode(std_opcodes, "RST 08H", 0xCF, 32, (regs, memory, args) -> regs.restart(args[0]));
//        setOpCode(std_opcodes, "RST 10H", 0xD7, 32, (regs, memory, args) -> regs.restart(args[0]));
//        setOpCode(std_opcodes, "RST 18H", 0xDF, 32, (regs, memory, args) -> regs.restart(args[0]));
//        setOpCode(std_opcodes, "RST 20H", 0xE7, 32, (regs, memory, args) -> regs.restart(args[0]));
//        setOpCode(std_opcodes, "RST 28H", 0xEF, 32, (regs, memory, args) -> regs.restart(args[0]));
//        setOpCode(std_opcodes, "RST 30H", 0xF7, 32, (regs, memory, args) -> regs.restart(args[0]));
//        setOpCode(std_opcodes, "RST 38H", 0xFF, 32, (regs, memory, args) -> regs.restart(args[0]));
//
//
//        /**
//         * Returns
//         */
//        // Pop two bytes from stack and jump to that address
//        setOpCode(std_opcodes, "RET", 0xC9, 8, (regs, memory, args) -> regs.popJmp());
//
//        // Return if following condition is true
//        setOpCode(std_opcodes, "RET NZ", 0xC0, 8, (regs, memory, args) -> regs.popJmpIf(regs.getZFlag()));
//        setOpCode(std_opcodes, "RET Z", 0xC8, 8, (regs, memory, args) -> regs.popJmpIf(regs.getZFlag()));
//        setOpCode(std_opcodes, "RET NC", 0xD0, 8, (regs, memory, args) -> regs.popJmpIf(regs.getCFlag()));
//        setOpCode(std_opcodes, "RET C", 0xD8, 8, (regs, memory, args) -> regs.popJmpIf(regs.getCFlag()));
//
//        // Pop two bytes from stack and jump to that address then enable interrupts
//        setOpCode(std_opcodes, "RET", 0xD9, 8, (regs, memory, args) -> regs.reti());

    }

    private void setOpCode(Instructions[] opcodes, String label, int opcode, int clocks, Operation op) {
        opcodes[opcode] = new Instructions(label, opcode, clocks, op);
    }

    public void execute(int opcode, Registers regs, Memory memory, short[] args) {
        std_opcodes[opcode].op.cmd(regs, memory, args);

    }
}