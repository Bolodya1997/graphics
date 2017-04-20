package ru.nsu.fit.g14203.popov.wireframe.spline;

import javax.swing.*;
import java.util.List;

public class SplineDialog extends JDialog {

    public SplineDialog(JFrame owner, List<Spline> splines) {
        super(owner, "Spline");

        setSize(600, 400);
        setResizable(false);

        setLocationRelativeTo(owner);

//        ------   splinePanel   ------
        SplinePanel splinePanel = new SplinePanel();
        add(splinePanel);

        SwingUtilities.invokeLater(() -> splinePanel.setSpline(new Spline()));

//        ------   end of init   ------
        setVisible(true);
    }
}
