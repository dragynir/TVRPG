package view.diolog_views;

import UnitedClasses.IconSet;
import UnitedClasses.R;
import controllers.GameProcess;
import view.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitDialog extends JFrame {


    private ScreenManager screenManager;
    private GameProcess gameProcess;
    private JPanel panel = new ExitPanel();


    private class ExitPanel extends JPanel{
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            ImageIcon icon =  (ImageIcon) IconSet.getIconByName(R.GAME_BACKGROUND);
            g.drawImage(icon.getImage(), 0,0,this.getWidth(),this.getHeight(),this);
        }
    }

    public ExitDialog(GameProcess gameProcess){
        this.gameProcess = gameProcess;
        this.screenManager = gameProcess.getScreenManager();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        setSize(new Dimension(R.EXIT_DIALOG_SIZE_X, R.EXIT_DIALOG_SIZE_Y));
        setLocation(R.GAME_VIEW_SIZE_X/2, R.GAME_VIEWS_SIZE_Y/3);


        setResizable(false);
        panel.setLayout(new BoxLayout(panel , BoxLayout.Y_AXIS));
        panel.setSize(new Dimension(R.EXIT_DIALOG_SIZE_X, R.EXIT_DIALOG_SIZE_Y));
        initView();
    }


    private void initView(){

        JButton exitButton = new JButton("exit");
        exitButton.setPreferredSize(
                new Dimension(R.EXIT_DIALOG_BUTTON_SIZE_X, R.EXIT_DIALOG_BUTTON_SIZE_Y));


        JButton saveOnExitButton = new JButton("save and exit");
        saveOnExitButton.setPreferredSize(
                new Dimension(R.EXIT_DIALOG_BUTTON_SIZE_X, R.EXIT_DIALOG_BUTTON_SIZE_Y));

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenManager.exit();
                ExitDialog.this.setVisible(false);
                ExitDialog.this.dispose();
            }
        });

        saveOnExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameProcess.saveCode();
                screenManager.exit();
                ExitDialog.this.setVisible(false);
                ExitDialog.this.dispose();
            }
        });

        JPanel combinePanel = new JPanel();
        combinePanel.setLayout(new BoxLayout(combinePanel, BoxLayout.X_AXIS));
        combinePanel.setOpaque(false);
        combinePanel.add(exitButton);
        combinePanel.add(saveOnExitButton);

        /*Label optionLabel = new Label("options:");

        add(optionLabel);*/

        //panel.add(optionLabel);
        panel.add(combinePanel);

        add(panel);
    }




}
