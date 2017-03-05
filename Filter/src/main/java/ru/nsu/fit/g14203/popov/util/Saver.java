package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;
import java.io.*;

class Saver {

    private static final String HOME_DIRECTORY = "FIT_14203_Popov_Vladimir_Life_Data";
    private static final String DEFAULT_NAME = "Untitled";

    private String name = DEFAULT_NAME;
    private String path = null;

    String getName() {
        return name;
    }

    void reset() {
        name = DEFAULT_NAME;
        path = null;
    }

    InputStream open() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(HOME_DIRECTORY);
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            return null;

        File file = chooser.getSelectedFile();
        name = file.getName();
        path = file.getPath();

        return new FileInputStream(file);
    }

    OutputStream save() throws FileNotFoundException {
        if (path == null)
            return saveAs();

        return new FileOutputStream(path);
    }

    OutputStream saveAs() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(HOME_DIRECTORY);
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
            return null;

        File file = chooser.getSelectedFile();
        name = file.getName();
        path = file.getPath();

        return new FileOutputStream(file);
    }
}
