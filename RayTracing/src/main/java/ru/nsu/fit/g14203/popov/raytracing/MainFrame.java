package ru.nsu.fit.g14203.popov.raytracing;

import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;

import javax.swing.*;

public class MainFrame extends AbstractMainFrame {

    private MainPanel mainPanel;

    public MainFrame() {
        super("Ray tracing");

//        ------   end of init   ------
        setVisible(true);
    }

    @Override
    protected JPanel createMainPanel() {
        if (mainPanel == null)
            mainPanel = new MainPanel();

        return mainPanel;
    }
}
