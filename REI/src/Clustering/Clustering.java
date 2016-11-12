package Clustering;

import SolrConnection.*;

import java.util.ArrayList;

public class Clustering {
	
	public Double maximumDistance = 0.0;

	public Integer minimumNeighbours = 0;
	
	public ArrayList<Document> documentList = new ArrayList<Document>();
    
    
	public void setDocumentList(ArrayList<Document> documentList) {
		this.documentList = documentList;
	}

	public void setMaximumDistance(Double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

	public void setMinimumNeighbours(int minimumNeighbours) {
		this.minimumNeighbours = minimumNeighbours;
	}
    
    public ArrayList<ArrayList<Document>> getDBSCAN() {
    	
    	ArrayList<ArrayList<Document>> resultList = new ArrayList<ArrayList<Document>>();
    	ArrayList<String> visitedList = new ArrayList<String>();
    	
    	for(Document document : documentList) {
    		if (!visitedList.contains(document.getId())) {	
    			visitedList.add(document.getId());
    			ArrayList<Document> neighbours1 = getNeighbours(document);
    			if(neighbours1.size() >= minimumNeighbours){
    				Integer neighboursIndex = 0;
    				while(neighbours1.size() > neighboursIndex) {
    					if(!visitedList.contains(neighbours1.get(neighboursIndex).getId())) {
    						visitedList.add(neighbours1.get(neighboursIndex).getId());
    						ArrayList<Document> neighbours2 = getNeighbours(neighbours1.get(neighboursIndex));
    						if(neighbours2.size() >= minimumNeighbours){
    							for(Document documentNeighbours2 : neighbours2) {
    								if(!neighbours1.contains(documentNeighbours2)) {
    									neighbours1.add(documentNeighbours2);
    								}
    							}
    						}
    					}
    					neighboursIndex++;
    				}

    			}
    			resultList.add(neighbours1);
    		}
    	} 
		return resultList;
    	
    }
    
    private ArrayList<Document> getNeighbours(Document documentEstimated) {
    	ArrayList<Document> neighbours = new ArrayList<Document>();
    	for(Document document : this.documentList) {
    		Double distance = Math.abs(document.getSortedIndex() - documentEstimated.getSortedIndex());
    		if(distance <=  this.maximumDistance) {
    			neighbours.add(document);
    		}
    	}
    	return neighbours;
    	
    }
    

    
}