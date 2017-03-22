package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.filter.filters.Filter;
import ru.nsu.fit.g14203.popov.util.State;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FilterPanel extends JPanel {

    private final static int AREA_WIDTH         = 350;
    private final static int AREA_HEIGHT        = 350;
    private final static Dimension AREA_SIZE    = new Dimension(AREA_WIDTH + 2, AREA_HEIGHT + 2);

    private final static int CHART_WIDTH        = AREA_WIDTH;
    private final static int CHART_HEIGHT       = 150;
    private final static Dimension CHART_SIZE   = new Dimension(CHART_WIDTH + 2, CHART_HEIGHT + 2);

    private State selectEnable  = new State(false);
    private State areaAFilled   = new State(false);
    private State areaBFilled   = new State(false);
    private State areaCFilled   = new State(false);

    private AreaA areaA;
    private Area areaB;
    private Area areaC;

    private Point[] absorptionPoints;
    private Point[][] emissionPoints;

    FilterPanel(State chartPainted) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0,
                1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(15, 15, 0, 0), 0, 0);

//        ------   areaA   ------
        areaA = new AreaA(areaAFilled, AREA_WIDTH, selectEnable);
        initArea(areaA);

        add(areaA, constraints);

//        ------   areaB   ------
        areaB = new Area(areaBFilled);
        initArea(areaB);

        areaA.addImageObserver((o, arg) -> areaB.setImage((BufferedImage) arg));

        add(areaB, constraints);

//        ------   areaC   ------
        areaC = new Area(areaCFilled);
        initArea(areaC);

        add(areaC, constraints);

//        ------   absorptionChart   ------
        JPanel absorptionChart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (chartPainted.isTrue()) {
                    g.setColor(Color.BLACK);
                    MyPainter.drawChart(g, absorptionPoints, CHART_WIDTH, CHART_HEIGHT, 0);
                }
            }
        };
        absorptionChart.setBorder(BorderFactory.createDashedBorder(Color.DARK_GRAY, 1, 5));
        absorptionChart.setPreferredSize(CHART_SIZE);

        ++constraints.gridy;
        add(absorptionChart, constraints);

//        ------   emissionChart   ------
        JPanel emissionChart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (chartPainted.isTrue()) {
                    g.setColor(Color.RED);
                    MyPainter.drawChart(g, emissionPoints[0], CHART_WIDTH, CHART_HEIGHT - 2, 0);
                    g.setColor(Color.GREEN);
                    MyPainter.drawChart(g, emissionPoints[1], CHART_WIDTH, CHART_HEIGHT - 2, 1);
                    g.setColor(Color.BLUE);
                    MyPainter.drawChart(g, emissionPoints[2], CHART_WIDTH, CHART_HEIGHT - 2, 2);
                }
            }
        };
        emissionChart.setBorder(BorderFactory.createDashedBorder(Color.DARK_GRAY, 1, 5));
        emissionChart.setPreferredSize(CHART_SIZE);

        add(emissionChart, constraints);

//        ------   spacers   ------
        ++constraints.gridy;
        constraints.gridx   = 3;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx   = 15;
        constraints.gridy   = 15;
        constraints.insets  = new Insets(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.BOTH;
        add(Box.createVerticalGlue(), constraints);
    }

    private void initArea(JLabel area) {
        area.setPreferredSize(AREA_SIZE);
        area.setBorder(BorderFactory.createDashedBorder(Color.DARK_GRAY, 10, 5));
    }

//    ------   states   ------

    State getSelectEnable() {
        return selectEnable;
    }

    State getAreaAFilled() {
        return areaAFilled;
    }

    State getAreaBFilled() {
        return areaBFilled;
    }

    State getAreaCFilled() {
        return areaCFilled;
    }

//    ------   actions   ------

    void clear() {
        areaA.setImage(null);
        areaB.setImage(null);
        areaC.setImage(null);
    }

    void openImage(InputStream stream) throws IOException {
        areaA.setImage(ImageIO.read(stream));
    }

    void saveImage(OutputStream stream) throws IOException {
        ImageIO.write(areaC.getImage(), "BMP", stream);
    }

    void copyBC() {
        areaC.setImage(areaB.getImage());
    }

    void copyCB() {
        areaB.setImage(areaC.getImage());
    }

    void useFilters(State ready, Filter...filters) {    //  TODO: replace with 1 thread pull
        ready.setState(false);

        new Thread(() -> {
            BufferedImage image = areaB.getImage();
            for (Filter filter : filters)
                image = filter.apply(image);

            areaC.setImage(image);
            ready.setState(true);
        }).start();
    }

//    ------   charts   ------

    void setAbsorptionPoints(Point[] absorptionPoints) {
        this.absorptionPoints = absorptionPoints;
    }

    void setEmissionPoints(Point[][] emissionPoints) {
        this.emissionPoints = emissionPoints;
    }
}
