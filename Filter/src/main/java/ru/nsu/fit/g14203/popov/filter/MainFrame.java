package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.util.DefaultMainFrame;
import ru.nsu.fit.g14203.popov.util.JToggleMenuItem;
import ru.nsu.fit.g14203.popov.util.Saver;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFrame extends DefaultMainFrame {

    private final static Runnable NO_ACTION = () -> {};

    private Saver saver = new Saver("FIT_14203_Popov_Vladimir_Filter_Data");

    private FilterPanel filterPanel;

    public MainFrame() {
        super("MainFrame", new FilterPanel());

        filterPanel = (FilterPanel) mainPanel;

//        ------   icons   ------
        ImageIcon newIcon = new ImageIcon(MainFrame.class.getResource("New.png"));
        ImageIcon openIcon = new ImageIcon(MainFrame.class.getResource("Open.png"));
        ImageIcon saveAsIcon = new ImageIcon(MainFrame.class.getResource("Save_as.png"));
        ImageIcon exitIcon = new ImageIcon(MainFrame.class.getResource("Exit.png"));
        ImageIcon selectIcon = new ImageIcon(MainFrame.class.getResource("Select.png"));

//        ------   menus   ------
//              ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//                      ------   New   ------
        JMenuItem newMenuItem = addMenuItem(fileMenu, "New", newIcon, KeyEvent.VK_N,
                "Create a new document", NO_ACTION);
//                      ------   Open   ------
        JMenuItem openMenuItem = addMenuItem(fileMenu, "Open", openIcon, KeyEvent.VK_O,
                "Open a new image", this::openAction);
//                      ------   Save   ------
        JMenuItem saveAsMenuItem = addMenuItem(fileMenu, "Save as...", saveAsIcon, KeyEvent.VK_S,
                "Save the resulting image with a new name", NO_ACTION);
//                      ------      ------
        fileMenu.addSeparator();
//                      ------   Exit   ------
        addMenuItem(fileMenu, "Exit", exitIcon, KeyEvent.VK_E,
                "Quit Filter", NO_ACTION);
//              ------   View   ------
        JMenu viewMenu = addMenu("View", KeyEvent.VK_V);
//                      ------   Select   ------
        JToggleMenuItem selectToggleMenuItem = addToggleMenuItem(viewMenu, "Select", selectIcon,
                KeyEvent.VK_S, "Enable selection", "Disable selection",
                NO_ACTION, filterPanel.getSelectEnable());

//        ------   tool bars   ------
//              ------   TOP   ------
        JToolBar topToolBar = addToolbar(TOP);
//                      ------   New   ------
        addToolBarButton(topToolBar, newMenuItem);
//                      ------   Open   ------
        addToolBarButton(topToolBar, openMenuItem);
//                      ------   Save as   ------
        addToolBarButton(topToolBar, saveAsMenuItem);
//                      ------      ------
        topToolBar.addSeparator();
//                      ------   Select   ------
        addToolBarToggleButton(topToolBar, selectToggleMenuItem);

//        ------   end of init   ------
        setVisible(true);
    }

    private void openAction() {
        JFileChooser fileChooser = new JFileChooser("FIT_14203_Popov_Vladimir_Filter_Data");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                return f.isDirectory() || name.endsWith(".bmp") || name.endsWith(".png");
            }

            @Override
            public String getDescription() {
                return null;
            }
        });

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            filterPanel.openImage(new FileInputStream(fileChooser.getSelectedFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
