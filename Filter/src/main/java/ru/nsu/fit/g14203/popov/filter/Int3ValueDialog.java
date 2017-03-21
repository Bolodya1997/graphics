package ru.nsu.fit.g14203.popov.filter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

class Int3ValueDialog extends JDialog {

    private int min;
    private int max;

    Int3ValueDialog(Frame owner, String title, Runnable action,
                    int min, int max,
                    String[] names, int[] values, Consumer<Integer>[] setters) {
        super(owner, title, true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                action.run();
            }
        });

        setSize(250, 100);
        setResizable(false);

        setLocationRelativeTo(owner);

        setLayout(new GridBagLayout());

        this.min = min;
        this.max = max;

        addColor(names[0], values[0], setters[0]);
        addColor(names[1], values[1], setters[1]);
        addColor(names[2], values[2], setters[2]);

        setVisible(true);
    }

    private void addColor(String name, int value, Consumer<Integer> setter) {
        GridBagConstraints constraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE,
                1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0);

//        ------   name   ------
        JLabel label = new JLabel(name);
        label.setHorizontalAlignment(JLabel.CENTER);

        add(label, constraints);

//        ------   slider   ------
        JSlider slider = new JSlider(min, max, value);

        ++constraints.gridx;
        add(slider, constraints);

//        ------   text field   ------
        JTextField textField = new JTextField(Integer.toString(value));
        textField.setHorizontalAlignment(JTextField.CENTER);

        ++constraints.gridx;
        add(textField, constraints);

//        ------   linkage   ------
        slider.addChangeListener(e -> {
            int __value = slider.getValue();
            textField.setText(Integer.toString(__value));

            setter.accept(__value);
        });

        Runnable textFieldAction = () -> {
            int __value;
            try {
                __value = Integer.decode(textField.getText());
            } catch (NumberFormatException e) {
                __value = slider.getValue();
            }

            __value = (__value < min) ? min
                    : (__value > max) ? max
                    : __value;
            slider.setValue(__value);
            textField.setText(Integer.toString(__value));

            setter.accept(__value);
        };
        textField.addActionListener(e -> textFieldAction.run());
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textFieldAction.run();
            }
        });

        setter.accept(value);
    }
}
