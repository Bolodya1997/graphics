package ru.nsu.fit.g14203.popov.isolines;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Point2D;

class AreaDialog extends JDialog {

    private Point2D.Double from ;
    private Point2D.Double to;

    AreaDialog(Frame owner, Point2D.Double from, Point2D.Double to) {
        super(owner, "Set area", true);

        setSize(300, 100);
        setResizable(false);
        setLocationRelativeTo(owner);

        setLayout(new GridBagLayout());

        this.from = from;
        this.to = to;

        addBlock("from", 0);
        addBlock("to", 1);

        setVisible(true);
    }

    private void addBlock(String name, int pos) {
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
}
