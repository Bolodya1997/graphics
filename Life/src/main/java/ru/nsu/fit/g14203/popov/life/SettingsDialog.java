package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MutableBoolean;
import ru.nsu.fit.g14203.popov.life.util.MutableDouble;
import ru.nsu.fit.g14203.popov.life.util.MutableInteger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Stream;

class SettingsDialog extends JDialog {

    private JPanel fieldPropertiesPanel = titledBorderPanel("Field properties");
    private JPanel modePanel = titledBorderPanel("Mode");
    private JPanel impactPanel = titledBorderPanel("Impact");
    private JPanel environmentPanel = titledBorderPanel("Environment");

    private Settings settings;
    private MutableBoolean changed;

    private MutableBoolean replace;

    SettingsDialog(JFrame owner, Settings settings, MutableBoolean changed, MutableBoolean replace) {
        super(owner, "Settings", true);
        setVisible(false);

        this.settings = settings;
        this.changed = changed;
        this.replace = replace;

//        ------   size   ------
        setSize(new Dimension(505, 300));
        setResizable(false);

//        ------   location   ------
        Point ownerLocation = owner.getLocation();
        Dimension ownerSize = owner.getSize();
        setLocation(ownerLocation.x + (ownerSize.width - getWidth()) / 2,
                ownerLocation.y + (ownerSize.height - getHeight()) / 2);

//        ------   layout   ------
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

//        ------   Field properties   ------
        fieldPropertiesPanel = titledBorderPanel("Field properties");
        add(fieldPropertiesPanel);
        gridBagLayout.addLayoutComponent(fieldPropertiesPanel, new GridBagConstraints(0, 0, 1,
                2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 0), 0, 0));
//              ------   Rows   ------
        addFieldPropertiesBlock("Rows", 1, 100, settings.gridHeight);
//              ------   Columns   ------
        addFieldPropertiesBlock("Columns", 1, 100, settings.gridWidth);
//              ------   Cell size   ------
        addFieldPropertiesBlock("Cell size", 2, 50, settings.size);
//              ------   Grid width   ------
        addFieldPropertiesBlock("Grid width", 1, 50, settings.width);
//        ------   Mode   ------
        modePanel = titledBorderPanel("Mode");
        add(modePanel);
        gridBagLayout.addLayoutComponent(modePanel, new GridBagConstraints(1, 0, 1,
                1, 0.5, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 0));
//              ------   Mode buttons   ------
        addModeButtons("Replace", "Xor", replace);
//        ------   Impact   ------
        impactPanel = titledBorderPanel("Impact");
        add(impactPanel);
        gridBagLayout.addLayoutComponent(impactPanel, new GridBagConstraints(1, 1, 1,
                1, 0.5, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 0));
//              ------   FST_IMPACT   ------
        addImpactBlock("FST_IMPACT", settings.firstImpact);
//              ------   SND_IMPACT   ------
        addImpactBlock("SND_IMPACT", settings.secondImpact);
//        ------   Environment   ------
        environmentPanel = titledBorderPanel("Environment");
        add(environmentPanel);
        gridBagLayout.addLayoutComponent(environmentPanel, new GridBagConstraints(0, 2, 1,
                2, 1.0, 0.3, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 4, 0), 0, 0));
//              ------   LIVE_BEGIN   ------
        addEnvironmentBlock("LIVE_BEGIN", settings.lifeBegin, 0, 0);
//              ------   LIVE_END   ------
        addEnvironmentBlock("LIVE_END", settings.lifeEnd, 1, 0);
//              ------   BIRTH_BEGIN   ------
        addEnvironmentBlock("BIRTH_BEGIN", settings.birthBegin, 0, 1);
//              ------   BIRTH_END   ------
        addEnvironmentBlock("BIRTH_END", settings.birthEnd, 1, 1);
//        ------   OK   ------
        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(e -> OKButtonAction());
        add(OKButton);
        gridBagLayout.addLayoutComponent(OKButton, new GridBagConstraints(1, 2, 1,
                1, 1.0, 0.3, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(20, 20, 10, 20), 0, 0));
