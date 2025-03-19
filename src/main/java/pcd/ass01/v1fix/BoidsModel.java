package pcd.ass01.v1fix;

import pcd.ass01.v1fix.Boid;

import java.util.ArrayList;
import java.util.List;

public class BoidsModel {

    private final List<Boid> boids;
    private boolean isRunning;
    private double separationWeight;

    private double alignmentWeight;
    private double cohesionWeight;
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;

    public BoidsModel(int nboids,
                      boolean headless,
                      double initialSeparationWeight,
                      double initialAlignmentWeight,
                      double initialCohesionWeight,
                      double width,
                      double height,
                      double maxSpeed,
                      double perceptionRadius,
                      double avoidRadius) {
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;

        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;

        this.isRunning = headless;
        boids = new ArrayList<>();

        if(headless)
            generateFixedBoids(nboids); // avoid random for jpf
        else
            generateBoids(nboids);
    }

    private void generateBoids(int nboids) {
        for (int i = 0; i < nboids; i++) {
            P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
            V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
            boids.add(new Boid(pos, vel));
        }
    }

    // jpf
    private void generateFixedBoids(int nboids) {
        for (int i = 0; i < nboids; i++) {
            P2d pos = new P2d(-width / 2 + 4000 * width, -height / 2 + 6 * height);
            V2d vel = new V2d(2 * maxSpeed / 2 - maxSpeed / 4, 9000 * maxSpeed / 2 - maxSpeed / 4);
            boids.add(new Boid(pos, vel));
        }
    }

    public synchronized void setNboids(int nboids) {
        int currentNboids = this.boids.size();
        System.out.println("Setting boids: " + currentNboids);

        if (nboids > currentNboids)
            generateFixedBoids(nboids - currentNboids);
        else {
            System.out.println("removing " + (currentNboids - nboids) + " items");
            if ((currentNboids - nboids) > 0) {
                boids.subList(0, (currentNboids - nboids)).clear();
            }
        }
    }

    public int getNboids() {
        return boids.size();
    }

    public List<Boid> getBoids() {
        return boids;
    }

    public double getMinX() {
        return -width / 2;
    }

    public double getMaxX() {
        return width / 2;
    }

    public double getMinY() {
        return -height / 2;
    }

    public double getMaxY() {
        return height / 2;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public synchronized void setSeparationWeight(double value) {
        this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(double value) {
        this.alignmentWeight = value;
    }

    public synchronized void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public synchronized boolean getIsRunning() {
        return this.isRunning;
    }

    public synchronized void setCohesionWeight(double value) {
        this.cohesionWeight = value;
    }

    public synchronized double getSeparationWeight() {
        return separationWeight;
    }

    public synchronized double getCohesionWeight() {
        return cohesionWeight;
    }

    public synchronized double getAlignmentWeight() {
        return alignmentWeight;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAvoidRadius() {
        return avoidRadius;
    }

    public double getPerceptionRadius() {
        return perceptionRadius;
    }
}
