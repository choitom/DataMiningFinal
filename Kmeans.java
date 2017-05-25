/**
	Authors: Jonah Tuchow, Tom Choi
	
	Implementation of K-means clustering algorithm to compare
	performnace against CURE and DBSCAN clustering algorithms.
*/

import java.io.*;
import java.util.*;

public class Kmeans{
	private int k;
	private String distance_type;
	private int decade;
	private County[] counties;
	private KmeansCluster[] clusters;
	
	public Kmeans(ArrayList<County> input, int decade, String distance_type, int k){
		this.k = k;
		this.decade = decade;
		this.distance_type = distance_type;
		this.clusters = new KmeansCluster[k];
		this.counties = new County[input.size()];
		int index = 0;
		for(County c : input){
			counties[index++] = new County(c.getID(), c.getVector());
		}
	}
	
	public void cluster(){
		// randomly pick 'k' centroids
		initCluster();
		
		// while not converged
		boolean converged = false;
		while(!converged){
			// clear data points within each cluster
			for(int i = 0; i < clusters.length; i++){
				clusters[i].clear();
			}
			
			// store centroids before assignment & update
			ArrayList<double[]> prev_centroids = new ArrayList<double[]>();
			for(int i = 0; i < clusters.length; i++){
				double[] centroid = clusters[i].getCentroid();
				double[] deep_copy = new double[centroid.length];
				for(int j = 0; j < centroid.length; j++){
					deep_copy[j] = centroid[j];
				}
				prev_centroids.add(deep_copy);
			}
			
			// assign data points
			for(int i = 0; i < counties.length; i++){
				assignDataPoint(counties[i]);
			}
			
			// update centroids
			for(int i = 0; i < clusters.length; i++){
				clusters[i].updateCentroid();
			}
			
			// check for convergence
			double error = 0.01;
			for(int i = 0; i < clusters.length; i++){
				converged = (clusters[i].distance(prev_centroids.get(i)) < error) ? true : false;
			}
		}
        
        // sort array of clusters by temporarily converting it to list
        List<KmeansCluster> clusterList = Arrays.asList(clusters);
        Collections.sort(clusterList, KmeansCluster.COMPARE_BY_QUALITY);
        clusters = clusterList.toArray(new KmeansCluster[clusterList.size()]);
			
		// print the clustering result
		printResult();
		
		// export the result to csv file
		exportClusters();
		exportCentroids();
	}
	
	private void exportCentroids(){
		String fileName = "Cluster_Result/" + decade + "_" + distance_type + "_KMEANS_CENTROID.csv";
		try{
			FileWriter fw = new FileWriter(fileName);
			fw.write("Less than High School,High School,Some College,Bachelors or Above\n");
			for(int i = 0; i < clusters.length; i++){
				double[] p = clusters[i].getCentroid();
				fw.write(p[0] + "," + p[1] + "," + p[2] + "," + p[3] + "\n");
			}
			fw.close();
		}catch(IOException e){
			System.out.println("Cluster Centroid Export Error");
		}
	}
	
	private void exportClusters(){
		String fileName = "Cluster_Result/" + decade + "_" + distance_type + "_KMEANS_RESULT.csv";
		ArrayList<double[]> centroids = new ArrayList<double[]>();
		try{
			FileWriter fw = new FileWriter(fileName);
			int id = 0;
            fw.write("Cluster,FIPS Code\n");
			for(int i = 0; i < clusters.length; i++){
				centroids.add(clusters[i].getCentroid());
				ArrayList<County> pts = clusters[i].getDataPoints();
				for(int j = 0; j < pts.size(); j++){
					fw.write(id + "," + pts.get(j).getID() + "\n");
				}
				id++;
			}
			for(double[] c : centroids){
				fw.write(c[0] + "," + c[1] + "," + c[2] + "," + c[3] + "\n");
			}
			fw.close();
		}catch(IOException e){
			System.out.println("Cluster Export Error");
		}
	}
	
	private void printResult(){
		System.out.println("\n\tK-means Clustering Result(" + distance_type + ")\n");
		for(int i = 0; i < clusters.length; i++){
			System.out.print("Cluster(" + i + ") -> ");
			clusters[i].print();
		}
	}
	
	// assign each data point to the closest cluster
	private void assignDataPoint(County c){
		int closestClusterIndex = -1;
		double closest = Double.MAX_VALUE;
		for(int i = 0; i < clusters.length; i++){
			double dist = clusters[i].distance(c.getVector());
			if(dist < closest){
				closest = dist;
				closestClusterIndex = i;
			}
		}
		clusters[closestClusterIndex].add(c);
	}
	
	// randomly select 'k' points for initial cluster centroids
	private void initCluster(){
		// shuffle counties
		int index;
		Random random = new Random();
		for(int i = counties.length-1; i > 0; i--){
			index = random.nextInt(i+1);
			County temp = counties[index];
			counties[index] = counties[i];
			counties[i] = temp;
		}
		
		// select first 'k' counties
		for(int i = 0; i < k; i++){
			KmeansCluster c = new KmeansCluster(this.distance_type);
			c.setCentroid(counties[i].getVector());
			clusters[i] = c;
		}
	}
}