package pcd.ass01.v1;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BoidWorker extends Thread {
    private List<Boid> boids;
    private BoidsModel model;
    private CyclicBarrier velocityBarrier, positionBarrier, collectDataBarrier, writeDataBarrier;

    public BoidWorker(List<Boid> boids, BoidsModel model) {
        super();
        this.boids = boids;
        this.model = model;
    }

    public BoidWorker(List<Boid> boids, BoidsModel model, CyclicBarrier velocityBarrier, CyclicBarrier positionBarrier) {
        super();
        this.boids = boids;
        this.model = model;
        this.velocityBarrier = velocityBarrier;
        this.positionBarrier = positionBarrier;
    }

    public void setModel(BoidsModel model){
        this.model = model;
    }

    public void run() {
        try {
            while (true) {
                for (Boid boid : boids) {
                    boid.getNearbyData(model);
                    //collectDataBarrier.await();

                    boid.updateVelocity(model);
                    //writeDataBarrier.await();
                }

                //System.out.println(Thread.currentThread().getName() + ": waiting on first barrier");
                velocityBarrier.await();

                for (Boid boid : boids) {
                    boid.updatePos(model);
                }

                //System.out.println(Thread.currentThread().getName() + ": waiting on second barrier");
                positionBarrier.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}