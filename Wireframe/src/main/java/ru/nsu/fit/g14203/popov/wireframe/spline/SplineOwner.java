package ru.nsu.fit.g14203.popov.wireframe.spline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.function.Consumer;

public class SplineOwner extends Observable {

    private final static SplineOwner INSTANCE = new SplineOwner();

    private double lengthFrom   = 0;
    private double lengthTo     = 1;

    private double rotateFrom   = 0;
    private double rotateTo     = 2 * Math.PI;

    private int lengthCount     = 10;
    private int lengthK         = 5;
    private int rotateCount     = 10;

    private List<Consumer<Spline>> actionsOnAdd     = new ArrayList<>();
    private List<Consumer<Spline>> actionsOnRemove  = new ArrayList<>();

    private List<Spline> splines = new ArrayList<>();

    public static SplineOwner getInstance() {
        return INSTANCE;
    }

    Spline[] getSplines() {
        return splines.stream().toArray(Spline[]::new);
    }

    public void addSpline(Spline spline) {
        splines.add(spline);
        actionsOnAdd.forEach(action -> action.accept(spline));
    }

    void removeSpline(Spline spline) {
        splines.remove(spline);
        actionsOnRemove.forEach(action -> action.accept(spline));
    }

    public void clear() {
        while (!splines.isEmpty()) {
            Spline spline = splines.remove(0);
            actionsOnRemove.forEach(action -> action.accept(spline));
        }
    }

    public void addActionOnAdd(Consumer<Spline> actionOnAdd) {
        actionsOnAdd.add(actionOnAdd);
    }

    public void addActionOnRemove(Consumer<Spline> actionOnRemove) {
        actionsOnRemove.add(actionOnRemove);
    }

    //    ------   properties start   ------

    public double getLengthFrom() {
        return lengthFrom;
    }

    public void setLengthFrom(double lengthFrom) {
        this.lengthFrom = lengthFrom;
        notifyObservers();
    }

    public double getLengthTo() {
        return lengthTo;
    }

    public void setLengthTo(double lengthTo) {
        this.lengthTo = lengthTo;
        notifyObservers();
    }

    public double getRotateFrom() {
        return rotateFrom;
    }

    public void setRotateFrom(double rotateFrom) {
        this.rotateFrom = rotateFrom;
        notifyObservers();
    }

    public double getRotateTo() {
        return rotateTo;
    }

    public void setRotateTo(double rotateTo) {
        this.rotateTo = rotateTo;
        notifyObservers();
    }

    public int getLengthCount() {
        return lengthCount;
    }

    public void setLengthCount(int lengthCount) {
        this.lengthCount = lengthCount;
        notifyObservers();
    }

    public int getLengthK() {
        return lengthK;
    }

    public void setLengthK(int lengthK) {
        this.lengthK = lengthK;
        notifyObservers();
    }

    public int getRotateCount() {
        return rotateCount;
    }

    public void setRotateCount(int rotateCount) {
        this.rotateCount = rotateCount;
        notifyObservers();
    }

//    ------   properties end   ------

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
