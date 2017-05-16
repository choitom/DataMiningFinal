/**
	Authors: Jonah Tuchow, Tom Choi
	
	Implementation of DBSCAN(Density-Based Spatial Clustering of Application with Noise)
	which works as follows with given radius and minimum points,
		
		For each data point
			if not yet classified
				if a core-object
					collect all objects density-reachable
					and assign them to a new cluster
				else
					assign it to NOISE
				
		*NOISE(outlier): a data point that doesn't have any adjacent neighbor within the radius
		*core-object: a data point whose number of neighbors within the radius is greater
					  or equal to the minimum points
		*density-reachable: a point p is density-reachable from point q
							if q is a core-object and p is in q's neighborhood
*/

import java.io.*;
import java.util.*;

public class DBSCAN{
	private int min_pts;
	private double radius;
	private String distance_type;
	private County initial_point;
	private ArrayList<County> counties;
	private ArrayList<County> NOISE;
	private HashSet<DBSCluster> clusters;
	
	public DBSCAN(ArrayList<County> c, String distance_type, int min_pts, double radius){
		this.min_pts = min_pts;
		this.radius = radius;
		this.distance_type = distance_type;
		this.counties = new ArrayList<County>();
		this.clusters = new HashSet<DBSCluster>();
		this.NOISE = new ArrayList<County>();
		
		for(County county : c){
			counties.add(county);
		}
		
		// find an initial core-object
		for(County county : counties){
			if(findNeighbors(county).size() >= min_pts){
				initial_point = county;
			}
		}
		
		// check for the case where there is no core-object
		if(initial_point == null){
			System.out.println("There exist no core-object with the given parameters");
			System.exit(0);
		}
		counties.remove(initial_point);
	}
	
	public void cluster(){
		County current = initial_point;
		DBSCluster current_cluster;
		Deque<County> stack;
		
		while(!counties.isEmpty()){
			// Core-object
			if(isCoreObject(current)){
				current_cluster = new DBSCluster(current);
				
				// repository of adjacent core-objects
				stack = new ArrayDeque<County>();
				
				// find its neighbors
				ArrayList<County> neighbors = findNeighbors(current);
				
				// if neighbor boundary-object, add it to cluster
				// if core-object, add it to cluster and enqueue
				// to search for more core and boundary objects
				for(County c : neighbors){
					current_cluster.add(c);
					counties.remove(c);
					if(isCoreObject(c)){
						stack.push(c);
					}
				}
				
				// while there exist core-objects
				while(!stack.isEmpty()){
					
					// dequeue a core-object
					County core = stack.pop();
					
					// find its neighbors
					neighbors = findNeighbors(core);
					
					// repeat searching for core-objects and enqueue them
					for(County c : neighbors){
						current_cluster.add(c);
						counties.remove(c);
						if(isCoreObject(c)){
							stack.push(c);
						}
					}
				}
				
				// once done testing density reachability, store the density cluster
				clusters.add(current_cluster);
			}
			// NOISE
			else{
				// add a noise to the list
				this.NOISE.add(current);
				
				// remove it from county set
				counties.remove(current);	
			}
			
			// pick a new point that is either noise or core-object
			current = pickValidPoint();
			//System.out.println(counties.size());
		}
		
		
		int cluster_ID = 0;
		System.out.println("\n\n\tDBSCAN(Density-Based Spatial Clustering of Application with Noise) Result\n");
		for(DBSCluster d : clusters){
			System.out.println("Cluster(" + cluster_ID + "): " + d.size());
			cluster_ID++;
			//d.print();
		}
		System.out.println("NOISE: " + NOISE.size());
	}
	
	private boolean isCoreObject(County c){
		return findNeighbors(c).size() >= min_pts ? true : false;
	}
	
	// pick core-object or noise
	private County pickValidPoint(){
		County ret = null;
		for(County c : counties){
			int n = findNeighbors(c).size();
			if(n >= min_pts || n == 0){
				ret = c;
			}
		}
		
		// if no core-object or any noise with 0 neighbors
		// then, the rest of points are all noise
		boolean picked = false;
		if(ret == null){
			for(County c : counties){
				if(!picked){
					ret = c;
					break;
				}
			}
		}
		return ret;
	}
	
	// find adjacent neighbors within radius
	private ArrayList<County> findNeighbors(County current){
		ArrayList<County> neighbors = new ArrayList<County>();
		for(County c : counties){
			if(current.equals(c)){
				continue;
			}
			if(distance(c.getVector(), current.getVector()) <= radius){
				neighbors.add(c);
			}
		}
		return neighbors;
	}
	
	// compute distance between two vectors
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
}