package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;

class Legend extends JPanel {

    private State functionLoaded = new State(false);

    private double min;
    private double max;

    private double[] borders;
    private Color[] colors;

    void setFunction(double min, double max, Color[] colors) {
        this.min = min;
        this.max = max;
        this.colors = colors;

        borders = new double[colors.length - 1];
        for (int i = 0; i < borders.length; i++)
            borders[i] = min + (max - min) / colors.length * (i + 1);

        functionLoaded.setState(true);
        SwingUtilities.invokeLater(this::repaint);
    }

    void clear() {
        functionLoaded.setState(false);
        SwingUtilities.invokeLater(this::repaint);
    }

    State getFunctionLoaded() {
        return functionLoaded;
    }

    double[] getBorders() {
        return borders;
    }

    Color[] getColors() {
        return colors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!functionLoaded.isTrue())
            return;

        int width = getWidth() / 2;
        double height = ((double) getHeight()) / colors.length;

        int fontSize = width / 3;
        g.setFont(new Font("Serif", Font.PLAIN, fontSize));

        g.drawString(String.format("%.3f", min), width + 1, 0);
        for (int i = 0; i < colors.length - 1; i++) {
            g.setColor(colors[i]);
            g.fillRect(0, (int) (height * i + 0.5), width, (int) (height + 1));

            g.setColor(Color.BLACK);
            g.drawString(String.format("%.3f", borders[i]), width + 1,
                                                            (int) (height * (i + 1) + 0.5) + fontSize / 3);
        }

        g.setColor(colors[colors.length - 1]);
        g.fillRect(0, (int) (height * (colors.length - 1) + 0.5), width, (int) (height + 0.5));
    }
}
