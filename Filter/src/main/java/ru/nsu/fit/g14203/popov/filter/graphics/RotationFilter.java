package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

public class RotationFilter implements Filter {

    private int angle = 45;

    public void setAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        double __angle = (Math.PI / 180) * (360 - angle);

        int width = image.getWidth() / 2;
        int height = image.getHeight() / 2;
        for (int x = -width; x < width; x++) {
            for (int y = -height; y < height; y++) {
                int __x = (int) (Math.cos(__angle) * x - Math.sin(__angle) * y + 0.5) + width;
                int __y = (int) (Math.sin(__angle) * x + Math.cos(__angle) * y + 0.5) + height;

                if (__x < 0 || __x >= image.getWidth() || __y < 0 || __y >= image.getHeight())
                    continue;

                int RGB = image.getRGB(__x, __y);
                result.setRGB(x + width, y + height, RGB);
            }
        }
        
        return result;
    }
}
