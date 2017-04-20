package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.stream.Stream;

public class MainPanel extends JPanel {

    private final static int BORDER = 20;

    private int size;
    private Camera camera = new Camera();

    private LinkedList<Figure3D> figures = new LinkedList<>();
    private BufferedImage[] projectionImages;

    private static Figure3D getScene() {
        Figure3D scene = Figure3D.getBrick(1, 1, 1);
        scene.setEdgesColor(Color.PINK);

        return scene;
    }

    public MainPanel() {
        figures.add(getScene());

//        Figure3D brick = Figure3D.getBrick(0.2, 0.2, 0.2);
//        brick.shift(new Vector(0.4, 0.2, 0.7));
//        brick.rotate(Math.PI / 2, Math.PI, Math.PI / 5);
//        figures.add(brick);

//        ------   resize   ------

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                recountImages();
                repaint();
            }
        });

//        ------   rotate   ------

        int[] mouseX = { 0 };
        int[] mouseY = { 0 };
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX[0] = e.getX();
                mouseY[0] = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double angleX = (mouseX[0] - e.getX()) * (Math.PI / 500);
                double angleY = (e.getY() - mouseY[0]) * (Math.PI / 500);
                for (Figure3D figure : figures)
                    figure.rotateCamera(camera, angleX, angleY);

                mouseX[0] = e.getX();
                mouseY[0] = e.getY();

                recountImages();
                repaint();
            }
        });

//        ------   scroll   ------

        addMouseWheelListener(e -> {
            camera.move(e.getWheelRotation());

            recountImages();
            repaint();
        });
    }

    private void recountImages() {
        size = Math.min(getWidth(), getHeight()) - BORDER * 2;
        if (size <= 0)
            return;

        Stream.Builder<ProjectionImage> __projectionImages = Stream.builder();
        for (Figure3D figure : figures)
            __projectionImages.add(new ProjectionImage(size, camera, figure));
        projectionImages = __projectionImages.build().toArray(ProjectionImage[]::new);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.clearRect(0, 0, getWidth(), getHeight());

        g2D.setColor(Color.BLACK);
        g2D.fillRect(BORDER, BORDER, size, size);

        if (projectionImages != null) {
            for (BufferedImage projectionImage : projectionImages)
                g2D.drawImage(projectionImage, BORDER, BORDER, this);
        }
    }

    public void addFigure(Figure3D figure) {
        figures.add(figure);
    }

    public void removeFigure(Figure3D figure) { //  TODO: think about spline -> figure controller
        figures.remove(figure);
    }
}
