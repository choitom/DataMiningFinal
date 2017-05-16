/**
	Authors: Jonah Tuchow, Tom Choi
	
	Cluster class for DBSCAN algorithm which only stores data points
*/

import java.util.*;

public class DBSCluster{
	private ArrayList<County> cluster;
	
	public DBSCluster(){
		this.cluster = new ArrayList<County>();
	}
	
	public void add(County c){
		this.cluster.add(c);
	}
	
	public void print(){
		for(County c : cluster){
			c.print();
		}
		System.out.println("--------------------------------------");
	}
	
	public int size(){
		return cluster.size();
	}
}