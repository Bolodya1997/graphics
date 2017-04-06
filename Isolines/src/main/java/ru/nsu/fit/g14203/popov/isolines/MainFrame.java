package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.util.JToggleMenuItem;
import ru.nsu.fit.g14203.popov.util.State;
import ru.nsu.fit.g14203.popov.util.WaitingDialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.KeyEvent;
import java.io.*;

public class MainFrame extends AbstractMainFrame {

    private final static Runnable NO_ACTION = () -> {};

    private MainPanel mainPanel;

    @Override
    protected JPanel createMainPanel() {
        mainPanel = new MainPanel(this::setStatusBarHint, this::resetStatusBar);
        return mainPanel;
    }

    public MainFrame() {
        super("Isolines");

        Icon openIcon           = new ImageIcon(MainFrame.class.getResource("Open.png"));

        Icon isolinesIcon       = new ImageIcon(MainFrame.class.getResource("Isolines.png"));
        Icon gridIcon           = new ImageIcon(MainFrame.class.getResource("Grid.png"));
        Icon pointsIcon         = new ImageIcon(MainFrame.class.getResource("Points.png"));
        Icon interpolationIcon  = new ImageIcon(MainFrame.class.getResource("Interpolation.png"));

        Icon setAreaIcon          = new ImageIcon(MainFrame.class.getResource("Set_area.png"));
        Icon clearIcon          = new ImageIcon(MainFrame.class.getResource("Clear.png"));

        Icon aboutIcon          = new ImageIcon(MainFrame.class.getResource("About.png"));

//        ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//              ------   Open   ------
        JMenuItem openMenuIten = addMenuItem(fileMenu, "Open", openIcon, KeyEvent.VK_O,
                "Open a new config file", this::openAction);
//              ------      ------
        fileMenu.addSeparator();
//              ------   Exit   ------
        addMenuItem(fileMenu, "Exit", null, KeyEvent.VK_E,
                "Exit the application", () -> System.exit(0));
//        ------   View   ------
        JMenu viewMenu = addMenu("View", KeyEvent.VK_V);
//              ------   Isolines   ------
        JToggleMenuItem isolinesToggleMenuItem = addToggleMenuItem(viewMenu, "Isolines", isolinesIcon,
                KeyEvent.VK_I, "Show isolines", "Hide isolines",
                NO_ACTION, mainPanel.getIsolinesShown(), mainPanel.getFunctionLoaded());
//              ------      ------
        viewMenu.addSeparator();
//              ------   Grid   ------
        JToggleMenuItem gridToggleMenuItem = addToggleMenuItem(viewMenu, "Grid", gridIcon, KeyEvent.VK_G,
                "Show grid", "Hide grid", NO_ACTION,
                mainPanel.getGridShown(), mainPanel.getFunctionLoaded());
//              ------   Points   ------
        JToggleMenuItem pointsToggleMenuItem = addToggleMenuItem(viewMenu, "Points", pointsIcon, KeyEvent.VK_P,
                "Show points", "Hide points", NO_ACTION,
                mainPanel.getPointsShown(), mainPanel.getFunctionLoaded());
//              ------      ------
        viewMenu.addSeparator();
//              ------   Interpolation   ------
        JToggleMenuItem interpolationToggleMenuItem = addToggleMenuItem(viewMenu, "Interpolation",
                interpolationIcon, KeyEvent.VK_I,
                "Turn interpolation on", "Turn interpolation off",
                NO_ACTION, mainPanel.getInterpolationOn(), mainPanel.getFunctionLoaded());
//        ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
//              ------   Set area   ------
        JMenuItem setAreaMenuItem = addMenuItem(editMenu, "Set area", setAreaIcon, KeyEvent.VK_A,
                "Set custom function area", this::setAreaAction, mainPanel.getFunctionLoaded());
//              ------   Clear isoline   ------
        JMenuItem clearMenuItem = addMenuItem(editMenu, "Clear isolines", clearIcon, KeyEvent.VK_C,
                "Clear user isoline", mainPanel::clearIsoline, mainPanel.getFunctionLoaded());
//        ------   Help   ------
        JMenu helpMenu = addMenu("Help", KeyEvent.VK_H);
//              ------   About   ------
        addMenuItem(helpMenu, "About", aboutIcon, KeyEvent.VK_A,
                "Show information about the application", this::showAboutDialog);

//        ------   top toolBar   ------
        JToolBar topToolBar = addToolBar(TOP);
//              ------   Open   ------
        addToolBarButton(topToolBar, openMenuIten);
//              ------      ------
        topToolBar.addSeparator();
//              ------   Isolines   ------
        addToolBarToggleButton(topToolBar, isolinesToggleMenuItem);
//              ------      ------
        topToolBar.addSeparator();
//              ------   Points   ------
        addToolBarToggleButton(topToolBar, pointsToggleMenuItem);
//              ------   Grid   ------
        addToolBarToggleButton(topToolBar, gridToggleMenuItem);
//              ------      ------
        topToolBar.addSeparator();
//              ------   Interpolation   ------
        addToolBarToggleButton(topToolBar, interpolationToggleMenuItem);
//              ------      ------
        topToolBar.addSeparator();
//              ------   Set area   ------
        addToolBarButton(topToolBar, setAreaMenuItem);
//              ------   Clear isoline   ------
        addToolBarButton(topToolBar, clearMenuItem);

//        ------  end of init   ------
        setVisible(true);
    }

    /**
     *  File format:
     *  X Y         isoline grid
     *  n           borders count
     *  R0 G0 B0
     *  ...
     *  Rn Gn Bn
     *  R G B       isoline color
     */
    private void loadFunction(Reader reader) throws Exception {
        State ready = new State(false);
        Exception[] exception = { null };

        new Thread(() -> {
            try {
                mainPanel.loadFunction(reader);
            } catch (Exception e) {
                exception[0] = e;
            }
            ready.setState(true);
        }).start();

        new WaitingDialog(this, ready);
        if (exception[0] != null)
            throw exception[0];
    }

    private void openAction() {
        JFileChooser fileChooser = new JFileChooser("FIT_14203_Popov_Vladimir_Isolines_Data");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                return f.isDirectory() || name.endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text files (.txt)";
            }
        });

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileChooser.getSelectedFile()));
            loadFunction(reader);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Bad config file", "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setAreaAction() {
        new AreaDialog(this, mainPanel.getFrom(), mainPanel.getTo());

        State ready = new State(false);
        new Thread(() -> {
            mainPanel.setArea();
            ready.setState(true);
        }).start();

        new WaitingDialog(this, ready);
    }
}
