package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;

public class JToggleMenuItem extends JRadioButtonMenuItem {

    private String defaultText;
    private Icon defaultIcon;
    private int defaultMnemonics;

    private String selectedText;
    private Icon selectedIcon;
    private int selectedMnemonics;

    private Runnable stateAction;

    JToggleMenuItem(String defaultText, Icon defaultIcon, int defaultMnemonics,
                    String selectedText, Icon selectedIcon, int selectedMnemonics) {
        super(defaultText, defaultIcon);

        this.defaultText = defaultText;
        this.defaultIcon = defaultIcon;
        this.defaultMnemonics = defaultMnemonics;

        this.selectedText = selectedText;
        this.selectedIcon = selectedIcon;
        this.selectedMnemonics = selectedMnemonics;

        stateAction = () -> {
            if (isSelected()) {
                setText(this.selectedText);
                setIcon(this.selectedIcon);
                setMnemonic(this.selectedMnemonics);
            } else {
                setText(this.defaultText);
                setIcon(this.defaultIcon);
                setMnemonic(this.defaultMnemonics);
            }
        };
        addActionListener(e -> stateAction.run());
    }

    String getDefaultText() {
        return defaultText;
    }

    void setDefaultText(String defaultText, int defaultMnemonics) {
        this.defaultText = defaultText;
        this.defaultMnemonics = defaultMnemonics;

        stateAction.run();
    }

    Icon getDefaultIcon() {
        return defaultIcon;
    }

    void setDefaultIcon(Icon defaultIcon) {
        this.defaultIcon = defaultIcon;

        stateAction.run();
    }

    String getSelectedText() {
        return selectedText;
    }

    void setSelectedText(String selectedText, int selectedMnemonics) {
        this.selectedText = selectedText;
        this.selectedMnemonics = selectedMnemonics;

        stateAction.run();
    }
}
