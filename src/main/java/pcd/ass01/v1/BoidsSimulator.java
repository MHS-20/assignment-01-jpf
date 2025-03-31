package pcd.ass01.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;

    private static final int FRAMERATE = 50; //25
    private int framerate;

    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        List<List<Boid>> partitions = new ArrayList<>();
        ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock(true);

        var boids = model.getBoids();
        int cores = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < cores; i++) {
            partitions.add(boids.subList(i * (boids.size() / cores), (boids.size() / cores) * (i + 1)));
        }

        var velocityBarrier = new CyclicBarrier(cores + 1);
        var positionBarrier = new CyclicBarrier(cores + 1);

//        var collectDataBarrier = new CyclicBarrier(cores + 1);
//        var writeDataBarrier = new CyclicBarrier(cores + 1);

        var workers = new ArrayList<BoidWorker>();

        for (List<Boid> partition : partitions) {
            var worker = new BoidWorker(partition, model, velocityBarrier, positionBarrier);
            worker.start();
            workers.add(worker);
        }

        while (true) {
            if (model.getIsRunning()) {
                var t0 = System.currentTimeMillis();

                /*
                for (BoidWorker w : workers){
                    if(!w.isAlive()){
                        w.start();
                    }
                }*/

                try {
//                    for (Boid boid : partitions.get(0)) {
//                        collectDataBarrier.await();
//                        collectDataBarrier.reset();
//                        writeDataBarrier.await();
//                        writeDataBarrier.reset();
//                    }

                    //System.out.println(Thread.currentThread().getName() + ": waiting on first barrier");
                    velocityBarrier.await();
                    //velocityBarrier.reset();

                    positionBarrier.await();
                    //positionBarrier.reset();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

                if (view.isPresent()) {
                    view.get().update(framerate);
                    var t1 = System.currentTimeMillis();
                    var dtElapsed = t1 - t0;
                    var frameratePeriod = 1000 / FRAMERATE;

                    if (dtElapsed < frameratePeriod) {
                        try {
                            Thread.sleep(frameratePeriod - dtElapsed);
                        } catch (Exception ex) {
                        }
                        framerate = FRAMERATE;
                    } else {
                        framerate = (int) (1000 / dtElapsed);
                    }

//                    try {
//                        System.out.println(Thread.currentThread().getName() + ": waiting on second barrier");
//                        positionBarrier.await();
//                        positionBarrier.reset();
//                    } catch (InterruptedException | BrokenBarrierException e) {
//                        throw new RuntimeException(e);
//                    }

                }
            }
        }
    }
}
