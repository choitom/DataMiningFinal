#!/bin/bash

#compile java source files
javac *.java

for ((i = 1970; i <= 2010; i += 10));
do
	echo "Clustering $i's..."
	java Main $i "euclidean"
	java Main $i "manhattan"
    echo "---------------------------------------------------------------------------"
done