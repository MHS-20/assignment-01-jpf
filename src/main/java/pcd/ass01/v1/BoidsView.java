package pcd.ass01.v1;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {
    private JFrame frame;
    private BoidsPanel boidsPanel;
    private JSlider cohesionSlider, separationSlider, alignmentSlider;
    private JButton startStopButton, suspendResumeButton;
    private JTextField boidsNumberInput;
    private BoidsModel model;
    private int width, height;
    private boolean initializeWorkers;

    public BoidsView(BoidsModel model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;

        frame = new JFrame("Boids Simulation");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel cp = new JPanel();
        LayoutManager layout = new BorderLayout();
        cp.setLayout(layout);

        boidsPanel = new BoidsPanel(this, model);
        cp.add(BorderLayout.CENTER, boidsPanel);

        JPanel controlPanel = new JPanel();
        JPanel slidersPanel = new JPanel();

        cohesionSlider = makeSlider();
        separationSlider = makeSlider();
        alignmentSlider = makeSlider();

        startStopButton = new JButton();
        startStopButton.setText("Stop");

        boidsNumberInput = new JTextField(10);
        boidsNumberInput.setEnabled(true);
        boidsNumberInput.addActionListener(e -> {
            String input = boidsNumberInput.getText();
            this.initializeWorkers = true;
            model.setNboids(Integer.valueOf(input));
        });

        startStopButton.addActionListener(e -> {
            startStopButton.setText(startStopButton.getText() == "Start" ? "Stop" : "Start");
            model.setIsRunning(!model.getIsRunning());
            boidsNumberInput.setEnabled(!model.getIsRunning());
            model.resetBoids();
        });

        suspendResumeButton = new JButton();
        suspendResumeButton.setText("Suspend");

        suspendResumeButton.addActionListener(e -> {
            suspendResumeButton.setText(suspendResumeButton.getText() == "Suspend" ? "Resume" : "Suspend");
            model.setIsRunning(!model.getIsRunning());
            boidsNumberInput.setEnabled(!model.getIsRunning());
        });

        controlPanel.add(startStopButton);
        controlPanel.add(suspendResumeButton);

        controlPanel.add(new JLabel("Boids number"));
        controlPanel.add(boidsNumberInput);

        slidersPanel.add(new JLabel("Separation"));
        slidersPanel.add(separationSlider);
        slidersPanel.add(new JLabel("Alignment"));
        slidersPanel.add(alignmentSlider);
        slidersPanel.add(new JLabel("Cohesion"));
        slidersPanel.add(cohesionSlider);

        cp.add(BorderLayout.SOUTH, slidersPanel);
        cp.add(BorderLayout.NORTH, controlPanel);

		frame.setContentPane(cp);
        frame.setVisible(true);
    }

    private JSlider makeSlider() {
        var slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        Hashtable labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("0"));
        labelTable.put(10, new JLabel("1"));
        labelTable.put(20, new JLabel("2"));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        return slider;
    }

    public void update(int frameRate) {
        boidsPanel.setFrameRate(frameRate);
        boidsPanel.repaint();
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == separationSlider) {
            var val = separationSlider.getValue();
            model.setSeparationWeight(0.1 * val);
        } else if (e.getSource() == cohesionSlider) {
            var val = cohesionSlider.getValue();
            model.setCohesionWeight(0.1 * val);
        } else if (e.getSource() == alignmentSlider) {
            var val = alignmentSlider.getValue();
            model.setAlignmentWeight(0.1 * val);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInitializeWorkers() {
        return initializeWorkers;
    }

    public void setInitializeWorkers(boolean initializeWorkers) {
        this.initializeWorkers = initializeWorkers;
    }
}