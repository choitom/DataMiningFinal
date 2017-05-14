/**
	Authors: Jonah Tuchow, Tom Choi
	
*/

import java.util.*;

public class Cluster{
	private ArrayList<County> cluster;
	private double[] centroid;
	
	public Cluster(){
		this.cluster = new ArrayList<County>();
		this.centroid = new double[4];
	}
	
	// getters
	public double[] getCentroid(){
		return this.centroid;
	}
	
	public ArrayList<County> getCounties(){
		return this.cluster;
	}
	
	// add a county to cluster
	public void add(County c){
		this.cluster.add(c);
		updateCentroid();
	}
	
	// compute SSE
	public double SSE(){
		double sse = 0;
		for(County c : cluster){
			double[] vector = c.getVector();
			for(int i = 0; i < vector.length; i++){
				double diff = vector[i] - centroid[i];
				sse += Math.pow(diff,2);
			}
		}
		return sse;
	}
	
	public void mergeCluster(Cluster c){
		ArrayList<County> counties = c.getCounties();
		for(County county : counties){
			this.cluster.add(county);
		}
	}
	
	public int size(){
		return this.cluster.size();
	}
	
	public void print(){
		System.out.println("Centroid: [" + centroid[0] + "," + centroid[1] + "," + centroid[2] + "," + centroid[3] + "]");
		for(County c : cluster){
			c.print();
		}
		System.out.println("-------------------------------------------------");
	}
	
	private void updateCentroid(){
		int size = cluster.size();
		double[] sum = new double[4];
		for(County c : cluster){
			double[] v = c.getVector();
			for(int i = 0; i < v.length; i++){
				sum[i] += v[i];
			}
		}
		for(int i = 0; i < sum.length; i++){
			centroid[i] = sum[i]/size;
		}
	}
}