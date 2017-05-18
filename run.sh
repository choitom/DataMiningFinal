#!/bin/bash

#compile java source files
javac *.java

for i in {1970..2010..10}
do
	echo "Clustering $i's..."
	java Main $i "euclidean"
	java Main $i "manhattan"
    echo "---------------------------------------------------------------------------"
done