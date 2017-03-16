package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.image.BufferedImage;

class Area extends JLabel {

    private BufferedImage image;
    private State filled;

    Area(State filled) {
        this.filled = filled;

        setHorizontalAlignment(LEFT);
        setVerticalAlignment(TOP);
    }

    BufferedImage getImage() {
        return image;
    }

    void setImage(BufferedImage image) {
        this.image = image;

        Icon icon;
        if (image == null) {
            filled.setState(false);
            icon = null;
        } else {
            filled.setState(true);
            icon = new ImageIcon(image);
        }

        setIcon(icon);
    }
}
