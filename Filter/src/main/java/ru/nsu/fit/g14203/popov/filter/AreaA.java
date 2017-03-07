package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.filter.graphics.MyPainter;
import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

class AreaA extends JLabel {

    private final static Color DARK_TRANSPARENT = new Color(0, 0, 0, 127);

    private class Selection {
        int x;
        int y;
        int size;   //  FIXME: change to { width, height }, add small image selection support

        boolean active = false;

        void relocate(int mouseX, int mouseY) {
            active = true;

            int newX = mouseX - size / 2;
            x = (newX < 0) ? 0
                           : (newX > usedWidth - size) ? usedWidth - size
                                                       : newX;
            int newY = mouseY - size / 2;
            y = (newY < 0) ? 0
                           : (newY > usedHeight - size) ? usedHeight - size
                                                        : newY;
        }

        void paintSelection(Graphics g) {
            g.setColor(DARK_TRANSPARENT);
            g.fillRect(0, 0, usedWidth, y);
            g.fillRect(0, y, x, usedHeight - y);
            g.fillRect(x + size, y, usedWidth - (x + size), usedHeight - y);
            g.fillRect(x, y + size, size, usedHeight - (y + size));
        }
    }

    private BufferedImage image;

    private final int SIZE;

    private int usedWidth;
    private int usedHeight;

    private Selection selection = new Selection();
    private State selectEnable;

    private BufferedImage selectedImage;
    private Observable imageUpdated = new Observable() {
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };

    AreaA(int size, State selectEnable) {
        SIZE = size;
        this.selectEnable = selectEnable;

        Observer enableObserver = (o, arg) -> {
            Boolean state = (Boolean) arg;
            if (!state) {
                selection.active = false;
                repaint();
            }
        };
        selectEnable.addObserver(enableObserver);

        setHorizontalAlignment(LEFT);
        setVerticalAlignment(TOP);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseAction(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selection.active = false;
                repaint();
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

        BufferedImage usedImage = MyPainter.shrinkImage(image, SIZE, SIZE);
        usedWidth = usedImage.getWidth();
        usedHeight = usedImage.getHeight();

        selection.size = (usedWidth > usedHeight) ? SIZE * usedWidth / image.getWidth()
                                                  : SIZE * usedHeight / image.getHeight();
        selection.active = false;

        setIcon(new ImageIcon(usedImage));
    }

    void addImageObserver(Observer o) {
        imageUpdated.addObserver(o);
    }

    private void recountImage() {
        int width = Integer.min(SIZE, image.getWidth());
        int height = Integer.min(SIZE, image.getHeight());

        int x, y;
        if (usedWidth > usedHeight) {
            x = Integer.min(selection.x * image.getWidth() / usedWidth, image.getWidth() - width);
            y = Integer.min(selection.y * image.getWidth() / usedWidth, image.getHeight() - height);
        } else {
            x = Integer.min(selection.x * image.getHeight() / usedHeight, image.getWidth() - width);
            y = Integer.min(selection.y * image.getHeight() / usedHeight, image.getHeight() - height);
        }

        selectedImage = image.getSubimage(x, y, width, height);
    }

    private void mouseAction(MouseEvent e) {
        if (!selectEnable.isTrue() || image == null)
            return;

        selection.relocate(e.getX(), e.getY());
        repaint();

        recountImage();
        imageUpdated.notifyObservers(selectedImage);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (selection.active)
            selection.paintSelection(g);
    }
}
