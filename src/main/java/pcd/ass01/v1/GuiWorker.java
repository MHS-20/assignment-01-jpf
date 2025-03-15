package pcd.ass01.v1;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class GuiWorker extends Thread {

    private List<Boid> partitionBoids;
    private Graphics g;

    public GuiWorker(List<Boid> partitionBoids, Graphics g) {
        this.partitionBoids = partitionBoids;
        this.g = g;
    }

    @Override
    public void run() {
        for (Boid b : partitionBoids) {
            b.computeUpdate(model);
            b.update(model);
        }
    }





}
