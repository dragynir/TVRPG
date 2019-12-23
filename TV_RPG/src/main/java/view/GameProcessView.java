package view;

import UnitedClasses.GameResult;
import UnitedClasses.GameState;
import UnitedClasses.IconSet;
import UnitedClasses.R;
import controllers.GameProcess;
import view.view_bars.GameControlBar;
import view.view_bars.OptionBar;

import javax.swing.*;
import java.awt.*;

public class GameProcessView extends JPanel {

    private GameView gameView;
    private TextArea textField;
    private OptionBar optionBar;
    private GameControlBar controlBar;

    private GameProcess gameProcess;

    public GameProcessView(GameProcess gameProcess){


        this.gameProcess = gameProcess;

        gameView = new GameView(this);
        textField = new TextArea(null, 40, 56);
        //textField.setPreferredSize( new Dimension( R.CODE_FIELD_SIZE_X, 0 ) );
        textField.setBackground(new Color(159,255,255));
        textField.setForeground(new Color(43,64,255));

        optionBar = new OptionBar(gameProcess);
        //optionBar.setAlignmentX(Component.RIGHT_ALIGNMENT);


        controlBar = new GameControlBar(this);


        //setLayout(new BoxLayout(this , BoxLayout.Y_AXIS));
        setLayout(new FlowLayout(FlowLayout.RIGHT));





        /*JPanel horizontalPanel = new JPanel();
        //horizontalPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
        horizontalPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        JPanel leftOffset = new JPanel();
        leftOffset.setPreferredSize(new Dimension(R.LEFT_OFFSET, 0));
        //leftOffset.setVisible(false);
        horizontalPanel.add(leftOffset);
        // gameView.setPreferredSize( new Dimension(R.GAME_VIEW_SIZE_X, 0 ) );

        horizontalPanel.add(textField);
        JPanel rightOffset = new JPanel();
        //rightOffset.setVisible(false);
        rightOffset.setOpaque(false);
        rightOffset.setPreferredSize(new Dimension(R.RIGHT_OFFSET, R.GAME_VIEWS_SIZE_Y));
        horizontalPanel.add(rightOffset);



        JPanel sepPanel = new JPanel();
        sepPanel.setOpaque(false);
        //sepPanel.setBackground(new Color(34,14,12));
        //sepPanel.setVisible(false);
        sepPanel.setPreferredSize(new Dimension(R.SEPARATE_PANEL_SIZE_X, R.GAME_VIEWS_SIZE_Y));
        horizontalPanel.add(sepPanel);
        horizontalPanel.add(gameView);*/




        /*JPanel downP = new JPanel();

        downP.setLayout(new BoxLayout(downP, BoxLayout.Y_AXIS));

        downP.add(horizontalPanel);
        downP.add(controlBar);


        JPanel ofP = new JPanel();
        ofP.setLayout(new BoxLayout(ofP, BoxLayout.X_AXIS));
        ofP.add(downP);
        JPanel ofR = new JPanel();
        ofR.setPreferredSize(new Dimension(100, 0));
        ofP.add(ofR);*/

        JPanel rightOffset = new JPanel();
        //rightOffset.setVisible(false);
        rightOffset.setOpaque(false);
        rightOffset.setPreferredSize(new Dimension(R.RIGHT_OFFSET, R.GAME_VIEWS_SIZE_Y));


        JPanel leftOffset = new JPanel();
        leftOffset.setOpaque(false);
        leftOffset.setPreferredSize(new Dimension(R.LEFT_OFFSET, 0));


        JPanel sepPanel = new JPanel();
        sepPanel.setOpaque(false);
        sepPanel.setPreferredSize(new Dimension(R.SEPARATE_PANEL_SIZE_X, R.GAME_VIEWS_SIZE_Y));

        JPanel vertical_panel = new JPanel();
        vertical_panel.setLayout(new BoxLayout(vertical_panel, BoxLayout.Y_AXIS));
        vertical_panel.add(optionBar);
        vertical_panel.add(textField);
        vertical_panel.add(controlBar);




        vertical_panel.setOpaque(false);
        controlBar.setOpaque(false);
        optionBar.setOpaque(false);

        //add(leftOffset);


        add(gameView);
        add(sepPanel);

        add(vertical_panel);

        add(rightOffset);
        //add(ofP);
        //add(controlBar, BorderLayout.SOUTH);

        this.setOpaque(false);
    }

    public void updateTextField(String code){
        textField.setText(code);
    }

    public void saveCodeFromTextField(){
        gameProcess.updateTmpCodeStorage(textField.getText());
        System.out.println("Curr.size");
        System.out.println(gameView.getSize());
    }

    public void runCodeFromTexField(){
        gameProcess.updateTmpCodeStorage(textField.getText());
        gameProcess.runCode();
    }


    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        ImageIcon icon =  (ImageIcon) IconSet.getIconByName(R.GAME_BACKGROUND);
        g.drawImage(icon.getImage(), 0,0,this.getWidth(),this.getHeight(),this);
    }



    public void showGameState(GameState gameState){
        gameView.showGameState(gameState);
    }

    /*public NextAction getNextAction(GameResult gameResult){
        return null;
    }*/


    // resutlDialogWindow() : ModalWindow
}
