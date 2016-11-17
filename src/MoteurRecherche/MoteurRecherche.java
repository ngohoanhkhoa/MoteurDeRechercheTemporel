package MoteurRecherche;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;

import Clustering.Clustering;
import SolrConnection.Document;
import SolrConnection.Solr;

import org.json.simple.JSONObject;


public class MoteurRecherche {
	public static void main(String [ ] args) {
		
		try {
			Solr server = new Solr();
			TreeSet<Document> reponse = server.getReponse("obama");
			ArrayList<Document> documentList = new ArrayList<Document>(reponse);
			Clustering cluster = new Clustering();
			
	    	Double periodEvent = Math.abs(documentList.get(0).getSortedIndex() - documentList.get(documentList.size()-1).getSortedIndex()); 
	    	
	    	Double maxDistance = periodEvent/4;
	    	
	    	
	    	System.out.println("Document Size: "+ documentList.size());
	    	System.out.println("PeriodEvent: "+ periodEvent);
	    	System.out.println("MaxDistance: "+ maxDistance);
	    	

	    	cluster.setMaximumDistance(maxDistance);
	    	cluster.setDocumentList(documentList);

	    	ArrayList<ArrayList<Document>> result = cluster.getDBSCAN();
	    	
	    	
	    	for(ArrayList<Document> documentGroup: result) {
	    		System.out.println("Cluster: ");
	    		for(Document document: documentGroup) {
	    			//System.out.println(document.getId());
	    			System.out.println(document.getTitle());
	    			//System.out.println(document.getScore());
	    			System.out.println(document.getDate());
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
	
	@SuppressWarnings("unchecked")
	public static void exportJSON(ArrayList<ArrayList<Document>> resultDocument) {
		JSONObject allCluster = new JSONObject();
		ArrayList<JSONObject> allClusterArray = new ArrayList<JSONObject>();
		Integer groupName = 0;
		for(ArrayList<Document> documentGroup: resultDocument) {
			JSONObject cluster = new JSONObject();
			ArrayList<JSONObject> clusterArray = new ArrayList<JSONObject>();
			for(Document document: documentGroup) {
				JSONObject member = new JSONObject();
				
				JSONObject memberDocument = new  JSONObject();
				ArrayList<JSONObject> memberDocumentArray = new ArrayList<JSONObject>();
				
				memberDocument.put("size", document.getScore());
				memberDocument.put("name", document.getTitle());
				
				member.put("name", document.getTitle().substring(0, document.getTitle().length()/5) + "...");
				member.put("url", document.getUrl());
				
				memberDocumentArray.add(memberDocument);
				member.put("children", memberDocumentArray);
				
				clusterArray.add(member);
			}
			
			String startTime = documentGroup.get(0).getDate();
			String endTime = documentGroup.get(documentGroup.size()-1).getDate();
			
			String startTimeClusterName = startTime.substring(6,8) + "/" + startTime.substring(4,6) + "/"
					+ startTime.substring(0,4);
			
			String endTimeClusterName = endTime.substring(6,8) + "/" + endTime.substring(4,6) + "/"
					+ endTime.substring(0,4);
			
			cluster.put("name", startTimeClusterName + "-"
			+ endTimeClusterName + ": " + documentGroup.size() + " documents");
			cluster.put("children", clusterArray);
			allClusterArray.add(cluster);
			groupName++;
		}
		allCluster.put("name", "Document");
		allCluster.put("children", allClusterArray);
		
		System.out.println(allCluster);
		
	}

}


/*ArrayList<Document> documentList1 = new ArrayList<Document>();

		documentList1.add(new Document(1.0, "1", "1", "20150607T094603Z", "1", 12.0));
		documentList1.add(new Document(2.0, "2", "2", "20150707T094603Z", "2", 12.0));
		documentList1.add(new Document(3.0, "3", "3", "20150807T094603Z", "3", 12.0));
		documentList1.add(new Document(4.0, "4", "4", "20150907T094603Z", "4", 12.0));


		documentList1.add(new Document(10.0, "5", "5", "20160607T094603Z", "5", 12.0));
		documentList1.add(new Document(11.0, "6", "6", "20160707T094603Z", "6", 12.0));
		documentList1.add(new Document(12.0, "7", "7", "20160807T094603Z", "7", 12.0));
		documentList1.add(new Document(12.0, "8", "8", "20160907T094603Z", "8", 12.0));

		documentList1.add(new Document(30.0, "9", "9", "20170607T094603Z", "9", 12.0));
		documentList1.add(new Document(31.0, "10", "10", "20170707T094603Z", "10", 12.0));
		documentList1.add(new Document(32.0, "11", "11", "20170807T094603Z", "11", 12.0));
		documentList1.add(new Document(34.0, "12", "12", "20170907T094603Z", "12", 12.0));
		documentList1.add(new Document(36.0, "13", "13", "20171007T094603Z", "13", 12.0));


		Clustering cluster1 = new Clustering();


		Double periodEvent1 = documentList1.get(0).getSortedIndex() - documentList1.get(documentList1.size()-1).getSortedIndex(); 
		Double maxDistance1 = Math.abs(periodEvent1/10);
		cluster1.setMaximumDistance(maxDistance1);
		System.out.println(maxDistance1);

		cluster1.setDocumentList(documentList1);


		ArrayList<ArrayList<Document>> result1 = cluster1.getDBSCAN();


		for(ArrayList<Document> documentGroup: result1) {
			System.out.print("Cluster: ");
			for(Document document: documentGroup) {
				System.out.print(document.getId() + " - ");
			}
			System.out.println();
		}
		
		exportJSON(result1);*/


/*
for(ArrayList<Document> documentGroup: result) {
	System.out.println("Cluster: ");
	for(Document document: documentGroup) {
		System.out.println(document.getId());
		System.out.println(document.getTitle());
		System.out.println(document.getScore());
		System.out.println(document.getDate());
	}
}
*/

/*
if(periodEvent > 10000000000.0) {
	//periodEvent is about 1 year, maxDistance is about 3 months;
	maxDistance =  periodEvent/4;
} else if(periodEvent > 600000000.0) {
	//periodEvent is about 6 months, maxDistance is about 2 months;
	maxDistance =  periodEvent/3;
} else if(periodEvent > 30000000.0) {
	//periodEvent is about 1 month, maxDistance is about 1 week;
	maxDistance =  periodEvent/4;
} else {
	maxDistance =  periodEvent/3;
}
*/
