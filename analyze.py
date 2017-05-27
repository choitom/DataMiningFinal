def get_counties():
    """returns sorted list of integer county IDs"""
    file = open('Cluster_Result/1970_euclidean_CURE_RESULT.csv')
    try:
        file.readline()
        counties = [int(line.rstrip().split(',')[1]) for line in file]
    finally:
        file.close()
    return sorted(counties)


def get_county_clusterings(algorithm, distance_type):
    """
    takes in two strings and returns a dict mapping str county IDs to their own list
    of cluster assignments
    """
    county_clusters= {str(counties):[] for counties in get_counties()}
    years = ['1970', '1980', '1990', '2000', '2010']
    for year in years:
        file = open('Cluster_Result/{}_{}_{}_RESULT.csv'.format(year, algorithm, distance_type))
        try:
            file.readline()
            for line in file:
                line_list = line.rstrip().split(',')
                cluster = line_list[0]
                county = line_list[1]
                if county in county_clusters:
                    county_clusters[county].append(cluster)
        finally:
            file.close()
    # assures that data exists for all 5 decades
    return {key: county_clusters[key] for key in county_clusters if len(county_clusters[key]) == 5}


def analyze_algorithm(distance_type, algorithm):
    """takes two strings and prints an analysis of an algorithm"""
    print(distance_type, algorithm)
    county_dict = get_county_clusterings(distance_type, algorithm)
    cluster_changes = [0, 0, 0, 0, 0]
    cluster_counts = [[0, 0, 0, 0, 0, 0] for i in range(5)]
    downgraded_counties = 0
    upgraded_counties = 0
    unchanged_counties = 0
    unchanged_county_clusters = [0, 0, 0, 0, 0, 0]
    total_counties = len(county_dict)
    for county in county_dict:
        set_length = len(set(county_dict[county]))
        cluster_changes[set_length - 1] += 1 # keeps track of how many different clusters each county was matched with over 5 decades
        for i in range(5):
            cluster_counts[i][int(county_dict[county][i])] += 1 
        if county_dict[county][0] < county_dict[county][4]:
            upgraded_counties += 1
        elif county_dict[county][0] > county_dict[county][4]:
            downgraded_counties += 1
        else:
            unchanged_counties += 1
            unchanged_county_clusters[int(county_dict[county][0])] += 1
    
    print("cluster sizes 0-5 1970",cluster_counts[0])
    print("cluster sizes 0-5 1980",cluster_counts[1])
    print("cluster sizes 0-5 1990",cluster_counts[2])
    print("cluster sizes 0-5 2000",cluster_counts[3])
    print("clusters sizes 0-5 2011",cluster_counts[4])
    for i in range(6):
        print("cluster {} size changed by {} counties".format(i, cluster_counts[4][i] - cluster_counts[0][i]))
    print("{}% of counties never change cluster".format(str(100*cluster_changes[0]/total_counties)[:5]))
    print("{}% of counties appear in 2 clusters".format(str(100*cluster_changes[1]/total_counties)[:5]))
    print("{}% of counties appear in 3 clusters".format(str(100*cluster_changes[2]/total_counties)[:5]))
    print("{}% of counties appear in 4 clusters".format(str(100*cluster_changes[3]/total_counties)[:5]))
    print("{}% of counties appear in 5 clusters".format(str(100*cluster_changes[4]/total_counties)[:5]))
    print("{}% of counties are in the same cluster in 2011 as in 1970".format(str(100*unchanged_counties/total_counties)[:5]))
    for i in range(6):
        print("{}% of counties in cluster {} in 1970 are unchanged over all 5 years".format(str(100*unchanged_county_clusters[i]/cluster_counts[0][i])[:5], i))
    print("% of counties that are in a 'lower cluster' in 2011 than 1970 (possibly indicates lower relative status to the rest of the U.S.): {}%".format(str(100*downgraded_counties/total_counties)[:5]))
    print("% of counties that are in a 'higher cluster' in 2011 than 1970: {}%".format(str(100*upgraded_counties/total_counties)[:5]))
    print("")

def main():
    analyze_algorithm("euclidean", "CURE")
    analyze_algorithm("euclidean", "KMEANS")
    analyze_algorithm("manhattan", "CURE")
    analyze_algorithm("manhattan", "KMEANS")                
                    
if __name__ == '__main__':
    main()