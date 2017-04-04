package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.util.State;
import ru.nsu.fit.g14203.popov.util.WaitingDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class MainFrame extends AbstractMainFrame {

    private FunctionPanel functionPanel;

    @Override
    protected JPanel createMainPanel() {
        functionPanel = new FunctionPanel();
        return functionPanel;
    }

    public MainFrame() {
        super("Isolines");

        Icon openIcon = new ImageIcon(MainFrame.class.getResource("Open.png"));

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
        JMenu viewMenu = addMenu("View", KeyEvent.VK_V);    //  grid, isolines, interpolation
//        ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);    //  clear user isolines
//        ------   Help   ------
        JMenu helpMenu = addMenu("Help", KeyEvent.VK_H);    //  about

//        ------   top toolBar   ------
        JToolBar topToolBar = addToolBar(TOP);
//              ------   Open   ------
        addToolBarButton(topToolBar, openMenuIten);

//        ------  end of init   ------
        setVisible(true);

        Reader reader = new StringReader("10 10\n" +
                                            "1\n" +
                                            "0 0 0\n" +
                                            "255 255 255\n" +
                                            "200 0 0\n" +
                                            "-20 -20\n" +
                                            "20 20");
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
     *  x0 y0       from
     *  x1 y1       to
     */
    private void loadFunction(Reader reader) {
        State ready = new State(false);
        new Thread(() -> {
            functionPanel.loadFunction(reader);
            ready.setState(true);
        }).start();

        new WaitingDialog(this, ready);
    }

    private void openAction() {

    }
}
