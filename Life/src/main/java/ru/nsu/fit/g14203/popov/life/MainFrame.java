package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MutableBoolean;
import ru.nsu.fit.g14203.popov.life.util.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private StatusBar statusBar = new StatusBar();

    private GamePanel gamePanel = new GamePanel();
    private JScrollPane gameScrollPane = new JScrollPane(gamePanel);

    public MainFrame() {
        super("Life");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        ------   size   ------
        setMinimumSize(new Dimension(800, 600));

//        ------   location   ------
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);

//        ------   icons   ------
        ImageIcon newIcon = new ImageIcon(MainFrame.class.getResource("New.png"));
        ImageIcon openIcon = new ImageIcon(MainFrame.class.getResource("Open.png"));
        ImageIcon saveIcon = new ImageIcon(MainFrame.class.getResource("Save.png"));
        ImageIcon exitIcon = new ImageIcon(MainFrame.class.getResource("Exit.png"));
        ImageIcon aboutIcon = new ImageIcon(MainFrame.class.getResource("About.png"));
        ImageIcon resetIcon = new ImageIcon(MainFrame.class.getResource("Reset.png"));
        ImageIcon gridSettingsIcon = new ImageIcon(MainFrame.class.getResource("Grid_settings.png"));

//        ------   menus   ------
        setJMenuBar(menuBar);
//              ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//                      ------   New   ------
        JMenuItem newMenuItem = addMenuItem(fileMenu, "New", newIcon, KeyEvent.VK_N,
                "Create new empty field", this::newAction);
//                      ------   Open   ------
        JMenuItem openMenuItem = addMenuItem(fileMenu, "Open", openIcon, KeyEvent.VK_O,
                "Open a field in game", this::openAction);
//                      ------   Save   ------
        JMenuItem saveMenuItem = addMenuItem(fileMenu, "Save", saveIcon, KeyEvent.VK_S,
                "Save field", this::saveAction);
//                      ------      ------
        fileMenu.addSeparator();
//                      ------   Exit   ------
        addMenuItem(fileMenu, "Exit", exitIcon, KeyEvent.VK_X,
                "Quit game", this::exitAction);
//              ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
//                      ------   Reset   ------
        JMenuItem resetMenuItem = addMenuItem(editMenu, "Reset", resetIcon, KeyEvent.VK_R,
                "Reset field", this::resetAction);
//              ------   View   ------
        JMenu viewMenu = addMenu("View", KeyEvent.VK_V);
//                      ------   Grid settings   ------
        addMenuItem(viewMenu, "Grid settings", gridSettingsIcon, KeyEvent.VK_T,
                "Edit grid settings", this::settingsAction);
//              ------   Help   ------
        JMenu helpMenu = addMenu("Help", KeyEvent.VK_H);
//                      ------   About   ------
        JMenuItem aboutMenuItem = addMenuItem(helpMenu, "About", aboutIcon, KeyEvent.VK_A,
                "Show information about Life", this::aboutAction);

//        ------   layout   ------
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

//        ------   toolbar   ------
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        add(toolBar);
        gridBagLayout.addLayoutComponent(toolBar, new GridBagConstraints(0, 0, 1,
                1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 16));
//        ------   New   ------
        addToolbarButton(newMenuItem);
//        ------   Open   ------
        addToolbarButton(openMenuItem);
//        ------   Save   ------
        addToolbarButton(saveMenuItem);
//        ------      -------
        toolBar.addSeparator();
//        ------   Reset   ------
        addToolbarButton(resetMenuItem);
//        ------      -------
        toolBar.addSeparator();
//        ------   About   ------
        addToolbarButton(aboutMenuItem);

//        ------   game panel   ------
        gameScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(gameScrollPane);
        gridBagLayout.addLayoutComponent(gameScrollPane, new GridBagConstraints(0, 1, 1,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

//        ------   status bar   ------
        add(statusBar);
        gridBagLayout.addLayoutComponent(statusBar, new GridBagConstraints(0, 2, 1,
                1, 1.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

//        ------   end of init   ------
        setVisible(true);
    }

    private JMenu addMenu(String name, int mnemonic) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);

        menuBar.add(menu);
        return menu;
    }

    private JMenuItem addMenuItem(JMenu menu,
                                  String name, Icon icon, int mnemonic,
                                  String statusBarText,
                                  Runnable action) {
        JMenuItem menuItem = new JMenuItem(name, icon);
        menuItem.setMnemonic(mnemonic);

        statusBar.addComponent(menuItem, statusBarText);
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setActiveComponent(menuItem);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setActiveComponent(null);
            }
        });

        menuItem.addActionListener(e -> {
            statusBar.setActiveComponent(null);
            action.run();
        });

        menu.add(menuItem);
        return menuItem;
    }

    private JButton addToolbarButton(JMenuItem menuItem) {
        JButton button = new JButton(menuItem.getIcon());
        button.setPreferredSize(new Dimension(16, 16));
        button.setMargin(new Insets(1, 1, 1, 1));

        button.setToolTipText(menuItem.getText());
        button.addMouseListener(menuItem.getMouseListeners()[1]);

        button.addActionListener(menuItem.getActionListeners()[0]);

        toolBar.add(button);
        return button;
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

    private void resetAction() {
        gamePanel.clearGrid();
        gamePanel.repaint();
    }

    private void settingsAction() {
        Grid grid = gamePanel.getGrid();
        Settings settings = new Settings(grid.getSettings());

        MutableBoolean changed = new MutableBoolean(false);
        new SettingsDialog(this, settings, changed);

        if (changed.isTrue()) {
            gamePanel.recountGrid(settings);
            gamePanel.repaint();
        }
    }

    private void aboutAction() {
        JOptionPane.showMessageDialog(this, "FIT NSU\ng14203\nPopov Vladimir", "About",
                                      JOptionPane.PLAIN_MESSAGE);
    }
}