//        ------   Cancel   ------
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelButtonAction());
        add(cancelButton);
        gridBagLayout.addLayoutComponent(cancelButton, new GridBagConstraints(1, 3, 1,
                1, 1.0, 0.3, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 20, 20, 20), 0, 0));

//        ------   end of init   ------
        setVisible(true);
    }

    private JPanel titledBorderPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(title));

        panel.setLayout(new GridBagLayout());

        return panel;
    }

    private void addFieldPropertiesBlock(String name, int min, int max, MutableInteger value) {
        GridBagLayout gridBagLayout = (GridBagLayout) fieldPropertiesPanel.getLayout();
        GridBagConstraints constraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 0), 0, 0);

//        ------   Name   ------
        JLabel nameLabel = new JLabel(name);

        fieldPropertiesPanel.add(nameLabel);
        gridBagLayout.addLayoutComponent(nameLabel, constraints);

//        ------   Value   ------
        JTextField valueTextField = new JTextField();
        valueTextField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() + str.length() > 3)
                    return;
                super.insertString(offs, str, a);
            }
        });
        valueTextField.setText(Integer.toString(value.getValue()));

        fieldPropertiesPanel.add(valueTextField);
        ++constraints.gridx;
        gridBagLayout.addLayoutComponent(valueTextField, constraints);

//        ------   Slider   ------
        JSlider slider = new JSlider(min, max, value.getValue());

        fieldPropertiesPanel.add(slider);
        ++constraints.gridx;
        gridBagLayout.addLayoutComponent(slider, constraints);

//        ------   link   ------
        Runnable valueChangeAction = () -> {
            int v;
            try {
                v = Integer.decode(valueTextField.getText());
            } catch (Exception e) {
                valueTextField.setText(Integer.toString(value.getValue()));
                return;
            }

            v = (v < min) ? min :
                    (max < v) ? max : v;

            value.setValue(v);
            valueTextField.setText(Integer.toString(v));

            slider.setValue(v);
        };

        valueTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                valueChangeAction.run();
            }
        });
        valueTextField.addActionListener(e -> valueChangeAction.run());

        slider.addChangeListener(e -> {
            valueTextField.setText(Integer.toString(slider.getValue()));

            value.setValue(slider.getValue());
        });
    }

    private void addImpactBlock(String name, MutableDouble value) {
        GridBagLayout gridBagLayout = (GridBagLayout) impactPanel.getLayout();
        GridBagConstraints constraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0);

//        ------   Name   ------
        JLabel nameLabel = new JLabel(name);

        impactPanel.add(nameLabel);
        gridBagLayout.addLayoutComponent(nameLabel, constraints);

