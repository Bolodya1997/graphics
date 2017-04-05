package ru.nsu.fit.g14203.popov.util;

import java.awt.image.BufferedImage;

public class ReversedBufferedImage extends BufferedImage {

    public ReversedBufferedImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    protected int reverseY(int y) {
        return (getHeight() - 1) - y;
    }

    @Override
    public synchronized void setRGB(int x, int y, int rgb) {
        super.setRGB(x, reverseY(y), rgb);
    }

    @Override
    public int getRGB(int x, int y) {
        return super.getRGB(x, reverseY(y));
    }
}
