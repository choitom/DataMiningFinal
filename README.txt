1. How to compile and run
	- Execute run.sh on terminal -> "./run.sh"
	- The script will execute all clustering algorithms
	  for each decade from 1970 to 2010 for Euclidean and Manhattan distances.
	- Once it finishes clustering, it prints out number of data points assigned to each cluster

2. What if you want to pick a single decade and distance type?
	Compile: javac *.java
	Execute: java Main Decade Distance_Type
	
	where,
		Decade = 1970, 1980, 1990, 2000, or 2010
		Distance_Type = manhattan, or euclidean
		
	Examples
		(1) java Main 1970 manhattan
		(2) java Main 2000 euclidean
			
3. Known bugs
	Haven't seen any
		
4. Notes
	a) It might take a while to finish running DBSCAN algorithm
	   because it has to constantly find a point(core-object)
	   whose number of neighbors is greater than or equal to
	   a certain threshold.
	b) user can manipulate parameters such as number of clusters for K-means
	   and agglomerative clustering.