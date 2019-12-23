package Interpreter;

import GameLogic.GameLogic;
import Instructions.Instruction;
import controllers.GameProcess;


public class MoveInstruction implements InterpretInstruction {
    private GameProcess gameProcess;
    private GameLogic gameLogic;
    private int delay;
    MoveInstruction(GameProcess gameProcess, GameLogic gameLogic, int delay) {
        this.gameLogic = gameLogic;
        this.gameProcess = gameProcess;
        this.delay = delay;
    }
    @Override
    public boolean interpret(Instruction instruction) {
        for(int i = 0; i < instruction.arguments.get(0); ++i) {
            gameProcess.showGameState(gameLogic.executeInstruction(instruction));
            if(gameLogic.isEnd()) {
                return true;
            }
            if(i != instruction.arguments.get(0) - 1) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
