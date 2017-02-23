package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.JRadioMenuItem;
import ru.nsu.fit.g14203.popov.life.util.MutableBoolean;
import ru.nsu.fit.g14203.popov.life.util.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainFrame extends JFrame {

    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private StatusBar statusBar = new StatusBar();

    private GamePanel gamePanel = new GamePanel();
    private JScrollPane gameScrollPane = new JScrollPane(gamePanel);

    private JRadioMenuItem XORRadioMenuItem;

    private java.util.List<JComponent> onPlay = new LinkedList<>();

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
        ImageIcon stepIcon = new ImageIcon(MainFrame.class.getResource("Step.png"));
        ImageIcon startIcon = new ImageIcon(MainFrame.class.getResource("Start.png"));
        ImageIcon stopIcon = new ImageIcon(MainFrame.class.getResource("Stop.png"));
        ImageIcon XORModeIcon = new ImageIcon(MainFrame.class.getResource("XOR_mode.png"));
        ImageIcon replaceModeIcon = new ImageIcon(MainFrame.class.getResource("Replace_mode.png"));
        ImageIcon impactIcon = new ImageIcon(MainFrame.class.getResource("Impact.png"));
        ImageIcon colorsIcon = new ImageIcon(MainFrame.class.getResource("Colors.png"));

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
        addMenuItem(fileMenu, "Exit", exitIcon, KeyEvent.VK_E,
                "Quit game", this::exitAction);
//              ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
//                      ------   Grid settings   ------
        addMenuItem(editMenu, "Grid settings", gridSettingsIcon, KeyEvent.VK_S,
                "Edit grid settings", this::settingsAction);
//                      ------   ------
        editMenu.addSeparator();
//                      ------   XOR | Replace   ------
        XORRadioMenuItem = addRadioMenuItem(editMenu,
                "XOR", XORModeIcon, KeyEvent.VK_X,
                "Replace", replaceModeIcon, KeyEvent.VK_R,
                "Inverse cell state on click", "Set cell alive",
                () -> gamePanel.getReplace().setState(false), () -> gamePanel.getReplace().setState(true));
//              ------   Action   ------
        JMenu actionMenu = addMenu("Action", KeyEvent.VK_A);
//                      ------   Reset   ------
        JMenuItem resetMenuItem = addMenuItem(actionMenu, "Reset", resetIcon, KeyEvent.VK_R,
                "Reset field", this::resetAction);
        onPlay.add(resetMenuItem);
//                      ------   Start | Stop   ------
        JRadioMenuItem playRadioMenuItem = addRadioMenuItem(actionMenu,
                "Start", startIcon, KeyEvent.VK_S,
                "Stop", stopIcon, KeyEvent.VK_S,
                "Start game", "Stop game",
                this::startAction, this::stopAction);
//                      ------   Step   ------
        JMenuItem stepMenuItem = addMenuItem(actionMenu, "Step", stepIcon, KeyEvent.VK_T,
                "Make single step", this::stepAction);
        onPlay.add(stepMenuItem);
//              ------   View   ------
        JMenu viewMenu = addMenu("View", KeyEvent.VK_V);
//                      ------   Impact   ------
        JRadioMenuItem impactRadioMenuItem = addRadioMenuItem(viewMenu,
                "Impact", impactIcon, KeyEvent.VK_I,
                "Impact", impactIcon, KeyEvent.VK_I,
                "Show impact values", "Hide impact values",
                () -> gamePanel.getImpact().setState(true), () -> gamePanel.getImpact().setState(false));
//                      ------   Colors   ------
        JRadioMenuItem colorsRadioMenuItem = addRadioMenuItem(viewMenu,
                "Colors", colorsIcon, KeyEvent.VK_I,
                "Colors", colorsIcon, KeyEvent.VK_I,
                "Show impact base colors", "Hide impact base colors",
                this::enableColors, this::disableColor);
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
                new Insets(0, 0, 0, 0), 0, 0));
//        ------   New   ------
        addToolbarButton(newMenuItem);
//        ------   Open   ------
        addToolbarButton(openMenuItem);
//        ------   Save   ------
        addToolbarButton(saveMenuItem);
//        ------      -------
        toolBar.addSeparator();
//        ------   Impact   -------
        addToolBarToggleButton(impactRadioMenuItem);
//        ------   Colors   ------
        addToolBarToggleButton(colorsRadioMenuItem);
