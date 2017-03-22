package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.image.BufferedImage;

public class GammaFilter implements Filter {

    private int gamma;

    public void setGamma(int gamma) {
        this.gamma = gamma;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = Util.copyImage(image);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int R = (image.getRGB(x, y) & 0xFF0000) / 0x010000;
                int G = (image.getRGB(x, y) & 0x00FF00) / 0x000100;
                int B = (image.getRGB(x, y) & 0x0000FF);

                R = (int) (Math.pow(R / ((double) 0xFF), gamma / 100.0) * 0xFF + 0.5);
                G = (int) (Math.pow(G / ((double) 0xFF), gamma / 100.0) * 0xFF + 0.5);
                B = (int) (Math.pow(B / ((double) 0xFF), gamma / 100.0) * 0xFF + 0.5);

                int RGB = R * 0x010000
                        + G * 0x000100
                        + B;
                result.setRGB(x, y, RGB);
            }
        }

        return result;
    }
}
