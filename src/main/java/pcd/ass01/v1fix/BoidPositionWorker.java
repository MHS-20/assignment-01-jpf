package pcd.ass01.v1fix;

import pcd.ass01.v1fix.Boid;
import pcd.ass01.v1fix.BoidsModel;

import java.util.List;

public class BoidPositionWorker extends Thread{
    private List<Boid> boids;
    private BoidsModel model;



    public BoidPositionWorker(List<Boid> boids, BoidsModel model) {
        super();
        this.boids = boids;
        this.model = model;
    }


    public void run() {
        for (Boid boid : boids) {
            boid.updatePos(model);
        }
    }


}
