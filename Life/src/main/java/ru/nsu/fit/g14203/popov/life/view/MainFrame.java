package ru.nsu.fit.g14203.popov.life.view;

import ru.nsu.fit.g14203.popov.life.view.util.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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
        ImageIcon newIcon = new ImageIcon(MainFrame.class.getResource("New.png"));
        ImageIcon openIcon = new ImageIcon(MainFrame.class.getResource("Open.png"));
        ImageIcon saveIcon = new ImageIcon(MainFrame.class.getResource("Save.png"));
        ImageIcon exitIcon = new ImageIcon(MainFrame.class.getResource("Exit.png"));
        ImageIcon aboutIcon = new ImageIcon(MainFrame.class.getResource("About.png"));

//        ------   menus   ------
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
//              ------   File   ------
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
//                      ------   New   ------
        JMenuItem newMenuItem = new JMenuItem("New", newIcon);
        newMenuItem.addActionListener(e -> newAction());
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        fileMenu.add(newMenuItem);
//                      ------   Open   ------
        JMenuItem openMenuItem = new JMenuItem("Open", openIcon);
        newMenuItem.addActionListener(e -> openAction());
        newMenuItem.setMnemonic(KeyEvent.VK_O);
        fileMenu.add(openMenuItem);
//                      ------   Save   ------
        JMenuItem saveMenuItem = new JMenuItem("Save", saveIcon);
        newMenuItem.addActionListener(e -> saveAction());
        newMenuItem.setMnemonic(KeyEvent.VK_S);
        fileMenu.add(saveMenuItem);
//                      ------      ------
        fileMenu.addSeparator();
//                      ------   Exit   ------
        JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
        exitMenuItem.addActionListener(e -> exitAction());
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        fileMenu.add(exitMenuItem);
//              ------   Edit   ------
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);
//              ------   View   ------
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);
//              ------   Help   ------
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);
//                      ------   About   ------
        JMenuItem aboutMenuItem = new JMenuItem("About", aboutIcon);
        aboutMenuItem.addActionListener(e -> aboutAction());
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        helpMenu.add(aboutMenuItem);

//        ------   toolbar   ------
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        add(toolBar);
//        ------   New   ------
        JButton newButton = new JButton(newIcon);
        newButton.setToolTipText("New");
        newButton.addActionListener(e -> newAction());
        addOnToolbar(toolBar, newButton);
//        ------   Open   ------
        JButton openButton = new JButton(openIcon);
        openButton.setToolTipText("Open");
        openButton.addActionListener(e -> openAction());
        addOnToolbar(toolBar, openButton);
//        ------   Save   ------
        JButton saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(e -> saveAction());
        addOnToolbar(toolBar, saveButton);
//        ------      -------
        toolBar.addSeparator();
//        ------   About   ------
        JButton aboutButton = new JButton(aboutIcon);
        aboutButton.setToolTipText("About");
        aboutButton.addActionListener(e -> aboutAction());
        addOnToolbar(toolBar, aboutButton);

//        ------   status bar   ------
        StatusBar statusBar = new StatusBar();
        add(statusBar);

//        ------   layout   ------
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

//        ------   toolbar location   ------
        gridBagLayout.addLayoutComponent(toolBar, new GridBagConstraints(0, 0, 1,
                1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 10));
//        ------   game panel location   ------
        JPanel panel = new JPanel();    //  tmp
        add(panel);
        gridBagLayout.addLayoutComponent(panel, new GridBagConstraints(0, 1, 1,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
//        ------   status bar location   ------
        gridBagLayout.addLayoutComponent(statusBar, new GridBagConstraints(0, 2, 1,
                1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 10));

        setVisible(true);   //  end of init
    }

    private void addOnToolbar(JToolBar toolBar, JButton button) {
        button.setPreferredSize(new Dimension(16, 16));
        button.setMargin(new Insets(1, 1, 1, 1));
        toolBar.add(button);
    }

    private void newAction() {

    }

    private void openAction() {

    }

    private void saveAction() {

    }

    private void exitAction() {
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void aboutAction() {
        JOptionPane.showMessageDialog(this, "FIT NSU     g14203     Popov Vladimir", "About",
                                      JOptionPane.PLAIN_MESSAGE);
    }
}
