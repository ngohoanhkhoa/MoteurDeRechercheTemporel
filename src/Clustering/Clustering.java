package Clustering;

import SolrConnection.*;

import java.util.ArrayList;

public class Clustering {
	
	private Double maximumDistance = 0.0;

	private Integer minimumNeighbours = 3;
	
	private ArrayList<Document> documentList = new ArrayList<Document>();
    
    
	public void setDocumentList(ArrayList<Document> documentList) {
		this.documentList = documentList;
		
		Integer sumNumberNeighbour = 0;
		for(Document document: documentList) {
			Integer neighbourSize = this.getNeighboursSize(document);
			sumNumberNeighbour = sumNumberNeighbour + neighbourSize;
		}
		this.setMinimumNeighbours((Integer) sumNumberNeighbour/documentList.size());
		
	}

	public void setMaximumDistance(Double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

	public void setMinimumNeighbours(int minimumNeighbours) {
		if(minimumNeighbours < 3) {
			this.minimumNeighbours = 3;
		} else {
			this.minimumNeighbours = minimumNeighbours;
		}
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
        			resultList.add(neighbours1);
    			}
    			
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
    
    private Integer getNeighboursSize(Document documentEstimated) {
    	Integer neighbourSize = 0;
    	for(Document document : this.documentList) {
    		Double distance = Math.abs(document.getSortedIndex() - documentEstimated.getSortedIndex());
    		if(distance <=  this.maximumDistance) {
    			neighbourSize++;
    		}
    	}
    	return neighbourSize;
    }
    

    
}



/*public ArrayList<ArrayList<Document>> getDBSCAN() {
	
	ArrayList<ArrayList<Document>> resultList = new ArrayList<ArrayList<Document>>();
	ArrayList<String> visitedList = new ArrayList<String>();
	
	for(Document document : documentList) {
		System.out.println("Document: " + document.getId().substring(0, 2));
		if (!visitedList.contains(document.getId())) {
			System.out.println("-" + document.getId().substring(0, 2)+ " is not visited");
			visitedList.add(document.getId());
			
			System.out.print("-Document visited: ");
			for(String vstring: visitedList) {
				System.out.print(vstring.substring(0,2) + " ");
			}
			System.out.println(" ");
			
			ArrayList<Document> neighbours1 = getNeighbours(document);
			
			System.out.print("-Document Neighbours1 of " + document.getId().substring(0, 2) + ": ");
			for(Document nei1: neighbours1) {
				System.out.print(nei1.getId().substring(0,2) + " ");
			}
			System.out.println(" ");
			System.out.println("-Neigh1 Size = " + neighbours1.size());
			
			if(neighbours1.size() >= minimumNeighbours){
				Integer neighboursIndex = 0;
				while(neighbours1.size() > neighboursIndex) {
					System.out.println("--Document Neighbour: " + neighbours1.get(neighboursIndex).getId().subSequence(0, 2));
					if(!visitedList.contains(neighbours1.get(neighboursIndex).getId())) {
						
						System.out.println("--" + neighbours1.get(neighboursIndex).getId().substring(0, 2)+ " is not visited");
						
						visitedList.add(neighbours1.get(neighboursIndex).getId());
						
						System.out.print("--Document visited after neibourgh: ");
		    			for(String vstring: visitedList) {
		    				System.out.print(vstring.substring(0,2) + " ");
		    			}
		    			System.out.println(" ");
						
						ArrayList<Document> neighbours2 = getNeighbours(neighbours1.get(neighboursIndex));
						
						System.out.print("--Document Neighbour2 of " + neighbours1.get(neighboursIndex).getId().substring(0, 2) + ": ");
		    			for(Document nei2: neighbours2) {
		    				System.out.print(nei2.getId().substring(0,2) + " ");
		    			}
		    			System.out.println(" ");
		    			System.out.println("--Neighbour2 Size = " + neighbours1.size());
						
						
						if(neighbours2.size() >= minimumNeighbours){
							for(Document documentNeighbours2 : neighbours2) {
								System.out.println("---Document Neighbour2: " + documentNeighbours2.getId().subSequence(0, 2));
								if(!neighbours1.contains(documentNeighbours2)) {
									neighbours1.add(documentNeighbours2);
								}
							
								
							}
							System.out.print("---Document Neighbour1 after add 2 " + ": ");
    		    			for(Document nei11: neighbours1) {
    		    				System.out.print(nei11.getId().substring(0,2) + " ");
    		    			}
    		    			System.out.println(" ");
						}
					}
					neighboursIndex++;
				}
				
				System.out.print("---Document Neighbour1 after all " + ": ");
    			for(Document nei112: neighbours1) {
    				System.out.print(nei112.getId().substring(0,2) + " ");
    			}
    			System.out.println(" ");
    			
    			
    			resultList.add(neighbours1);
			}
			
		}
	} 
	
	for(ArrayList<Document> documentGroup: resultList) {
		System.out.println("Cluster: ");
		for(Document document: documentGroup) {
			System.out.println(document.getId().substring(0,2));
			//System.out.println(document.getTitle());
			//System.out.println(document.getScore()*100);
			//System.out.println(document.getDate());
		}
	}
	
	
	return resultList;
	
}*/