package ru.nsu.fit.g14203.popov.life.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Life");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        ------   size   ------
        setMinimumSize(new Dimension(800, 600));

//        ------   location   ------
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - 400, screenSize.height / 2 - 300);

//        ------   icons   ------
        ImageIcon exitIcon = new ImageIcon(MainFrame.class.getResource("Exit.png"));
        ImageIcon aboutIcon = new ImageIcon(MainFrame.class.getResource("About.png"));

//        ------   menus   ------
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
//              ------   File   ------
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
//                      ------   Exit   ------
        JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
        exitMenuItem.addActionListener(e -> closeWindow());
        fileMenu.add(exitMenuItem);
//              ------   Edit   ------
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
//              ------   View   ------
        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
//              ------   Help   ------
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
//                      ------   About   ------
        JMenuItem aboutMenuItem = new JMenuItem("About", aboutIcon);
        aboutMenuItem.addActionListener(e -> about());
        helpMenu.add(aboutMenuItem);

//        ------   toolbar   ------
//        JToolBar toolBar = new JToolBar();
//        add(toolBar);

        setVisible(true);   //  end of init
    }

    private void closeWindow() {
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void about() {
        JOptionPane.showMessageDialog(this, "FIT 14203 Popov", "About",
                                      JOptionPane.PLAIN_MESSAGE);
    }
}
