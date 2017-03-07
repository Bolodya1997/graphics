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
        if (image == null) {
            filled.setState(false);
            return;
        }

        setIcon(new ImageIcon(image));
        filled.setState(true);
    }
}
