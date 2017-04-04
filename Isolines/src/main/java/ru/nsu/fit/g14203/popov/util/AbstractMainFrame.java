package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public abstract class AbstractMainFrame extends JFrame {

    private static final int DEFAULT_WIDTH  = 800;
    private static final int DEFAULT_HEIGHT = 600;

    public static final String LEFT  = BorderLayout.WEST;
    public static final String TOP   = BorderLayout.NORTH;
    public static final String RIGHT = BorderLayout.EAST;

    private HashMap<JComponent, State> enableMap = new HashMap<>();
    private HashMap<AbstractButton, State> selectedMap = new HashMap<>();

    private JMenuBar menuBar = new JMenuBar();
    private StatusBar statusBar = new StatusBar();

    protected abstract JPanel createMainPanel();

    public AbstractMainFrame(String title) {
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        ------   size   ------
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

//        ------   location   ------
        setLocationRelativeTo(null);

//        ------   menu   ------
        setJMenuBar(menuBar);

//        ------   layout   -----
        setLayout(new BorderLayout());

//        ------   main panel   ------
        add(createMainPanel(), BorderLayout.CENTER);

//        ------   status bar   ------
        add(statusBar, BorderLayout.SOUTH);
    }

    public AbstractMainFrame(String title, Runnable exitAction) {
        this(title);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.run();
            }
        });
    }

    private void setEnableState(JComponent component, State enable) {
        component.setEnabled(enable.isTrue());

        Observer enableObserver = (Observable o, Object arg) -> {
            Boolean state = (Boolean) arg;
            component.setEnabled(state);
        };
        enable.addObserver(enableObserver);

        enableMap.put(component, enable);
    }

    private void setSelectedState(AbstractButton button, State selected) {
        button.setSelected(selected.isTrue());

        Observer enableObserver = (Observable o, Object arg) -> {
            Boolean state = (Boolean) arg;
            button.setSelected(state);
        };
        selected.addObserver(enableObserver);

        selectedMap.put(button, selected);
    }

//    ------   menus   ------

    protected JMenu addMenu(String name, int mnemonic) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);

        menuBar.add(menu);
        return menu;
    }

    protected JMenuItem addMenuItem(JMenu menu,
                                    String name, Icon icon, int mnemonic,
                                    String statusBarText,
                                    Runnable action,
                                    State enable) {
        JMenuItem menuItem = new JMenuItem(name, icon);
        menuItem.setMnemonic(mnemonic);

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setHint(statusBarText);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.reset();
            }
        });

        menuItem.addActionListener(e -> {
            statusBar.reset();
            action.run();
        });

        if (enable != null)
            setEnableState(menuItem, enable);

        menu.add(menuItem);
        return menuItem;
    }

    protected JMenuItem addMenuItem(JMenu menu,
                                    String name, Icon icon, int mnemonic,
                                    String statusBarText,
                                    Runnable action) {
        return addMenuItem(menu,
                           name, icon, mnemonic,
                           statusBarText, action,
                           null);
    }

    protected JToggleMenuItem addToggleMenuItem(JMenu menu,
                                                String defaultName, Icon defaultIcon, int defaultMnemonic,
                                                String selectedName, Icon selectedIcon, int selectedMnemonic,
                                                String defaultStatusBarText, String selectedStatusBarText,
                                                Runnable defaultAction, Runnable selectedAction,
                                                State selected, State enable) {
        JToggleMenuItem toggleMenuItem = new JToggleMenuItem(defaultName, defaultIcon, defaultMnemonic,
                                                             selectedName, selectedIcon, selectedMnemonic);

        toggleMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (toggleMenuItem.isSelected())
                    statusBar.setHint(selectedStatusBarText);
                else
                    statusBar.setHint(defaultStatusBarText);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.reset();
            }
        });

        setSelectedState(toggleMenuItem, selected);
        toggleMenuItem.addActionListener(e -> selected.setState(toggleMenuItem.isSelected()));

        toggleMenuItem.addActionListener(e -> {
            statusBar.reset();

            if (selected.isTrue())    //  selection is already inverted
                defaultAction.run();
            else
                selectedAction.run();
        });

        if (enable != null)
            setEnableState(toggleMenuItem, enable);

        menu.add(toggleMenuItem);
        return toggleMenuItem;
    }

    protected JToggleMenuItem addToggleMenuItem(JMenu menu,
                                                String defaultName, Icon defaultIcon, int defaultMnemonic,
                                                String selectedName, Icon selectedIcon, int selectedMnemonic,
                                                String defaultStatusBarText, String selectedStatusBarText,
                                                Runnable defaultAction, Runnable selectedAction,
                                                State selected) {
        return addToggleMenuItem(menu,
                                 defaultName, defaultIcon, defaultMnemonic,
                                 selectedName, selectedIcon, selectedMnemonic,
                                 defaultStatusBarText, selectedStatusBarText,
                                 defaultAction, selectedAction,
                                 selected, null);
    }

    protected JToggleMenuItem addToggleMenuItem(JMenu menu,
                                                String name, Icon icon, int mnemonic,
                                                String defaultStatusBarText, String selectedStatusBarText,
                                                Runnable action,
                                                State selected) {
        return addToggleMenuItem(menu,
                                 name, icon, mnemonic,
                                 name, icon, mnemonic,
                                 defaultStatusBarText, selectedStatusBarText,
                                 action, action,
                                 selected);
    }

    protected JToggleMenuItem addToggleMenuItem(JMenu menu,
                                                String name, Icon icon, int mnemonic,
                                                String defaultStatusBarText, String selectedStatusBarText,
                                                Runnable action,
                                                State selected, State enable) {
        JToggleMenuItem result = addToggleMenuItem(menu,
                                                   name, icon, mnemonic,
                                                   defaultStatusBarText, selectedStatusBarText,
                                                   action,
                                                   selected);
        setEnableState(result, enable);

        return result;
    }

