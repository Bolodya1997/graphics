package ru.nsu.fit.g14203.popov.filter.graphics;

public class SharpenFilter extends AreaFilter {
    @Override
    int[][] getKernel() {
        return new int[][]{ {  0, -1,  0 },
                            { -1,  5, -1 },
                            {  0, -1,  0 } };
    }

    @Override
    int getDivider() {
        return 1;
    }

    @Override
    int getOffset() {
        return 0;
    }
}
