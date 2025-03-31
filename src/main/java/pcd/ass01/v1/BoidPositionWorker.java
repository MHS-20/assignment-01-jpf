package pcd.ass01.v1;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BoidPositionWorker extends Thread{
    private List<Boid> boids;
    private BoidsModel model;
    private CyclicBarrier barrier;

    public BoidPositionWorker(List<Boid> boids, BoidsModel model) {
        super();
        this.boids = boids;
        this.model = model;
    }

    public BoidPositionWorker(List<Boid> boids, BoidsModel model, CyclicBarrier barrier) {
        super();
        this.boids = boids;
        this.model = model;
        this.barrier = barrier;
    }

    public void run() {
        try {
            while (true) {
                for (Boid boid : boids) {
                    boid.updatePos(model);
                }
                barrier.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }


}
