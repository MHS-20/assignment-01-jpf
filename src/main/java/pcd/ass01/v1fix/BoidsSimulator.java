package pcd.ass01.v1fix;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoidsSimulator {

    private final BoidsModel model;
    private Optional<BoidsView> view;
    private final List<BoidWorker> boidWorkers = new ArrayList<>();

    private static final int FRAMERATE = 50;
    private int framerate;
    private final int CORES = Runtime.getRuntime().availableProcessors();
    private final int N_WORKERS = CORES + 1;
    private long t0;

    private Monitor managerMonitor = new Monitor();
    private Barrier computeVelocityBarrier;
    private Barrier updateVelocityBarrier;
    private Barrier upddatePositionBarrier;
    private boolean isTime0Updated = false;

    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
        initWorkers();
    }

    private void initWorkers() {
        boidWorkers.clear();

        List<List<Boid>> partitions = new ArrayList<>();
        for (int i = 0; i < N_WORKERS; i++) {
            partitions.add(new ArrayList<>());
        }

        int i = 0;
        for (Boid boid : model.getBoids()) {
            i = (i == partitions.size() ? 0 : i);
            partitions.get(i).add(boid);
            i++;
        }

        managerMonitor = new Monitor();
        computeVelocityBarrier = new MyCyclicBarrier(N_WORKERS);
        updateVelocityBarrier = new MyCyclicBarrier(N_WORKERS);
        upddatePositionBarrier = new MyBarrier(N_WORKERS);

        i = 0;
        for (List<Boid> partition : partitions) {
            boidWorkers.add(new BoidWorker("W" + i,
                    partition,
                    model,
                    managerMonitor,
                    computeVelocityBarrier,
                    updateVelocityBarrier,
                    upddatePositionBarrier
            ));
            i++;
        }

        startWorkers();
    }

    private void startWorkers() {
        boidWorkers.forEach(BoidWorker::start);
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        while (true) {
            if (view.isPresent()) {
                if (view.get().isRunning()) {
                    managerMonitor.startWork();
                    updateTime0();
                    if (upddatePositionBarrier.isBroken()) {
                        view.get().update(framerate);
                        updateFrameRate(t0);
                        upddatePositionBarrier.reset();
                    }

                } else {
                    managerMonitor.stopWork();
                }
                if (view.get().isResetButtonPressed()) {
                    managerMonitor.stopWork();
                    model.resetBoids(view.get().getNumberOfBoids());
                    view.get().update(framerate);
                    initWorkers();
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
