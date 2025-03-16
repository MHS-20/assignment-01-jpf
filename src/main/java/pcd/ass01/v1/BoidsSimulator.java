package pcd.ass01.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;

    private static final int FRAMERATE = 100;
    private int framerate;

    private volatile boolean running;
    private Thread simThread;

    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);

        view.setToggleSimulation(() -> {
            if (!running) {
                start();
            } else {
                stop();
            }
        });
    }

    public void start() {
        if (simThread == null || !simThread.isAlive()) {
            running = true;
            simThread = new Thread(this::runSimulation);
            simThread.start();
        }
    }

    public void stop() {
        running = false;
    }

    public void runSimulation() {
        var boids = model.getBoids();
        int numCores = Runtime.getRuntime().availableProcessors();

        List<Boid>[] partitions = new List[numCores];
        int step = boids.size() / numCores;

        BoidWorker[] workers = new BoidWorker[numCores];
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

        for (int i = 0; i < numCores; i++) {
            partitions[i] = boids.subList(i * step, (i + 1) * step);
        }

        while (running) {
            var t0 = System.currentTimeMillis();
            boids = model.getBoids();

            for (int i = 0; i < numCores; i++) {
                //partition = boids.subList(i * step, (i + 1) * step);
                workers[i] = new BoidWorker(rwLock, boids, partitions[i], model);
                workers[i].start();
            }

            for (BoidWorker w : workers) {
                try {
                    w.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

//            for (Boid boid : boids) {
//                    boid.computeUpdate(model);
//                    boid.update(model);
//            }

            if (view.isPresent()) {
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var framratePeriod = 1000 / FRAMERATE;

                if (dtElapsed < framratePeriod) {
                    try {
                        Thread.sleep(framratePeriod - dtElapsed);
                    } catch (Exception ex) {
                    }
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000 / dtElapsed);
                }
            }
        }
    }
}
