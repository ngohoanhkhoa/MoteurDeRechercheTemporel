package bob.paper3.utilities;

import java.util.ArrayList;

import bob.paper3.models.Cluster;
import bob.paper3.models.Document;

public class ClusterUtility {

	public static ArrayList<Cluster> clusterDocs(ArrayList<Document> documentList, long meanDistance) {
		ArrayList<Cluster> clusters = new ArrayList<>();
		ArrayList<Document> newDocumentList = new ArrayList<>();

		for (int i = 0, c = documentList.size() - 1; i < c; i++) {
			Cluster cluster = new Cluster();
			cluster.getChildren().add(documentList.get(i));
			documentList.get(i).setClusterNumber(clusters.size());

			for (int j = i + 1; j < c + 1; j++) {
				if (DocUtility.getInstance().getDistance(documentList.get(i), documentList.get(j)) > meanDistance
						|| j == c) {
					i = j;
					if (cluster.getChildren().size() > 1) {
						clusters.add(cluster);
					}
					break;
				}
				// if (documentList.get(j) == null) {
				// System.out.println("null");
				// }
				if (cluster.getChildren().size() == 1)
					newDocumentList.add(documentList.get(i));

				documentList.get(j).setClusterNumber(documentList.get(i).getClusterNumber());
				cluster.getChildren().add(documentList.get(j));
				newDocumentList.add(documentList.get(j));
			}
		}

		documentList = newDocumentList;
		return clusters;
	}

}
