package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class StatusBar extends JPanel {

    private JLabel hintLabel = new JLabel("Ready");

    private JLabel capsLabel = new JLabel("CAP");
    private JLabel numLabel = new JLabel("NUM");
    private JLabel scrollLabel = new JLabel("SCR");

    StatusBar() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

//        ------   Hint area   ------
        add(hintLabel);
        gridBagLayout.addLayoutComponent(hintLabel, new GridBagConstraints(0, 0, 1,
                1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));

        GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1,
                1, 0.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0,5), 0, 0);

//        ------   CAP   ------
        add(capsLabel);
        gridBagLayout.addLayoutComponent(capsLabel, constraints);
//        ------   NUM   ------
        add(numLabel);
        gridBagLayout.addLayoutComponent(numLabel, constraints);
//        ------   SCR   ------
        add(scrollLabel);
        gridBagLayout.addLayoutComponent(scrollLabel, constraints);
    }

    void setHint(String hint) {
        hintLabel.setText(hint);
    }

    void reset() {
        hintLabel.setText("Ready");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        capsLabel.setEnabled(toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK));
        numLabel.setEnabled(toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK));
        scrollLabel.setEnabled(toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK));

        g.clearRect(0, 0, getWidth(), getHeight());
        repaint();
    }
}
