package Interpreter;

import GameLogic.GameLogic;
import Instructions.Instruction;
import Instructions.InstructionList;
import UnitedClasses.GameResult;
import UnitedClasses.R_PATH;
import controllers.GameProcess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Interpreter {
    private InstructionList instructionList;
    private GameLogic gameLogic;

    private int stepsDelay;
    private List<InterpretInstruction> interpretInstruction;

    public Interpreter(GameProcess gameProcess, InstructionList instructionList, int levelNumber) throws IOException {
        this.instructionList = instructionList;
        this.gameLogic = new GameLogic(levelNumber);

        Properties properties = new Properties();

        File f = new File(R_PATH.res_path + "/levels/level_" + levelNumber + "/gameplayConfig.properties");
        InputStream inputStream = new FileInputStream(f);

        //properties.load(AcceptableInstructions.class.getResourceAsStream("../resources/acceptableInstructions.properties"));
        properties.load(inputStream);


        //properties.load(Interpreter.class.getResourceAsStream( R_PATH.res_path + "/levels/level_" + levelNumber + "/gameplayConfig.properties"));
        this.stepsDelay = Integer.parseInt(properties.getProperty("steps_delay"));

        interpretInstruction = new ArrayList<>();
        interpretInstruction.add(new MoveInstruction(gameProcess, gameLogic, stepsDelay));
        interpretInstruction.add(new MoveInstruction(gameProcess, gameLogic, stepsDelay));
        interpretInstruction.add(new MoveInstruction(gameProcess, gameLogic, stepsDelay));
        interpretInstruction.add(new MoveInstruction(gameProcess, gameLogic, stepsDelay));
        interpretInstruction.add(new RepeatInstruction(this, stepsDelay));

        gameProcess.showGameState(gameLogic.getStartState());
    }
    public GameResult run() {
        gameLogic.restart();
        for(Instruction instruction : instructionList.instructions) {
            boolean interpretResult = interpretInstruction.get(instruction.instructionId).interpret(instruction);
            if(interpretResult) {
                return gameLogic.finish();
            }
            if(instructionList.instructions.indexOf(instruction) != instructionList.instructions.size() - 1) {
                try {
                    Thread.sleep(stepsDelay);
                } catch (InterruptedException e) {
                    return gameLogic.finish();
                }
            }
        }
        return gameLogic.finish();
    }


    InterpretInstruction getInterpret(int id) {
        return interpretInstruction.get(id);
    }
}

