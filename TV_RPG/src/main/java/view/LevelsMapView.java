package view;

import UnitedClasses.IconSet;
import UnitedClasses.R;
import controllers.LevelsMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelsMapView extends JPanel {
    private ScreenManager screenManager;
    private List<JButton> levels_buttons;

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

        for(int i = 0; i < levels_count; ++i){

            JButton levelButton = new JButton("+");

            x_pos = R.LVL_BUTTON_SIZE  * i * 2 + R.LVL_BUTTON_SIZE;

            y_pos = random.nextInt(R.SCREEN_SIZE_Y / 2);

            levelButton.setBounds(x_pos, y_pos, R.LVL_BUTTON_SIZE, R.LVL_BUTTON_SIZE);

            levelButton.setBorder(new RoundedBorder(R.LVL_BUTTON_RADIUS)); //10 is the radius
            levelButton.setForeground(Color.GREEN);

            levelButton.addActionListener(e ->
                    screenManager.openLevel(levels_buttons.indexOf(levelButton)));



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
