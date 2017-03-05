package ru.nsu.fit.g14203.popov.util;

import java.util.LinkedList;
import java.util.Observable;
import java.util.function.Consumer;

public class State extends Observable {

    private boolean state = false;
    private LinkedList<Consumer<Boolean>> stateChangedActionList = new LinkedList<>();

    public State() {
    }

    public State(boolean state) {
        this.state = state;
    }

    public void addStateChangedAction(Consumer<Boolean> stateChangedAction) {
        stateChangedActionList.add(stateChangedAction);
    }

    public void setState(boolean state) {
        if (this.state == state)
            return;

        this.state = state;
        setChanged();
        notifyObservers(state);

        for (Consumer<Boolean> consumer : stateChangedActionList)
            consumer.accept(state);
    }

    public boolean isTrue() {
        return state;
    }
}
