package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.filter.graphics.*;
import ru.nsu.fit.g14203.popov.util.DefaultMainFrame;
import ru.nsu.fit.g14203.popov.util.JToggleMenuItem;
import ru.nsu.fit.g14203.popov.util.Saver;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFrame extends DefaultMainFrame {

    private final static Runnable NO_ACTION = () -> {};

    private final static SimpleFilter GRAYSCALE_FILTER = new GrayscaleFilter();
    private final static SimpleFilter INVERT_FILTER = new InvertFilter();
    private final static SimpleFilter MAGNIFY_FILTER = new MagnifyFilter();
    private final static SimpleFilter BLUR_FILTER = new BlurFilter();
    private final static SimpleFilter SHARPEN_FILTER = new SharpenFilter();
    private final static SimpleFilter EMBOSS_FILTER = new EmbossFilter();

    private Saver saver = new Saver("FIT_14203_Popov_Vladimir_Filter_Data");

    private FilterPanel filterPanel;

    public MainFrame() {
        super("MainFrame", new FilterPanel());

        filterPanel = (FilterPanel) mainPanel;

//        ------   icons   ------
        ImageIcon newIcon = new ImageIcon(MainFrame.class.getResource("New.png"));
        ImageIcon openIcon = new ImageIcon(MainFrame.class.getResource("Open.png"));
        ImageIcon saveAsIcon = new ImageIcon(MainFrame.class.getResource("Save_as.png"));
        ImageIcon exitIcon = new ImageIcon(MainFrame.class.getResource("Exit.png"));
        ImageIcon selectIcon = new ImageIcon(MainFrame.class.getResource("Select.png"));
        ImageIcon copyBCIcon = new ImageIcon(MainFrame.class.getResource("Copy_B_C.png"));
        ImageIcon copyCBIcon = new ImageIcon(MainFrame.class.getResource("Copy_C_B.png"));
        ImageIcon grayscaleIcon = new ImageIcon(MainFrame.class.getResource("Grayscale.png"));
        ImageIcon invertIcon = new ImageIcon(MainFrame.class.getResource("Invert.png"));
        ImageIcon magnifyIcon = new ImageIcon(MainFrame.class.getResource("Magnify.png"));
        ImageIcon blurIcon = new ImageIcon(MainFrame.class.getResource("Blur.png"));
        ImageIcon sharpenIcon = new ImageIcon(MainFrame.class.getResource("Sharpen.png"));
        ImageIcon embossIcon = new ImageIcon(MainFrame.class.getResource("Emboss.png"));

//        ------   menus   ------
//              ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//                      ------   New   ------
        JMenuItem newMenuItem = addMenuItem(fileMenu, "New", newIcon, KeyEvent.VK_N,
                "Create a new document", NO_ACTION);
//                      ------   Open   ------
        JMenuItem openMenuItem = addMenuItem(fileMenu, "Open", openIcon, KeyEvent.VK_O,
                "Open a new image", this::openAction);
//                      ------   Save   ------
        JMenuItem saveAsMenuItem = addMenuItem(fileMenu, "Save as...", saveAsIcon, KeyEvent.VK_S,
                "Save the resulting image with a new name", NO_ACTION);
//                      ------      ------
        fileMenu.addSeparator();
//                      ------   Exit   ------
        addMenuItem(fileMenu, "Exit", exitIcon, KeyEvent.VK_E,
                "Quit Filter", NO_ACTION);
//              ------   View   ------
        JMenu viewMenu = addMenu("View", KeyEvent.VK_V);
//                      ------   Select   ------
        JToggleMenuItem selectToggleMenuItem = addToggleMenuItem(viewMenu, "Select", selectIcon,
                KeyEvent.VK_S, "Enable selection", "Disable selection",
                NO_ACTION, filterPanel.getSelectEnable());
//                      ------   Copy B -> C   ------
        JMenuItem copyBCMenuItem = addMenuItem(viewMenu, "Copy B -> C", copyBCIcon, KeyEvent.VK_B,
                "Copy the image from the area B to the area C", filterPanel::copyBC,
                filterPanel.getAreaBFilled());
//                      ------   Copy C -> B   ------
        JMenuItem copyCBMenuItem = addMenuItem(viewMenu, "Copy C -> B", copyCBIcon, KeyEvent.VK_C,
                "Copy the image from the area C to the area B", filterPanel::copyCB,
                filterPanel.getAreaCFilled());
//              ------   Edit   ------
        JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
//                      ------   Grayscale   ------
        JMenuItem grayscaleMenuItem = addMenuItem(editMenu, "Grayscale", grayscaleIcon, KeyEvent.VK_G,
                "Use the grayscale filter", () -> filterPanel.useFilters(GRAYSCALE_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Invert   ------
        JMenuItem invertMenuItem = addMenuItem(editMenu, "Invert", invertIcon, KeyEvent.VK_I,
                "Use the invert filter", () -> filterPanel.useFilters(INVERT_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Invert   ------
        JMenuItem magnifyMenuItem = addMenuItem(editMenu, "Magnify", magnifyIcon, KeyEvent.VK_M,
                "Magnify the image", () -> filterPanel.useFilters(MAGNIFY_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Blur   ------
        JMenuItem blurMenuItem = addMenuItem(editMenu, "Blur", blurIcon, KeyEvent.VK_B,
                "Use the blur filter", () -> filterPanel.useFilters(BLUR_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Sharpen   ------
        JMenuItem sharpenMenuItem = addMenuItem(editMenu, "Sharpen", sharpenIcon, KeyEvent.VK_S,
                "Use the sharpen filter", () -> filterPanel.useFilters(SHARPEN_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Emboss   ------
        JMenuItem embossMenuItem = addMenuItem(editMenu, "Emboss", embossIcon, KeyEvent.VK_E,
                "Use the emboss filter", () -> filterPanel.useFilters(EMBOSS_FILTER, GRAYSCALE_FILTER),
                filterPanel.getAreaBFilled());

//        ------   toolbars   ------
//              ------   TOP   ------
        JToolBar topToolBar = addToolbar(TOP);
//                      ------   New   ------
        addToolBarButton(topToolBar, newMenuItem);
//                      ------   Open   ------
        addToolBarButton(topToolBar, openMenuItem);
//                      ------   Save as   ------
        addToolBarButton(topToolBar, saveAsMenuItem);
//                      ------      ------
        topToolBar.addSeparator();
//                      ------   Select   ------
        addToolBarToggleButton(topToolBar, selectToggleMenuItem);
//                      ------   Copy B -> C   ------
        addToolBarButton(topToolBar, copyBCMenuItem);
//                      ------   Copy C -> B   ------
        addToolBarButton(topToolBar, copyCBMenuItem);
//                      ------      ------
        topToolBar.addSeparator();
//                      ------   Grayscale   ------
        addToolBarButton(topToolBar, grayscaleMenuItem);
//                      ------   Invert   ------
        addToolBarButton(topToolBar, invertMenuItem);
//                      ------   Magnify   ------
        addToolBarButton(topToolBar, magnifyMenuItem);
//                      ------   Blur   ------
        addToolBarButton(topToolBar, blurMenuItem);
//                      ------   Sharpen   ------
        addToolBarButton(topToolBar, sharpenMenuItem);
//                      ------   Emboss   ------
        addToolBarButton(topToolBar, embossMenuItem);

//        ------   end of init   ------
        setVisible(true);
    }

    private void openAction() {
        JFileChooser fileChooser = new JFileChooser("FIT_14203_Popov_Vladimir_Filter_Data");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                return f.isDirectory() || name.endsWith(".bmp") || name.endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "BMP, PNG files (*.bmp, *.png)";
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            filterPanel.openImage(new FileInputStream(fileChooser.getSelectedFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
