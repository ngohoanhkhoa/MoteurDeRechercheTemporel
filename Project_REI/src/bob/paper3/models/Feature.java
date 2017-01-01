package bob.paper3.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Feature implements Serializable {

	String word;
	double probValue;
	ArrayList<Double> probValuesWindowTime = new ArrayList<>();
	ArrayList<Integer> docBinary = new ArrayList<>();
	ArrayList<Integer> windowTimeBinary = new ArrayList<>();

	public Feature(String word, double probValue, int nDocument, int nWindowTime) {
		super();
		this.word = word;
		this.probValue = probValue;
		// for (int i = 0; i < nDocument; i++) {
		// docBinary.add(0);
		// }
		// for (int i = 0; i < nWindowTime; i++) {
		// windowTimeBinary.add(0);
		// }
	}

	public Feature(String word, int nDocument, int nWindowTime) {
		super();
		this.word = word;
		// for (int i = 0; i < nDocument; i++) {
		// docBinary.add(0);
		// }
		// for (int i = 0; i < nWindowTime; i++) {
		// windowTimeBinary.add(0);
		// }
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getProbValue() {
		return probValue;
	}

	public void setProbValue(double probValue) {
		this.probValue = probValue;
	}

	public ArrayList<Integer> getDocBinary() {
		return docBinary;
	}

	public void setDocBinary(ArrayList<Integer> docBinary) {
		this.docBinary = docBinary;
	}

	public ArrayList<Integer> getWindowTimeBinary() {
		return windowTimeBinary;
	}

	public void setWindowTimeBinary(ArrayList<Integer> windowTimeBinary) {
		this.windowTimeBinary = windowTimeBinary;
	}
	
	public double getProbWindowsContain(int clustersSize) {
		return (double)windowTimeBinary.size()/(double)clustersSize;
	}

	public void addIndexWindowTimeBinary(int index) {
		for (Integer integer : windowTimeBinary) {
			if (integer == index)
				return;
		}
		windowTimeBinary.add(index);
	}

	public ArrayList<Double> getProbValuesWindowTime() {
		return probValuesWindowTime;
	}

	public void setProbValuesWindowTime(ArrayList<Double> probValuesWindowTime) {
		this.probValuesWindowTime = probValuesWindowTime;
	}

	public ArrayList<Integer> getWindowCLusterVectorByIndex(int clusterLength) {
		ArrayList<Integer> list = new ArrayList<>();

		for (int i = 0; i < clusterLength; i++) {
			list.add(0);
		}

		for (Integer i : windowTimeBinary) {
			if (i<clusterLength)
				list.set(i, 1);
		}
		return list;
	}

	public ArrayList<Document> getDocsByIndex(ArrayList<Document> documents) {
		ArrayList<Document> list = new ArrayList<>();

		for (Integer i : docBinary) {
			list.add(documents.get(i));
		}
		return list;
	}

	public ArrayList<Integer> getDocsVectorByIndex(int documentLength) {
		ArrayList<Integer> list = new ArrayList<>();

		for (int i = 0; i < documentLength; i++) {
			list.add(0);
		}

		for (Integer i : docBinary) {
			list.set(i, 1);
		}
		return list;
	}

	public boolean isBelongTo(int indexWindowTimeCluster) {
		for (Integer index : windowTimeBinary) {
			if (indexWindowTimeCluster == index)
				return true;
		}

		return false;
	}
}
