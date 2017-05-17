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
	private County[] counties;
	private KmeansCluster[] clusters;
	
	public Kmeans(ArrayList<County> input, String distance_type, int k){
		this.k = k;
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
		boolean converged = true;
		while(!converged){
			// assign data points
		
			// update centroids
		}
			
		// print the clustering result
		
		// export the result to text file
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