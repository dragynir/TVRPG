package view;

import UnitedClasses.R;

import javax.swing.*;
import java.awt.*;

public class AnimButtons {
    public static JToggleButton getNewButton(ImageIcon icon, ImageIcon pressedIcon){
        JToggleButton button = new JToggleButton();
        button.setIcon(icon);
        button.setPressedIcon(pressedIcon);
        button.setFocusPainted(false);

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        return button;
    }
}
