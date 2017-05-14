/**
	Authors: Jonah Tuchow, Tom Choi
	
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