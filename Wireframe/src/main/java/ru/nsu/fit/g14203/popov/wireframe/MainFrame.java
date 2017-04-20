package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.wireframe.figures.MainPanel;
import ru.nsu.fit.g14203.popov.wireframe.spline.Spline;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineDialog;

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

        mainPanel.addFigure(new SplineFigure3D(new Spline()));

//        ------   end of init   ------
        setVisible(true);

//        new SplineDialog(this, null);
    }
}
