package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.SingleThreadPool;
import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Consumer;

class FunctionMap extends JPanel {

    private final static Color POINTS_COLOR = Color.RED;

    private boolean functionLoaded = false;

    private State functionShown;
    private State isolinesShown;
    private State gridShown;
    private State pointsShown;

    private Consumer<String> showInStatusBar;

    private Function2D function;
    private FunctionImage functionImage;

    private Point2D.Double from;
    private Point2D.Double to;

    private Legend legend;

    private int gridWidth;
    private int gridHeight;

    private Isoline[] baseIsolines;
    private BufferedImage[] baseIsolinesImages;
    private BufferedImage[] basePointsImages;

    private Isoline isoline;
    private BufferedImage isolineImage;
    private BufferedImage pointsImage;

    private Color isolineColor;

    private MouseEvent reverseInput(MouseEvent e) {
        return new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(),
                              e.getX(), (getHeight() - 1) - e.getY(), e.getClickCount(), e.isPopupTrigger(),
                              e.getButton());
    }

    FunctionMap(State functionShown, State isolinesShown,
                State gridShown, State pointsShown,
                Consumer<String> showInStatusBar, Runnable clearStatusBar) {
        this.functionShown = functionShown;
        this.isolinesShown = isolinesShown;
        this.gridShown = gridShown;
        this.pointsShown = pointsShown;
        this.showInStatusBar = showInStatusBar;

        SingleThreadPool resizePool = new SingleThreadPool();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizePool.execute(FunctionMap.this::recount);
            }
        });

        SingleThreadPool threadPool = new SingleThreadPool();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                threadPool.execute(() -> drawIsoline(reverseInput(e)));
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                showInStatus(reverseInput(e));
                threadPool.execute(() -> drawIsoline(reverseInput(e)));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                showInStatus(reverseInput(e));
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                clearStatusBar.run();
            }
        });
    }

    void setFunction(Point2D.Double from, Point2D.Double to,
                     Function2D function, Legend legend,
                     int gridWidth, int gridHeight,
                     Color isolineColor) {
        if (getWidth() == 0 || getHeight() == 0)
            return;

        this.from = from;
        this.to = to;
        this.function = function;
        this.isolineColor = isolineColor;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.legend = legend;

        baseIsolines = Arrays.stream(legend.getLevels())
                .mapToObj(level -> new Isoline(gridWidth, gridHeight,
                                               from, to,
                                               function, level))
                .toArray(Isoline[]::new);
        baseIsolinesImages = new BufferedImage[baseIsolines.length];
        basePointsImages = new BufferedImage[baseIsolines.length];

        isoline = null;

        recount();
    }

    private void recountFunction() {
        if (legend == null || !legend.getFunctionLoaded().isTrue())
            return;

        double dx = (to.getX() - from.getX()) / (getWidth() - 1);
        double dy = (to.getY() - from.getY()) / (getHeight() - 1);
        this.functionImage = new FunctionImage(getWidth(), getHeight(),
                                               from, dx, dy,
                                               function, legend);

        functionLoaded = true;
    }

    private void recountIsolines() {
        if (baseIsolinesImages == null || (!isolinesShown.isTrue() && !pointsShown.isTrue()))
            return;

        if (isoline != null) {
            isolineImage = new IsolineImage(getWidth(), getHeight(),
                                            isoline, isolineColor);
            pointsImage = new PointsImage(getWidth(), getHeight(),
                                          isoline, POINTS_COLOR);
        } else {
            isolineImage = null;
            pointsImage = null;
        }

        for (int i = 0; i < baseIsolinesImages.length; i++) {
            baseIsolinesImages[i] = new IsolineImage(getWidth(), getHeight(),
                                                     baseIsolines[i], isolineColor);
            basePointsImages[i] = new PointsImage(getWidth(), getHeight(),
                                                  baseIsolines[i], POINTS_COLOR);
        }
    }

    void recount() {
        recountFunction();
        recountIsolines();

        SwingUtilities.invokeLater(this::repaint);
    }

    private void drawIsoline(MouseEvent e) {
        if (!functionLoaded || (!isolinesShown.isTrue() && !pointsShown.isTrue()))
            return;

        double x = from.getX() + e.getX() * (to.getX() - from.getX()) / getWidth();
        double y = from.getY() + e.getY() * (to.getY() - from.getY()) / getHeight();
        isoline = new Isoline(gridWidth, gridHeight,
                              from, to,
                              function, function.getValue(x, y));

        isolineImage = new IsolineImage(getWidth(), getHeight(),
                                        isoline, isolineColor);
        pointsImage = new PointsImage(getWidth(), getHeight(),
                                        isoline, POINTS_COLOR);

        SwingUtilities.invokeLater(this::repaint);
    }

    void clearIsoline() {
        isoline = null;

        isolineImage = null;
        pointsImage = null;

        repaint();
    }

    private void showInStatus(MouseEvent e) {
        if (!functionLoaded)
            return;

        double x = from.getX() + (to.getX() - from.getX()) * e.getX() / getWidth();
        double y = from.getY() + (to.getY() - from.getY()) * e.getY() / getHeight();
        showInStatusBar.accept(String.format("x : %.3f     y : %.3f     f(x, y) : %.3f",
                                             x, y, function.getValue(x, y)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.clearRect(0, 0, getWidth(), getHeight());

        if (!functionLoaded) {
            g2D.setColor(Color.GRAY);
            g2D.fillRect(0, 0, getWidth(), getHeight());

            g2D.setColor(Color.BLACK);
            g2D.drawString("No config loaded", getWidth() / 2 - 55, getHeight() / 2);
            return;
        }

        if (functionShown.isTrue())
            g2D.drawImage(functionImage, 0, 0, getWidth(), getHeight(), this);

        if (gridShown.isTrue()) {
            g2D.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                    10, new float[] { 0.1f, 3.9f }, 0));
            g2D.setColor(Color.BLUE);

            double width = (getWidth() + 1) / ((double) gridWidth);
            for (int i = 0; i < gridWidth; i++)
                g2D.drawLine((int) (width * i - 1.5), 0, (int) (width * i - 1.5), getHeight());

            double height = (getHeight() + 1) / ((double) gridHeight);
            for (int i = 0; i < gridHeight; i++)
                g2D.drawLine(0, (int) (height * i - 1.5), getWidth(), (int) (height * i - 1.5));
        }

        if (isolinesShown.isTrue()) {
            if (isolineImage != null)
                g2D.drawImage(isolineImage, 0, 0, getWidth(), getHeight(), this);

            for (BufferedImage image : baseIsolinesImages)
                g2D.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        if (pointsShown.isTrue()) {
            if (pointsImage != null)
                g2D.drawImage(pointsImage, 0, 0, getWidth(), getHeight(), this);

            for (BufferedImage image : basePointsImages)
                g2D.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
