package ru.nsu.fit.g14203.popov.filter.filters;

public class EmbossFilter extends AreaFilter {

    @Override
    int[][] getKernel() {
        return new int[][]{ {  0, -1,  0 },
                            { -1,  0,  1 },
                            {  0,  1,  0 } };
    }

    @Override
    int getDivider() {
        return 1;
    }

    @Override
    int getOffset() {
        return 128;
    }
}
