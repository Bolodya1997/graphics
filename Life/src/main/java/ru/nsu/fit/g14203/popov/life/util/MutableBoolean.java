package ru.nsu.fit.g14203.popov.life.util;

public class MutableBoolean {

    private boolean state;

    public MutableBoolean(boolean state) {
        this.state = state;
    }

    public boolean isTrue() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
