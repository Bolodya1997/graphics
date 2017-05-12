package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.figures.Camera;
import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.figures.ProjectionImage;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Matrix;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;

class MainPanel extends JPanel {

    private final static int BORDER = 20;

    private int size;
    private int height;
    private int width;

    private Camera camera = Camera.getInstance();
    {
        camera.addObserver((o, arg) -> update());
    }

    private LinkedList<Figure3D> figures = new LinkedList<>();
    private Figure3D scene = Figure3D.getBrick(1, 1, 1);

    private BufferedImage[] projectionImages;

    MainPanel() {
//        ------   resize   ------

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                update();
            }
        });

//        ------   rotate   ------

        int[] mouseX = { 0 };
        int[] mouseY = { 0 };
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                grabFocus();

                mouseX[0] = e.getX();
                mouseY[0] = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double angleX = (mouseX[0] - e.getX()) * (Math.PI / 500);
                double angleY = (e.getY() - mouseY[0]) * (Math.PI / 500);

                FigureMover figureMover = FigureMover.getInstance();
                if (figureMover.getFigure() != null && figureMover.isEnable()) {
                    Vector.Translation cameraTranslation = new Vector.Translation(Vector.zero(),
                            camera.axisX, camera.axisY, camera.axisZ);
                    Matrix rotation = Matrix.identity()
                            .apply(cameraTranslation)
                            .apply(camera.getRotation())
                            .rotate(angleX, angleY, 0)
                            .applyInversed(camera.getRotation())
                            .applyInversed(cameraTranslation);

                    figureMover.getFigure().rotate(rotation);
                } else {
                    camera.rotate(angleX, angleY);
                }

                mouseX[0] = e.getX();
                mouseY[0] = e.getY();

                update();
            }
        });

//        ------   scroll   ------

        addMouseWheelListener(e -> {
            camera.move(e.getWheelRotation());
        });

//        ------   move   ------

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                FigureMover figureMover = FigureMover.getInstance();
                if (figureMover.getFigure() == null)
                    return;

                double step = 0.1;
                Vector shift;
                switch (e.getKeyChar()) {
                    case '4':
                        shift = new Vector(step, 0, 0);
                        break;
                    case '1':
                        shift = new Vector(-step, 0, 0);
                        break;
                    case '5':
                        shift = new Vector(0, step, 0);
                        break;
                    case '2':
                        shift = new Vector(0, -step, 0);
                        break;
                    case '6':
                        shift = new Vector(0, 0, step);
                        break;
                    case '3':
                        shift = new Vector(0, 0, -step);
                        break;
                    default:
                        shift = new Vector(0, 0, 0);
                }

                figureMover.getFigure().shift(shift);
                update();
            }
        });
    }

    private Matrix computeMatrixToScene() {
        Vector[] minMaxVectors = figures.stream()
                .flatMap(figure3D -> Arrays.stream(figure3D.getMinMaxPoints()))
                .toArray(Vector[]::new);
        if (minMaxVectors.length == 0)
            return Matrix.identity();

        double[] minMaxX = { minMaxVectors[0].getX(), minMaxVectors[1].getX() };
        double[] minMaxY = { minMaxVectors[0].getY(), minMaxVectors[1].getY() };
        double[] minMaxZ = { minMaxVectors[0].getZ(), minMaxVectors[1].getZ() };
        Arrays.stream(minMaxVectors)
                .forEach(vector -> {
                    minMaxX[0] = Double.min(vector.getX(), minMaxX[0]);
                    minMaxX[1] = Double.max(vector.getX(), minMaxX[1]);

                    minMaxY[0] = Double.min(vector.getY(), minMaxY[0]);
                    minMaxY[1] = Double.max(vector.getY(), minMaxY[1]);

                    minMaxZ[0] = Double.min(vector.getZ(), minMaxZ[0]);
                    minMaxZ[1] = Double.max(vector.getZ(), minMaxZ[1]);
                });

        Vector center = new Vector((minMaxX[1] + minMaxX[0]) / 2,
                                   (minMaxY[1] + minMaxY[0]) / 2,
                                   (minMaxZ[1] + minMaxZ[0]) / 2);

        double sizeX = Double.max(Math.abs(minMaxX[0] - center.getX()),
                                  Math.abs(minMaxX[1] - center.getX()));
        double sizeY = Double.max(Math.abs(minMaxY[0] - center.getY()),
                                  Math.abs(minMaxY[1] - center.getY()));
        double sizeZ = Double.max(Math.abs(minMaxZ[0] - center.getZ()),
                                  Math.abs(minMaxZ[1] - center.getZ()));
        double size = Double.max(sizeX, Double.max(sizeY, sizeZ));
        if (size == 0)
            size = 1;

        return Matrix.identity()
                .shift(center.resize(-1))
                .resize(1 / size);
    }

    private void recountImages() {
        size = Math.min(getWidth(), getHeight()) - BORDER * 2;
        if (size <= 0)
            return;

        width = (int) (size * camera.getWidth());
        height = (int) (size * camera.getHeight());

        Matrix toScene = computeMatrixToScene();

        Stream.Builder<ProjectionImage> __projectionImages = Stream.builder();
        for (Figure3D figure : figures)
            __projectionImages.add(new ProjectionImage(width, height, toScene, camera, figure));
        __projectionImages.add(new ProjectionImage(width, height, Matrix.identity(), camera, scene));

        projectionImages = __projectionImages.build().toArray(ProjectionImage[]::new);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.clearRect(0, 0, getWidth(), getHeight());

        g2D.setColor(camera.getColor());
        g2D.fillRect(BORDER, BORDER, width, height);

        if (projectionImages != null) {
            for (BufferedImage projectionImage : projectionImages)
                g2D.drawImage(projectionImage, BORDER, BORDER, this);
        }
    }

    void update() {
        recountImages();
        SwingUtilities.invokeLater(this::repaint);
    }

    void addFigure(Figure3D figure) {
        figures.add(figure);
        update();
    }

    void removeFigure(Figure3D figure) {
        figures.remove(figure);
        update();
    }

    void resetFigures() {
        camera.reset();
        update();
    }
}
