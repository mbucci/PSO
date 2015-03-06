/*
 * Particle
 * Particle ojbect
 *
 * NIC - Professor Majercik
 * Max Bucci, Nikki Morin, Megan Maher, Kuangji Chen
 * Created: 2/26/15
 * Last Modified: 2/26/15
 *
 */


import java.util.*;

public class Particle {
	
	//Random Number Generator 
	private Random rand = new Random();
	
	//Particle attributes
	public double[] location;
	public double[] velocity;
	public double[] personalBestLoc;
	public double personalBestValue;
	public double[] hoodBestLoc;
	public double hoodBestValue;
	private int[] hoodMembers;
	private int hoodSize;
	private int dimension;
	
	
	//Overloaded constructor 
	public Particle(int dim) {
		
		this.location = new double[dim];
		this.velocity = new double[dim];
		this.personalBestLoc = new double[dim];
		this.hoodBestLoc = new double[dim];
		this.dimension = dim;
	}
		
		
	//Initiliaze a particle given location and speed bounds
	public Particle initParticle(int bound, double minLoc, double maxLoc, double minSpeed, double maxSpeed) {
		
		for (int i = 0; i < this.dimension; i++) {
			double temp = bound * rand.nextDouble();
			while (temp < minLoc || temp > maxLoc) {
				temp = bound * rand.nextDouble();
			}
			this.location[i] = temp;
			this.velocity[i] = minSpeed + rand.nextDouble() * (maxSpeed - minSpeed);
			this.personalBestValue = Double.MAX_VALUE;
			this.hoodBestValue = Double.MAX_VALUE;		
		}
		return this;
	}
	
	
	//Setter for neighboord and its size
	public void setNeighborhood(int[] hood, int size) {
		
		this.hoodMembers = hood;
		this.hoodSize = size;
	}
	
	
	//Getter for a specific member of a particle's neighborhood
	public int getNeighborhoodMember(int e) {
		
		return this.hoodMembers[e];	
	}

	
	//Getter for hoodSize
	public int getHoodSize() {
		
		return this.hoodSize;
	}
	
	
	//Getter for dimension
	public int getDimension() {
		
		return this.dimension;
	}
}
