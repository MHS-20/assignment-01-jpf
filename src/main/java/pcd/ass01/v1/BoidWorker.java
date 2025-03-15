package pcd.ass01.v1;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidWorker extends Thread {

    private int start, finish;
    private List<Boid> allBoids;
    private List<Boid> partitionBoids;
    private CyclicBarrier barrier;
    private BoidsModel model;

    public BoidWorker() {
    }

    public BoidWorker(CyclicBarrier barrier, List<Boid> allBoids, List<Boid> partitionBoids, BoidsModel model) {
        this.barrier = barrier;
        this.allBoids = allBoids;
        this.partitionBoids = partitionBoids;
        this.model = model;
    }

    public void setAllBoids(List<Boid> boids) {
        this.allBoids = boids;
    }

    public void setPartitionBoids(List<Boid> boids) {
        this.partitionBoids = boids;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        for (Boid b : partitionBoids) {
            b.computeUpdate(model);
            b.update(model);
        }
    }
}