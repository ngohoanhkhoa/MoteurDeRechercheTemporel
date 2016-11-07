package SolrConnection;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

public class Solr {
	
	public String urlString = "http://localhost:8983/solr/REI";
	
	
	public TreeSet<Document> getReponse(String queryString) throws SolrServerException, IOException {
	
	SolrClient solr = new HttpSolrClient.Builder(this.urlString).build();
	
	SolrQuery query = new SolrQuery();
    query.setQuery(queryString);
    query.setStart(0);    
    
	QueryResponse response = solr.query(query);
	SolrDocumentList list = response.getResults();
	
	TreeSet<Document> result = new TreeSet<Document>(new SortedClass());
	for (SolrDocument doc : list) {		
		
		Double index = Double.parseDouble(doc.get("dct").toString().replaceAll("[^0-9.]", ""));
		
		String id = doc.get("id").toString();
		String date = doc.get("dct").toString().substring(1, doc.get("dct").toString().length()-1);
		String title = doc.get("title").toString().substring(1, doc.get("title").toString().length()-1);
		String url =  doc.get("url").toString().substring(1, doc.get("url").toString().length()-1);
		
		Document document = new Document(index, id, title, date, url);
		result.add(document);
	}
	
	return result;
	
  }
  
}

class SortedClass implements Comparator<Document>{


	@Override
	public int compare(Document o1, Document o2) {
		if(o1.getSortedIndex() > o2.getSortedIndex()){
            return 1;
        } else {
            return -1;
        }
	}
}