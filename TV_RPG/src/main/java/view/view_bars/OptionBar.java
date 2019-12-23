package view.view_bars;

import UnitedClasses.IconSet;
import UnitedClasses.R;
import controllers.GameProcess;
import controllers.LevelsMap;
import view.AnimButtons;
import view.ScreenManager;
import view.diolog_views.ExitDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionBar extends JPanel {

    private ScreenManager screenManager;

    private GameProcess gameProcess;

    private JToggleButton backToMapButton;

    private JToggleButton showHelpButton;

    private JToggleButton exitButton;



    // TODO make fun jtogglebutton constructor +
    // TODO change basic JOption dialogs
    // TODO google JOptionPain Decoration
    // 1) change color
    // 2) change background

    public OptionBar(GameProcess gameProcess){

        this.gameProcess = gameProcess;
        this.screenManager = gameProcess.getScreenManager();


        ImageIcon help_icon =  (ImageIcon) IconSet.getIconByName(R.HELP_IMAGE);
        ImageIcon help_icon_pressed =  (ImageIcon) IconSet.getIconByName(R.HELP_PRESSED_IMAGE);
        showHelpButton = AnimButtons.getNewButton(help_icon, help_icon_pressed);
        showHelpButton.setPreferredSize(
                new Dimension(R.OPTION_BUTTON_SIZE_X, R.OPTION_BUTTON_SIZE_Y));



        // showHelpButton.setRolloverEnabled(false);




        showHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(screenManager,
                R.HELP_INFO);
            }
        });



        //showHelpButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        //setAlignmentX(Component.RIGHT_ALIGNMENT);


        /*backToMapButton = new JButton("", back_icon);
        backToMapButton.setBorderPainted(false);
        backToMapButton.setContentAreaFilled(false);*/
        //backToMapButton.setFocusPainted(false);


        ImageIcon back_icon =  (ImageIcon) IconSet.getIconByName(R.BACK_TO_MAP_IMAGE);
        ImageIcon back_icon_pressed =  (ImageIcon) IconSet.getIconByName(R.BACK_TO_MAP_PRESSED_IMAGE);
        backToMapButton = AnimButtons.getNewButton(back_icon, back_icon_pressed);
        backToMapButton.setPreferredSize(
                new Dimension(R.OPTION_BUTTON_SIZE_X, R.OPTION_BUTTON_SIZE_Y));



        backToMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                int result = JOptionPane.showConfirmDialog(
                        screenManager,
                        "Вернуться на карту уровней?",
                        "Возврат на карту уровней",
                        JOptionPane.YES_NO_OPTION);
                if(JOptionPane.YES_OPTION == result){
                    screenManager.changePanel(LevelsMap.NAME);
                }
            }
        });






        ImageIcon out_icon =  (ImageIcon) IconSet.getIconByName(R.OUT_IMAGE);
        ImageIcon out_icon_pressed =  (ImageIcon) IconSet.getIconByName(R.OUT_PRESSED_IMAGE);
        exitButton = AnimButtons.getNewButton(out_icon, out_icon_pressed);
        exitButton.setPreferredSize(
                new Dimension(R.OPTION_BUTTON_SIZE_X, R.OPTION_BUTTON_SIZE_Y));



        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int result = JOptionPane.showConfirmDialog(
                        screenManager,
                        "Выйти с сохранением?",
                        "Выход из игры",
                        JOptionPane.YES_NO_OPTION);
                if(JOptionPane.YES_OPTION == result){
                    gameProcess.saveCode();
                    screenManager.exit();
                }else if(JOptionPane.NO_OPTION == result){
                    screenManager.exit();
                }
            }
        });

        //backToMapButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        /*JPanel rightOffset = new JPanel();
        rightOffset.setPreferredSize(new Dimension(R.RIGHT_OFFSET, 0));
        rightOffset.setOpaque(false);*/

        add(backToMapButton);
        add(showHelpButton);
        add(exitButton);
        //add(rightOffset);
    }



}
