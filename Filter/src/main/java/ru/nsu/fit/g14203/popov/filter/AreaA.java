package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.filter.graphics.MyPainter;
import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

class AreaA extends JLabel {

    private final static Color DARK_TRANSPARENT = new Color(0, 0, 0, 63);

    private class Selection {
        int x;
        int y;
        int size;

        void relocate(int mouseX, int mouseY) {
            int newX = mouseX - size / 2;
            x = (newX < 0) ? 0
                           : (newX > usedWidth - size) ? usedWidth - size
                                                       : newX;
            int newY = mouseY - size / 2;
            y = (newY < 0) ? 0
                           : (newY > usedHeight - size) ? usedHeight - size
                                                        : newY;
        }
    }

    private BufferedImage image;

    private int usedWidth;
    private int usedHeight;

    private State selectEnable = new State(false);
    private Selection selection = new Selection();

    private BufferedImage selectedImage;

    {
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(TOP);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseAction(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseAction(e);
            }
        });
    }

    void setImage(BufferedImage image) {
        this.image = image;

        BufferedImage usedImage = MyPainter.shrinkImage(image, getWidth(), getHeight());
        usedWidth = usedImage.getWidth();
        usedHeight = usedImage.getHeight();

        selection.size = 50;

        setIcon(new ImageIcon(usedImage));
    }

    private void mouseAction(MouseEvent e) {
        selection.relocate(e.getX(), e.getY());
        repaint(selection.x, selection.y, selection.size, selection.size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(DARK_TRANSPARENT);
        g.fillRect(0, 0, getWidth(), selection.y);
        g.fillRect(0, selection.y, selection.x, getHeight());
    }
}
