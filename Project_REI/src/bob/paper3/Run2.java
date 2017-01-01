package bob.paper3;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;

import bob.paper3.models.Cluster;
import bob.paper3.models.Document;
import bob.paper3.models.Feature;
import bob.paper3.utilities.ClusterUtility;
import bob.paper3.utilities.DocUtility;
import bob.paper3.utilities.Utility;

public class Run2 {
	public static void main(String[] args) throws SolrServerException, IOException {
		Solr server = new Solr();
		TreeSet<Document> reponse = server.getReponse("paris+store+lyon+obama");
		ArrayList<Document> documents = new ArrayList<Document>(reponse);

		long meanDistance = DocUtility.getInstance().getNormalDistance(documents);
		// clustering - time windows
		ArrayList<Cluster> clusters = ClusterUtility.clusterDocs(documents, meanDistance);
		//
		// reCalculate the meanDistance
		meanDistance = 0;
		for (Cluster cluster : clusters) {
			meanDistance += DocUtility.getInstance().getNormalDistance(cluster.getChildren());
		}
		meanDistance /= clusters.size();
		clusters = ClusterUtility.clusterDocs(documents, meanDistance);

		// **** TEST ****
		System.out.println();
		System.out.println("meanDistance " + meanDistance);
		System.out.println("cluster size  " + clusters.size());
		System.out.println();

		// **** / END TEST ****

		//
		// For each doc -> add Features
		// for each cluster add features
		ArrayList<Feature> features = new ArrayList<>();
		HashMap<String, Feature> featuresMap = DocUtility.getInstance().getDocumentFrequencyByBinaryMap(documents,
				clusters.size(), features, DocUtility.getInstance().STEMMER_NO_STOP_WORDS);

		int[][] matrixWindowFeature = new int[features.size()][];
		for (int i = 0, c = features.size(); i < c; i++) {
			int[] subMatrix = new int[clusters.size()];
			for (int j = 0, ct = features.get(i).getWindowTimeBinary().size(); j < ct; j++) {
				subMatrix[features.get(i).getWindowTimeBinary().get(j)] = 1;
			}
			matrixWindowFeature[i] = subMatrix;
		}
		// **** TEST ****
		System.out.println("\n ********** ");
		System.out.println("features size : " + features.size());
		System.out.println();

		// **** / END TEST ****

		//
		// Calculate probability of Features
		// remove non hot feature
		for (int i = 0, c = features.size(); i < c; i++) {
			Feature feature = features.get(i);
			double L = 0.0;
			double pFeature = 0.0;
			// double L = clusters.size();
			// double pFeature = feature.getWindowTimeBinary().size();
			// double L = documents.size();
			// double pFeature = feature.getDocBinary().size();
			// TODO:

			for (int j = 0, ct = clusters.size(); j < ct; j++) {
				L += matrixWindowFeature[i][j];
				double nij = (double) clusters.get(j).getNFeature(feature.getWord());
				double N = clusters.get(j).getChildren().size();
				double Pnij = (N != 0) ? nij / N : 0;

				feature.getProbValuesWindowTime().add(Pnij);
				pFeature += Pnij;
				// if (clusters.get(j).getNFeature(features.get(i).getWord()) !=
				// 0) {
				// System.out.println(ppFeature);
				// }
			}

			feature.setProbValue((double) (pFeature / (double) L));
		}

		ArrayList<Feature> newFeatures = new ArrayList<>();
		for (int i = 0, c = features.size(); i < c; i++) {
			Feature feature = features.get(i);

			boolean isRemoved = true;
			for (int j = 0, ct = feature.getProbValuesWindowTime().size(); j < ct; j++) {
				Double prob = feature.getProbValuesWindowTime().get(j);
				// TODO:
				if (prob - feature.getProbValue() > 0.3 && feature.getProbValue() < 0.3
						&& feature.getProbWindowsContain(clusters.size()) < 0.5) {
					newFeatures.add(feature);
					System.out.println(feature.getWord() + " added " + feature.getWindowTimeBinary().toString() + " ("
							+ prob + " > " + feature.getProbValue());
					isRemoved = false;
					break;
				}
			}
			// if (isRemoved)
			// System.err.println(feature.getWord() + " removed");
		}
		features = newFeatures;
		newFeatures = null;

		// **** TEST ****
		System.out.println("\n ********** ");
		System.out.println("Probabylities of " + features.size() + " features");
		for (int i = 0, c = features.size(); i < c; i++) {
			System.out.print(features.get(i).getProbValue() + " - ");
		}

		System.out.println();
		System.out.println();

		// **** / END TEST ****

		//
		// Calculate Features of each window time (cluster)
		for (int j = 0; j < clusters.size(); j++) {
			Cluster cluster = clusters.get(j);
			for (int i = 0; i < features.size(); i++) {
				Feature feature = features.get(i);
				if (!feature.isBelongTo(j))
					continue;
				cluster.getFeaturesMap().put(i, feature.getWord());
			}
		}

		//
		// Calculate P(Ek)
		ArrayList<Double> PEk = new ArrayList<>();
		for (int i = 0, count = clusters.size(); i < count; i++) {
			Cluster cluster = clusters.get(i);
			ArrayList<Integer> firstVector = new ArrayList<>(), secondVector = new ArrayList<>();

			for (int j = 0, count2 = features.size(); j < count2; j++) {
				Feature feature = features.get(j);
				if (!feature.isBelongTo(i))
					continue;

				firstVector = Utility.intersectVertors(firstVector,
						feature.getWindowCLusterVectorByIndex(clusters.size()));
				secondVector = Utility.unionVertors(secondVector,
						feature.getWindowCLusterVectorByIndex(clusters.size()));
			}
			double first = Utility.getAbsoluteValue(firstVector);
			double second = Utility.getAbsoluteValue(secondVector);

			PEk.add((second != 0.0) ? (double) first / (double) second : 0.0);
		}
		// Way 2
		// double[] pFeaturesVector = new double[features.size()];
		// for (int i = 0, count = features.size(); i < count; i++) {
		// Feature feature = features.get(i);
		// pFeaturesVector[i] = feature.getWindowTimeBinary().size();
		// pFeaturesVector[i] /= clusters.size();
		// }
		// for (int i = 0, count = clusters.size(); i < count; i++) {
		// Cluster cluster = clusters.get(i);
		// double pCluster = 1.0;
		// for (int j = 0, count2 = features.size(); j < count2; j++) {
		// Feature feature = features.get(j);
		//
		// if (!feature.isBelongTo(i))
		// pCluster += 1 - pFeaturesVector[j];
		// else
		// pCluster += pFeaturesVector[j];
		// }
		// PEk.add(pCluster / features.size());
		// }

		//
		// Calculate P(D|Ek)
		ArrayList<Double> PD_Ek = new ArrayList<>();

		double[] D = new double[features.size()];
		double[] M = new double[clusters.size()];

		for (int j = 0, count2 = features.size(); j < count2; j++) {
			Feature feature = features.get(j);
			D[j] = Utility.getAbsoluteValue(feature.getDocsVectorByIndex(documents.size()));
		}

		for (int i = 0, count = clusters.size(); i < count; i++) {
			Cluster cluster = clusters.get(i);
			ArrayList<Integer> MVectors = new ArrayList<>();

			for (int j = 0, count2 = features.size(); j < count2; j++) {
				Feature feature = features.get(j);
				if (!feature.isBelongTo(i))
					continue;
				MVectors = Utility.unionVertors(MVectors, feature.getDocsVectorByIndex(documents.size()));
			}

			double Mi = Utility.getAbsoluteValue(MVectors);
			M[i] = Mi;
			double Dj_M = 1.0;

			for (int j = 0, count2 = features.size(); j < count2; j++) {
				Feature feature = features.get(j);
				if (!feature.isBelongTo(i)) {
					Dj_M += 1 - D[j] / Mi;
				} else {
					Dj_M += D[j] / Mi;
				}
			}
			PD_Ek.add(Dj_M / features.size());
		}

		//
		// Calculate cost(Ek|D)
		double[] cEk_D = new double[clusters.size()];
		for (int i = 0, count = clusters.size(); i < count; i++) {

			cEk_D[i] = Math.log(M[i]) * count - Math.log(PEk.get(i));
			double sum = 0.0;
			for (int j = 0, count2 = features.size(); j < count2; j++) {
				Feature feature = features.get(j);
				if (!feature.isBelongTo(i)) {
					sum += Math.log(M[i] - D[j]);
				} else {
					sum += Math.log(D[j]);
				}
			}
			cEk_D[i] -= sum;
		}

		// mean Cost
		double meanCostPEk_D = 0.0;
		double countCost = 0;
		for (double cost : cEk_D) {
			if (cost > 0.0 && cost != Double.NaN 
					&& cost != Double.NEGATIVE_INFINITY
					&& cost != Double.POSITIVE_INFINITY) {
				meanCostPEk_D += cost;
				countCost++;
			}
		}
		meanCostPEk_D /= cEk_D.length;
		meanCostPEk_D = Math.abs(meanCostPEk_D);

		int countTrueEvent = 0;
		for (int i = 0; i < cEk_D.length; i++) {
			if (Math.abs(cEk_D[i]) > Math.abs(meanCostPEk_D)) {
				countTrueEvent++;
			}
		}

		// //
		// // Calculate P(Ek|D)
		// double[] pEk_D = new double[clusters.size()];
		// for (int i = 0, count = clusters.size(); i < count; i++) {
		// double p = PD_Ek.get(i) * PEk.get(i)
		// / ((double) clusters.get(i).getChildren().size() / (double)
		// documents.size());
		// pEk_D[i] = p;
		// }

		// **** TEST ****

		for (int i = 0; i < clusters.size(); i++) {
			System.out.print(clusters.get(i).getChildren().size() + " - ");
		}

		System.out.println("\n ********** ");
		System.out.println();
		System.out.println();

		// **** / END TEST ****

		// **** TEST ****
		System.out.println("\n ********** ");
		System.out.println();
		System.out.println();

		// **** / END TEST ****
	}
}
