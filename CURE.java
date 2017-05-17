/**
	Authors: Jonah Tuchow, Tom Choi
	
	Implementation of CURE(Clustering Using REpresentatives)
	algorithm which works as follows
		1. Pick a random sample of points
		2. Cluster sample points hierarchically to create the initial cluster
			(We decided to use Agglomerative Clustering)
		3. Pick representative points from the initial cluster
		4. For each data point, find the cluster that has the closest representative point
*/

import java.io.*;
import java.util.*;

public class CURE{
	private County[] counties;
	private County[] samples;
	private String distance_type;
	private int cluster_size;	// number of clusters to have
	private int num_rep;		// number of representative points
	
	public CURE(ArrayList<County> c, String distance_type, double fraction, int cluster_size, int num_rep){
		int sample_size = (int)(c.size() * fraction);
		this.distance_type = distance_type;
		this.counties = new County[c.size()];
		this.samples = new County[sample_size];
		this.cluster_size = cluster_size;
		this.num_rep = num_rep;
		
		int index = 0;
		for(County county : c){
			counties[index++] = new County(county.getID(), county.getVector());
		}
		randomSamples(sample_size);
	}
	
	public void cluster(){
		// First part: pick representative points using agglomerative clustering
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		ArrayList<Double> sses = new ArrayList<Double>();
		for(int i = 0; i < samples.length; i++){
			Cluster c = new Cluster(distance_type);
			c.add(samples[i]);
			clusters.add(c);
		}
		
		sses.add(SSEs(clusters));
		while(clusters.size() > cluster_size){
			mergeCluster(clusters);
			sses.add(SSEs(clusters));
		}
		
		// for each cluster, pick as many representative points as num_rep
		for(Cluster c : clusters){
			c.pickRepresentatives(num_rep);
			c.moveRepresentatives();
		}
		
		// Second part: assign all the other data points
		for(int i = 0; i < counties.length; i++){
			County county = counties[i];
			
			// find the closest representative point
			double min_distance = Double.MAX_VALUE;
			int min_cluster_index = -1;
			for(int j = 0; j < clusters.size(); j++){
				Cluster c = clusters.get(j);
				ArrayList<County> representatives = c.getRepresentatives();
				for(County rep : representatives){
					double dist = distance(county.getVector(), rep.getVector());
					if(dist < min_distance){
						min_distance = dist;
						min_cluster_index = j;
					}
				}
			}
			// assign data point
			clusters.get(min_cluster_index).addDataPoint(county);
		}
		
		// print out the result
		int id = 0;
		System.out.println("\n\tCURE(Clusting Using REpresentatives) Result\n");
		for(Cluster c : clusters){
			System.out.print("Cluster("+ id + ") -> ");
			c.print();
			id++;
		}
		
		// export the result to text file
		exportClusters(clusters);
	}
	
	// export clusters to text file
	private void exportClusters(ArrayList<Cluster> clusters){
		try{
			FileWriter fw = new FileWriter("Cluster_Result/CURE_RESULT.txt");
			int id = 0;
			for(Cluster d : clusters){
				ArrayList<County> pts = d.getDataPoints();
				fw.write("Cluster(" + id + "): ");
				for(int i = 0; i < pts.size(); i++){
					if(i == pts.size()-1){
						fw.write(pts.get(i).getID() + "\n");
					}else{
						fw.write(pts.get(i).getID() + " ");
					}
				}
				id++;
			}
			fw.close();
		}catch(IOException e){
			System.out.println("Cluster Export Error");
		}
	}
	
	// export SSE values into .csv file
	private void exportCSV(ArrayList<Double> sses){
		int num_clusters = sses.size();
		try{
			FileWriter fw = new FileWriter("SSE.csv");
			for(int i = 0; i < sses.size(); i++){
				fw.write(num_clusters + "," + sses.get(i) + "\n");
				num_clusters--;
			}
			fw.close();
		}catch(IOException e){
			System.out.println("Error writing SSEs");
		}
	}
	
	// use centroid distance to measure distance between clusters
	private void mergeCluster(ArrayList<Cluster> clusters){
		// find two closest clusters
		double min_dist = Double.MAX_VALUE;
		Cluster min1 = null;
		Cluster min2 = null;
		for(int i = 0; i < clusters.size()-1; i++){
			for(int j = i+1; j < clusters.size(); j++){
				
				// calculate centroid distance
				double dist = distance(clusters.get(i).getCentroid(), clusters.get(j).getCentroid());
				if(dist < min_dist){
					min_dist = dist;
					min1 = clusters.get(i);
					min2 = clusters.get(j);
				}
			}
		}
		// merge
		min1.mergeCluster(min2);
		
		// remove min2 from cluster list
		clusters.remove(min2);
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
	
	// randomly shuffle county array and select the first 'sample_size' counties
	private void randomSamples(int sample_size){
		int index;
		Random random = new Random();
		for(int i = counties.length-1; i > 0; i--){
			index = random.nextInt(i+1);
			County temp = counties[index];
			counties[index] = counties[i];
			counties[i] = temp;
		}
		
		for(int i = 0; i < sample_size; i++){
			samples[i] = counties[i];
		}
	}
	
	// return the sum of errors of all clusters
	private double SSEs(ArrayList<Cluster> clusters){
		double sum = 0;
		for(Cluster c : clusters){
			sum += c.SSE();
		}
		return sum;
	}
}