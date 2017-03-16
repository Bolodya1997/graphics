package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observer;
import java.util.function.Consumer;

public class ActiveChangingDialog extends JDialog {

    private final State ready;
    private Observer readyObserver;

    private int localValue;
    private boolean localValueFilled = true;

    public ActiveChangingDialog(Frame owner, String title, State ready,
                                int min, int max, int value, Consumer<Integer> actionOnChange) {
        super(owner, title, true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        setSize(200, 100);
        setResizable(false);

        setLocationRelativeTo(owner);

        this.ready = ready;
        readyObserver = (o, arg) -> updateValue(actionOnChange);
        ready.addObserver(readyObserver);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE,
                1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0);

//        ------   slider   ------
        JSlider slider = new JSlider(min, max, value);
        add(slider, constraints);

//        ------   text field   ------
        JTextField textField = new JTextField(Integer.toString(value));
        textField.setHorizontalAlignment(JTextField.CENTER);
        add(textField, constraints);

//        ------   linkage   ------
        slider.addChangeListener(e -> {
            int __value = slider.getValue();
            textField.setText(Integer.toString(__value));

            localValue = __value;
            localValueFilled = true;
            updateValue(actionOnChange);
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

            localValue = __value;
            localValueFilled = true;
            updateValue(actionOnChange);
        };
        textField.addActionListener(e -> textFieldAction.run());
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textFieldAction.run();
            }
        });

        this.localValue = value;
        updateValue(actionOnChange);

        setVisible(true);
    }

    private void updateValue(Consumer<Integer> actionOnChange) {
        if (ready.isTrue() && localValueFilled) {
            localValueFilled = false;
            actionOnChange.accept(localValue);
        }
    }

    private void close() {
        ready.deleteObserver(readyObserver);
        new WaitingDialog((JFrame) getOwner(), ready);
    }
}
