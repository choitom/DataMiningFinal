1. How to compile
		javac *.java
	
2. How to run
		java Main Decade Distance_Type
	
		where,
			Decade = 1970, 1980, 1990, 2000, or 2010
			Distance_Type = manhattan, or euclidean
		
		Examples
			(1) java Main 1970 manhattan
			(2) java Main 2000 euclidean
			
3. Known bugs
		Haven't seen any
		
4. Notes
		It might take a while to finish running DBSCAN algorithm
		because it has to constantly find a point(core-object)
		whose number of neighbors is greater than or equal to
		a certain threshold.