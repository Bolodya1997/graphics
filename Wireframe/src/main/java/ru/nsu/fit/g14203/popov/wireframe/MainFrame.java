package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.wireframe.figures.MainPanel;

import javax.swing.*;

public class MainFrame extends AbstractMainFrame {

    private MainPanel mainPanel;

    protected JPanel createMainPanel() {
        if (mainPanel == null)
            mainPanel = new MainPanel();

        return mainPanel;
    }

    public MainFrame() {
        super("Wireframe");

//        ------   end of init   ------
        setVisible(true);
    }
}
