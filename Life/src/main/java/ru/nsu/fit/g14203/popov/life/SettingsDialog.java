package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MutableBoolean;
import ru.nsu.fit.g14203.popov.life.util.MutableDouble;
import ru.nsu.fit.g14203.popov.life.util.MutableInteger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

class SettingsDialog extends JDialog {

    JPanel fieldPropertiesPanel = titledBorderPanel("Field properties");
    JPanel modePanel = titledBorderPanel("Mode");
    JPanel impactPanel = titledBorderPanel("Impact");
    JPanel environmentPanel = titledBorderPanel("Environment");

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
        addFieldPropertiesBlock("Cell size", 1, 100, settings.size);
//              ------   Grid width   ------
        addFieldPropertiesBlock("Grid width", 1, 100, settings.width);
//        ------   Mode   ------
        modePanel = titledBorderPanel("Mode");
        add(modePanel);
        gridBagLayout.addLayoutComponent(modePanel, new GridBagConstraints(1, 0, 1,
                1, 0.5, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 0));
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
        JTextField valueTextField = new JTextField(Integer.toString(value.getValue()));
        valueTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String next = valueTextField.getText() + e.getKeyChar();

                try {
                    Integer.decode(next);
                } catch (NumberFormatException exc) {
                    e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
                }

                if (next.length() > 3)
                    e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
            }
        });

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
            int v = Integer.decode(valueTextField.getText());
            v = (v < min) ? min :
                    (max < v) ? max : v;

            valueTextField.setText(Integer.toString(v));
            slider.setValue(v);

            value.setValue(v);
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
        JTextField valueTextField = new JTextField(Double.toString(value.getValue()));
        valueTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String next = valueTextField.getText() + e.getKeyChar();

                try {
                    Double.parseDouble(next);
                } catch (NumberFormatException exc) {
                    e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
                }

                if (next.length() > 4)
                    e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
            }
        });

        Runnable valueChangeAction = () -> {
            double old = value.getValue();
            value.setValue(Double.parseDouble(valueTextField.getText()));

            if (Double.isNaN(value.getValue()) || Double.isInfinite(value.getValue())) {
                valueTextField.setText(Double.toString(old));
                value.setValue(old);
                return;
            }

            if (value.getValue() >= 10) {
                valueTextField.setText(Double.toString(old));
                value.setValue(old);
                return;
            }

            valueTextField.setText(Double.toString(value.getValue()));
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
        valueTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String next = valueTextField.getText() + e.getKeyChar();

                try {
                    Double.parseDouble(next);
                } catch (NumberFormatException exc) {
                    e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
                }

                if (next.length() > 4)
                    e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
            }
        });

        Runnable valueChangeAction = () -> {
            double old = value.getValue();
            value.setValue(Double.parseDouble(valueTextField.getText()));

            if (Double.isNaN(value.getValue()) || Double.isInfinite(value.getValue())) {
                valueTextField.setText(Double.toString(old));
                value.setValue(old);
                return;
            }

            if (value.getValue() >= 10) {
                valueTextField.setText(Double.toString(old));
                value.setValue(old);
                return;
            }

            if (settings.lifeBegin.getValue() < 0
                    || settings.birthBegin.getValue() < settings.lifeBegin.getValue()
                    || settings.birthEnd.getValue() < settings.birthBegin.getValue()
                    || settings.lifeEnd.getValue() < settings.birthEnd.getValue()) {
                valueTextField.setText(Double.toString(old));
                value.setValue(old);
            }

            valueTextField.setText(Double.toString(value.getValue()));
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

    private void addModeBlock(String name, boolean template, MutableBoolean value) {
        GridBagLayout gridBagLayout = (GridBagLayout) modePanel.getLayout();
        GridBagConstraints constraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1,
                1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0);

//        ------   Name   ------
        JLabel nameLabel = new JLabel(name);

        environmentPanel.add(nameLabel);
        gridBagLayout.addLayoutComponent(nameLabel, constraints);

//        ------   radio button   ------
        JRadioButton radioButton = new JRadioButton();
        radioButton.setSelected(template == value.isTrue());

//        TODO: create link between buttons

        environmentPanel.add(radioButton);
        ++constraints.gridx;
        gridBagLayout.addLayoutComponent(radioButton, constraints);
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
