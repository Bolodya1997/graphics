package ru.nsu.fit.g14203.popov.filter;

import javax.swing.*;
import java.awt.image.BufferedImage;

class Area extends JLabel {

    private BufferedImage image;

    {
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(TOP);
    }

    BufferedImage getImage() {
        return image;
    }

    void setImage(BufferedImage image) {
        this.image = image;
        setIcon(new ImageIcon(image));
    }
}
