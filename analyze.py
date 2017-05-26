from functools import reduce

def get_counties():
    file = open('Cluster_Result/1970_euclidean_CURE_RESULT.csv')
    try:
        file.readline()
        counties = [int(line.rstrip().split(',')[1]) for line in file]
    finally:
        file.close()
    return sorted(counties)


def get_county_clusterings(algorithm, distance_type):
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
    print(distance_type, algorithm)
    county_dict = get_county_clusterings(distance_type, algorithm)
    cluster_changes = [0, 0, 0, 0, 0]
    downgraded_counties = 0
    upgraded_counties = 0
    unchanged_counties = 0
    total_counties = len(county_dict)
    for county in county_dict:
        set_length = len(set(county_dict[county]))
        cluster_changes[set_length - 1] += 1 # keeps track of how many different clusters each county was matched with over 5 decades
        
        if county_dict[county][0] < county_dict[county][4]:
            upgraded_counties += 1
        elif county_dict[county][0] > county_dict[county][4]:
            downgraded_counties += 1
        else:
            unchanged_counties += 1
    print("% of counties that never change cluster: {}%".format(100*cluster_changes[0]/total_counties))
    print("% of counties that appear in 2 clusters: {}%".format(100*cluster_changes[1]/total_counties))
    print("% of counties that appear in 3 clusters: {}%".format(100*cluster_changes[2]/total_counties))
    print("% of counties that appear in 4 clusters: {}%".format(100*cluster_changes[3]/total_counties))
    print("% of counties that appear in 5 clusters: {}%".format(100*cluster_changes[4]/total_counties))
    print("% of counties that are in the same cluster in 2011 as in 1970: {}%".format(100*unchanged_counties/total_counties))
    print("% of counties that are in a 'lower cluster' in 2011 than 1970 (possibly indicates lower relative status to the rest of the U.S.): {}%".format(100*downgraded_counties/total_counties))
    print("% of counties that are in a 'higher cluster' in 2011 than 1970: {}%".format(100*upgraded_counties/total_counties))
    print("")

def main():
#    distance_types = ['euclidean', 'manhattan']
#    algorithms = ['CURE', 'KMEANS']
    analyze_algorithm("euclidean", "CURE")
    analyze_algorithm("euclidean", "KMEANS")
    analyze_algorithm("manhattan", "CURE")
    analyze_algorithm("manhattan", "KMEANS")                
                    
if __name__ == '__main__':
    main()