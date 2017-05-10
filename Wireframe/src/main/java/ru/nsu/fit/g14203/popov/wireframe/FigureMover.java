package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;

public class FigureMover {

    private final static FigureMover INSTANCE = new FigureMover();

    private FigureMover() {}

    public static FigureMover getInstance() {
        return INSTANCE;
    }

    private Figure3D figure;
    private boolean enable;

    public Figure3D getFigure() {
        return figure;
    }

    public void setFigure(Figure3D figure) {
        this.figure = figure;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
