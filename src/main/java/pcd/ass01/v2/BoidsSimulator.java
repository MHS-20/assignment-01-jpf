package pcd.ass01.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class BoidsSimulator {

    private final BoidsModel model;
    private Optional<BoidsView> view;

    private static final int FRAMERATE = 50;
    private int framerate;
    private final int CORES = Runtime.getRuntime().availableProcessors();
    private final int N_WORKERS = CORES + 1;
    private long t0;
    private boolean isTime0Updated = false;

    private ForkJoinPool pool;
    List<Callable<Void>> calculateVelocityTaskList = new ArrayList<>();
    List<Callable<Void>> updateVelocityTaskList = new ArrayList<>();
    List<Callable<Void>> updatePositionTaskList = new ArrayList<>();
    private volatile boolean loop = true ;
    private ExecutorService exc;

    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
        initTasks();
    }

    private void initTasks() {
        var boids = model.getBoids();
        //pool = new ForkJoinPool(N_WORKERS);
        exc = Executors.newFixedThreadPool(N_WORKERS);

        boids.forEach(boid -> {
            calculateVelocityTaskList.add(new Task(boid, model, Boid::calculateVelocity));
            updateVelocityTaskList.add(new Task(boid, model, Boid::updateVelocity));
            updatePositionTaskList.add(new Task(boid, model, Boid::updatePosition));
        });
    }

    private void waitForCompletion(List<Future<Void>> futures) throws ExecutionException, InterruptedException {
        for (Future<Void> future : futures) {
            future.get();
        }
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        while (loop) {
            if (view.isPresent()) {
                if (view.get().isRunning()) {
                    t0 = System.currentTimeMillis();

                    try {
                        waitForCompletion(exc.invokeAll(calculateVelocityTaskList));
                        waitForCompletion(exc.invokeAll(updateVelocityTaskList));
                        waitForCompletion(exc.invokeAll(updatePositionTaskList));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    view.get().update(framerate);
                    updateFrameRate(t0);
                }
                if (view.get().isResetButtonPressed()) {
                    model.resetBoids(view.get().getNumberOfBoids());
                    view.get().update(framerate);
                    initTasks();
                    view.get().setResetButtonUnpressed();
                }
            }
        }
    }

    private void updateTime0() {
        if (!isTime0Updated) {
            t0 = System.currentTimeMillis();
            isTime0Updated = true;
        }
    }

    private void updateFrameRate(long t0) {
        isTime0Updated = false;
        var t1 = System.currentTimeMillis();
        var dtElapsed = t1 - t0;
        var frameratePeriod = 1000 / FRAMERATE;
        if (dtElapsed < frameratePeriod) {
            try {
                Thread.sleep(frameratePeriod - dtElapsed);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            framerate = FRAMERATE;
        } else {
            framerate = (int) (1000 / dtElapsed);
        }
    }
}
