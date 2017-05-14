/**
	Authors: Jonah Tuchow, Tom Choi
	
	County class that stores
		- county ID
		- educational attainment information
			1. Less than high school
			2. High school
			3. Some college 2-3 years)
			4. College(4 years) or above
*/

public class County{
	private int ID;
	private double[] vector;
	
	public County(int ID, double[] data){
		this.ID = ID;
		this.vector = new double[data.length];
		for(int i = 0; i < vector.length; i++){
			vector[i] = data[i];
		}
	}
	
	public void setVector(double[] v){
		for(int i = 0; i < vector.length; i++){
			vector[i] = v[i];
		}
	}
	
	public int getID(){
		return this.ID;
	}
	
	public double[] getVector(){
		return this.vector;
	}
	
	public void print(){
		System.out.println("ID: " + ID + ", Data: [" + vector[0] + "," + vector[1] + "," + vector[2] + "," + vector[3] + "]");
	}
}