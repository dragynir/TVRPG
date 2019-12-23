package controllers;

import UnitedClasses.IconSet;
import UnitedClasses.R;
import UnitedClasses.R_PATH;
import view.LevelsMapView;
import view.RoundedBorder;
import view.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelsMap implements GameController {


    public final static String NAME = "levels_map";
    private LevelsMapView view;




    public LevelsMap(ScreenManager screenManager){


        view = new LevelsMapView(screenManager);
        //view.setOpaque(false);

        int levels_count = getLevelsCount();

        if(levels_count < 0){
            assert 0 == 1;
        }
        view.initView(levels_count);
    }




    @Override
    public JPanel getView() {
        return view;
    }

    private int getLevelsCount(){

        File config_file = new File(R_PATH.res_path + R.configs_file);
        int levels_count = -1;

        try {
            InputStream inputStream = new FileInputStream(config_file);
            Scanner scanner = new Scanner(inputStream);

            if(scanner.hasNextLine()){
                String lvls_count_line = scanner.nextLine();
                System.out.println(lvls_count_line);
                System.out.println(R.config_line_regex);

                Pattern pattern = Pattern.compile(R.config_line_regex, Pattern.CASE_INSENSITIVE);

                Matcher matcher = pattern.matcher(lvls_count_line);

                if(matcher.find()) {
                    String config_name = matcher.group(1);
                    levels_count = Integer.parseInt(matcher.group(2));
                }else{
                    System.out.println("No match");
                    System.exit(0);
                }

                assert levels_count == 9;

            }else{

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }


        return levels_count;
    }

}
