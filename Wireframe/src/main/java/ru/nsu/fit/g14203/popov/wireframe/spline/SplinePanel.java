package ru.nsu.fit.g14203.popov.wireframe.spline;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class SplinePanel extends JPanel {

    private Spline spline;
    private BufferedImage image;

    void setSpline(Spline spline) {
        this.spline = spline;
        image = new SplineImage(getWidth(), getHeight(), spline);
    }

    void clear() {
        spline = null;
    }

    @Override
    protected void paintComponent(Graphics g) {


        if (spline != null)
            g.drawImage(image, 0, 0, this);
    }
}
