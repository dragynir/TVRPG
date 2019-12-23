package Interpreter;

import Instructions.Instruction;

public interface InterpretInstruction {
    boolean interpret(Instruction instruction);
}
