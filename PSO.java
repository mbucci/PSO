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
		
		//set bounds for initial position and speed depending on
		//evaluation function chosen
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
		
		//Set topology given that chosen by user
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
		
		if (!(swarmSize==12 || swarmSize==20 || swarmSize==50)) {
			System.out.println("Choose a swarm size of 12, 20, or 50");
			System.exit(0);
		}
		
		//Run it
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
	
	/* Neighborhood is the two particles directly to left and right of it */
	public static void initTopologyRing() {
		for (int i = 0; i < swarmSize; i++) {
			int[] temp = new int[2];
			if (i == 0) {
				temp[0] = swarmSize-1;
				temp[1] = 1;
			}
			else if (i == swarmSize-1) {
				temp[0] = swarmSize - 2;
				temp[1] = 0;
			}
			else {
				temp[0] = i-1;
				temp[1] = i+1;
			}
			particles.get(i).setNeighborhood(temp, 2);
		}
	}
	
	/* In size 4 array holds [up, down, left, right] neighbors */
	public static void initTopologyVonNeumann() {
		// 12 particles: 3x4 array
		// 20 particles: 4x5 array
		// 50 particles: 5x10 array
		int rowSize = 5; // 20 or 50 particle swarm

		for (int i = 0; i < swarmSize; i++) {
			int[] temp = new int[4];

			if (swarmSize==12) { //12 particle swarm
				rowSize = 4;
			}

			if (i<rowSize) { // top row
				temp[0] = swarmSize+i-rowSize;
				temp[1] = i+rowSize;
			} else if (i+rowSize >= swarmSize) { // bottom row
				temp[0] = i-rowSize;
				temp[1] = i+rowSize-swarmSize;
			} else {
				temp[0] = i-rowSize;
				temp[1] = i+rowSize;
			}


			if (i%rowSize == 0) { //left column
				temp[2] = i+(rowSize-1);
				temp[3] = i+1;
			} else if ((i+1)%rowSize==0) { //right column
				temp[2] = i-1;
				temp[3] = i-(rowSize-1);
			} else {
				temp[2] = i-1;
				temp[3] = i+1;
			}

			particles.get(i).setNeighborhood(temp, 4);
		}
	}
	
	
	public static void initTopologyRandom() {
		int K=5; //neiborhood size
		Random rand = new Random();
		
		for (int i=0; i<swarmSize;i++ ){
			int[] temp = new int[K-1];
			//initia
			for (int j=0; j<K-1; j++){
				temp[j]=(int)rand.nextDouble()*(K-1);
			}
			
			particles.get(i).setNeighborhood(temp, K-1);
		}
		
	}
}
