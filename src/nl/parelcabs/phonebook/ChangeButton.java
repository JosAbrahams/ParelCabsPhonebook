package nl.parelcabs.phonebook;

import javax.swing.*;
import java.awt.*;

public class ChangeButton extends JButton {

    public ChangeButton(){
        super();
        setPreferredSize(new Dimension(25,25));
        Icon icon = createImageIcon("icons/edit.png",
                "Pencil, indicating a 'modify' function");
        setIcon(icon);
    }

    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
