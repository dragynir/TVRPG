package UnitedClasses;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class IconSet {
    static private Map<String , Icon> icons = new HashMap<>();

    public static void loadIcons(){
        icons.put(R.LVL_BUTTON_BACKGROUND, new ImageIcon(R_PATH.MAP_BACKGROUND));
        icons.put(R.GAME_BACKGROUND, new ImageIcon(R_PATH.GAME_BACKGROUND));
        icons.put(R.CAT_IMAGE, new ImageIcon(R_PATH.CAT_IMAGE));
        icons.put(R.ROCK_IMAGE, new ImageIcon(R_PATH.ROCK_IMAGE));
        icons.put(R.SPIKE_IMAGE, new ImageIcon(R_PATH.SPIKE_IMAGE));
        icons.put(R.GAME_VIEW_BACK, new ImageIcon(R_PATH.GAME_VIEW_BACK));


        icons.put(R.SAVE_IMAGE, new ImageIcon(R_PATH.SAVE_IMAGE));
        icons.put(R.SAVE_PRESSED_IMAGE, new ImageIcon(R_PATH.SAVE_PRESSED_IMAGE));


        icons.put(R.RUN_IMAGE, new ImageIcon(R_PATH.RUN_IMAGE));
        icons.put(R.RUN_PRESSED_IMAGE, new ImageIcon(R_PATH.RUN_PRESSED_IMAGE));


        icons.put(R.BACK_TO_MAP_IMAGE, new ImageIcon(R_PATH.BACK_TO_MAP_IMAGE));
        icons.put(R.BACK_TO_MAP_PRESSED_IMAGE, new ImageIcon(R_PATH.BACK_TO_MAP_PRESSED_IMAGE));



        icons.put(R.HELP_IMAGE, new ImageIcon(R_PATH.HELP_IMAGE));
        icons.put(R.HELP_PRESSED_IMAGE, new ImageIcon(R_PATH.HELP_PRESSED_IMAGE));

        icons.put(R.OUT_IMAGE, new ImageIcon(R_PATH.OUT_IMAGE));
        icons.put(R.OUT_PRESSED_IMAGE, new ImageIcon(R_PATH.OUT_PRESSED_IMAGE));


        System.out.println("load");
        System.out.println(R_PATH.MAP_BACKGROUND);
        System.out.println(icons.get(R.LVL_BUTTON_BACKGROUND).getIconHeight());
    }

    public static Icon getIconByName(String name){
        return icons.get(name);
    }
}