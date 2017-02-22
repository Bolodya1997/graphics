package ru.nsu.fit.g14203.popov.life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class SettingsDialog extends JDialog {

    private int gridWidth = 10;
    private int gridHeight = 10;
    private int size = 2;
    private int width = 35;

    SettingsDialog(JFrame owner) {
        super(owner, "Settings", true);
        setVisible(false);

//        ------   size   ------
        setSize(new Dimension(300, 300));
        setResizable(false);

//        ------   location   ------
        Point ownerLocation = owner.getLocation();
        Dimension ownerSize = owner.getSize();
        setLocation(ownerLocation.x + (ownerSize.width - getWidth()) / 2,
                ownerLocation.y + (ownerSize.height - getHeight()) / 2);

//        ------   exit   ------
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnSettings((MainFrame) owner);
            }
        });

//        ------   end of init   ------
        setVisible(true);
    }

    private void returnSettings(MainFrame mainFrame) {
        mainFrame.setGridSettings(gridWidth, gridHeight, size, width);
    }
}
