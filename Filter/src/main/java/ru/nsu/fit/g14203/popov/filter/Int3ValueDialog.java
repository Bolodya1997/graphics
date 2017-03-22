package ru.nsu.fit.g14203.popov.filter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

class Int3ValueDialog extends JDialog {

    Int3ValueDialog(Frame owner, String title, Runnable action,
                    int[] min, int[] max,
                    String[] names, int[] values, Consumer<Integer>[] setters) {
        super(owner, title, true);

        setSize(250, 120);
        setResizable(false);

        setLocationRelativeTo(owner);

        setLayout(new GridBagLayout());

        for (int i = 0; i < 3; i++)
            addColor(names[i], min[i], max[i], values[i], setters[i]);

        GridBagConstraints constraints = new GridBagConstraints(1, 4,
                1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0);

//        ------   applyButton   ------
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            dispose();
            action.run();
        });

        add(applyButton, constraints);

//        ------   cancelButton   ------
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        constraints.gridx = 2;
        add(cancelButton, constraints);

        setVisible(true);
    }

    private void addColor(String name, int min, int max, int value, Consumer<Integer> setter) {
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
