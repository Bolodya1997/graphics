package ru.nsu.fit.g14203.popov.life.util;

import javax.swing.*;

public class JRadioMenuItem extends JRadioButtonMenuItem {

    private String defaultText;
    private Icon defaultIcon;

    private String selectedText;

    public JRadioMenuItem(String defaultText, Icon defaultIcon, int defaultMnemonics,
                          String selectedText, Icon selectedIcon, int selectedMnemonics) {
        super(defaultText, defaultIcon);

        this.defaultText = defaultText;
        this.defaultIcon = defaultIcon;

        this.selectedText = selectedText;
        setSelectedIcon(selectedIcon);

        addActionListener(e -> {
            if (isSelected()) {
                setText(selectedText);
                setIcon(selectedIcon);
                setMnemonic(selectedMnemonics);
            } else {
                setText(defaultText);
                setIcon(defaultIcon);
                setMnemonic(defaultMnemonics);
            }
        });
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public Icon getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(Icon defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
}
