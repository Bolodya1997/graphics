package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.SingleThreadPool;
import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

class FunctionMap extends JPanel {
    private State functionLoaded = new State(false);

    private Point2D.Double from;
    private Point2D.Double to;

    private Function2D function;

    private FunctionImage functionImage;
    private IsolineImage isolineImage;

    private Color isolineColor;

    FunctionMap() {
        SingleThreadPool threadPool = new SingleThreadPool();
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                threadPool.execute(() -> {
                    if (!functionLoaded.isTrue())
                        return;

                    double x = from.getX() + e.getX() * (to.getX() - from.getX()) / getWidth();
                    double y = from.getY() + e.getY() * (to.getY() - from.getY()) / getHeight();
                    Isoline isoline = new Isoline(100, 100,
                                                  from, to,
                                                  function, function.getValue(x, y));
                    isolineImage = new IsolineImage(getWidth(), getHeight(),
                                                    isoline, isolineColor);

                    SwingUtilities.invokeLater(FunctionMap.this::repaint);
                });
            }
        });
    }

    void setFunction(Point2D.Double from, Point2D.Double to,
                     Function2D function, Legend legend,
                     Color isolineColor) {
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
