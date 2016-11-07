package MoteurRecherche;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;

import Clustering.Clustering;
import SolrConnection.Document;
import SolrConnection.Solr;


public class MoteurRecherche {
	public static void main(String [ ] args) {
		
		Solr server = new Solr();
		try {
			TreeSet<Document> reponse = server.getReponse("obama");
			ArrayList<Document> documentList = new ArrayList<Document>(reponse);
			Clustering cluster = new Clustering();
	    	
	    	cluster.setMaximumDistance(300000000.0);
	    	cluster.setMinimumNeighbours(documentList.size()/2);
	    	
	    	cluster.setDocumentList(documentList);
	    	
	    	ArrayList<ArrayList<Document>> result = cluster.getDBSCAN();
	    
	    	
	    	for(ArrayList<Document> document : result) {
	    		System.out.println("Cluster");
	    		for(Document doc : document) {
	    			System.out.println(doc.getTitle());
	    			System.out.println(doc.getDate());
	    			
	        	}
	    	}
	    	System.out.println("End");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    }
}
