package ru.nsu.fit.g14203.popov.wireframe;

import javafx.stage.FileChooser;
import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.spline.Spline;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineDialog;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineOwner;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.*;
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

        Icon openIcon = new ImageIcon(MainFrame.class.getResource("Open.png"));
        Icon saveIcon = new ImageIcon(MainFrame.class.getResource("Save.png"));

        Icon splinesIcon = new ImageIcon(MainFrame.class.getResource("Function.png"));
        Icon initIcon = new ImageIcon(MainFrame.class.getResource("Clear.png"));

//        ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//              ------   Open   ------
        JMenuItem openMenuItem = addMenuItem(fileMenu, "Open", openIcon, KeyEvent.VK_O,
                "Load scene from file", this::openAction);
//              ------   Save   ------
        JMenuItem saveMenuItem = addMenuItem(fileMenu, "Save", saveIcon, KeyEvent.VK_S,
                "Save scene into file", this::saveAction);
//        ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
//              ------   Splines   ------
        JMenuItem splinesMenuItem = addMenuItem(editMenu, "Splines", splinesIcon, KeyEvent.VK_S,
                "Open splines edit dialog", this::splinesAction);
//              ------   Init   ------
        JMenuItem initMenuItem = addMenuItem(editMenu, "Init", initIcon, KeyEvent.VK_I,
                "Set camera rotation to default", mainPanel::resetFigures);

        JToolBar topToolBar = addToolBar(TOP);
//              ------   Open   ------
        addToolBarButton(topToolBar, openMenuItem);
//              ------   Save   ------
        addToolBarButton(topToolBar, saveMenuItem);
//              ------   Splines   ------
        addToolBarButton(topToolBar, splinesMenuItem);
//              ------   Reset   ------
        addToolBarButton(topToolBar, initMenuItem);

//        ------   end of init   ------
        setVisible(true);
    }

    private void openAction() {
        JFileChooser fileChooser = new JFileChooser("FIT_14203_Popov_Vladimir_Life_Wireframe_Data");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            InputStream stream = new FileInputStream(fileChooser.getSelectedFile());
            FileLoader.load(stream, figures);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while reading data",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAction() {
        JFileChooser fileChooser = new JFileChooser("FIT_14203_Popov_Vladimir_Life_Wireframe_Data");
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            OutputStream stream = new FileOutputStream(fileChooser.getSelectedFile());
            FileLoader.save(stream, figures);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while saving data",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void splinesAction() {
        splineDialog.setVisible(true);
    }
}
