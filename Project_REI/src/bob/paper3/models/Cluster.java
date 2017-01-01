package bob.paper3.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Cluster implements Serializable {

	ArrayList<Document> children = new ArrayList<>();
	HashMap<Integer, String> featuresMap = new HashMap<>();

	public Cluster(ArrayList<Document> children) {
		super();
		this.children = children;
	}

	public Cluster() {
		super();
	}

	public ArrayList<Document> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Document> children) {
		this.children = children;
	}

	public int getSize() {
		return children.size();
	}

	public HashMap<Integer, String> getFeaturesMap() {
		return featuresMap;
	}

	public void setFeaturesMap(HashMap<Integer, String> featuresMap) {
		this.featuresMap = featuresMap;
	}

	public boolean isBelongToFeature(int indexFeature) {
		return featuresMap.containsKey(indexFeature) ? true : false;
	}

	public double getNFeature(String word) {
		double count = 0;
		for (Document document : children) {
			if (document.getFeatures().containsKey(word))
				++count;
		}
		return count;
	}

}
