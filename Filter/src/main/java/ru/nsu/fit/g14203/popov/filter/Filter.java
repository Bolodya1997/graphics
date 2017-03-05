package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.util.MainFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Filter extends MainFrame {

    private final static Runnable NO_ACTION = () -> {};

    public Filter() {
        super("Filter", new JPanel());

//        ------   icons   ------
        ImageIcon newIcon = new ImageIcon(Filter.class.getResource("New.png"));
        ImageIcon openIcon = new ImageIcon(Filter.class.getResource("Open.png"));

//        ------   menus   ------
//              ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//                      ------   New   ------
        JMenuItem newMenuItem = addMenuItem(fileMenu, "New", newIcon, KeyEvent.VK_N,
                "Create a new document", NO_ACTION);
//                      ------   Open   ------
        JMenuItem openMenuItem = addMenuItem(fileMenu, "Open", openIcon, KeyEvent.VK_O,
                "Open image", NO_ACTION);

//        ------   tool bars   ------
//              ------   TOP   ------
        JToolBar topToolBar = addToolbar(TOP);
//                      ------   New   ------
        addToolBarButton(topToolBar, newMenuItem);
//                      ------   Open   ------
        addToolBarButton(topToolBar, openMenuItem);

//        ------   end of init   ------
        setVisible(true);
    }
}
