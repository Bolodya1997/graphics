package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.util.State;
import ru.nsu.fit.g14203.popov.util.WaitingDialog;

import javax.swing.*;
import java.io.*;

public class MainFrame extends AbstractMainFrame {

    private FunctionPanel functionPanel;

    @Override
    protected JPanel createMainPanel() {
        functionPanel = new FunctionPanel();
        return functionPanel;
    }

    public MainFrame() {
        super("Isolines");

//        ------  end of init   ------
        setVisible(true);

        Reader reader = new StringReader("10 10\n" +
                                            "32\n" +
                                            "0 0 0\n" +
                                            "8 8 8\n" +
                                            "16 16 16\n" +
                                            "24 24 24\n" +
                                            "32 32 32\n" +
                                            "40 40 40\n" +
                                            "48 48 48\n" +
                                            "56 56 56\n" +
                                            "64 64 64\n" +
                                            "72 72 72\n" +
                                            "80 80 80\n" +
                                            "88 88 88\n" +
                                            "96 96 96\n" +
                                            "104 104 104\n" +
                                            "112 112 112\n" +
                                            "120 120 120\n" +
                                            "128 128 128\n" +
                                            "136 136 136\n" +
                                            "144 144 144\n" +
                                            "152 152 152\n" +
                                            "160 160 160\n" +
                                            "168 168 168\n" +
                                            "176 176 176\n" +
                                            "184 184 184\n" +
                                            "192 192 192\n" +
                                            "200 200 200\n" +
                                            "208 208 208\n" +
                                            "216 216 216\n" +
                                            "224 224 224\n" +
                                            "232 232 232\n" +
                                            "240 240 240\n" +
                                            "248 248 248\n" +
                                            "255 255 255\n" +
                                            "255 255 0\n" +
                                            "-6.83 -3.84\n" +
                                            "6.82 3.83");
        loadFunction(reader);
    }

    /**
     *  File format:
     *  X Y         isoline grid
     *  n           borders count
     *  R0 G0 B0
     *  ...
     *  Rn Gn Bn
     *  R G B       isoline color
     *  x0 y0       from
     *  x1 y1       to
     */
    private void loadFunction(Reader reader) {
        State ready = new State(false);
        new Thread(() -> {
            functionPanel.loadFunction(reader);
            ready.setState(true);
        }).start();

        new WaitingDialog(this, ready);
    }
}
