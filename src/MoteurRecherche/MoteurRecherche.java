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
		
		/*ArrayList<Document> documentList1 = new ArrayList<Document>();
		
		Document d1 = new Document(1.0, "1", "1", "1", "1", 12.0);
    	Document d2 = new Document(2.0, "2", "2", "2", "2", 12.0);
    	Document d3 = new Document(3.0, "3", "3", "3", "3", 12.0);
    	Document d4 = new Document(40.0, "4", "4", "4", "4", 12.0);
    	Document d5 = new Document(41.0, "5", "5", "5", "5", 12.0);
    	Document d6 = new Document(42.0, "6", "6", "6", "6", 12.0);
    	Document d7 = new Document(43.0, "7", "7", "7", "7", 12.0);
    	
    	documentList1.add(d1);
    	documentList1.add(d2);
    	documentList1.add(d3);
    	documentList1.add(d4);
    	documentList1.add(d5);
    	documentList1.add(d6);
    	documentList1.add(d7);
		
		
		Clustering cluster1 = new Clustering();
    	
    	cluster1.setMaximumDistance(300000000.0);
    	cluster1.setMinimumNeighbours(documentList1.size()/5);
    	
    	cluster1.setDocumentList(documentList1);

    	
    	ArrayList<ArrayList<Document>> result1 = cluster1.getDBSCAN();
    	
    	
    	for(ArrayList<Document> documentGroup: result1) {
    		System.out.println("Cluster: ");
    		for(Document document: documentGroup) {
    			System.out.println(document.getTitle());
    			System.out.println(document.getScore()*100);
    		}
    	}
    
    	exportJSON(result1);*/
		
		try {
			Solr server = new Solr();
			TreeSet<Document> reponse = server.getReponse("obama");
			ArrayList<Document> documentList = new ArrayList<Document>(reponse);
			Clustering cluster = new Clustering();
	    	
	    	cluster.setMaximumDistance(300000000.0);
	    	cluster.setMinimumNeighbours(documentList.size()/5);
	    	
	    	cluster.setDocumentList(documentList);

	    	
	    	ArrayList<ArrayList<Document>> result = cluster.getDBSCAN();
	    	
	    	
	    	for(ArrayList<Document> documentGroup: result) {
	    		System.out.println("Cluster: ");
	    		for(Document document: documentGroup) {
	    			System.out.println(document.getTitle());
	    			System.out.println(document.getScore()*100);
	    		}
	    	}
	    
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
		Integer groupName = 1;
		for(ArrayList<Document> documentGroup: resultDocument) {
			JSONObject cluster = new JSONObject();
			ArrayList<JSONObject> clusterArray = new ArrayList<JSONObject>();
			for(Document document: documentGroup) {
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
		
		System.out.println(allCluster);
		
	}

}
