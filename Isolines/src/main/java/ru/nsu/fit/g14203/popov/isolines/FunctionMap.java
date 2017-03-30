package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.SingleThreadPool;
import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class FunctionMap extends JPanel {
    private State functionLoaded = new State(false);

    private DoublePoint from;
    private DoublePoint to;

    private Field2D function;

    private FunctionImage functionImage;
    private IsolineImage isolineImage;

    private Color isolineColor;

    FunctionMap() {
        SingleThreadPool threadPool = new SingleThreadPool();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                threadPool.execute(() -> {
                    if (!functionLoaded.isTrue())
                        return;

                    double x = from.getX() + e.getX() * (to.getX() - from.getX()) / getWidth();
                    double y = from.getY() + e.getY() * (to.getY() - from.getY()) / getHeight();
                    Isoline isoline = new Isoline(1258, 702,
                                                  from, to,
                                                  function, function.getValue(x, y));
                    isolineImage = new IsolineImage(getWidth(), getHeight(),
                                                    from, to,
                                                    isoline, isolineColor);

                    SwingUtilities.invokeLater(FunctionMap.this::repaint);
                });
            }
        });
    }

    void setFunction(DoublePoint from, DoublePoint to,
                     Field2D function, Legend legend, Color isolineColor) {
        if (getWidth() == 0 || getHeight() == 0)
            return;

        this.from = from;
        this.to = to;
        this.function = function;
        this.isolineColor = isolineColor;

        double dx = (to.getX() - from.getX()) / (getWidth() - 1);
        double dy = (to.getY() - from.getY()) / (getHeight() - 1);
        this.functionImage = new FunctionImage(getWidth(), getHeight(),
                                               from, to, dx, dy,
                                               function, legend);

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

    @Override
    protected void paintComponent(Graphics g) {
        if (!functionLoaded.isTrue())
            return;

        g.drawImage(functionImage, 0, 0, getWidth(), getHeight(), this);

        if (isolineImage != null)
            g.drawImage(isolineImage, 0, 0, getWidth(), getHeight(), this);
    }
}
