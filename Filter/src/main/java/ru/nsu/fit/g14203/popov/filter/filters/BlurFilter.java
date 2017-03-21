package ru.nsu.fit.g14203.popov.filter.filters;

public class BlurFilter extends AreaFilter {

    @Override
    int[][] getKernel() {
        return new int[][]{ { 0, 1, 0 },
                            { 1, 2, 1 },
                            { 0, 1, 0 } };
    }

    @Override
    int getDivider() {
        return 6;
    }

    @Override
    int getOffset() {
        return 0;
    }
}
