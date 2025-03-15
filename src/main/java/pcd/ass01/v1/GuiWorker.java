package pcd.ass01.v1;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class GuiWorker extends Thread {

    private List<Boid> partitionBoids;
    private Graphics g;
    private double xScale;
    private int h, w;

    public GuiWorker(List<Boid> partitionBoids, Graphics g ,double xscale, int w, int h) {
        this.partitionBoids = partitionBoids;
        this.g = g;
        this.xScale = xscale;
        this.h = h;
        this.w = w;
    }

    @Override
    public void run() {
        for (Boid b : partitionBoids) {
                var x = b.getPos().x();
                var y = b.getPos().y();
                int px = (int)(w/2 + x*xScale);
                int py = (int)(h/2 - y*xScale);
                g.fillOval(px,py, 5, 5);
        }
    }





}
