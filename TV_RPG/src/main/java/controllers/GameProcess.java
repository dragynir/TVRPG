package controllers;

import Instructions.InstructionList;
import Interpreter.Interpreter;
import Parser.Parser;
import UnitedClasses.*;
import music.GameSounds;
import view.GameProcessView;
import view.GameView;
import view.LevelsMapView;
import view.ScreenManager;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class GameProcess implements GameController {


    public final static String NAME = "game_process";
    private int level_number = 0;

    private TemporaryCodeStorage codeStorage;
    private GameProcessView view;
    private ScreenManager screenManager;


    private InstructionList instructionList;
    private Parser parser;
    private Interpreter interpreter;

    private Thread gameThread;

    // private Map<String, Integer> gameConfigs;




    /*public void saveCode(String code){
        codeStorage.setCode(code);
        System.out.println("On save");
    }*/

    public GameProcess(ScreenManager screenManager, int level_number){
        this.screenManager = screenManager;



        System.out.println("open lvl: " + level_number);

        this.level_number = level_number;

        view = new GameProcessView(this);
        codeStorage = new TemporaryCodeStorage();
        //gameConfigs

        instructionList = new InstructionList();



        try {
            System.out.println("Lever number is: " + level_number);
            parser = new Parser(codeStorage, instructionList);
            interpreter = new Interpreter(this, instructionList, level_number);
        } catch (IOException e) {
            System.out.println("GG");
            e.printStackTrace();
        }
        initCode();
    }


    private void initCode(){

        //StringBuffer code = new StringBuffer();
        String code = null;
        //File f = new File(R_PATH.res_path + "/levels/level_" + this.level_number + "/save");


        try {
            code = new String(Files.readAllBytes(Paths.get(R_PATH.res_path + "/levels/level_" + this.level_number + "/save")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        /*try {
            FileInputStream fileInputStream =  new FileInputStream(f);


            Scanner scanner = new Scanner(fileInputStream);

            while (scanner.hasNextLine()){
                code.append(scanner.nextLine());

            }


        } catch (IOException e) {
            e.printStackTrace();
        }*/

        if(null != code) {
            codeStorage.setCode(code.toString());
            view.updateTextField(code.toString());
        }

    }

    public ScreenManager getScreenManager(){
        return screenManager;
    }



    @Override
    public JPanel getView() {
        return view;
    }





    public void showGameState(GameState gameState){
        view.showGameState(gameState);
    }

    public void runCode(){



        if(null != gameThread && gameThread.isAlive()){
            return;
        }


        boolean result =  parser.parse();
        if(!result){
            JOptionPane.showMessageDialog(screenManager,
                    "Compile error");
        }else{

            gameThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    GameResult result =  interpreter.run();

                    manageGameResult(result);

                }
            });
            gameThread.start();
        }
    }

    private void manageGameResult(GameResult result){

        String resultString  = "You win";
        Clip result_clip = null;
        if(!result.isWin){
            result_clip = GameSounds.getClipSound(R_PATH.LOSE_SOUND);
            if(null != result_clip){
                // result_clip.start();
            }
            resultString = "Lose";
        }

        String[] options = new String[] {"back to map", "retry", "next level"};

        int response = JOptionPane.showOptionDialog(null, resultString, "Title",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if(0 == response){
            if(null != result_clip){
                result_clip.stop();
            }
            screenManager.changePanel(LevelsMap.NAME);
        }else if(2 == response){
            if(null != result_clip){
                result_clip.stop();
            }
            screenManager.openLevel(level_number + 1);
        }

    }


    public void saveCode(){
        view.saveCodeFromTextField();
    }

    public void updateTmpCodeStorage(String code){
        codeStorage.setCode(code);


        File f = new File(R_PATH.res_path + "/levels/level_" + this.level_number + "/save");
        try {
            FileOutputStream fileOutputStream =  new FileOutputStream(f);
            fileOutputStream.write(code.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(code);
        System.out.println("On save");
    }

}
