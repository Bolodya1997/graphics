package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.spline.Spline;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineDialog;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineOwner;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class MainFrame extends AbstractMainFrame {

    private MainPanel mainPanel;

    private Map<Spline, SplineFigure3D> figures = new HashMap<>();
    {
        SplineOwner splineOwner = SplineOwner.getInstance();
        splineOwner.addActionOnAdd(spline -> {
            SplineFigure3D figure3D = new SplineFigure3D(spline);
            figures.put(spline, figure3D);

            mainPanel.addFigure(figure3D);
            splineOwner.addObserver(figure3D);
        });
        splineOwner.addActionOnRemove(spline -> {
            SplineFigure3D figure3D = figures.remove(spline);

            mainPanel.removeFigure(figure3D);
            splineOwner.deleteObserver(figure3D);
        });

        Observer updater = (o, arg) -> mainPanel.update();
        splineOwner.addObserver(updater);
    }

    private SplineDialog splineDialog = new SplineDialog(this, spline -> {
        FigureMover figureMover = FigureMover.getInstance();
        if (spline == null)
            figureMover.setFigure(null);
        else
            figureMover.setFigure(figures.get(spline));
        mainPanel.update();
    });

    protected JPanel createMainPanel() {
        if (mainPanel == null)
            mainPanel = new MainPanel();

        return mainPanel;
    }

    public MainFrame() {
        super("Wireframe");

        Icon splinesIcon = new ImageIcon(MainFrame.class.getResource("Function.png"));

        Icon resetIcon = new ImageIcon(MainFrame.class.getResource("Clear.png"));

//        ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//        ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
//              ------   Splines   ------
        JMenuItem splinesMenuItem = addMenuItem(editMenu, "Splines", splinesIcon, KeyEvent.VK_S,
                "Open splines edit dialog", this::splinesAction);
//              ------   Reset   ------
        JMenuItem resetMenuItem = addMenuItem(editMenu, "Reset", resetIcon, KeyEvent.VK_R,
                "Reset camera rotation", mainPanel::resetFigures);

        JToolBar topToolBar = addToolBar(TOP);
//              ------   Splines   ------
        addToolBarButton(topToolBar, splinesMenuItem);
//              ------   Reset   ------
        addToolBarButton(topToolBar, resetMenuItem);

//        ------   end of init   ------
        setVisible(true);
    }

    private void splinesAction() {
        splineDialog.setVisible(true);
    }
}
