package pcd.ass01.v1;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BoidWorker extends Thread {

    private List<Boid> allBoids;
    private List<Boid> partitionBoids;
    private BoidsModel model;
    private ReentrantReadWriteLock.ReadLock readLock;
    private ReentrantReadWriteLock.WriteLock writeLock;

    public BoidWorker() {
    }

    public BoidWorker(ReentrantReadWriteLock rwlock, List<Boid> allBoids, List<Boid> partitionBoids, BoidsModel model) {
        this.readLock =  rwlock.readLock();
        this.writeLock = rwlock.writeLock();
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

    @Override
    public void run() {
        for (Boid b : partitionBoids) {
            readLock.lock();
            b.computeUpdate(model);
            readLock.unlock();

            writeLock.lock();
            b.update(model);
            writeLock.unlock();
        }
    }
}