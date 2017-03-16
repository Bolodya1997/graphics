package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.filter.graphics.*;
import ru.nsu.fit.g14203.popov.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class MainFrame extends AbstractMainFrame {

    private final static Runnable NO_ACTION = () -> {};

    private final static Filter GRAYSCALE_FILTER                    = new GrayscaleFilter();
    private final static Filter INVERT_FILTER                       = new InvertFilter();
    private final static OrderedDitheringFilter OD_FILTER           = new OrderedDitheringFilter();
    private final static Filter MAGNIFY_FILTER                      = new MagnifyFilter();
    private final static SobelFilter SOBEL_FILTER                   = new SobelFilter();
    private final static Filter BLUR_FILTER                         = new BlurFilter();
    private final static Filter SHARPEN_FILTER                      = new SharpenFilter();
    private final static Filter EMBOSS_FILTER                       = new EmbossFilter();
    private final static Filter MEDIAN_FILTER                       = new MedianFilter();
    private final static RotationFilter ROTATION_FILTER             = new RotationFilter();

    private Saver saver = new Saver("FIT_14203_Popov_Vladimir_Filter_Data");

    private FilterPanel filterPanel;

    @Override
    protected JPanel createMainPanel() {
        filterPanel = new FilterPanel();
        return filterPanel;
    }

    public MainFrame() {
        super("Filter");

//        ------   icons   ------
        ImageIcon newIcon           = new ImageIcon(MainFrame.class.getResource("New.png"));
        ImageIcon openIcon          = new ImageIcon(MainFrame.class.getResource("Open.png"));
        ImageIcon saveAsIcon        = new ImageIcon(MainFrame.class.getResource("Save_as.png"));
        ImageIcon exitIcon          = new ImageIcon(MainFrame.class.getResource("Exit.png"));
        ImageIcon aboutIcon         = new ImageIcon(MainFrame.class.getResource("About.png"));
        ImageIcon selectIcon        = new ImageIcon(MainFrame.class.getResource("Select.png"));
        ImageIcon copyBCIcon        = new ImageIcon(MainFrame.class.getResource("Copy_B_C.png"));
        ImageIcon copyCBIcon        = new ImageIcon(MainFrame.class.getResource("Copy_C_B.png"));
        ImageIcon grayscaleIcon     = new ImageIcon(MainFrame.class.getResource("Grayscale.png"));
        ImageIcon invertIcon        = new ImageIcon(MainFrame.class.getResource("Invert.png"));
        ImageIcon magnifyIcon       = new ImageIcon(MainFrame.class.getResource("Magnify.png"));
        ImageIcon blurIcon          = new ImageIcon(MainFrame.class.getResource("Blur.png"));
        ImageIcon sharpenIcon       = new ImageIcon(MainFrame.class.getResource("Sharpen.png"));
        ImageIcon embossIcon        = new ImageIcon(MainFrame.class.getResource("Emboss.png"));
        ImageIcon watercolorIcon    = new ImageIcon(MainFrame.class.getResource("Watercolor.png"));

//        ------   menus   ------
//              ------   File   ------
        JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
//                      ------   New   ------
        JMenuItem newMenuItem = addMenuItem(fileMenu, "New", newIcon, KeyEvent.VK_N,
                "Create a new document", this::newAction);
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
                NO_ACTION, filterPanel.getSelectEnable(),
                filterPanel.getAreaAFilled());
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
                "Use the grayscale filter", () -> filtersAction(GRAYSCALE_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Invert   ------
        JMenuItem invertMenuItem = addMenuItem(editMenu, "Invert", invertIcon, KeyEvent.VK_I,
                "Use the invert filter", () -> filtersAction(INVERT_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Ordered dithering   ------
        JMenuItem orderedDitheringMenuItem = addMenuItem(editMenu, "Ordered dithering",
                aboutIcon, KeyEvent.VK_O, "Use the ordered dithering filter",
                () -> filtersAction(OD_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Magnify   ------
        JMenuItem magnifyMenuItem = addMenuItem(editMenu, "Magnify", magnifyIcon, KeyEvent.VK_M,
                "Magnify the image", () -> filtersAction(MAGNIFY_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Sobel   ------
        JMenuItem sobelMenuItem = addMenuItem(editMenu, "Sobel", aboutIcon, KeyEvent.VK_S,
                "Use the Sobel filter",
                () -> intValueFiltersAction(1, 0xFF, 55,
                                         SOBEL_FILTER::setLimit, GRAYSCALE_FILTER, SOBEL_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Blur   ------
        JMenuItem blurMenuItem = addMenuItem(editMenu, "Blur", blurIcon, KeyEvent.VK_B,
                "Use the blur filter", () -> filtersAction(BLUR_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Sharpen   ------
        JMenuItem sharpenMenuItem = addMenuItem(editMenu, "Sharpen", sharpenIcon, KeyEvent.VK_H,
                "Use the sharpen filter", () -> filtersAction(SHARPEN_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Emboss   ------
        JMenuItem embossMenuItem = addMenuItem(editMenu, "Emboss", embossIcon, KeyEvent.VK_E,
                "Use the emboss filter", () -> filtersAction(EMBOSS_FILTER, GRAYSCALE_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Watercolor   ------
        JMenuItem watercolorMenuItem = addMenuItem(editMenu, "Watercolor", watercolorIcon, KeyEvent.VK_W,
                "Use the watercolor filter", () -> filtersAction(MEDIAN_FILTER, SHARPEN_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Rotation   ------
        JMenuItem rotationMenuItem = addMenuItem(editMenu, "Rotation", aboutIcon, KeyEvent.VK_R,
                "Turn the picture", () -> intValueFiltersAction(0, 360, 0,
                        ROTATION_FILTER::setAngle, ROTATION_FILTER),
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
//                      ------   Ordered dithering   ------
        addToolBarButton(topToolBar, orderedDitheringMenuItem);
//                      ------   Magnify   ------
        addToolBarButton(topToolBar, magnifyMenuItem);
//                      ------   Sobel   ------
        addToolBarButton(topToolBar, sobelMenuItem);
//                      ------   Blur   ------
        addToolBarButton(topToolBar, blurMenuItem);
//                      ------   Sharpen   ------
        addToolBarButton(topToolBar, sharpenMenuItem);
//                      ------   Emboss   ------
        addToolBarButton(topToolBar, embossMenuItem);
//                      ------   Watercolor   ------
        addToolBarButton(topToolBar, watercolorMenuItem);
//                      ------   Rotation   ------
        addToolBarButton(topToolBar, rotationMenuItem);

//        ------   end of init   ------
        setVisible(true);
    }

    private void newAction() {
        filterPanel.clear();
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

    private void filtersAction(Filter ...filters) {
        State ready = new State(false);

        filterPanel.useFilters(ready, filters);
        new WaitingDialog(this, ready);
    }

    private void intValueFiltersAction(int min, int max, int value,
                                       Consumer<Integer> setValue, Filter ...filters) {
        State ready = new State(true);
        Consumer<Integer> actionOnChange = i -> {
            setValue.accept(i);
            filterPanel.useFilters(ready, filters);
        };

        new ActiveChangingDialog(this, "Set value", ready,
                                 min, max, value, actionOnChange);
    }
}
