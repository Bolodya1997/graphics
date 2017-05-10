package ru.nsu.fit.g14203.popov.wireframe.spline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

class SplinePanel extends JPanel {

    private final static int DIAMETER = 20;

    private Point2D from;
    private Point2D to;

    private Spline spline;

    private JPanel imagePanel;

    SplinePanel(Spline spline,
                Runnable updateAction, Runnable removeAction) {
        this.spline = spline;
        recountBorders();

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

//        ------   image panel   ------
        createImagePanel(updateAction);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        add(imagePanel, constraints);

//        ------   remove figure button   ------
        JButton removeFigureButton = new JButton("remove figure");
        removeFigureButton.addActionListener(e -> removeAction.run());

        constraints.gridx = GridBagConstraints.RELATIVE;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.05;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        add(removeFigureButton, constraints);

//        ------   color button   ------
        JButton colorButton = new JButton("color");
        colorButton.addActionListener(e -> {
            spline.setColor(JColorChooser.showDialog(this, "Spline color", spline.getColor()));
            updateAction.run();
        });

        add(colorButton, constraints);

//        ------   remove point button   ------
        JButton removePointButton = new JButton("remove point");
        removePointButton.addActionListener(e -> {
            spline.removePoint();
            updateAction.run();
        });

        add(removePointButton, constraints);
    }

    private void recountBorders() {
        double maxX = 1.0;
        double maxY = 1.0;
        for (Point2D point2D : spline.getPoints()) {
            maxX = Double.max(maxX, Math.abs(point2D.getX()));
            maxY = Double.max(maxY, Math.abs(point2D.getY()));
        }

        double max = Double.max(maxX, maxY);
        max += 2 * DIAMETER / ((double) getWidth()) * max;

        from = new Point2D.Double(-max, -max);
        to = new Point2D.Double(max, max);
    }

    private Point2D formatToScreen(Point2D oldPoint) {
        double kX = imagePanel.getWidth() / (to.getX() - from.getX());
        double kY = imagePanel.getHeight() / (to.getY() - from.getY());

        return new Point2D.Double((oldPoint.getX() - from.getX()) * kX,
                                  (oldPoint.getY() - from.getY()) * kY);
    }

    private Point2D formatToSpline(Point2D oldPoint) {
        double kX = (to.getX() - from.getX()) / imagePanel.getWidth();
        double kY = (to.getY() - from.getY()) / imagePanel.getHeight();

        return new Point2D.Double(oldPoint.getX() * kX + from.getX(),
                                  oldPoint.getY() * kY + from.getY());
    }

    private Point2D findPoint(Point2D point) {
        return spline.getPoints().stream()
                .filter(splinePoint -> formatToScreen(splinePoint).distance(point) < DIAMETER / 2)
                .findFirst().orElse(null);
    }

    private void createImagePanel(Runnable updateAction) {
        final Point2D[] closest = { null };

        imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                recountBorders();

                BufferedImage image = new SplineImage(getWidth(), getHeight(), from, to, spline);
                g.drawImage(image, 0, 0, this);

                spline.getPoints().forEach(point2D -> {
                    if (point2D == closest[0])
                        g.setColor(Color.YELLOW);
                    else
                        g.setColor(Color.WHITE);

                    Point2D formatted = formatToScreen(point2D);
                    g.drawOval((int) formatted.getX() - DIAMETER / 2,
                               (int) formatted.getY() - DIAMETER / 2,
                               DIAMETER, DIAMETER);
                });
            }
        };

        final Point2D[] lastPoint = { null };

        imagePanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastPoint[0] == null)
                    return;

                lastPoint[0].setLocation(formatToSpline(e.getPoint()));
                closest[0] = lastPoint[0];

                updateAction.run();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                closest[0] = findPoint(e.getPoint());
                imagePanel.repaint();
            }
        });

        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON3)
                    return;

                Point2D newPoint = formatToSpline(e.getPoint());
                spline.addPoint(newPoint);

                closest[0] = newPoint;
                updateAction.run();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;

                lastPoint[0] = findPoint(e.getPoint());
                closest[0] = lastPoint[0];
                imagePanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;

                lastPoint[0] = null;
            }
        });
    }
}
