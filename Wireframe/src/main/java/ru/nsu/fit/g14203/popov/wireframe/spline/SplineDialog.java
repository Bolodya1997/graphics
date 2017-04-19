package ru.nsu.fit.g14203.popov.wireframe.spline;

import javax.swing.*;
import java.util.List;

public class SplineDialog extends JDialog {

    SplineDialog(JFrame owner, List<Spline> splines) {
        super(owner, "Spline", true);

        setLocationRelativeTo(owner);

//        ------   end of init   ------
        setVisible(true);
    }
}
