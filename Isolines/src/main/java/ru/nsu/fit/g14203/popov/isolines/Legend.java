package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Observer;

class Legend extends JPanel {

    private final static Function2D FUNCTION = new Function2D() {
        @Override
        double getValue(double x, double y) {
            return y;
        }
    };

    private State functionLoaded = new State(false);
    private State interpolationOn;

    private double min;
    private double max;
    private double step;

    private Color[] colors;
    private double[] levels;

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

        step = (max - min) / colors.length;

        levels = new double[colors.length - 1];
        for (int i = 0; i < levels.length; i++)
            levels[i] = max - step * (i + 1);

        functionLoaded.setState(true);
        setImage();
    }

    private void setImage() {
        if (!functionLoaded.isTrue())
            return;

        Point2D.Double from = new Point2D.Double(0, min);
        double dy = (max - min) / (getHeight() - 1);
        image = new FunctionImage(getWidth() / 2, getHeight(),
                                  from, 1, dy,
                                  FUNCTION, Legend.this);

        SwingUtilities.invokeLater(Legend.this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        if (!functionLoaded.isTrue()) {
            g2D.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, getHeight(), Color.BLACK));
            g2D.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        g2D.clearRect(0, 0, getWidth(), getHeight());
        g2D.drawImage(image, 0, 0, this);

        double height = getHeight() / (levels.length + 1.0);
        int fontSize = getWidth() / 7 - 1;
        g2D.setFont(new Font("Serif", Font.PLAIN, fontSize));
        for (int i = 0; i < colors.length - 1; i++)
            g2D.drawString(String.format("%.3f", levels[i]), getWidth() / 2 + 1,
                                                             (int) (height * (i + 1) + 0.5) + fontSize / 3);
    }

    private Color simpleColor(double value) {
        if (value >= max)
            return colors[colors.length - 1];

        return colors[(int) ((value - min) / step)];
    }

    private Color interpolatedColor(double value) {
        if (value - min <= step / 2)
            return colors[0];
        if (max - value <= step / 2)
            return colors[colors.length - 1];

        int block = (int) ((value - min) / (step / 2));
        int i = block / 2 + block % 2;

        double pos = (((value - min) + (step / 2)) - step * i) / step;

        int dR = (int) ((colors[i].getRed() - colors[i - 1].getRed()) * pos + 0.5);
        int dG = (int) ((colors[i].getGreen() - colors[i - 1].getGreen()) * pos + 0.5);
        int dB = (int) ((colors[i].getBlue() - colors[i - 1].getBlue()) * pos + 0.5);

        return new Color(colors[i - 1].getRed() + dR,
                         colors[i - 1].getGreen() + dG,
                         colors[i - 1].getBlue() + dB);
    }

//    ------   getters   ------

    State getFunctionLoaded() {
        return functionLoaded;
    }

    Color getColor(double value) {
        if (interpolationOn.isTrue())
            return interpolatedColor(value);

        return simpleColor(value);
    }

    double[] getLevels() {
        return levels;
    }
}
