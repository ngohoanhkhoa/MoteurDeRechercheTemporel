package MoteurRecherche;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;

import Clustering.Clustering;
import SolrConnection.Document;
import SolrConnection.Solr;

import org.json.simple.JSONObject;

public class MoteurRecherche {
	public String getdata(String request) {

		Solr server = new Solr();
		try {
			TreeSet<Document> reponse = server.getReponse(request);
			ArrayList<Document> documentList = new ArrayList<Document>(reponse);
			Clustering cluster = new Clustering();

			cluster.setMaximumDistance(300000000.0);
			cluster.setMinimumNeighbours(documentList.size() / 5);

			cluster.setDocumentList(documentList);

			ArrayList<ArrayList<Document>> result = cluster.getDBSCAN();

			for (ArrayList<Document> documentGroup : result) {
				System.out.println("Cluster: ");
				for (Document document : documentGroup) {
					System.out.println(document.getTitle());
					System.out.println(document.getScore() * 100);
				}
			}

			return exportJSON(result);

			// System.out.println("End");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";

	}

	public String exportJSON(ArrayList<ArrayList<Document>> resultDocument) {
		JSONObject allCluster = new JSONObject();
		ArrayList<JSONObject> allClusterArray = new ArrayList<JSONObject>();
		Integer groupName = 1;
		for (ArrayList<Document> documentGroup : resultDocument) {
			JSONObject cluster = new JSONObject();
			ArrayList<JSONObject> clusterArray = new ArrayList<JSONObject>();
			for (Document document : documentGroup) {
				JSONObject member = new JSONObject();
				member.put("name", document.getTitle());
				member.put("size", document.getScore());
				clusterArray.add(member);
			}

			cluster.put("name", "Group " + groupName);
			cluster.put("children", clusterArray);
			allClusterArray.add(cluster);
			groupName++;
		}
		allCluster.put("name", "Document");
		allCluster.put("children", allClusterArray);

		return allCluster.toString();

	}

}
