package Interpreter;

import Instructions.Instruction;

public class RepeatInstruction implements InterpretInstruction {
    private Interpreter interpreter;
    private int delay;
    RepeatInstruction(Interpreter interpreter, int delay) {
        this.interpreter = interpreter;
        this.delay = delay;
    }
    @Override
    public boolean interpret(Instruction instruction) {
        assert (4 == instruction.instructionId);
        for(int i = 0; i < instruction.arguments.get(0); ++i) {
            for (Instruction subInstruction : instruction.body) {
                boolean interpretResult = interpreter.getInterpret(subInstruction.instructionId).interpret(subInstruction);
                if (interpretResult) {
                    return true;
                }
                if(instruction.body.indexOf(instruction) != instruction.body.size() - 1) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
