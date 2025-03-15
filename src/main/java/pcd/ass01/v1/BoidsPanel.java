package pcd.ass01.v1;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoidsPanel extends JPanel {

    private BoidsView view;
    private BoidsModel model;
    private int framerate;

    public BoidsPanel(BoidsView view, BoidsModel model) {
        this.model = model;
        this.view = view;
    }

    public void setFrameRate(int framerate) {
        this.framerate = framerate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        var w = view.getWidth();
        var h = view.getHeight();
        var envWidth = model.getWidth();
        var xScale = w / envWidth;
        // var envHeight = model.getHeight();
        // var yScale = h/envHeight;

        var boids = model.getBoids();
        g.setColor(Color.BLUE);

//        int numCores = Runtime.getRuntime().availableProcessors();
//        GuiWorker[] workers = new GuiWorker[numCores];
//        int step = boids.size() / numCores;
//        List<Boid> partition;
//
//        for (int i = 0; i < numCores; i++) {
//            partition = boids.subList(i * step, (i + 1) * step);
//            workers[i] = new GuiWorker(partition, g, xScale, w, h);
//            workers[i].start();
//        }
//
//        for (GuiWorker wrk : workers) {
//            try {
//                wrk.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }

        for (Boid boid : boids) {
            var x = boid.getPos().x();
            var y = boid.getPos().y();
            int px = (int) (w / 2 + x * xScale);
            int py = (int) (h / 2 - y * xScale);
            g.fillOval(px, py, 5, 5);
        }

        g.setColor(Color.BLACK);
        g.drawString("Num. Boids: " + boids.size(), 10, 25);
        g.drawString("Framerate: " + framerate, 10, 40);
    }
}
