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
		/*
		ArrayList<Document> documentList1 = new ArrayList<Document>();
    	
    	documentList1.add(new Document(1.0, "1", "1", "1", "1", 12.0));
    	documentList1.add(new Document(2.0, "2", "2", "2", "2", 12.0));
    	documentList1.add(new Document(3.0, "3", "3", "3", "3", 12.0));
    	documentList1.add(new Document(4.0, "4", "4", "4", "4", 12.0));

    	
    	documentList1.add(new Document(10.0, "5", "5", "5", "5", 12.0));
    	documentList1.add(new Document(11.0, "6", "6", "6", "6", 12.0));
    	documentList1.add(new Document(12.0, "7", "7", "7", "7", 12.0));
    	documentList1.add(new Document(12.0, "8", "8", "7", "7", 12.0));
    	
    	documentList1.add(new Document(30.0, "9", "9", "5", "5", 12.0));
    	documentList1.add(new Document(31.0, "10", "10", "6", "6", 12.0));
    	documentList1.add(new Document(32.0, "11", "11", "7", "7", 12.0));
    	documentList1.add(new Document(34.0, "12", "12", "7", "7", 12.0));
		
		
		Clustering cluster1 = new Clustering();
    	
		
		Double periodEvent1 = documentList1.get(0).getSortedIndex() - documentList1.get(documentList1.size()-1).getSortedIndex(); 
		cluster1.setMaximumDistance(Math.abs(periodEvent1/10));

    	cluster1.setDocumentList(documentList1);

    	
    	ArrayList<ArrayList<Document>> result1 = cluster1.getDBSCAN();
    	
    	
    	for(ArrayList<Document> documentGroup: result1) {
    		System.out.print("Cluster: ");
    		for(Document document: documentGroup) {
    			System.out.print(document.getTitle() + " - ");
    		}
    		System.out.println();
    	}
    
    	exportJSON(result1);
		*/
		
		
		try {
			Solr server = new Solr();
			TreeSet<Document> reponse = server.getReponse("usa");
			ArrayList<Document> documentList = new ArrayList<Document>(reponse);
			Clustering cluster = new Clustering();
			
	    	Double periodEvent = documentList.get(0).getSortedIndex() - documentList.get(documentList.size()-1).getSortedIndex(); 

	    	//System.out.println(Math.abs(periodEvent/2));
	    	cluster.setMaximumDistance(Math.abs(periodEvent/2));
	    	
	    	cluster.setDocumentList(documentList);

	    	ArrayList<ArrayList<Document>> result = cluster.getDBSCAN();
	    	
	    	for(ArrayList<Document> documentGroup: result) {
	    		System.out.println("Cluster: ");
	    		for(Document document: documentGroup) {
	    			//System.out.println(document.getId());
	    			System.out.println(document.getTitle());
	    			//System.out.println(document.getScore()*100);
	    			//System.out.println(document.getDate());
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
