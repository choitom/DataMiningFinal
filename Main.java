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
	public static void main(String[] args) throws IOException{
		argCheck(args);
		int decade = Integer.parseInt(args[0]);
		String distance_type = args[1];
		
		// read input file
		String input_file = "Education.csv";
		File file = new File(input_file);
		Scanner scan = new Scanner(file);
		
		// list of counties
		ArrayList<County> counties = new ArrayList<County>();
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
		
		// read parameters
		double CURE_fraction = -1;
		int CURE_cluster_size = -1;
		int CURE_num_rep = -1;
		int DBSCAN_min_pts = -1;
		double DBSCAN_radius = -1;
		int KMEANS_cluster_size = -1;
		
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
		
		CURE cure = new CURE(counties, distance_type, CURE_fraction, CURE_cluster_size, CURE_num_rep);
		cure.cluster();
		
		DBSCAN dbscan = new DBSCAN(counties, distance_type, DBSCAN_min_pts, DBSCAN_radius);
		dbscan.cluster();
		
		Kmeans kmeans = new Kmeans(counties, distance_type, KMEANS_cluster_size);
		kmeans.cluster();
	}
	
	private static void argCheck(String[] args){
		if(args.length < 2){
			System.out.println("Please Enter: java Main Decade Distance_Type");
			System.out.println("(Ex) java Main 2010 manhattan  OR  java Main 2000 euclidean");
			System.exit(0);
		}
	}
}