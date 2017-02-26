package ru.nsu.fit.g14203.popov.life;

import javax.swing.*;
import java.io.*;

class Saver {

    private static final String HOME_DIRECTORY = "FIT_14203_Popov_Vladimir_Life_Data";
    private static final String DEFAULT_NAME = "Untitled";

    private File currentFile;
    private String name = DEFAULT_NAME;

    String getName() {
        return name;
    }

    void reset() {
        name = DEFAULT_NAME;
    }

    InputStream open() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(HOME_DIRECTORY);
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            throw new FileNotFoundException();

        currentFile = chooser.getSelectedFile();
        name = currentFile.getName();

        return new FileInputStream(currentFile);
    }

    OutputStream save() throws FileNotFoundException {
        if (currentFile != null)
            return new FileOutputStream(currentFile);

        return saveAs();
    }

    OutputStream saveAs() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(HOME_DIRECTORY);
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
            throw new FileNotFoundException();

        currentFile = chooser.getSelectedFile();
        name = currentFile.getName();

        return new FileOutputStream(chooser.getSelectedFile());
    }
}
