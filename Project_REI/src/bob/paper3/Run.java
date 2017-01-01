package bob.paper3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;

import bob.paper3.models.Cluster;
import bob.paper3.models.Document;
import bob.paper3.utilities.ClusterUtility;
import bob.paper3.utilities.DocUtility;
import bob.paper3.utilities.Utility;

public class Run {
	public static void main(String[] args) throws SolrServerException, IOException {
		Solr server = new Solr();
		TreeSet<Document> reponse = server.getReponse("paris+store+lyon+obama");
		ArrayList<Document> documentList = new ArrayList<Document>(reponse);

		// Collections.sort(dates);
		// Collections.reverse(dates);

		long meanDistance = DocUtility.getInstance().getNormalDistance(documentList);
		// clustering
		ArrayList<Cluster> clusters = ClusterUtility.clusterDocs(documentList, meanDistance);

		//
		// reCalculate the meanDistance
		meanDistance = 0;
		for (Cluster cluster : clusters) {
			meanDistance += DocUtility.getInstance().getNormalDistance(cluster.getChildren());
		}
		meanDistance /= clusters.size();
		clusters = ClusterUtility.clusterDocs(documentList, meanDistance);

		//
		// Get Features and Remove weak words
		ArrayList<HashMap<String, Integer>> listFeature = new ArrayList<>();
		for (Cluster cluster : clusters) {
			HashMap<String, Integer> feature = DocUtility.getInstance().getDocumentFrequency(cluster.getChildren(),
					DocUtility.getInstance().TOKENIZER_NO_STOP_WORDS);
			feature = DocUtility.getInstance().removeWeakWords(feature);
			listFeature.add(feature);
		}

		//
		// Merge Features
		HashMap<String, Integer> features = new HashMap<>();
		for (HashMap<String, Integer> map : listFeature) {
			for (Entry<String, Integer> entry : map.entrySet()) {
				String word = entry.getKey();
				int value = entry.getValue();
				features.put(word, features.containsKey(word) ? features.get(word) + value : value);
			}
		}

		//
		// Get features' histogram of all documents
		HashMap<String, Integer> featuresSequences = DocUtility.getInstance().getDocumentFrequencyByFeatures(
				documentList, features, DocUtility.getInstance().TOKENIZER_NO_STOP_WORDS);

		//
		// Apply automate two threshold
		ArrayList<Double> thresholds = new ArrayList<>();
		thresholds.add(0.0);
		thresholds.add(2.0);
		double maxInterval = 0.8;
		double upperThreshold = 0.0, lowerThreshold = 0.0;
		int upperCounter = 0, lowerCounter = 0;

		while (thresholds.get(thresholds.size() - 1) - thresholds.get(thresholds.size() - 2) > maxInterval) {
			double threshold = thresholds.get(thresholds.size() - 1);
			upperThreshold = 0.0;
			lowerThreshold = 0.0;
			upperCounter = 0;
			lowerCounter = 0;

			for (Entry<String, Integer> entry : featuresSequences.entrySet()) {
				String word = entry.getKey();
				int value = entry.getValue();

				if (value >= threshold) {
					upperThreshold += value;
					upperCounter++;
				} else {
					lowerThreshold += value;
					lowerCounter++;
				}
			}

			threshold = (upperThreshold / upperCounter + lowerThreshold / lowerCounter) / 2;
			thresholds.add(threshold);
		}

		/*
		 * TEST
		 */
		//
		// Show the results
		int count = 0;
		for (int i = 0; i < clusters.size(); i++) {
			count += clusters.get(i).getChildren().size();
			System.out.println(i + "# " + clusters.get(i).getChildren().size() + " # From "
					+ clusters.get(i).getChildren().get(0).getDate() + " -> To "
					+ clusters.get(i).getChildren().get(clusters.get(i).getChildren().size() - 1).getDate() + " # "
					+ DocUtility.getInstance().getDistance(clusters.get(i).getChildren().get(0),
							clusters.get(i).getChildren().get(clusters.get(i).getChildren().size() - 1)) / 1000000
					+ " days ");
		}
		System.out.println(count); // the rest accepted
		System.out.println(meanDistance);

		// first feature
		HashMap<String, Integer> hits = listFeature.get(0);
		double meanHits = Utility.getNormalDistance(hits);
		System.out.println("\n ++ First feature mean => " + meanHits);
		for (Entry<String, Integer> entry : hits.entrySet()) {
			String word = entry.getKey();
			Integer value = entry.getValue();
			System.out.println(word + " = " + value + " docs");
		}

		// Merged Features
		System.out.println("\n\n\n ++++ All Features +++");
		for (Entry<String, Integer> entry : features.entrySet()) {
			String word = entry.getKey();
			Integer value = entry.getValue();
			System.out.println(word + " = " + value + " docs");
		}

		// Thresholds
		System.out.println("\n\n\n ++++ All Features Thresholds +++");
		for (double threshold : thresholds) {
			System.out.println(threshold);
		}

		System.out.println("\n\n\n ++++ Features threshold " + thresholds.get(thresholds.size() - 1) + "  +++");

		System.out.println("Done");

	}
}
