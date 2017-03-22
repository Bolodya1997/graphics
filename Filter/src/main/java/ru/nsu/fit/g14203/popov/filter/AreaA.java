package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.util.State;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

class AreaA extends Area {

    private final static Color DARK_TRANSPARENT = new Color(0, 0, 0, 0x7F);

    private final static int BORDER = 1;

    private class Selection {
        int x;
        int y;

        int width;
        int height;

        boolean active = false;

        void relocate(int mouseX, int mouseY) {
            active = true;

            int newX = (mouseX - BORDER) - width / 2;
            x = (newX < 0) ? 0
                           : (newX > usedWidth - width) ? usedWidth - width
                                                        : newX;

            int newY = (mouseY - BORDER) - height / 2;
            y = (newY < 0) ? 0
                           : (newY > usedHeight - height) ? usedHeight - height
                                                          : newY;
        }

        void paintSelection(Graphics g) {   //  TODO: fix bugged partition repaint
            g.setXORMode(DARK_TRANSPARENT);

            g.fillRect(BORDER, BORDER, usedWidth, usedHeight);
            g.fillRect(x + BORDER, y + BORDER, width, height);

            g.setPaintMode();
            g.setColor(DARK_TRANSPARENT);

            g.fillRect(BORDER, BORDER, usedWidth, y);
            g.fillRect(BORDER, (y + height) + BORDER, usedWidth, usedHeight - (y + height));
            g.fillRect(BORDER, y + BORDER, x, height);
            g.fillRect((x + width) + BORDER, y + BORDER, usedWidth - (x + width), height);
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

    AreaA(State filled, int size, State selectEnable) {
        super(filled);

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

        if (image == null) {
            selectEnable.setState(false);
            super.setImage(null);
            return;
        }

        BufferedImage usedImage = MyPainter.shrinkImage(image, SIZE, SIZE);
        usedWidth = usedImage.getWidth();
        usedHeight = usedImage.getHeight();

        selection.width = ((SIZE < image.getWidth()) ? SIZE : image.getWidth())
                        * usedWidth / image.getWidth();
        selection.height = ((SIZE < image.getHeight()) ? SIZE : image.getHeight())
                         * usedHeight / image.getHeight();

        selection.active = false;

        super.setImage(usedImage);
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
