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
	public static void main(String [ ] args) {
		
		Solr server = new Solr();
		try {
			TreeSet<Document> reponse = server.getReponse("obama");
			ArrayList<Document> documentList = new ArrayList<Document>(reponse);
			Clustering cluster = new Clustering();
	    	
	    	cluster.setMaximumDistance(300000000.0);
	    	cluster.setMinimumNeighbours(documentList.size()/5);
	    	
	    	cluster.setDocumentList(documentList);
	    	
	    	ArrayList<ArrayList<Document>> result = cluster.getDBSCAN();
	    
	    	exportJSON(result);
	    	
	    	System.out.println("End");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    }
	
	public static void exportJSON(ArrayList<ArrayList<Document>> resultDocument) {
		JSONObject allCluster = new JSONObject();
		ArrayList<JSONObject> allClusterArray = new ArrayList<JSONObject>();
		for(ArrayList<Document> documentGroup: resultDocument) {
			JSONObject cluster = new JSONObject();
			ArrayList<JSONObject> clusterArray = new ArrayList<JSONObject>();
			for(Document document: documentGroup) {
				JSONObject member = new JSONObject();
				member.put("name", document.getId());
				member.put("size", document.getSortedIndex());
				clusterArray.add(member);
			}
			cluster.put("name", "Doc");
			cluster.put("children", clusterArray);
			allClusterArray.add(cluster);
		}
		allCluster.put("name", "Doc");
		allCluster.put("children", allClusterArray);
		
		System.out.print(allCluster);
	}

}
