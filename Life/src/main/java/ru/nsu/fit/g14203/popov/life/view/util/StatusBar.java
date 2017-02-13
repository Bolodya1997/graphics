package ru.nsu.fit.g14203.popov.life.view.util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * | Hint area                      | CAP | NUM | SCR |
 */
public class StatusBar extends JPanel {

    private JLabel hintLabel = new JLabel("Hint area");

    private JLabel capsLabel = new JLabel("CAP");
    private JLabel numLabel = new JLabel("NUM");
    private JLabel scrollLabel = new JLabel("SCR");

    private Map<JComponent, String> hints = new HashMap<>();

    public StatusBar() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

//        ------   Hint area   ------
        hintLabel.setPreferredSize(new Dimension(16, 16));
        add(hintLabel);
        gridBagLayout.addLayoutComponent(hintLabel, new GridBagConstraints(0, 0, 1,
                1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
//        ------   CAP   ------
        capsLabel.setPreferredSize(new Dimension(16, 16));
        add(capsLabel);
        gridBagLayout.addLayoutComponent(capsLabel, new GridBagConstraints(1, 0, 1,
                1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
//        ------   NUM   ------
        numLabel.setPreferredSize(new Dimension(16, 16));
        add(numLabel);
        gridBagLayout.addLayoutComponent(numLabel, new GridBagConstraints(1, 0, 1,
                1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
//        ------   SCR   ------
        scrollLabel.setPreferredSize(new Dimension(16, 16));
        add(scrollLabel);
        gridBagLayout.addLayoutComponent(scrollLabel, new GridBagConstraints(1, 0, 1,
                1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
    }
}
