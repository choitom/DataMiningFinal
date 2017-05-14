/**
	Authors: Jonah Tuchow, Tom Choi
	
	Implmentation of cluster for CURE algorithm that store
		1. centroid
		2. ramdomly sampled data points
		3. representative points
		4. actual data points that are assigned to the cluster
*/

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Cluster{
	private ArrayList<County> cluster;
	private ArrayList<County> dataPoints;
	private ArrayList<County> representatives;
	private double[] centroid;
	
	public Cluster(){
		this.cluster = new ArrayList<County>();
		this.dataPoints = new ArrayList<County>();
		this.representatives = new ArrayList<County>();
		this.centroid = new double[4];
	}
	
	// getters
	public double[] getCentroid(){
		return this.centroid;
	}
	
	public ArrayList<County> getCounties(){
		return this.cluster;
	}
	
	public ArrayList<County> getRepresentatives(){
		return this.representatives;
	}
	
	// add a county to cluster
	public void add(County c){
		this.cluster.add(c);
		updateCentroid();
	}
	
	public void addDataPoint(County c){
		this.dataPoints.add(c);
	}
	
	// pick representative points from the cluster
	public void pickRepresentatives(int n){
		// include all points if it has less than n data points
		if(cluster.size() <= n){
			for(County c : cluster){
				this.representatives.add(c);
			}
		}else{
			HashSet<County> set = new HashSet<County>();
			for(County c : cluster){
				set.add(c);
			}
			
			// pick the first random point
			int randomIndex = ThreadLocalRandom.current().nextInt(0, cluster.size());
			County current = cluster.get(randomIndex);
			this.representatives.add(current);
			set.remove(current);
			
			// choose farthest data points
			for(int i = 1; i < n; i++){
				double max_distance = Double.MIN_VALUE;
				County farthest_county = null;
				for(County c : set){
					double dist = distance(current.getVector(), c.getVector());
					if(dist > max_distance){
						max_distance = dist;
						farthest_county = c;
					}
				}
				current = farthest_county;
				this.representatives.add(current);
				set.remove(current);
			}
		}
	}
	
	// move repesentative points toward the centroid by 20%
	public void moveRepresentatives(){
		for(County c : representatives){
			double[] diff = new double[4];
			double[] v = c.getVector();
			for(int i = 0; i < diff.length; i++){
				diff[i] = centroid[i] - v[i];
			}
			
			for(int i = 0; i < v.length; i++){
				v[i] = v[i] + 0.2 * diff[i];
			}
			c.setVector(v);
		}
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
	
	// merge one cluster into another
	public void mergeCluster(Cluster c){
		ArrayList<County> counties = c.getCounties();
		for(County county : counties){
			this.cluster.add(county);
		}
	}
	
	// get cluster size
	public int size(){
		return this.cluster.size();
	}
	
	// print out cluster centroid and its data points
	public void printRepresentatives(){
		System.out.println("Centroid: [" + centroid[0] + "," + centroid[1] + "," + centroid[2] + "," + centroid[3] + "]");
		for(County c : cluster){
			c.print();
		}
		System.out.println("-------------------------------------------------");
	}
	
	public void printDataPoints(){
		System.out.println("Centroid: [" + centroid[0] + "," + centroid[1] + "," + centroid[2] + "," + centroid[3] + "]");
		for(County c : dataPoints){
			c.print();
		}
		System.out.println("-------------------------------------------------");
	}
	
	// euclidean distance of two 4 dimensional vectors
	private double distance(double[] v1, double[] v2){
		double distance = 0;
		for(int i = 0; i < v1.length; i++){
			distance += Math.pow(v1[i]-v2[i],2);
		}
		return Math.sqrt(distance);
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