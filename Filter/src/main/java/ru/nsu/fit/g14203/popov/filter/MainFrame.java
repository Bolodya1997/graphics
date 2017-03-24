package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.filter.filters.*;
import ru.nsu.fit.g14203.popov.filter.filters.rendering.Rendering3DFilter;
import ru.nsu.fit.g14203.popov.util.AbstractMainFrame;
import ru.nsu.fit.g14203.popov.util.JToggleMenuItem;
import ru.nsu.fit.g14203.popov.util.State;
import ru.nsu.fit.g14203.popov.util.WaitingDialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.function.Consumer;

public class MainFrame extends AbstractMainFrame {

    private final static Runnable NO_ACTION = () -> {};

    private final static Filter GRAYSCALE_FILTER            = new GrayscaleFilter();
    private final static Filter INVERT_FILTER               = new InvertFilter();
    private final static OrderedDitheringFilter OD_FILTER   = new OrderedDitheringFilter();
    private final static FSDitheringFilter FSD_FILTER       = new FSDitheringFilter();
    private final static Filter MAGNIFY_FILTER              = new MagnifyFilter();
    private final static SobelFilter SOBEL_FILTER           = new SobelFilter();
    private final static RobertsFilter ROBERTS_FILTER       = new RobertsFilter();
    private final static Filter BLUR_FILTER                 = new BlurFilter();
    private final static Filter SHARPEN_FILTER              = new SharpenFilter();
    private final static Filter EMBOSS_FILTER               = new EmbossFilter();
    private final static Filter MEDIAN_FILTER               = new MedianFilter();
    private final static RotationFilter ROTATION_FILTER     = new RotationFilter();
    private final static GammaFilter GAMMA_FILTER           = new GammaFilter();
    private final static Rendering3DFilter RENDERING_FILTER = new Rendering3DFilter();

    private FilterPanel filterPanel;

    @Override
    protected JPanel createMainPanel() {
        filterPanel = new FilterPanel(RENDERING_FILTER.getConfigLoaded());
        return filterPanel;
    }

