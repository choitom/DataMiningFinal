#!bin/bash

#compile java source files
javac *.java

fro i in {1970..2020..10}
do
	echo "Clustering decade: $i"
	java Main $i "euclidean"
	java Main $i "manhattan"
done