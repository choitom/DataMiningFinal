/**
	Authors: Jonah Tuchow, Tom Choi
	
	Cluster class for K-means clustering algorithm
	which stores a centroid and data points
*/

import java.util.*;

public class KmeansCluster{
	private String distance_type;
	private double[] centroid;
	private ArrayList<County> cluster;
	
	public KmeansCluster(String distance_type){
		this.distance_type = distance_type;
		this.cluster = new ArrayList<County>();
	}
	
	// getters
	public double[] getCentroid(){
		return this.centroid;
	}
	
	public ArrayList<County> getDataPoints(){
		return this.cluster;
	}
	
	// set centroid
	public void setCentroid(double[] vector){
		this.centroid = new double[vector.length];
		for(int i = 0; i < centroid.length; i++){
			centroid[i] = vector[i];
		}
	}
	
	// add a county to cluster
	public void add(County c){
		this.cluster.add(c);
	}
	
	// print centroid & number of data points
	public void print(){
		System.out.println("Centroid: [" + centroid[0] + "," + centroid[1] + "," + centroid[2] + "," + centroid[3] + "], Size: " + cluster.size());
	}
}