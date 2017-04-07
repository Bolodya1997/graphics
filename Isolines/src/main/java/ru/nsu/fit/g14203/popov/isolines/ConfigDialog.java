package ru.nsu.fit.g14203.popov.isolines;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Point2D;

class ConfigDialog extends JDialog {

    private Point2D.Double from ;
    private Point2D.Double to;

    private int gridWidth;
    private int gridHeight;

    ConfigDialog(Frame owner,
                 Point2D.Double from, Point2D.Double to,
                 int gridWidth, int gridHeight) {
        super(owner, "Customize image", true);

        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(owner);

        setLayout(new GridBagLayout());

        this.from = from;
        this.to = to;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        addPointBlock("from", 0);
        addPointBlock("to", 1);

        addGridBlock("rows", 2);
        addGridBlock("colimns", 3);
    }

    int[] getValues() {
        setVisible(true);
        return new int[] { gridWidth, gridHeight };
    }

    private void addPointBlock(String name, int pos) {
        GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, pos,
                1, 1,
                1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0);

        Point2D.Double point, other;
        boolean greater;
        if (pos == 0) {
            point = from;
            other = to;
            greater = false;
        } else {
            point = to;
            other = from;
            greater = true;
        }

//        ------   label   ------
        JLabel label = new JLabel(name);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, constraints);

//        ------   xTextField   ------
        JTextField xTextField = new JTextField(String.format("%.3f", point.x));
        Runnable xAction = () -> {
            String text = String.format("%.3f", point.x);

            double x;
            try {
                x = Double.parseDouble(xTextField.getText());
            } catch (Exception e) {
                xTextField.setText(text);
                return;
            }

            if (greater && x <= other.x || !greater && x >= other.x) {
                xTextField.setText(text);
                return;
            }

            xTextField.setText(String.format("%.3f", x));
            point.x = x;
        };

        xTextField.addActionListener(e -> xAction.run());
        xTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                xAction.run();
            }
        });

        add(xTextField, constraints);

//        ------   yTextField   ------
        JTextField yTextField = new JTextField(String.format("%.3f", point.y));
        Runnable yAction = () -> {
            String text = String.format("%.3f", point.y);

            double y;
            try {
                y = Double.parseDouble(yTextField.getText());
            } catch (Exception e) {
                yTextField.setText(text);
                return;
            }

            if (greater && y <= other.y || !greater && y >= other.y) {
                yTextField.setText(text);
                return;
            }

            yTextField.setText(String.format("%.3f", y));
            point.y = y;
        };

        yTextField.addActionListener(e -> yAction.run());
        yTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                yAction.run();
            }
        });

        add(yTextField, constraints);
    }

    private void addGridBlock(String name, int pos) {
        GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, pos,
                1, 1,
                1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0);

//        ------   label   ------
        JLabel label = new JLabel(name);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, constraints);

//        ------   textField   ------
        JTextField textField = new JTextField(Integer.toString((pos == 2) ? gridWidth : gridHeight));

        Runnable textFieldAction = () -> {
            String text = Integer.toString((pos == 2) ? gridWidth : gridHeight);
            int val;
            try {
                val = Integer.decode(textField.getText());
            } catch (Exception e) {
                textField.setText(text);
                return;
            }

            if (val <= 0) {
                textField.setText(text);
                return;
            }

            if (pos == 2)
                gridWidth = val;
            else
                gridHeight = val;

            textField.setText(Integer.toString(val));
        };

        textField.addActionListener(e -> textFieldAction.run());
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textFieldAction.run();
            }
        });

        constraints.gridwidth = 2;
        add(textField, constraints);
    }
}
