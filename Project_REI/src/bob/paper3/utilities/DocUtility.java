package bob.paper3.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.rei.tools.FrenchStemmer;
import org.rei.tools.FrenchTokenizer;
import org.rei.tools.Normalizer;

import bob.paper3.models.Cluster;
import bob.paper3.models.Document;
import bob.paper3.models.Feature;

public class DocUtility {

	private static String STOPWORDS_FILENAME = "D:/frenchST.txt";
	public Normalizer TOKENIZER_NO_STOP_WORDS;
	public Normalizer STEMMER_ALL_WORDS;
	public Normalizer STEMMER_NO_STOP_WORDS;
	public Normalizer TOKENIZER_ALL_WORDS = new FrenchTokenizer();
	public Normalizer[] normalizers = { STEMMER_ALL_WORDS, STEMMER_NO_STOP_WORDS, TOKENIZER_ALL_WORDS,
			TOKENIZER_NO_STOP_WORDS };

	private static boolean isInitialized = false;
	private static DocUtility instance = null;

	private DocUtility() {
		try {
			STEMMER_ALL_WORDS = new FrenchStemmer();
			STEMMER_NO_STOP_WORDS = new FrenchStemmer(new File(STOPWORDS_FILENAME));
			TOKENIZER_ALL_WORDS = new FrenchTokenizer();
			TOKENIZER_NO_STOP_WORDS = new FrenchTokenizer(new File(STOPWORDS_FILENAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DocUtility getInstance() {
		if (!isInitialized) {
			instance = new DocUtility();
		}
		isInitialized = true;
		return instance;
	}

	// DF
	public HashMap<String, Integer> getDocumentFrequency(ArrayList<Document> documents, Normalizer normalizer)
			throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();

		// Parcours des fichiers et remplissage de la table
		for (Document doc : documents) {
			System.err.println("Analysing document: " + doc.getId());
			// TODO
			HashMap<String, Integer> hitsFile = new HashMap<String, Integer>();
			// Appel de la methode de normalisation
			ArrayList<String> words = normalizer.normalize(doc.getInfo().replaceAll("(( |\t){2,}|(\n))", " "));
			// .replaceAll("([^0-9a-zA-Z-.' ])+","")

			for (String word : words) {
				word = word.toLowerCase();
				hitsFile.put(word, 1);
			}

			for (Entry<String, Integer> entry : hitsFile.entrySet()) {
				String word = entry.getKey();
				hits.put(word, hits.containsKey(word) ? hits.get(word) + 1 : 1);
			}
		}
		return hits;
	}

	public HashMap<String, Integer> getDocumentFrequencyByFeatures(ArrayList<Document> documents,
			HashMap<String, Integer> features, Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();

		// Parcours des fichiers et remplissage de la table
		for (Document doc : documents) {
			// System.err.println("Analysing document: " + doc.getId());
			// .replaceAll("([^0-9a-zA-Z' ])+","")
			for (Entry<String, Integer> entry : features.entrySet()) {
				String word = entry.getKey();
				if (doc.getInfo().indexOf(word) != -1)
					hits.put(word, hits.containsKey(word) ? hits.get(word) + 1 : 1);
			}
		}
		return hits;
	}

	public HashMap<String, Feature> getDocumentFrequencyByBinaryMap(ArrayList<Document> documents, int nCluster, 
			ArrayList<Feature> features, Normalizer normalizer) throws IOException {
		HashMap<String, Feature> hits = new HashMap<>();
		STEMMER_NO_STOP_WORDS = new FrenchStemmer(new File(STOPWORDS_FILENAME));

		for (int i = 0; i < documents.size(); i++) {
			Document doc = documents.get(i);
//			System.err.println("Analysing document: " + doc.getId());
			System.err.print(".");
			// TODO
			HashMap<String, Integer> hitsFile = new HashMap<String, Integer>();
			ArrayList<String> words = STEMMER_NO_STOP_WORDS.normalize(doc.getInfo().toLowerCase().replaceAll("[^'a-zàâçéèêëîïôûùüÿñæœ .-]"," "));
			//.replaceAll("(( |\t){2,}|(\n))", " ")
			// .replaceAll("([^0-9a-zA-Z' ])+","")

			for (String word : words) {
				word = word.toLowerCase();
				hitsFile.put(word, 1);
			}

			for (Entry<String, Integer> entry : hitsFile.entrySet()) {
				String word = entry.getKey();
				if (!hits.containsKey(word)) {
					Feature f = new Feature(word, documents.size(), nCluster);
					f.getDocBinary().add(i);
					f.addIndexWindowTimeBinary(doc.getClusterNumber());
					hits.put(word, f);
					features.add(f);
					doc.getFeatures().put(word, f);
				} else {
					
					doc.getFeatures().put(word, hits.get(word));
					hits.get(word).getDocBinary().add(i);
					hits.get(word).addIndexWindowTimeBinary(doc.getClusterNumber());
				}
			}
		}

		return hits;
	}

	// END DF

	public int countSequenceWord(String document, String word) {
		int index = document.indexOf(word);
		int count = 0;
		for (; index != -1; index = document.indexOf(word)) {
			count++;
			document = document.substring(index + 1);
		}
		return count;
	}

	public HashMap<String, Integer> removeWeakWords(HashMap<String, Integer> feature) {
		HashMap<String, Integer> hits = new HashMap<>();
		double meanHits = Utility.getNormalDistance(feature);
		for (Entry<String, Integer> entry : feature.entrySet()) {
			Integer value = entry.getValue();
			if (meanHits <= value) {
				hits.put(entry.getKey(), value);
			}
		}
		return hits;
	}

	public long getDistance(Document doc1, Document doc2) {
		return Math.abs(doc1.getDate() - doc2.getDate());
	}

	public long getNormalDistance(ArrayList<Document> documents) {
		long meanDistance = 0;
		for (int i = 0, c = documents.size() - 1; i < c; i++) {
			meanDistance += getDistance(documents.get(i), documents.get(i + 1));
		}
		meanDistance /= documents.size();
		return meanDistance;
	}

}
