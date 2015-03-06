/*
 * PSORunner 
 * Runner class for multi-dimensional particle environments
 *
 * NIC - Professor Majercik
 * Adapted from code written by Stephen Majercik 4/6/13
 * Max Bucci
 * Last Modified: 2/27/15
 *
 */


import java.util.*;


public class PSORunner {
	
	
	//Random Number Generator
	private Random rand = new Random();
	
	//Personal best acceleration coefficient
	private final double PHI1 = 2.05;
	//Global best acceleration coefficient
	private final double PHI2 = 2.05;  
	private final double CONSTRICTION_FACTOR = 0.7298;
	
	//Algorithm variables and data structures
	private String testFunction;
	private List<Particle> particles;
	private double[] bestLoc;
	private double bestValue;
	
	//Test functions
	private final String ROSENBROCK_FUNCTION_NUM = "rok";
	private final String ACKLEY_FUNCTION_NUM = "ack";
	private final String RASTRIGIN_FUNCTION_NUM = "ras";
	
	
	//Overloaded Constructor
	public PSORunner(String function, List<Particle> ps) {
		
		this.testFunction = function;
		this.particles = ps;
		this.bestValue = Double.MAX_VALUE;
		
		// create particles and calculate initial personal bests; 
		// find the initial global best
		for (int i = 0 ; i < ps.size(); i++) {
			
			Particle temp = ps.get(i);
			
			// initial value
			double currValue = eval(ps.get(i));
			
			temp.personalBestValue = currValue;
			
			// ****** check for new global best and store, if necessary, in the variables provided
			if (currValue <= temp.hoodBestValue) {
				temp.hoodBestLoc = temp.location;
				temp.hoodBestValue = currValue;
				updateHoodBest(temp);
			} 
		}
	}
	
	
	//Run PSO
	public void runPSO(int numIterations, String topology) {
		
		int count = 1;
		do {

			if (count%10 == 0){
				System.out.println("Iteration #" + count + " Best value: " + bestValue);
			}
			// update all the particles
			for (int i = 0 ; i < this.particles.size() ; i++) {
				
				Particle temp = this.particles.get(i);
				for (int j = 0; j < temp.getDimension(); j++) {
					// ****** compute the acceleration due to personal best
					double pAccel = temp.personalBestLoc[j] - temp.location[j];
					
					// ****** compute the acceleration due to global best
					double gAccel = temp.hoodBestLoc[j] - temp.location[j];
					
					// ****** constrict the new velocity and reset the current velocity
					double newVel = (temp.velocity[j] + ((rand.nextDouble() * PHI1) * pAccel) + ((rand.nextDouble() * PHI2) * gAccel));
					newVel = CONSTRICTION_FACTOR * newVel;
					temp.velocity[j] = newVel;
					
					// ****** update the position
					temp.location[j] += newVel;
				}
				
				
				// ****** find the value of the new position
				double currValue = eval(temp);
				// System.out.println(currValue);
				
				// ****** update personal best and global best, if necessary
				if (currValue <= temp.personalBestValue) {
					temp.personalBestLoc = temp.location;
					temp.personalBestValue = currValue;
				}
				
				
				for (int h = 0; h < temp.getHoodSize(); h++) {
					//System.out.println(temp.getHoodSize());
					
					Particle hoodMem = particles.get(temp.getNeighborhoodMember(h));
					double memValue = eval(hoodMem);
					
					if (currValue <= temp.hoodBestValue) {
						temp.hoodBestLoc = temp.location;
						temp.hoodBestValue = currValue;
						// System.out.println(temp.hoodBestValue);
					} 
					if (memValue <= temp.hoodBestValue) {
						temp.hoodBestLoc = hoodMem.location;
						temp.hoodBestValue = memValue;
						updateHoodBest(temp);
						//System.out.println(temp.hoodBestValue);
					}
				}
				
				if (temp.hoodBestValue <= bestValue) {
					this.bestValue = temp.hoodBestValue;
					this.bestLoc = temp.hoodBestLoc;
						
				}
			}

			/*update the random neighborhood if necessary */
			if(topology.equals("ra")){
				for (int i = 0 ; i < this.particles.size() ; i++) {
					
					Particle temp = this.particles.get(i);
					
					int K=5; //neighborhood size
					Random rand = new Random();
					
					for (int j=0; j<temp.getHoodSize();j++ ){
						int[] newHood = new int[K];
						int index;
						//initialization
						for (int m=0; m<K; m++){
							index=(int)(rand.nextDouble()*(this.particles.size()));
							
							//the particle itself will not be in the neighborhood
							while(index==i){
								index=(int)(rand.nextDouble()*(this.particles.size()));
							}
							
							newHood[m]=index;
						}
						
						temp.setNeighborhood(newHood, K);
					}
				
				}
			}   
			count++;    
		} while (count <= numIterations);
	}
	
	private void updateHoodBest(Particle temp){
		for (int m = 0; m < temp.getHoodSize(); m++) {
			particles.get(temp.getNeighborhoodMember(m)).hoodBestValue = temp.hoodBestValue;
			particles.get(temp.getNeighborhoodMember(m)).hoodBestLoc = temp.hoodBestLoc;
		}
	}

	
	//Returns the value of the specified function for point (x, y)
	public double eval(Particle part) {
		
		/*
		for (int i = 0; i < part.getDimension(); i++) {
			part.getLocation()[i] -= FUNCTION_SHIFT;      
		}
		*/
		
		double ret = 0.0;
		
		if (this.testFunction.equals(ROSENBROCK_FUNCTION_NUM)) {
			ret = evalRosenbrock(part);
		}  
		else if (this.testFunction.equals(RASTRIGIN_FUNCTION_NUM)) {
			ret = evalRastrigin(part);
		}  
		else if (this.testFunction.equals(ACKLEY_FUNCTION_NUM)) {
			ret = evalAckley(part);
		}  
		
		return ret;
	}
	
	
	//Returns the value of the Rosenbrock Function at point (x, y)
	//minimum is 0.0, which occurs at (1.0,...,1.0)
	public double evalRosenbrock (Particle part) {
		
		double ret = 0.0;
		for (int i = 0; i < part.getDimension() - 1; i++) {
			double current = part.location[i];
			double currentPlus1 = part.location[i+1];
			ret += Math.pow((1-current),2) + 100.0*Math.pow((currentPlus1-current*current),2);
		}

		return ret;
	}
	
	
	//Returns the value of the Rastrigin Function at point (x, y)
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public double evalRastrigin (Particle part) {
		
		double ret = 0.0;
		for (int i = 0; i < part.getDimension(); i++) {
			double temp = part.location[i];
			ret += temp*temp - 10.0*Math.cos(2.0*Math.PI*temp) + 10.0;    
		}
		
		return ret;
	}
	
	
	//Returns the value of the Ackley Function at point (x, y)
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public double evalAckley (Particle part) {
		
		double a = 20.0;
		double b = 0.2;
		double c = 2*Math.PI;
		double firstSum = 0.0;
		double secondSum = 0.0;
		
		for (int i = 0; i < part.getDimension(); i++) {
			firstSum += Math.pow(part.location[i],2);
			secondSum += Math.cos(c * part.location[i]);
		}
		
		firstSum = -b * Math.sqrt(firstSum/part.getDimension());
		secondSum = secondSum/part.getDimension();
		
		return (-a * Math.exp(firstSum)) - secondSum + a + Math.exp(1);
	}
}  


