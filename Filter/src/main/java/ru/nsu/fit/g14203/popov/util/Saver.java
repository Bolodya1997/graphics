package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;
import java.io.*;

public class Saver {

    private static final String DEFAULT_NAME = "Untitled";

    private String homeDirectory;

    private String name = DEFAULT_NAME;
    private String path = null;

    public Saver(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getName() {
        return name;
    }

    public void reset() {
        name = DEFAULT_NAME;
        path = null;
    }

    public InputStream open() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(homeDirectory);
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            return null;

        File file = chooser.getSelectedFile();
        name = file.getName();
        path = file.getPath();

        return new FileInputStream(file);
    }

    public OutputStream save() throws FileNotFoundException {
        if (path == null)
            return saveAs();

        return new FileOutputStream(path);
    }

    public OutputStream saveAs() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(homeDirectory);
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
            return null;

        File file = chooser.getSelectedFile();
        name = file.getName();
        path = file.getPath();

        return new FileOutputStream(file);
    }
}
