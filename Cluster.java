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
	private String distance_type;
    // Compares Clusters by subjective quality of distribution
    // (Bachelor's or higher is really good, less than high school is really bad)
    public static Comparator<Cluster> COMPARE_BY_QUALITY = new Comparator<Cluster>() {
        public int compare(Cluster cluster1, Cluster cluster2) {
            double totalVal1 = 3*cluster1.centroid[3] + cluster1.centroid[2] - cluster1.centroid[1] - 3*cluster1.centroid[0];
            double totalVal2 = 3*cluster2.centroid[3] + cluster2.centroid[2] - cluster1.centroid[1] - 3*cluster2.centroid[0];
            return Double.compare(totalVal1, totalVal2);
        }
    };
	
	public Cluster(String distance_type){
		this.distance_type = distance_type;
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
	
	public ArrayList<County> getDataPoints(){
		return this.dataPoints;
	}
	
	// add a county to cluster
	public void add(County c){
		this.cluster.add(c);
		updateCentroid(cluster);
	}
	
	public void addDataPoint(County c){
		this.dataPoints.add(c);
		updateCentroid(dataPoints);
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
	
	// print out cluster information such as centroid, number of data points
	public void print(){
		System.out.println("Centroid: [" + centroid[0] + "," + centroid[1] + "," + centroid[2] + "," + centroid[3] + "], Size: " + dataPoints.size());
	}
	
	// euclidean distance of two 4 dimensional vectors
	private double distance(double[] v1, double[] v2){
		double dist = 0;
		if(distance_type.equals("manhattan")){
			for(int i = 0; i < v1.length; i++){
				dist += Math.abs(v1[i]-v2[i]);
			}	
		}else{
			for(int i = 0; i < v1.length; i++){
				dist += Math.pow(v1[i]-v2[i], 2);
			}
			dist = Math.sqrt(dist);
		}
		return dist;
	}
	
	private void updateCentroid(ArrayList<County> list){
		int size = list.size();
		double[] sum = new double[4];
		for(County c : list){
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