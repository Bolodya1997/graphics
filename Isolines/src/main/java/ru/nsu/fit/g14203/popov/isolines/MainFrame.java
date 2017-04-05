package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.Main;
import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.util.JToggleMenuItem;
import ru.nsu.fit.g14203.popov.util.State;
import ru.nsu.fit.g14203.popov.util.WaitingDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class MainFrame extends AbstractMainFrame {

    private final static Runnable NO_ACTION = () -> {};
    private final static String DEFAULT_SETTINGS = "50 50\n" +
                                                   "5\n" +
                                                   "13 41 34\n" +
                                                   "164 225 0\n" +
                                                   "16 25 114\n" +
                                                   "4 225 0\n" +
                                                   "225 225 0\n" +
                                                   "164 225 225\n" +
                                                   "0 0 0\n" +
                                                   "-2 -2\n" +
                                                   "2 2";

    private MainPanel mainPanel;

    @Override
    protected JPanel createMainPanel() {
        mainPanel = new MainPanel(this::setStatusBarHint, this::resetStatusBar);
        return mainPanel;
    }

    public MainFrame() {
        super("Isolines");

        Icon openIcon           = new ImageIcon(MainFrame.class.getResource("Open.png"));

        Icon gridIcon           = new ImageIcon(MainFrame.class.getResource("Grid.png"));
        Icon isolinesIcon       = new ImageIcon(MainFrame.class.getResource("Isolines.png"));
        Icon interpolationIcon  = new ImageIcon(MainFrame.class.getResource("Interpolation.png"));

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
//              ------   Grid   ------
        JToggleMenuItem gridToggleMenuItem = addToggleMenuItem(viewMenu, "Grid", gridIcon, KeyEvent.VK_G,
                "Show grid", "Hide grid", NO_ACTION,
                mainPanel.getGridShown());
//              ------   Isolines   ------
        JToggleMenuItem isolinesToggleMenuItem = addToggleMenuItem(viewMenu, "Isolines", isolinesIcon,
                KeyEvent.VK_I, "Show isolines", "Hide isolines",
                NO_ACTION, mainPanel.getIsolinesShown());
//              ------   Interpolation   ------
        JToggleMenuItem interpolationToggleMenuItem = addToggleMenuItem(viewMenu, "Interpolation",
                interpolationIcon, KeyEvent.VK_I,
                "Turn interpolation on", "Turn interpolation off",
                NO_ACTION, mainPanel.getInterpolationOn());
//        ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
//              ------   Clear isoline   ------
        JMenuItem clearMenuItem = addMenuItem(editMenu, "Clear isolines", clearIcon, KeyEvent.VK_C,
                "Clear user isoline", mainPanel::clearIsoline);
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
//              ------   Grid   ------
        addToolBarToggleButton(topToolBar, gridToggleMenuItem);
//              ------   Isolines   ------
        addToolBarToggleButton(topToolBar, isolinesToggleMenuItem);
//              ------   Interpolation   ------
        addToolBarToggleButton(topToolBar, interpolationToggleMenuItem);
//              ------      ------
        topToolBar.addSeparator();
//              ------   Clear isoline   ------
        addToolBarButton(topToolBar, clearMenuItem);

//        ------  end of init   ------
        setVisible(true);

        Reader reader = new StringReader(DEFAULT_SETTINGS);
        loadFunction(reader);
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
    private void loadFunction(Reader reader) {
        State ready = new State(false);
        new Thread(() -> {
            mainPanel.loadFunction(reader);
            ready.setState(true);
        }).start();

        new WaitingDialog(this, ready);
    }

    private void openAction() {

    }
}