//        ------   Value   ------
        JTextField valueTextField = new JTextField();
        valueTextField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str.startsWith(".") && offs == 0)
                    return;
                if (getLength() + str.length() > 3)
                    return;
                super.insertString(offs, str, a);
            }
        });
        valueTextField.setText(Double.toString(value.getValue()));

        Runnable valueChangeAction = () -> {
            double old = value.getValue();

            double v;
            try {
                v = Double.parseDouble(valueTextField.getText());
            } catch (Exception e) {
                valueTextField.setText(Double.toString(old));
                return;
            }

            if (Double.isNaN(v) || Double.isInfinite(v)) {
                valueTextField.setText(Double.toString(old));
                return;
            }

            if (v >= 10) {
                valueTextField.setText(Double.toString(old));
                return;
            }

            value.setValue(v);
            valueTextField.setText(Double.toString(v));
        };

        valueTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                valueChangeAction.run();
            }
        });
        valueTextField.addActionListener(e -> valueChangeAction.run());

        impactPanel.add(valueTextField);
        ++constraints.gridx;
        gridBagLayout.addLayoutComponent(valueTextField, constraints);
    }

    private void addEnvironmentBlock(String name, MutableDouble value, int x, int y) {
        GridBagLayout gridBagLayout = (GridBagLayout) environmentPanel.getLayout();
        GridBagConstraints constraints = new GridBagConstraints(x * 2, y, 1,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0);

//        ------   Name   ------
        JLabel nameLabel = new JLabel(name);

        environmentPanel.add(nameLabel);
        gridBagLayout.addLayoutComponent(nameLabel, constraints);

//        ------   Value   ------
        JTextField valueTextField = new JTextField(Double.toString(value.getValue()));
        valueTextField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str.startsWith(".") && offs == 0)
                    return;
                if (getLength() + str.length() > 3)
                    return;
                super.insertString(offs, str, a);
            }
        });
        valueTextField.setText(Double.toString(value.getValue()));

        Runnable valueChangeAction = () -> {
            double old = value.getValue();

            double v;
            try {
                v = Double.parseDouble(valueTextField.getText());
            } catch (Exception e) {
                valueTextField.setText(Double.toString(old));
                return;
            }

            if (Double.isNaN(v) || Double.isInfinite(v)) {
                valueTextField.setText(Double.toString(old));
                return;
            }

            if (v >= 10) {
                valueTextField.setText(Double.toString(old));
                return;
            }

            value.setValue(v);
            if (settings.lifeBegin.getValue() < 0
                    || settings.birthBegin.getValue() < settings.lifeBegin.getValue()
                    || settings.birthEnd.getValue() < settings.birthBegin.getValue()
                    || settings.lifeEnd.getValue() < settings.birthEnd.getValue()) {
                value.setValue(old);
                valueTextField.setText(Double.toString(old));
                return;
            }

            valueTextField.setText(Double.toString(v));
        };

        valueTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                valueChangeAction.run();
            }
        });
        valueTextField.addActionListener(e -> valueChangeAction.run());

        environmentPanel.add(valueTextField);
        ++constraints.gridx;
        constraints.insets = new Insets(15, 0, 15, 25);
        gridBagLayout.addLayoutComponent(valueTextField, constraints);
    }

    private void addModeButtons(String nameTrue, String nameFalse, MutableBoolean value) {
        GridBagLayout gridBagLayout = (GridBagLayout) modePanel.getLayout();
        GridBagConstraints constraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0);

//        ------   Name true   ------
        JLabel nameLabelTrue = new JLabel(nameTrue);

        modePanel.add(nameLabelTrue);
        gridBagLayout.addLayoutComponent(nameLabelTrue, constraints);

//        ------   radio button true   ------
        JRadioButton radioButtonTrue = new JRadioButton();
        radioButtonTrue.setSelected(value.isTrue());

        modePanel.add(radioButtonTrue);
        ++constraints.gridx;
        gridBagLayout.addLayoutComponent(radioButtonTrue, constraints);

//        ------   Name false   ------
        JLabel nameLabelFalse = new JLabel(nameFalse);

        modePanel.add(nameLabelFalse);
        constraints.gridx = 0;
        constraints.gridy = 1;
        gridBagLayout.addLayoutComponent(nameLabelFalse, constraints);

//        ------   radio button false   ------
        JRadioButton radioButtonFalse = new JRadioButton();
        radioButtonFalse.setSelected(!value.isTrue());

        modePanel.add(radioButtonFalse);
        ++constraints.gridx;
        gridBagLayout.addLayoutComponent(radioButtonFalse, constraints);

//        ------   link   ------
        radioButtonTrue.addActionListener(e -> {
            if (radioButtonTrue.isSelected())
                radioButtonFalse.setSelected(false);
            else
                radioButtonFalse.setSelected(true);

            value.setState(radioButtonTrue.isSelected());
        });

        radioButtonFalse.addActionListener(e -> {
            if (radioButtonFalse.isSelected())
                radioButtonTrue.setSelected(false);
            else
                radioButtonTrue.setSelected(true);

            value.setState(radioButtonTrue.isSelected());
        });
    }

    private void OKButtonAction() {
        changed.setState(true);
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void cancelButtonAction() {
        changed.setState(false);
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
