package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MutableBoolean;
import ru.nsu.fit.g14203.popov.life.util.MyPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

class GamePanel extends JPanel {

    private Grid grid = new Grid(new Settings());

    private BufferedImage canvas;
    private Dimension preferredSize;

    private MutableBoolean play = new MutableBoolean(false);
    private MutableBoolean replace = new MutableBoolean(true);

    private MutableBoolean impact = new MutableBoolean(false);
    private MutableBoolean colors = new MutableBoolean(false);

    private boolean changed = false;

    private Point lastDragged = null;

    private Color deadDeadColor   = new Color(0x8EF3FF);
    private Color aliveAliveColor = new Color(0x2CC446);
    private Color deadAliveColor  = new Color(0xFFFB7D);
    private Color aliveDeadColor  = new Color(0x00A6C1);

    private Timer playTimer = new Timer(1000, e -> step());

    GamePanel() {
        recountGrid(grid.getSettings());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paintCell(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                lastDragged = null;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                paintCell(e);
            }
        });
    }

//    ------   change state   ------

    Settings getGridSettings() {
        return grid.getSettings();
    }

    MutableBoolean getReplace() {
        return replace;
    }

    MutableBoolean getImpact() {
        return impact;
    }

    MutableBoolean getColors() {
        return colors;
    }

    void recountGrid(Settings settings) {
        grid.setSettings(settings);

        drawGrid();
        fillCells();
        repaint();
    }

    void readFromStream(InputStream stream) {
        grid = Grid.parseStream(stream);

        drawGrid();
        fillCells();
        repaint();
    }

    void printToStream(OutputStream stream) {
        grid.printToStream(stream);
    }

    void clearGrid() {
        grid.clear();

        fillCells();
        repaint();
    }

    void step() {
        grid.step();
        fillCells();
        repaint();
    }

    void play() {
        play.setState(true);
        playTimer.restart();
    }

    void stop() {
        play.setState(false);
        playTimer.stop();
    }

//    ------   draw   ------

    @Override
    protected void paintComponent(Graphics g) {
        setPreferredSize(preferredSize);
        setSize(preferredSize);

        g.clearRect(0, 0, getWidth(), getHeight());

        g.drawImage(canvas, 0, 0, null);

        if (impact.isTrue())
            drawImpact(g);
    }

    private void drawGrid() {
        int gridWidth = grid.getSettings().gridWidth.getValue();
        int gridHeight = grid.getSettings().gridHeight.getValue();
        int size = grid.getSettings().size.getValue();
        int width = grid.getSettings().width.getValue();

        int pixelWidth = GridInfo.getWidth(gridWidth, size, width);
        int pixelHeight = GridInfo.getHeight(gridHeight, size, width);
        preferredSize = new Dimension(pixelWidth, pixelHeight);

        canvas = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        canvas.getGraphics().fillRect(0, 0, pixelWidth, pixelHeight);

        Graphics2D g = canvas.createGraphics();
        g.setStroke(new BasicStroke(width));
        g.setColor(Color.BLACK);

        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                Point[] points = GridInfo.getPoints(gridX, gridY, size, width);
                for (int p = 0; p < 6; p++) {
                    if (width == 1)
                        MyPainter.drawLine(canvas, points[p].x, points[p].y,
                                points[(p + 1) % 6].x, points[(p + 1) % 6].y, Color.BLACK);
                    else
                        g.drawLine(points[p].x, points[p].y, points[(p + 1) % 6].x, points[(p + 1) % 6].y);
                }
            }
        }
    }

    void fillCells() {
        int gridWidth = grid.getSettings().gridWidth.getValue();
        int gridHeight = grid.getSettings().gridHeight.getValue();
        int size = grid.getSettings().size.getValue();
        int width = grid.getSettings().width.getValue();

        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.BLACK);

        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                Point point = GridInfo.getCenter(gridX, gridY, size, width);
                MyPainter.fillArea(canvas, point.x, point.y, cellColor(gridX, gridY));
            }
        }
    }

    private Color cellColor(int gridX, int gridY) {
        final double BIRTH_BEGIN = grid.getSettings().birthBegin.getValue();
        final double BIRTH_END = grid.getSettings().birthEnd.getValue();
        final double LIVE_BEGIN = grid.getSettings().lifeBegin.getValue();
        final double LIVE_END = grid.getSettings().lifeEnd.getValue();

        if (colors.isTrue()) {      //  color impact
            double impact = grid.getImpact(gridX, gridY);
            if (grid.getAlive(gridX, gridY)) {
                if (LIVE_BEGIN <= impact && impact <= LIVE_END)
                    return aliveAliveColor;
                else
                    return aliveDeadColor;
            } else {
                if (BIRTH_BEGIN <= impact && impact <= BIRTH_END)
                    return deadAliveColor;
                else
                    return deadDeadColor;
            }
        } else {                    //  color alive
            return grid.getAlive(gridX, gridY) ? aliveAliveColor : deadDeadColor;
        }
    }

    private void drawImpact(Graphics g) {
        int gridWidth = grid.getSettings().gridWidth.getValue();
        int gridHeight = grid.getSettings().gridHeight.getValue();
        int size = grid.getSettings().size.getValue();
        int width = grid.getSettings().width.getValue();

        g.setFont(new Font("SERIF", Font.PLAIN, GridInfo.getFontSize(size)));
        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                Point pos = GridInfo.getImpactPosition(gridX, gridY, size, width);
                String impact = String.format("%.1f", grid.getImpact(gridX, gridY));
                g.drawChars(impact.toCharArray(), 0, impact.length(), pos.x, pos.y);
            }
        }
    }

    private void paintCell(MouseEvent e) {
        if (play.isTrue())
            return;

        if (e.getX() < 0 || e.getX() >= canvas.getWidth())
            return;
        if (e.getY() < 0 || e.getY() >= canvas.getHeight())
            return;

        int RGB = canvas.getRGB(e.getX(), e.getY()) & 0xFFFFFF;
        if (RGB == 0x000000)
            return;

        int gridWidth = grid.getSettings().gridWidth.getValue();
        int gridHeight = grid.getSettings().gridHeight.getValue();
        int size = grid.getSettings().size.getValue();
        int width = grid.getSettings().width.getValue();

        Point pos = GridInfo.getGridPosition(e.getX(), e.getY(), size, width);
        if (pos.x < 0 || gridWidth <= pos.x || pos.y < 0 || gridHeight <= pos.y)
            return;

        if (lastDragged != null) {
            if (lastDragged.equals(pos))
                return;
        }
        lastDragged = pos;

        if (replace.isTrue())
            grid.replace(pos.x, pos.y);
        else
            grid.xor(pos.x, pos.y);

        fillCells();
        repaint();
    }
}
