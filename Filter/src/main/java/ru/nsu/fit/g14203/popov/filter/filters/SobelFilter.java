package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.image.BufferedImage;

public class SobelFilter extends AreaFilter {

    private final static int[][] KERNEL_X = { {  1,  0, -1 },
                                              {  2,  0, -2 },
                                              {  1,  0, -1 } };
    private final static int[][] KERNEL_Y = { {  1,  2,  1 },
                                              {  0,  0,  0 },
                                              { -1, -2, -1 } };

    private int[][] kernel;

    private int limit;

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    int[][] getKernel() {
        return kernel;
    }

    @Override
    int getDivider() {
        return 1;
    }

    @Override
    int getOffset() {
        return 0;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        kernel = KERNEL_X;
        BufferedImage result = super.apply(image);

        kernel = KERNEL_Y;
        BufferedImage tmp = super.apply(image);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int grayX = result.getRGB(x, y) & 0xFF;
                int grayY = tmp.getRGB(x, y) & 0xFF;
                int gray = (int) (Math.sqrt(grayX * grayX + grayY * grayY) + 0.5);

                int RGB = (gray < limit) ? 0x000000 : 0xFFFFFF;
                result.setRGB(x, y, RGB);
            }
        }

        return result;
    }
}