//    ------   toolbars   ------

    protected JToolBar addToolBar(String side) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        add(toolBar, side);
        return toolBar;
    }

    protected void addToolBarButton(JToolBar toolBar, JMenuItem menuItem) {
        JButton button = new JButton(menuItem.getIcon());
        button.setMargin(new Insets(1, 1, 1, 1));

        button.setToolTipText(menuItem.getText());
        button.addMouseListener(menuItem.getMouseListeners()[1]);

        button.addActionListener(menuItem.getActionListeners()[0]);

        if (enableMap.containsKey(menuItem))
            setEnableState(button, enableMap.get(menuItem));

        toolBar.add(button);
    }

    protected void addToolBarToggleButton(JToolBar toolBar, JToggleMenuItem toggleMenuItem) {
        JToggleButton toggleButton = new JToggleButton(toggleMenuItem.getIcon());
        toggleButton.setSelectedIcon(toggleMenuItem.getSelectedIcon());
        toggleButton.setMargin(new Insets(1, 1, 1, 1));

        toggleButton.setToolTipText(toggleMenuItem.getText());
        toggleButton.addMouseListener(toggleMenuItem.getMouseListeners()[1]);

        toggleButton.addActionListener(e -> {
            toggleMenuItem.doClick();
            toggleButton.setToolTipText(toggleMenuItem.getText());
        });

        setSelectedState(toggleButton, selectedMap.get(toggleMenuItem));

        if (enableMap.containsKey(toggleMenuItem))
            setEnableState(toggleButton, enableMap.get(toggleMenuItem));

        toolBar.add(toggleButton);
    }

//    ------   status bar   ------

    protected void setStatusBarHint(String hint) {
        statusBar.setHint(hint);
    }

    protected void resetStatusBar() {
        statusBar.reset();
    }

//    ------   About dialog   ------

    protected void showAboutDialog() {
        JOptionPane.showMessageDialog(this, "FIT NSU\ng14203\nPopov Vladimir", "About",
                                      JOptionPane.PLAIN_MESSAGE);
    }
}
