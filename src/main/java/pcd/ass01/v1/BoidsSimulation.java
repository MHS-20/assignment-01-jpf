package pcd.ass01.v1;

import javax.swing.*;

public class BoidsSimulation {

	final static int N_BOIDS = 1500;
	static int n_boids = N_BOIDS;

	final static double SEPARATION_WEIGHT = 1.0;
    final static double ALIGNMENT_WEIGHT = 1.0;
    final static double COHESION_WEIGHT = 1.0;

    final static int ENVIRONMENT_WIDTH = 1000; 
	final static int ENVIRONMENT_HEIGHT = 1000;
    static final double MAX_SPEED = 4.0;
    static final double PERCEPTION_RADIUS = 50.0;
    static final double AVOID_RADIUS = 20.0;

	final static int SCREEN_WIDTH = 800;
	final static int SCREEN_HEIGHT = 800;

    public static void main(String[] args) {
		// arg set by jpf
		boolean headless = args.length > 0 && args[0].equals("headless");
		if(!headless)
			promptForBoidCount();
		else
			n_boids = 10;

		var model = new BoidsModel(
						n_boids, headless,
    					SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT, 
    					ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
    					MAX_SPEED,
    					PERCEPTION_RADIUS,
    					AVOID_RADIUS); 
    	var sim = new BoidsSimulator(model);

		if(!headless){
			var view = new BoidsView(model, SCREEN_WIDTH, SCREEN_HEIGHT);
			sim.attachView(view);
		}
    	sim.runSimulation();
    }

	public static void promptForBoidCount() {
		String input = JOptionPane.showInputDialog(null, "Enter number of boids:", "Boid Count", JOptionPane.QUESTION_MESSAGE);
		if (input != null && !input.isEmpty()) {
			try {
				int numBoids = Integer.parseInt(input);

				if (numBoids > 0) {
					n_boids = numBoids;
				} else {
					n_boids = N_BOIDS;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}