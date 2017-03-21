package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.image.BufferedImage;

public class RotationFilter implements Filter {

    private int angle = 45;

    public void setAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        double __angle = (Math.PI / 180) * -angle;

        double width = (image.getWidth() - 1) / 2.0;
        double height = (image.getHeight() - 1) / 2.0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int __x = (int) (Math.cos(__angle) * (x - width) - Math.sin(__angle) * (y - height)
                        + width + 0.5);
                int __y = (int) (Math.sin(__angle) * (x - width) + Math.cos(__angle) * (y - height)
                        + height + 0.5);

                int RGB;
                if (__x < 0 || __x >= image.getWidth() || __y < 0 || __y >= image.getHeight())
                    RGB = 0xFFFFFF;
                else
                    RGB = image.getRGB(__x, __y);
                result.setRGB(x, y, RGB);
            }
        }
        
        return result;
    }
}
