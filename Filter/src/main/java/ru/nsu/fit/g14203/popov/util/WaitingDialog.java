package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;
import java.util.Observer;

public class WaitingDialog extends JDialog {

    private final static Icon[] waitingIcons =
                  { new ImageIcon(WaitingDialog.class.getResource("Waiting1.png")),
                    new ImageIcon(WaitingDialog.class.getResource("Waiting2.png")),
                    new ImageIcon(WaitingDialog.class.getResource("Waiting3.png")),
                    new ImageIcon(WaitingDialog.class.getResource("Waiting4.png")),
                    new ImageIcon(WaitingDialog.class.getResource("Waiting5.png")),
                    new ImageIcon(WaitingDialog.class.getResource("Waiting6.png")),
                    new ImageIcon(WaitingDialog.class.getResource("Waiting7.png")),
                    new ImageIcon(WaitingDialog.class.getResource("Waiting8.png")) };

    private Timer timer;
    private int state = 0;

    private State ready;
    private Observer readyObserver;

    public WaitingDialog(JFrame owner, State ready) {
        super(owner, true);
        setSize(200, 100);
        setResizable(false);
        setUndecorated(true);

        setLocationRelativeTo(owner);

        this.ready = ready;

//        ------   label   ------
        JLabel label = new JLabel("Waiting...", waitingIcons[0], JLabel.CENTER);
        add(label);

        timer = new Timer(125, a -> {
            state = (state + 1) % 8;
            label.setIcon(waitingIcons[state]);
        });

        this.ready = ready;
        readyObserver = (o, arg) -> close();
        synchronized (ready) {
            if (ready.isTrue()) {
                dispose();
                return;
            }

            ready.addObserver(readyObserver);
            timer.start();
        }

        setVisible(true);
    }

    private void close() {
        timer.stop();
        ready.deleteObserver(readyObserver);

        dispose();
    }
}
