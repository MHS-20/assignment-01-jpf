package pcd.ass01.v1fix;

import java.util.List;

public class BoidWorker extends Thread {

    private final List<Boid> boidsPartition;
    private final BoidsModel model;
    private final Monitor monitor;
    private Barrier computeVelocityBarrier;
    private final Barrier updateVelocityBarrier;
    private final Barrier upddatePositionBarrier;

    public BoidWorker(String name,
                      List<Boid> boidsPartition,
                      BoidsModel model,
                      Monitor monitor,
                      Barrier computeVelocityBarrier,
                      Barrier updateVelocityBarrier,
                      Barrier upddatePositionBarrier) {
        super(name);
        this.boidsPartition = boidsPartition;
        this.model = model;
        this.monitor = monitor;
        this.computeVelocityBarrier = computeVelocityBarrier;
        this.updateVelocityBarrier = updateVelocityBarrier;
        this.upddatePositionBarrier = upddatePositionBarrier;
    }

    public void run() {
        while (true) {
            monitor.waitUntilWorkStart();
            calculateVelocityAndWaitBarrier();
            updateVelocityAndWaitBarrier();
            updatePositionAndWaitBarrier();
        }
    }

    private void calculateVelocityAndWaitBarrier() {
        boidsPartition.forEach(boid -> boid.calculateVelocity(model));
        computeVelocityBarrier.await();
    }

    private void updateVelocityAndWaitBarrier() {
        boidsPartition.forEach(boid -> boid.updateVelocity(model));
        updateVelocityBarrier.await();
    }

    private void updatePositionAndWaitBarrier() {
        boidsPartition.forEach(boid -> boid.updatePosition(model));
        upddatePositionBarrier.await();
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + this + "] " + getName() + " -> " + msg);
        }
    }

}
