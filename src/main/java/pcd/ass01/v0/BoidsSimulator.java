package pcd.ass01.v0;

import java.util.Optional;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;

    private static final int FRAMERATE = 50;
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
        while (running) {
            var t0 = System.currentTimeMillis();
            var boids = model.getBoids();
            for (Boid boid : boids) {
                boid.update(model);
            }

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
