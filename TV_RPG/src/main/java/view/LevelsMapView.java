package view;

import UnitedClasses.IconSet;
import UnitedClasses.R;
import UnitedClasses.R_PATH;
import controllers.LevelsMap;
import music.GameSounds;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



// TODO set background music

public class LevelsMapView extends JPanel {
    private ScreenManager screenManager;
    private List<JToggleButton> levels_buttons;

    private JToggleButton exitButton;



    public LevelsMapView(ScreenManager screenManager){



        this.screenManager = screenManager;
        levels_buttons = new ArrayList<>();
        this.setOpaque(false);
        this.setLayout(null);
        this.setOpaque(false);


        /*ImageIcon icon =  (ImageIcon) IconSet.getIconByName(R.LVL_BUTTON_BACKGROUND);
        JLabel contentPane = new JLabel();
        contentPane.setIcon(icon);
        contentPane.setLayout( new BorderLayout() );
        setCon( contentPane );*/



    }

    public void initView(int levels_count){

        ImageIcon out_icon =  (ImageIcon) IconSet.getIconByName(R.OUT_IMAGE);
        ImageIcon out_icon_pressed =  (ImageIcon) IconSet.getIconByName(R.OUT_PRESSED_IMAGE);
        exitButton = AnimButtons.getNewButton(out_icon, out_icon_pressed);
        exitButton.setPreferredSize(
                new Dimension(R.OPTION_BUTTON_SIZE_X, R.OPTION_BUTTON_SIZE_Y));


        int x_pos = R.SCREEN_SIZE_X - R.OPTION_BUTTON_SIZE_X;

        int y_pos = R.SCREEN_SIZE_Y;


        x_pos = 1400;
        //exitButton.setLocation(1450, 200);
        exitButton.setBounds(R.EXIT_BUTTON_POS_X, R.EXIT_BUTTON_POS_Y, R.CONTROL_BUTTON_SIZE_X, R.CONTROL_BUTTON_SIZE_Y);


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int result = JOptionPane.showConfirmDialog(
                        screenManager,
                        "Выйти из игры?",
                        "Выход из игры",
                        JOptionPane.YES_NO_OPTION);
                if(JOptionPane.YES_OPTION == result){
                    screenManager.exit();
                }


            }
        });
        add(exitButton);


        assert levels_count > 0;

        Random random = new Random();

        ImageIcon island_icon = (ImageIcon)IconSet.getIconByName(R.ISLAND_IMAGE);
        ImageIcon island_use_icon = (ImageIcon)IconSet.getIconByName(R.ISLAND_USE_IMAGE);

        for(int i = 0; i < levels_count; ++i){
            JToggleButton levelButton = AnimButtons.getNewButton(island_icon, null);

            // TODO lock sometimes, no shine
            levelButton.setRolloverIcon(island_use_icon);
            //levelButton.setSelectedIcon(island_icon);


            levelButton.setPreferredSize(
                    new Dimension(R.LVL_BUTTON_SIZE, R.LVL_BUTTON_SIZE));




            x_pos = R.LVL_BUTTON_SIZE  * i * 2;

            y_pos = random.nextInt(R.SCREEN_SIZE_Y / 2);

            levelButton.setBounds(x_pos, y_pos, R.LVL_BUTTON_SIZE, R.LVL_BUTTON_SIZE);

            //levelButton.setBorder(new RoundedBorder(R.LVL_BUTTON_RADIUS)); //10 is the radius
            //levelButton.setForeground(Color.GREEN);



            /*levelButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!getVisibleRect().contains(e.getPoint())) {
                        levelButton.setIcon(island_use_icon);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    levelButton.setIcon(island_icon);
                }
            });*/

            JToggleButton finalLevelButton = levelButton;

            levelButton.addActionListener(e ->
            {
                    screenManager.openLevel(levels_buttons.indexOf(finalLevelButton));
                    finalLevelButton.setSelected(false);

            });


            // levelButton.setFocusPainted(false);
            // levelButton.setBorderPainted(false);
            // levelButton.setContentAreaFilled(false);

            levels_buttons.add(levelButton);
            add(levelButton);
        }
    }


    /*@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("paint");
        ImageIcon icon =  (ImageIcon) IconSet.getIconByName(R.LVL_BUTTON_BACKGROUND);
        g.drawImage(icon.getImage(), 0, 0, null);
    }*/

    /*@Override
    public void paint(Graphics g) {
        ImageIcon icon =  (ImageIcon) IconSet.getIconByName(R.GAME_BACKGROUND);
        g.drawImage(icon.getImage(), 0, 0, this);
        System.out.println(icon.getImage().getHeight(this));
        super.paint(g);
    }*/

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        ImageIcon icon =  (ImageIcon) IconSet.getIconByName(R.GAME_BACKGROUND);
        g.drawImage(icon.getImage(), 0,0,this.getWidth(),this.getHeight(),this);
    }


}
