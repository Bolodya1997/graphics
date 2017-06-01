package ru.nsu.fit.g14203.popov.raytracing;

import ru.nsu.fit.g14203.popov.raytracing.tracing.*;
import ru.nsu.fit.g14203.popov.raytracing.tracing.Box;
import ru.nsu.fit.g14203.popov.wireframe.Camera;
import ru.nsu.fit.g14203.popov.wireframe.ProjectionImage;
import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class MainPanel extends JPanel {

    private Camera camera = new Camera();

    private int width   = 600;
    private int height  = 600;

    private List<RealFigure3D> figures = new ArrayList<>();
    {
        figures.add(new Box(
                new float[]{ 0.0f, 0.0f, 0.0f },
                new float[]{ 1.0f, 1.0f, 1.0f },
                1f,
                new Vector(-0.5, -0.5, -0.5),
                new Vector( 0.5,  0.5,  0.5)
        ));

        figures.add(new Box(
                new float[]{ 0.0f, 1.0f, 0.0f },
                new float[]{ 0.0f, 0.0f, 0.0f },
                1f,
                new Vector(-0.5,  0.5,  0.2),
                new Vector(-0.4,  0.7,  0.5)
        ));
        figures.add(new Box(
                new float[]{ 0.0f, 1.0f, 0.0f },
                new float[]{ 0.0f, 0.0f, 0.0f },
                1f,
                new Vector(-0.1,  0.5,  0.2),
                new Vector( 0,    0.7,  0.5)
        ));
        figures.add(new Box(
                new float[]{ 0.0f, 0.0f, 0.0f },
                new float[]{ 1.0f, 1.0f, 1.0f },
                1f,
                new Vector(-0.5,  0.7,   0.2),
                new Vector( 0,    0.72,  0.5)
        ));

        figures.add(new Box(
                new float[]{ 1.0f, 1.0f, 0.0f },
                new float[]{ 0.0f, 0.0f, 0.0f },
                1f,
                new Vector(-0.5,  0.72,  0.2),
                new Vector(-0.4,  0.92,  0.5)
        ));
        figures.add(new Box(
                new float[]{ 1.0f, 1.0f, 0.0f },
                new float[]{ 0.0f, 0.0f, 0.0f },
                1f,
                new Vector(-0.1,  0.72,  0.2),
                new Vector( 0,    0.92,  0.5)
        ));
        figures.add(new Box(
                new float[]{ 0.0f, 0.0f, 0.0f },
                new float[]{ 1.0f, 1.0f, 1.0f },
                1f,
                new Vector(-0.5,  0.92,  0.2),
                new Vector( 0,    0.94,  0.5)
        ));

        figures.add(new Box(
                new float[]{ 1.0f, 0.0f, 0.0f },
                new float[]{ 0.0f, 0.0f, 0.0f },
                1f,
                new Vector(-0.5,  0.94,  0.2),
                new Vector(-0.4,  1.14,  0.5)
        ));
        figures.add(new Box(
                new float[]{ 1.0f, 0.0f, 0.0f },
                new float[]{ 0.0f, 0.0f, 0.0f },
                1f,
                new Vector(-0.1,  0.94,  0.2),
                new Vector( 0,    1.14,  0.5)
        ));
        figures.add(new Box(
                new float[]{ 0.0f, 0.0f, 0.0f },
                new float[]{ 1.0f, 1.0f, 1.0f },
                1f,
                new Vector(-0.5,  1.14,   0.2),
                new Vector( 0,    1.16,  0.5)
        ));
    }
    private List<Light> lights = new ArrayList<>();
    {
        lights.add(new Light(
                new Vector(-0.25, 0.6, 0.3),
                new float[]{ 1f, 0f, 1f }
        ));

        lights.add(new Light(
                new Vector(-0.25, 0.82, 0.3),
                new float[]{ 0f, 0f, 1f }
        ));

        lights.add(new Light(
                new Vector(-0.25, 1.04, 0.3),
                new float[]{ 0f, 1f, 1f }
        ));

        lights.add(new Light(
                camera.getPosition(),
                new float[]{ 1f, 0.8f, 0.55f }
        ));
    }

    private float[] ambientLight = { 0.3f, 0.24f, 0.165f };

    private BufferedImage[] projectionImages;
    private BufferedImage renderImage;

    MainPanel() {
//        projectionImages = figures.stream()
//                .map(figure -> new ProjectionImage(width, height, camera, figure))
//                .toArray(BufferedImage[]::new);

        renderImage = new RenderImage(width, height, camera,
                figures, lights, ambientLight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        if (renderImage != null)
            g.drawImage(renderImage, 0, 0, width, height, this);

        if (projectionImages != null) {
            for (BufferedImage projectionImage : projectionImages) {
                g.drawImage(projectionImage, 0, 0, width, height, this);
            }
        }
    }
}
