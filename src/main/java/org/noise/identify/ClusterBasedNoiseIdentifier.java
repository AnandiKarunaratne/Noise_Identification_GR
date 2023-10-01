package org.noise.identify;

import java.util.*;

public class ClusterBasedNoiseIdentifier extends NoiseIdentifier {
    ClusterBasedNoiseIdentifier(List<String> traces) {
        super(traces);
    }

    @Override
    List<String> identifyNoise() {
        // clustering algorithm: k-means
        int numOfClusters = 5;
        int maxIterations = 100;

        // cluster
        List<List<String>> unfilteredClusters = cluster(traces, numOfClusters, maxIterations);

        // filter the clusters removing empty clusters
        List<List<String>> filteredClusters = new ArrayList<>();
        for (List<String> cluster : unfilteredClusters) {
            if (!cluster.isEmpty()) {
                filteredClusters.add(cluster);
            }
        }
        for (int i = 0; i < filteredClusters.size(); i++) {
            System.out.println("Cluster " + (i + 1) + ": " + filteredClusters.get(i));
        }

        // set noise threshold. clusters with fewer than a certain number of data points as noise
        int noiseThreshold = (int) (traces.size() * 0.1);

        // label those clusters as noise
        Set<String> noise = new HashSet<>();
        for (List<String> cluster : filteredClusters) {
            if(cluster.size() < noiseThreshold) {
                for (String noiseTrace : cluster) {
                    noise.add(noiseTrace);
                }
            }
        }

        return new ArrayList<>(noise);

        // iterate maybe?
    }

    List<List<String>> cluster(List<String> traces, int numOfClusters, int maxIterations) {
        List<List<String>> clusters = initializeClusters(traces, numOfClusters);

        // perform K-Means clustering
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // assign data points to clusters
            Map<String, Integer> assignments = assignToClusters(traces, clusters);

            // update cluster centroids
            clusters = updateClusterCentroids(traces, assignments, numOfClusters);

            // check for convergence (no change in assignments)
            boolean converged = true;
            for (String str : traces) {
                int currentCluster = assignments.get(str);
                int previousCluster = assignments.getOrDefault(str, -1);
                if (currentCluster != previousCluster) {
                    converged = false;
                    break;
                }
            }

            if (converged) {
                break; // convergence reached
            }
        }

        return clusters;
    }

    double calculateStringDistance(String string1, String string2) {
        return StringDistanceCalculator.calculateLevenshteinDistance(string1, string2);
    }

    private List<List<String>> updateClusterCentroids(List<String> strings, Map<String, Integer> assignments, int k) {
        List<List<String>> newClusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            newClusters.add(new ArrayList<>());
        }

        for (String str : strings) {
            int clusterIndex = assignments.get(str);
            newClusters.get(clusterIndex).add(str);
        }

        return newClusters;
    }

    private Map<String, Integer> assignToClusters(List<String> strings, List<List<String>> clusters) {
        Map<String, Integer> assignments = new HashMap<>();
        for (String str : strings) {
            int clusterIndex = -1;
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < clusters.size(); i++) {
                for (String centroid : clusters.get(i)) {
                    double distance = calculateStringDistance(str, centroid);
                    if (distance < minDistance) {
                        minDistance = distance;
                        clusterIndex = i;
                    }
                }
            }
            assignments.put(str, clusterIndex);
        }
        return assignments;
    }

    private List<List<String>> initializeClusters(List<String> strings, int k) {
        List<List<String>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }

        // assign each string to a random cluster
        for (String str : strings) {
            int randomCluster = (int) (Math.random() * k);
            clusters.get(randomCluster).add(str);
        }

        return clusters;
    }


}
