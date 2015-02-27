/*
 * PSO 
 * Implements PSO with Global, Ring, Von Neumann and Random particle topologies
 *
 * NIC - Professor Majercik
 * Max Bucci, Nikki Morin, Megan Maher, Kuangji Chen
 * Created: 2/26/15
 * Last Modified: 2/26/15
 *
 */


import java.util.*;

public class PSO {
	
	//PSORunner instance
	private static PSORunner runner;
			
	//PSO Constants
	private static final double CONSTRICTION_FACTOR = 0.7298;
	private static final int BOUND = 500;
	
	//Particles variable
	private static List<Particle> particles = new ArrayList<Particle>();

	//PSO Variables
	private static String topology;
	private static int swarmSize;
	private static int numIterations;
	private static String testFunction;
	private static int dimension;
	
	//PSO Bounds
	private static double minPosition;
	private static double maxPosition;
	private static double minVelocity;
	private static double maxVelocity;
	
	//Main Function
	public static void main (String args[]) {
		
		topology = args[0];
		swarmSize = Integer.parseInt(args[1]);
		numIterations = Integer.parseInt(args[2]);
		testFunction = args[3];
		dimension = Integer.parseInt(args[4]);
		
		if (testFunction.equals("rok")) {
			minPosition = 15.0;
			maxPosition = 30.0;
			minVelocity = -2.0;
			maxVelocity = 2.0;
		} else if (testFunction.equals("ack")) {
			minPosition = 16.0;
			maxPosition = 32.0;
			minVelocity = -2.0;
			maxVelocity = 4.0;
		} else if (testFunction.equals("ras")) {
			minPosition = 2.56;
			maxPosition = 5.12;
			minVelocity = -2.0;
			maxVelocity = 4.0;	
		} else {
			System.out.println("Improper input\n");	
			System.exit(0);
		}
		
		initParticles();
		
		if (topology.equals("gl")) {
			initTopologyGlobal();
		} else if (topology.equals("ri")) {
			initTopologyRing();
		} else if (topology.equals("vn")) {
			initTopologyVonNeumann();
		} else if (topology.equals("ra")) {
			initTopologyRandom();
		} else {
			System.out.println("Improper input\n");	
			System.exit(0);
		} 
		
		runner = new PSORunner(testFunction, particles);
		runner.runPSO(numIterations);
	}
	
	
	public static void initParticles() {
		
		for (int i = 0; i < swarmSize; i++) {
			Particle temp = new Particle(dimension);
			temp.initParticle(BOUND, minPosition, maxPosition, minVelocity, maxVelocity);
			particles.add(temp);
		}
	}
	
	
	public static void initTopologyGlobal() {
		
		for (int i = 0; i < swarmSize; i++) {
			
			int[] temp = new int[swarmSize-1];
			for (int j = 0; j < swarmSize-1; j++) {
				//Include all other particles but itself
				if (j < i) temp[j] = j;	 
				else if (j > i) temp[j] = j-1;		
			}
			particles.get(i).setNeighborhood(temp, swarmSize-1);
		}
	}
	
	
	public static void initTopologyRing() {
	
		
	}
	
	
	public static void initTopologyVonNeumann() {
	
		
	}
	
	
	public static void initTopologyRandom() {
	
		
	}
}