//        ------      -------
        toolBar.addSeparator();
//        ------   XOR | Replace   ------
        addToolBarToggleButton(XORRadioMenuItem);
//        ------      -------
        toolBar.addSeparator();
//        ------   Reset   ------
        addToolbarButton(resetMenuItem);
//        ------   Play | Stop   ------
        addToolBarToggleButton(playRadioMenuItem);
//        ------   Step   ------
        addToolbarButton(stepMenuItem);
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
        button.setMargin(new Insets(1, 1, 1, 1));

        button.setToolTipText(menuItem.getText());
        button.addMouseListener(menuItem.getMouseListeners()[1]);

        button.addActionListener(menuItem.getActionListeners()[0]);

        if (onPlay.contains(menuItem))
            onPlay.add(button);

        toolBar.add(button);
        return button;
    }

    private JRadioMenuItem addRadioMenuItem(JMenu menu,
                                            String defaultName, Icon defaultIcon, int defaultMnemonic,
                                            String selectedName, Icon selectedIcon, int selectedMnemonic,
                                            String defaultStatusBarText, String selectedStatusBarText,
                                            Runnable startAction, Runnable stopAction) {
        JRadioMenuItem radioMenuItem = new JRadioMenuItem(defaultName, defaultIcon, defaultMnemonic,
                                                          selectedName, selectedIcon, selectedMnemonic);

        final JComponent __selected = new JButton();
        statusBar.addComponent(__selected, selectedStatusBarText);
        statusBar.addComponent(radioMenuItem, defaultStatusBarText);
        radioMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (radioMenuItem.isSelected())
                    statusBar.setActiveComponent(__selected);
                else
                    statusBar.setActiveComponent(radioMenuItem);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setActiveComponent(null);
            }
        });

        radioMenuItem.addActionListener(e -> {
            statusBar.setActiveComponent(null);

            boolean selected = radioMenuItem.isSelected();
            if (selected)
                startAction.run();
            else
                stopAction.run();
        });

        menu.add(radioMenuItem);
        return radioMenuItem;
    }

    private JToggleButton addToolBarToggleButton(JRadioMenuItem radioMenuItem) {
        JToggleButton button = new JToggleButton(radioMenuItem.getDefaultIcon());
        button.setSelectedIcon(radioMenuItem.getSelectedIcon());
        button.setMargin(new Insets(1, 1, 1, 1));

        button.setToolTipText(radioMenuItem.getDefaultText());
        button.addMouseListener(radioMenuItem.getMouseListeners()[1]);

        button.addActionListener(e -> {
            radioMenuItem.doClick();
            if (button.isSelected()) {
                button.setToolTipText(radioMenuItem.getSelectedText());
            } else {
                button.setToolTipText(radioMenuItem.getDefaultText());
            }
        });

        radioMenuItem.addActionListener(e -> button.setSelected(radioMenuItem.isSelected()));

        if (onPlay.contains(radioMenuItem))
            onPlay.add(button);

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

    private void settingsAction() {
        Settings settings = new Settings(gamePanel.getGridSettings());
        MutableBoolean changed = new MutableBoolean(false);

        MutableBoolean replace = new MutableBoolean(gamePanel.getReplace().isTrue());

        new SettingsDialog(this, settings, changed, replace);

        if (changed.isTrue()) {
            gamePanel.recountGrid(settings);
        }

        if (replace.isTrue() ^ gamePanel.getReplace().isTrue())
            XORRadioMenuItem.doClick();

    }

    private void resetAction() {
        gamePanel.clearGrid();
        gamePanel.repaint();
    }

    private void stepAction() {
        gamePanel.step();
    }

    private void startAction() {
        for (JComponent jComponent : onPlay)
            jComponent.setEnabled(false);

        gamePanel.play();
    }

    private void stopAction() {
        for (JComponent jComponent : onPlay)
            jComponent.setEnabled(true);

        gamePanel.stop();
    }

    private void aboutAction() {
        JOptionPane.showMessageDialog(this, "FIT NSU\ng14203\nPopov Vladimir", "About",
                                      JOptionPane.PLAIN_MESSAGE);
    }

    private void enableColors() {
        gamePanel.getColors().setState(true);
        gamePanel.fillCells();
        gamePanel.repaint();
    }

    private void disableColor() {
        gamePanel.getColors().setState(false);
        gamePanel.fillCells();
        gamePanel.repaint();
    }
}