    public MainFrame() {
        super("Filter");

//        ------   icons   ------
        ImageIcon newIcon               = new ImageIcon(MainFrame.class.getResource("New.png"));
        ImageIcon openIcon              = new ImageIcon(MainFrame.class.getResource("Open.png"));
        ImageIcon saveAsIcon            = new ImageIcon(MainFrame.class.getResource("Save_as.png"));
        ImageIcon exitIcon              = new ImageIcon(MainFrame.class.getResource("Exit.png"));

        ImageIcon selectIcon            = new ImageIcon(MainFrame.class.getResource("Select.png"));
        ImageIcon copyBCIcon            = new ImageIcon(MainFrame.class.getResource("Copy_B_C.png"));
        ImageIcon copyCBIcon            = new ImageIcon(MainFrame.class.getResource("Copy_C_B.png"));

        ImageIcon grayscaleIcon         = new ImageIcon(MainFrame.class.getResource("Grayscale.png"));
        ImageIcon invertIcon            = new ImageIcon(MainFrame.class.getResource("Invert.png"));
        ImageIcon orderedDitheringIcon  = new ImageIcon(MainFrame.class.getResource("OrderedDithering.png"));
        ImageIcon FSDitheringIcon       = new ImageIcon(MainFrame.class.getResource("FSDithering.png"));
        ImageIcon magnifyIcon           = new ImageIcon(MainFrame.class.getResource("Magnify.png"));
        ImageIcon sobelIcon             = new ImageIcon(MainFrame.class.getResource("Sobel.png"));
        ImageIcon robertsIcon           = new ImageIcon(MainFrame.class.getResource("Roberts.png"));
        ImageIcon blurIcon              = new ImageIcon(MainFrame.class.getResource("Blur.png"));
        ImageIcon sharpenIcon           = new ImageIcon(MainFrame.class.getResource("Sharpen.png"));
        ImageIcon embossIcon            = new ImageIcon(MainFrame.class.getResource("Emboss.png"));
        ImageIcon watercolorIcon        = new ImageIcon(MainFrame.class.getResource("Watercolor.png"));
        ImageIcon rotationIcon          = new ImageIcon(MainFrame.class.getResource("Rotation.png"));
        ImageIcon gammaIcon             = new ImageIcon(MainFrame.class.getResource("Gamma.png"));

        ImageIcon configIcon            = new ImageIcon(MainFrame.class.getResource("Config.png"));
        ImageIcon absorptionIcon        = new ImageIcon(MainFrame.class.getResource("Absorption.png"));
        ImageIcon emissionIcon          = new ImageIcon(MainFrame.class.getResource("Emission.png"));
        ImageIcon renderIcon            = new ImageIcon(MainFrame.class.getResource("Render.png"));

        ImageIcon aboutIcon             = new ImageIcon(MainFrame.class.getResource("About.png"));

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
                "Save the resulting image with a new name", this::saveAsAction,
                filterPanel.getAreaCFilled());
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
                orderedDitheringIcon, KeyEvent.VK_O, "Use the ordered dithering filter",
                () -> ditheringFiltersAction(4, OD_FILTER::setRSize,
                                             4, OD_FILTER::setGSize,
                                             4, OD_FILTER::setBSize,
                                             OD_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Floyd-Steinberg dithering   ------
        JMenuItem fsDitheringMenuItem = addMenuItem(editMenu, "Floyd-Steinberg dithering",
                FSDitheringIcon, KeyEvent.VK_F, "Use the Floyd-Steinberg dithering filter",
                () -> ditheringFiltersAction(4, FSD_FILTER::setRSize,
                                             4, FSD_FILTER::setGSize,
                                             4, FSD_FILTER::setBSize,
                                             FSD_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Magnify   ------
        JMenuItem magnifyMenuItem = addMenuItem(editMenu, "Magnify", magnifyIcon, KeyEvent.VK_M,
                "Magnify the image", () -> filtersAction(MAGNIFY_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Sobel   ------
        JMenuItem sobelMenuItem = addMenuItem(editMenu, "Sobel", sobelIcon, KeyEvent.VK_S,
                "Use the Sobel filter",
                () -> intValueFiltersAction("Set limit", 1, 0xFF,
                                            55, SOBEL_FILTER::setLimit,
                                            GRAYSCALE_FILTER, SOBEL_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Roberts cross   ------
        JMenuItem robertsMenuItem = addMenuItem(editMenu, "Roberts cross", robertsIcon, KeyEvent.VK_R,
                "Use the Roberts cross filter",
                () -> intValueFiltersAction("Set limit", 1, 0xFF,
                                            16, ROBERTS_FILTER::setLimit,
                                            GRAYSCALE_FILTER, ROBERTS_FILTER),
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
        JMenuItem rotationMenuItem = addMenuItem(editMenu, "Rotation", rotationIcon, KeyEvent.VK_T,
                "Rotate the picture",
                () -> intValueFiltersAction("Set angle", -180, 180,
                                            0, ROTATION_FILTER::setAngle,
                                            ROTATION_FILTER),
                filterPanel.getAreaBFilled());
//                      ------   Gamma   ------
        JMenuItem gammaMenuItem = addMenuItem(editMenu, "Gamma", gammaIcon, KeyEvent.VK_G,
                "Gamma correction",
                () -> intValueFiltersAction("Set gamma * 100", 10, 300,
                        100, GAMMA_FILTER::setGamma,
                        GAMMA_FILTER),
                filterPanel.getAreaBFilled());
//              ------   Rendering   ------
        JMenu renderingMenu = addMenu("Rendering", KeyEvent.VK_R);
//                      ------   Config   ------
        JMenuItem configMenuItem = addMenuItem(renderingMenu, "Config", configIcon, KeyEvent.VK_C,
                "Open rendering configuration file", this::configAction,
                filterPanel.getAreaBFilled());
//                      ------      ------
        renderingMenu.addSeparator();
//                      ------   Absorption   ------
        JToggleMenuItem absorptionToggleMenuItem = addToggleMenuItem(renderingMenu, "Absorption",
                absorptionIcon, KeyEvent.VK_A,
                "Enable absorption", "Disable absorption",
                NO_ACTION, RENDERING_FILTER.getAbsorptionEnable(),
                RENDERING_FILTER.getConfigLoaded());
//                      ------   Emission   ------
        JToggleMenuItem emissionToggleMenuItem = addToggleMenuItem(renderingMenu, "Emission",
                emissionIcon, KeyEvent.VK_E,
                "Enable emission", "Disable emission",
                NO_ACTION, RENDERING_FILTER.getEmissionEnable(),
                RENDERING_FILTER.getConfigLoaded());
//                      ------   Render   ------
        JMenuItem renderMenuItem = addMenuItem(renderingMenu, "Render", renderIcon, KeyEvent.VK_R,
                "Use the 3D rendering filter", this::renderAction,
                RENDERING_FILTER.getConfigLoaded());
//              ------   Help   ------
        JMenu helpMenu = addMenu("Help", KeyEvent.VK_H);
//                      ------   About   ------
        addMenuItem(helpMenu, "About", aboutIcon, KeyEvent.VK_A,
                "Show information about Filter", this::showAboutDialog);

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
//                      ------   Floyd-Steinberg dithering   ------
        addToolBarButton(topToolBar, fsDitheringMenuItem);
//                      ------   Magnify   ------
        addToolBarButton(topToolBar, magnifyMenuItem);
//                      ------   Sobel   ------
        addToolBarButton(topToolBar, sobelMenuItem);
//                      ------   Roberts cross   ------
        addToolBarButton(topToolBar, robertsMenuItem);
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
//                      ------   Gamma   ------
        addToolBarButton(topToolBar, gammaMenuItem);
//                      ------      ------
        topToolBar.addSeparator();
//                      ------   Config   ------
        addToolBarButton(topToolBar, configMenuItem);
//                      ------      ------
        topToolBar.addSeparator();
//                      ------   Absorption   ------
        addToolBarToggleButton(topToolBar, absorptionToggleMenuItem);
//                      ------   Emission   ------
        addToolBarToggleButton(topToolBar, emissionToggleMenuItem);
//                      ------   Render   ------
        addToolBarButton(topToolBar, renderMenuItem);

//        ------   end of init   ------
        setVisible(true);
    }

    private void newAction() {
        filterPanel.clear();
        RENDERING_FILTER.clear();
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Bad picture", "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAsAction() {
        JFileChooser fileChooser = new JFileChooser("FIT_14203_Popov_Vladimir_Filter_Data");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                return f.isDirectory() || name.endsWith(".bmp");
            }

            @Override
            public String getDescription() {
                return "BMP files (*.bmp)";
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".bmp"))
                path += ".bmp";

            filterPanel.saveImage(new FileOutputStream(path));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Permission denied", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtersAction(Filter ...filters) {
        State ready = new State(false);

        filterPanel.useFilters(ready, filters);
        new WaitingDialog(this, ready);
    }

    private void ditheringFiltersAction(int R, Consumer<Integer> setR,
                                        int G, Consumer<Integer> setG,
                                        int B, Consumer<Integer> setB,
                                        Filter ...filters) {
        String[] names = { "R", "G", "B" };
        int[] min = { 2, 2, 2 };
        int[] max = { 0xFF, 0xFF, 0xFF };
        int[] values = { R, G, B };
        Consumer[] setters = { setR, setG, setB };

        new Int3ValueDialog(this, "Set palette size", () -> filtersAction(filters),
                            min, max,
                            names, values, setters);
    }

    private void intValueFiltersAction(String title, int min, int max,
                                       int value, Consumer<Integer> setValue,
                                       Filter ...filters) {
        State ready = new State(true);
        Consumer<Integer> actionOnChange = i -> {
            setValue.accept(i);
            filterPanel.useFilters(ready, filters);
        };

        new IntValueDialog(this, title, ready,
                                 min, max, value, actionOnChange);
    }

    private void configAction() {
        JFileChooser fileChooser = new JFileChooser("FIT_14203_Popov_Vladimir_Filter_Data");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                return f.isDirectory() || name.endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text files (*.txt)";
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            RENDERING_FILTER.config(new FileInputStream(fileChooser.getSelectedFile()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Bad config file", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        filterPanel.setAbsorptionPoints(RENDERING_FILTER.getAbsorptionEdges());
        filterPanel.setEmissionPoints(RENDERING_FILTER.getEmissionEdges());
        filterPanel.repaint();
    }

    private void renderAction() {
        Dimension size = filterPanel.getImageSize();

        String[] names = { "X", "Y", "Z" };
        int[] min = { 1, 1, 1 };
        int[] max = { Integer.min(350, size.width), Integer.min(350, size.height), 350 };
        Consumer[] setters = { ((Consumer<Integer>) RENDERING_FILTER::setSizeX),
                               ((Consumer<Integer>) RENDERING_FILTER::setSizeY),
                               ((Consumer<Integer>) RENDERING_FILTER::setSizeZ) };

        new Int3ValueDialog(this, "Set step count", () -> filtersAction(RENDERING_FILTER),
                min, max,
                names, max, setters);
    }
}
