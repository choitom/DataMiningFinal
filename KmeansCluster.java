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
    //Compares Clusters by subjective quality of distribution
    //(Bachelor's or higher is really good, less than high school is really bad)
    public static Comparator<KmeansCluster> COMPARE_BY_QUALITY = new Comparator<KmeansCluster>() {
        public int compare(KmeansCluster cluster1, KmeansCluster cluster2) {
            double totalVal1 = 3*cluster1.centroid[3] + cluster1.centroid[2] - cluster1.centroid[1] - 3*cluster1.centroid[0];
            double totalVal2 = 3*cluster2.centroid[3] + cluster2.centroid[2] - cluster1.centroid[1] - 3*cluster2.centroid[0];
            return Double.compare(totalVal1, totalVal2);
        }
    };
	
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
	
	// measure distance from the centroid to a vector
	public double distance(double[] vector){
		double dist = 0;
		if(distance_type.equals("euclidean")){
			for(int i = 0; i < vector.length; i++){
				dist += Math.pow(vector[i]-centroid[i], 2);
			}
			dist = Math.sqrt(dist);
		}else{
			for(int i = 0; i < vector.length; i++){
				dist += Math.abs(vector[i]-centroid[i]);
			}
		}
		return dist;
	}
	
	// clear data points
	public void clear(){
		this.cluster = new ArrayList<County>();
	}
	
	// update centroid
	public void updateCentroid(){
		// mean for Euclidean distance
		if(distance_type.equals("euclidean")){
			double[] newCentroid = new double[this.centroid.length];
			for(County c : cluster){
				double[] v = c.getVector();
				for(int i = 0; i < v.length; i++){
					newCentroid[i] += v[i];
				}
			}
			for(int i = 0; i < newCentroid.length; i++){
				this.centroid[i] = newCentroid[i]/cluster.size();
			}
		}
		// median for Manhattan distance
		else{
			int mid = cluster.size()/2;
			for(int i = 0; i < centroid.length; i++){
				double[] repo = new double[cluster.size()];
				int idx = 0;
				for(County c : cluster){
					repo[idx++] = c.getVector()[i];
				}
				Arrays.sort(repo);
				this.centroid[i] = repo[mid];
			}
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