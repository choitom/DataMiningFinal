/**
	Authors: Jonah Tuchow, Tom Choi
	
*/

import java.io.*;
import java.util.*;

public class CURE{
	private County[] counties;
	private County[] samples;
	
	public CURE(ArrayList<County> c, double fraction){
		int sample_size = (int)(c.size() * fraction);
		this.counties = new County[c.size()];
		this.samples = new County[sample_size];
		
		int index = 0;
		for(County county : c){
			counties[index++] = new County(county.getID(), county.getVector());
		}
		randomSamples(sample_size);
	}
	
	public void cluster(){
		// First part: pick representative points
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		ArrayList<Double> sses = new ArrayList<Double>();
		for(int i = 0; i < samples.length; i++){
			Cluster c = new Cluster();
			c.add(samples[i]);
			clusters.add(c);
		}
		
		sses.add(SSEs(clusters));
		while(clusters.size() > 6){
			mergeCluster(clusters);
			sses.add(SSEs(clusters));
		}
		
		for(Cluster c : clusters){
			System.out.println(c.size());
			c.print();
		}
		
		/*
		CODE FOR DETERMINING NUMBER OF CLUSTERS
		
		ArrayList<Double> sses = new ArrayList<Double>();
		for(int i = 0; i < samples.length; i++){
			Cluster c = new Cluster();
			c.add(samples[i]);
			clusters.add(c);
		}
		sses.add(SSEs(clusters));
		while(clusters.size() > 1){
			mergeCluster(clusters);
			sses.add(SSEs(clusters));
		}
		exportCSV(sses);
		*/
		
		
		// Second part: assign all the other data points
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
		double distance = 0;
		for(int i = 0; i < v1.length; i++){
			distance += Math.pow(v1[i]-v2[i],2);
		}
		return Math.sqrt(distance);
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
	
	private double SSEs(ArrayList<Cluster> clusters){
		double sum = 0;
		for(Cluster c : clusters){
			sum += c.SSE();
		}
		return sum;
	}
}