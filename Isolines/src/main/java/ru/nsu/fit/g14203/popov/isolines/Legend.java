package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Observer;
import java.util.stream.Stream;

class Legend extends JPanel {

    private final static int INTERPOLATION_COUNT = 225;

    private boolean functionLoaded = false;

    private State interpolationOn;

    private double min;
    private double max;

    private double[] borders;
    private Color[] colors;

    private double[] interpolationBorders;
    private Color[] interpolationColors;

    private Function2D function2D;
    private BufferedImage image;

    Legend(State interpolationOn) {
        this.interpolationOn = interpolationOn;

        Observer interpolationObserver = (o, arg) -> setImage();
        interpolationOn.addObserver(interpolationObserver);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setImage();
            }
        });
    }

    void setFunction(double min, double max, Color[] colors) {
        this.min = min;
        this.max = max;
        this.colors = colors;

        borders = new double[colors.length - 1];
        for (int i = 0; i < borders.length; i++)
            borders[i] = min + (max - min) / colors.length * (i + 1);

        computeInterpolatedColors();
        interpolationBorders = new double[interpolationColors.length - 1];
        for (int i = 0; i < interpolationBorders.length; i++)
            interpolationBorders[i] = min + (max - min) / interpolationColors.length * (i + 1);

        function2D = new Function2D() {
            @Override
            double getValue(double x, double y) {
                return y;
            }
        };

        functionLoaded = true;
        setImage();
    }

    private void computeInterpolatedColors() {
        Stream.Builder<Color> __interpolationColors = Stream.builder();
        for (int i = 0; i < colors.length - 1; i++) {
            double[] RGB = { colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue() };
            double dR = (colors[i + 1].getRed() - RGB[0]) / INTERPOLATION_COUNT;
            double dG = (colors[i + 1].getGreen() - RGB[1]) / INTERPOLATION_COUNT;
            double dB = (colors[i + 1].getBlue() - RGB[2]) / INTERPOLATION_COUNT;

            for (int k = 0; k < INTERPOLATION_COUNT; k++) {
                __interpolationColors.add(new Color((int) (RGB[0] + dR * k + 0.5),
                                                   (int) (RGB[1] + dG * k + 0.5),
                                                   (int) (RGB[2] + dB * k + 0.5)));
            }
        }

        interpolationColors = __interpolationColors.build().toArray(Color[]::new);
    }

    private void setImage() {
        if (!functionLoaded) {
            image = new BufferedImage(getWidth() / 2, getHeight(), BufferedImage.TYPE_INT_ARGB);
            return;
        }

        Point2D.Double from = new Point2D.Double(0, min);
        Point2D.Double to = new Point2D.Double(getWidth() / 2, max);
        double dy = (max - min) / (getHeight() - 1);
        image = new FunctionImage(getWidth() / 2, getHeight(),
                                  from, 1, dy,
                function2D, Legend.this);

        SwingUtilities.invokeLater(Legend.this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!functionLoaded)
            return;

        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, this);

        double height = getHeight() / (borders.length + 1.0);
        int fontSize = getWidth() / 7;
        g.setFont(new Font("Serif", Font.PLAIN, fontSize));
        for (int i = 0; i < borders.length; i++)
            g.drawString(String.format("%.3f", borders[(borders.length - 1) - i]), getWidth() / 2 + 1,
                    (int) (height * (i + 1) + 0.5) + fontSize / 3);
    }

//    ------   getters   ------

    boolean isFunctionLoaded() {
        return functionLoaded;
    }

    double[] getBorders() {
        if (interpolationOn.isTrue())
            return interpolationBorders;
        else
            return borders;
    }

    Color[] getColors() {
        if (interpolationOn.isTrue())
            return interpolationColors;
        else
            return colors;
    }

    double[] getBaseLevels() {
        return borders;
    }
}
