package ru.nsu.fit.g14203.popov.filter.graphics;

public class EmbossFilter extends MatrixFilter {

    @Override
    int[][] getMatrix() {
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
