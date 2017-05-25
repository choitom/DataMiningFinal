/**
	Authors: Jonah Tuchow, Tom Choi
	
	Implementation of
		- CURE(Clustering Using REpresentatives)
		- DBSCAN(Density-Based Spatial Clustering of Application with Noise)
	algorithms on educational attainment for the counties in the states from 1970-2015 per decade.
*/

import java.io.*;
import java.util.*;

public class Main{
	private static double CURE_fraction = -1;
	private static int CURE_cluster_size = -1;
	private static int CURE_num_rep = -1;
	private static int DBSCAN_min_pts = -1;
	private static double DBSCAN_radius = -1;
	private static int KMEANS_cluster_size = -1;
	private static int decade = -1;
	private static String distance_type;
	private static ArrayList<County> counties;
	
	public static void main(String[] args) throws IOException{
		argCheck(args);
		decade = Integer.parseInt(args[0]);
		distance_type = args[1];
		
		// read input file
		String input_file = "Education.csv";
		File file = new File(input_file);
		Scanner scan = new Scanner(file);
		
		// list of counties
		counties = new ArrayList<County>();
		int index = 3 + 4*((decade-1970)/10);
		
		while(scan.hasNextLine()){
			String[] countyData = scan.nextLine().split(",");
			int id = Integer.parseInt(countyData[0]);
			
			// countyData length < index or countyData empty at index
			if(countyData.length-1 < index){
				continue;
			}else{
				if(countyData[index].equals("") || countyData[index+1].equals("") ||
				countyData[index+2].equals("") || countyData[index+3].equals("")){
					continue;
				}else{
					double[] data = {Double.parseDouble(countyData[index]),
									Double.parseDouble(countyData[index+1]),
									Double.parseDouble(countyData[index+2]),
									Double.parseDouble(countyData[index+3])};
					counties.add(new County(id, data));	
				}
			}
		}
		
		
		String param_file = "param.txt";
		file = new File(param_file);
		scan = new Scanner(file);
		
		int line_count = 0;
		while(line_count < 6){
			String[] line = scan.nextLine().split(" ");
			String value = line[1];
			switch(line_count){
				case 0:
					CURE_fraction = Double.parseDouble(value);
					break;
				case 1:
					CURE_cluster_size = Integer.parseInt(value);
					break;
				case 2:
					CURE_num_rep = Integer.parseInt(value);
					break;
				case 3:
					DBSCAN_min_pts = Integer.parseInt(value);
					break;
				case 4:
					DBSCAN_radius = Double.parseDouble(value);
					break;
				case 5:
					KMEANS_cluster_size = Integer.parseInt(value);
					break;
				default:
					System.err.println("Error\n");
					System.exit(0);
					break;
			}
			line_count++;
		}
		
		CURECluster();
		KmeansCluster();
		DBSCANCluster();
	}
	
	private static void argCheck(String[] args){
		if(args.length < 2){
			System.out.println("Please Enter: java Main Decade Distance_Type");
			System.out.println("(Ex) java Main 2010 manhattan  OR  java Main 2000 euclidean");
			System.exit(0);
		}
	}
	
	private static void CURECluster(){
		HashMap<Integer, int[]> countyMatchings = new HashMap<Integer, int[]>();
		for(County c : counties){
			int[] v = new int[CURE_cluster_size];
			countyMatchings.put(c.getID(), v);
		}
		
		for(int i = 0; i < 30; i++){
			CURE cure = new CURE(counties, decade, distance_type, CURE_fraction, CURE_cluster_size, CURE_num_rep);
			ArrayList<Cluster> clusters = cure.cluster();
			int cluster_ID = 0;
			for(Cluster c : clusters){
				ArrayList<County> dataPoints = c.getDataPoints();
				for(County county : dataPoints){
					int id = county.getID();
					int[] v = countyMatchings.get(id);
					v[cluster_ID]++;
					countyMatchings.put(id,v);
				}
				cluster_ID++;
			}
		}
		
		// set of (County_ID, County)
		HashMap<Integer, County> map = new HashMap<Integer, County>();
		for(County c : counties){
			map.put(c.getID(), c);
		}
		
		Cluster[] clusters = new Cluster[CURE_cluster_size];
		for(int i = 0; i < CURE_cluster_size; i++){
			clusters[i] = new Cluster(distance_type);
		}
		
		for(int County_ID : countyMatchings.keySet()){
			int[] votes = countyMatchings.get(County_ID);
			int maxVote = votes[0];
			int maxIndex = 0;
			for(int i = 1; i < votes.length; i++){
				if(votes[i] > maxVote){
					maxVote = votes[i];
					maxIndex = i;
				}
			}
			County c = map.get(County_ID);
			
			// assign it to the cluster with max vote
			clusters[maxIndex].addDataPoint(c);
		}
		
		for(Cluster c : clusters){
			c.print();
		}
		exportClusters(clusters, "CURE");
	}
	
	private static void KmeansCluster(){
		Kmeans kmeans = new Kmeans(counties, decade, distance_type, KMEANS_cluster_size);
		kmeans.cluster();
	}
	
	private static void DBSCANCluster(){
		DBSCAN dbscan = new DBSCAN(counties, decade, distance_type, DBSCAN_min_pts, DBSCAN_radius);
		dbscan.cluster();
	}
	
	private static void exportClusters(Cluster[] clusters, String cluster_type){
        String fileName = "Cluster_Result/" + decade + "_" + distance_type + "_" + cluster_type +"_RESULT.csv";
		ArrayList<double[]> centroids = new ArrayList<double[]>();
		try{
			FileWriter fw = new FileWriter(fileName);
			int id = 0;
            fw.write("Cluster,FIPS Code\n");
			for(Cluster d : clusters){
				centroids.add(d.getCentroid());
				ArrayList<County> pts = d.getDataPoints();
				for(int i = 0; i < pts.size(); i++){
					fw.write(id + "," + pts.get(i).getID() + "\n");
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
}