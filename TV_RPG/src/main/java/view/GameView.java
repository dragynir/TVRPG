package view;

import UnitedClasses.Character;
import UnitedClasses.Coordinate;
import UnitedClasses.GameState;
import UnitedClasses.IconSet;
import UnitedClasses.R;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {


    private GameState currGameState;

    private ImageIcon icon_cat;
    private ImageIcon icon_rock;
    private ImageIcon icon_spike;

    public GameView(GameProcessView view){
        setPreferredSize(new Dimension(R.GAME_VIEW_SIZE_X, R.GAME_VIEWS_SIZE_Y));
        setSize(new Dimension(R.GAME_VIEW_SIZE_X, R.GAME_VIEWS_SIZE_Y));
        setMaximumSize(new Dimension(R.GAME_VIEW_SIZE_X, R.GAME_VIEWS_SIZE_Y));
        //setBackground(new Color(34,124,12));

        loadIcons();
        setOpaque(false);
        setDoubleBuffered(true);
    }

    private void loadIcons(){
        icon_cat =  (ImageIcon) IconSet.getIconByName(R.CAT_IMAGE);
        icon_rock =  (ImageIcon) IconSet.getIconByName(R.ROCK_IMAGE);
        icon_spike =  (ImageIcon) IconSet.getIconByName(R.SPIKE_IMAGE);
    }


    public void showGameState(GameState gameState){
        currGameState = gameState;
        //revalidate();
        repaint();
        System.out.println("Cat go");
        //paintComponent(getGraphics());
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        //g.clearRect(0, 0, getHeight(), getWidth());

        Graphics2D g2 = (Graphics2D)g;

        if(null != currGameState){

            for(Coordinate c : currGameState.borders){
                g2.drawImage(icon_rock.getImage(), c.x * R.GAME_CELL_SIZE,
                        c.y * R.GAME_CELL_SIZE, this);
            }

            for(Coordinate c : currGameState.deathTraps){
                g2.drawImage(icon_spike.getImage(), c.x * R.GAME_CELL_SIZE,
                        c.y * R.GAME_CELL_SIZE, this);
            }


            for(Coordinate c : currGameState.escapes){
                //g.drawImage(icon_rock.getImage(), c.x * R.GAME_CELL_SIZE,
                        //c.y * R.GAME_CELL_SIZE, this);
            }


            System.out.println(currGameState.characters.size());


            for(Character c: currGameState.characters){

                if(!c.isAlive){

                }


                Coordinate pos = c.position;
                //g2.drawImage(null, pos.x * R.GAME_CELL_SIZE,
                        //pos.y * R.GAME_CELL_SIZE, this);
                g2.drawImage(icon_cat.getImage(), pos.x * R.GAME_CELL_SIZE,
                        pos.y * R.GAME_CELL_SIZE, this);


            }


        }








        /*ImageIcon icon_cat =  (ImageIcon) IconSet.getIconByName(R.CAT_IMAGE);
        ImageIcon icon_rock =  (ImageIcon) IconSet.getIconByName(R.ROCK_IMAGE);
        ImageIcon icon_spike =  (ImageIcon) IconSet.getIconByName(R.SPIKE_IMAGE);
        for(int i = 0;  i < 30; i++){
            g.drawImage(icon_cat.getImage(), i * 48, 0, this);
        }
        for(int i = 0;  i < 30; i++){
            g.drawImage(icon_rock.getImage(), i * 48, 48, this);
        }
        for(int i = 0;  i < 30; i++){
            g.drawImage(icon_spike.getImage(), i * 48, 96, this);
        }*/

        //g.drawImage(icon.getImage(), 0, 0, this);

    }

    @Override
    public void paint(Graphics g) {
        ImageIcon icon =  (ImageIcon) IconSet.getIconByName(R.GAME_VIEW_BACK);
        g.drawImage(icon.getImage(), 0, 0, this);
        System.out.println(icon.getImage().getHeight(this));
        super.paint(g);
    }

}
