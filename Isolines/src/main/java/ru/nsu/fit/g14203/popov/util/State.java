package ru.nsu.fit.g14203.popov.util;

import java.util.Observable;

public class State extends Observable {

    private boolean state = false;

    public State(boolean state) {
        this.state = state;
    }

    public void setState(boolean state) {
        if (this.state == state)
            return;

        synchronized (this) {
            this.state = state;
        }

        setChanged();
        notifyObservers(state);
    }

    public boolean isTrue() {
        return state;
    }
}
