package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FSDitheringFilter implements Filter {

    private int RSize = 2;
    private int GSize = 2;
    private int BSize = 2;

    public void setRSize(int RSize) {
        this.RSize = RSize;
    }

    public void setGSize(int GSize) {
        this.GSize = GSize;
    }

    public void setBSize(int BSize) {
        this.BSize = BSize;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = Util.copyImage(image);

        int off[][] = { { 1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };  //  { dx, dy }
        int k[] = { 7, 3, 5, 1 };

        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                int oR = (result.getRGB(x, y) & 0xFF0000) / 0x010000;
                int oG = (result.getRGB(x, y) & 0x00FF00) / 0x000100;
                int oB = (result.getRGB(x, y) & 0x0000FF);

                int R = Util.getClosestColor(oR, RSize);
                int G = Util.getClosestColor(oG, GSize);
                int B = Util.getClosestColor(oB, BSize);

                int RGB = R * 0x010000
                        + G * 0x000100
                        + B;
                result.setRGB(x, y, RGB);

                for (int i = 0; i < off.length; i++) {
                    int __x = x + off[i][0];
                    int __y = y + off[i][1];
                    if (__x < 0 || __x >= result.getWidth() || __y < 0 || __y >= result.getHeight())
                        continue;

                    int __R = (result.getRGB(__x, __y) & 0xFF0000) / 0x010000
                            + (int) ((oR - R) * k[i] / 16.0 + 0.5);
                    int __G = (result.getRGB(__x, __y) & 0x00FF00) / 0x000100
                            + (int) ((oG - G) * k[i] / 16.0 + 0.5);
                    int __B = (result.getRGB(__x, __y) & 0x0000FF)
                            + (int) ((oB - B) * k[i] / 16.0 + 0.5);

                    __R = (__R < 0) ? 0
                                    : (__R > 0xFF) ? 0xFF
                                    : __R;
                    __G = (__G < 0) ? 0
                                    : (__G > 0xFF) ? 0xFF
                                    : __G;
                    __B = (__B < 0) ? 0
                                    : (__B > 0xFF) ? 0xFF
                                    : __B;

                    int __RGB = __R * 0x010000
                              + __G * 0x000100
                              + __B;
                    result.setRGB(__x, __y, __RGB);
                }
            }
        }

        return result;
    }
}
