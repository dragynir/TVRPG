package view;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import UnitedClasses.IconSet;
import UnitedClasses.R;
import UnitedClasses.R_PATH;
import controllers.*;
import music.GameSounds;

public class ScreenManager extends JFrame{

    private JPanel rootPanel;
    private CardLayout cardLayout;

    private Map<String, GameController> controllers;

    private Clip backgroundClip;


    public ScreenManager(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        controllers = new HashMap<>();
        rootPanel = new JPanel();
        cardLayout = new CardLayout();
        rootPanel.setLayout(cardLayout);

        IconSet.loadIcons();




        //controllers.put(GameProcess.NAME, new GameProcess(this));
        controllers.put(LevelsMap.NAME, new LevelsMap(this));


        //rootPanel.add(controllers.get(GameProcess.NAME).getView(), GameProcess.NAME);
        rootPanel.add(controllers.get(LevelsMap.NAME).getView(), LevelsMap.NAME);
        add(rootPanel);

        cardLayout.show(rootPanel , LevelsMap.NAME);
        setSize(R.SCREEN_SIZE_X, R.SCREEN_SIZE_Y);
        getContentPane().setSize(R.SCREEN_SIZE_X , R.SCREEN_SIZE_Y);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated (true);
        setResizable(false);
        pack();

        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("TitledBorder.border", new LineBorder(Color.BLACK, 1));
        JDialog.setDefaultLookAndFeelDecorated(true);
        UIManager.put("OptionPane.minimumSize",new Dimension(350,200));
        UIManager.put("OptionPane.background", new Color(76, 218, 255));
        UIManager.put("Panel.background", new Color(76, 218, 255));
        UIManager.put("OptionPane.errorDialog.titlePane.foreground", Color.lightGray);
        UIManager.put("OptionPane.errorDialog.titlePane.background", Color.black);
        UIManager.put("OptionPane.errorDialog.border.background", Color.white);


        backgroundClip = GameSounds.getClipSound(R_PATH.BACKGOUND_SOUND);

        if(null != backgroundClip){
            //clip.start();
            backgroundClip.loop(20);
        }
    }


    public void openLevel(int level_number){

        if(null != backgroundClip){
            //clip.start();
           // backgroundClip.stop();
        }

        GameProcess game_controller = new GameProcess(this, level_number);
        rootPanel.add(game_controller.getView(), GameProcess.NAME);
        cardLayout.show(rootPanel, GameProcess.NAME);
    }

    public void changePanel(String screen_name){

        if(null != backgroundClip){
            backgroundClip.loop(20);
        }
        cardLayout.show(rootPanel , screen_name);
    }

    public void exit(){
        setVisible(false);
        dispose();
    }
}
