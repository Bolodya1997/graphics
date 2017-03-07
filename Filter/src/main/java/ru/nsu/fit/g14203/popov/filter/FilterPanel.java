package ru.nsu.fit.g14203.popov.filter;

import ru.nsu.fit.g14203.popov.filter.graphics.SimpleFilter;
import ru.nsu.fit.g14203.popov.util.State;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

class FilterPanel extends JPanel {

    private final static int AREA_WIDTH      = 350;
    private final static int AREA_HEIGHT     = 350;
    private final static Dimension AREA_SIZE = new Dimension(AREA_WIDTH, AREA_HEIGHT);

    private State selectEnable = new State(false);
    private State areaBFilled = new State(false);
    private State areaCFilled = new State(false);

    private AreaA areaA;
    private Area areaB;
    private Area areaC;

    FilterPanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0,
                1, 1, 0, 0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0);

//        ------   areaA   ------
        areaA = new AreaA(AREA_WIDTH, selectEnable);
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

        constraints.insets.right = 10;
        add(areaC, constraints);

//        ------   spacers   ------
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        add(Box.createHorizontalGlue(), constraints);

        constraints.gridx = 3;
        constraints.gridy = 1;
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

    State getAreaBFilled() {
        return areaBFilled;
    }

    State getAreaCFilled() {
        return areaCFilled;
    }

//    ------   actions   ------

    void openImage(InputStream stream) throws IOException {
        areaA.setImage(ImageIO.read(stream));
    }

    void copyBC() {
        areaC.setImage(areaB.getImage());
    }

    void copyCB() {
        areaB.setImage(areaC.getImage());
    }

    void useFilter(SimpleFilter filter) {
        BufferedImage image = areaB.getImage();
        areaC.setImage(filter.apply(image));
    }
}
