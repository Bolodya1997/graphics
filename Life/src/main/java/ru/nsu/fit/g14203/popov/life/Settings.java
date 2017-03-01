package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MutableDouble;
import ru.nsu.fit.g14203.popov.life.util.MutableInteger;

class Settings {

    MutableInteger gridWidth = new MutableInteger(10);
    MutableInteger gridHeight = new MutableInteger(10);

    MutableInteger size = new MutableInteger(50);
    MutableInteger width = new MutableInteger(1);

    MutableDouble lifeBegin = new MutableDouble(2.0);
    MutableDouble lifeEnd = new MutableDouble(3.3);

    MutableDouble birthBegin = new MutableDouble(2.3);
    MutableDouble birthEnd = new MutableDouble(2.9);

    MutableDouble firstImpact = new MutableDouble(1.0);
    MutableDouble secondImpact = new MutableDouble(0.3);

    Settings() {

    }

    Settings(Settings settings) {
        gridWidth = new MutableInteger(settings.gridWidth.getValue());
        gridHeight = new MutableInteger(settings.gridHeight.getValue());

        size = new MutableInteger(settings.size.getValue());
        width = new MutableInteger(settings.width.getValue());

        lifeBegin = new MutableDouble(settings.lifeBegin.getValue());
        lifeEnd = new MutableDouble(settings.lifeEnd.getValue());

        birthBegin = new MutableDouble(settings.birthBegin.getValue());
        birthEnd = new MutableDouble(settings.birthEnd.getValue());

        firstImpact = new MutableDouble(settings.firstImpact.getValue());
        secondImpact = new MutableDouble(settings.secondImpact.getValue());
    }
}
