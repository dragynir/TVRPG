package view.view_bars;

import UnitedClasses.IconSet;
import UnitedClasses.R;
import controllers.GameProcess;
import view.AnimButtons;
import view.GameProcessView;
import view.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameControlBar extends JPanel {



    private JToggleButton saveCodeButton;

    private JToggleButton runCodeButton;

    public GameControlBar(GameProcessView processView){



        ImageIcon save_icon =  (ImageIcon) IconSet.getIconByName(R.SAVE_IMAGE);
        ImageIcon save_icon_pressed =  (ImageIcon) IconSet.getIconByName(R.SAVE_PRESSED_IMAGE);
        saveCodeButton = AnimButtons.getNewButton(save_icon, save_icon_pressed);

        ImageIcon run_icon =  (ImageIcon) IconSet.getIconByName(R.RUN_IMAGE);
        ImageIcon run_icon_pressed =  (ImageIcon) IconSet.getIconByName(R.RUN_PRESSED_IMAGE);
        runCodeButton = AnimButtons.getNewButton(run_icon, run_icon_pressed);


        runCodeButton.setPreferredSize(
                new Dimension(R.CONTROL_BUTTON_SIZE_X, R.CONTROL_BUTTON_SIZE_Y));

        saveCodeButton.setPreferredSize(
                new Dimension(R.CONTROL_BUTTON_SIZE_X, R.CONTROL_BUTTON_SIZE_Y));

        saveCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processView.saveCodeFromTextField();
            }
        });

        runCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processView.runCodeFromTexField();
            }
        });


        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(saveCodeButton);
        controlPanel.add(runCodeButton);

        /*JPanel rightOffset = new JPanel();
        //rightOffset.setVisible(false);
        rightOffset.setPreferredSize(new Dimension(R.RIGHT_OFFSET, 0));
        controlPanel.add(rightOffset);*/


        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(controlPanel);
        JPanel side = new JPanel();
        side.setOpaque(false);
        //side.setVisible(false);
        side.setPreferredSize(new Dimension(0, R.SIDE_OFFSET));
        add(side);
        setOpaque(false);

    }




}
