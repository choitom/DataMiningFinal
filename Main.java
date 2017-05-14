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
		if(args.length < 1){
			System.out.println("Require decade as an argument e.g. 1970, 1980, ...!");
			System.exit(0);
		}
		String str_decade = args[0];
		int decade = Integer.parseInt(str_decade);
		
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
		
		CURE cure = new CURE(counties, 0.1);
		cure.cluster();
		//DBSCAN dbscan = new DBSCAN(counties);
	}
}