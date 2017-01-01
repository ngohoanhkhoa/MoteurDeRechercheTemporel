package bob.paper3.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Utility {

	public static double getNormalDistance(HashMap<String, Integer> hits) {
		double meanDistance = 0;
		for (Entry<String, Integer> entry : hits.entrySet()) {
			meanDistance += entry.getValue();
		}
		meanDistance /= hits.size();
		return meanDistance;
	}

	public static double getAbsoluteValue(ArrayList<Integer> vertor) {
		return getAbsoluteValue(vertor, vertor.size(), false);
	}

	public static double getAbsoluteValue(ArrayList<Integer> vertor, int length, boolean isIndex) {
		double sum = 0;
		if (isIndex) {
			// TODO:
		} else {
			for (Integer integer : vertor) {
				sum += integer;
			}
			sum = Math.sqrt(sum);
		}
		return sum;
	}

	public static ArrayList<Integer> intersectVertors(ArrayList<Integer>... vectors) {
		ArrayList<Integer> result = new ArrayList<>();

		// TODO:
		if (vectors.length < 1) {
			System.err.println("intersect Length = 0");
			return result;
		}
		if (vectors.length == 1) {
			System.err.println("intersect Length = 1");
			return vectors[0];
		}
		if (vectors[0].size() < 1) {
			return vectors[1];
		}

		for (int i = 0; i < vectors[0].size(); i++) {
			result.add(vectors[0].get(i));
		}

		for (int i = 1; i < vectors.length; i++) {
			if (vectors[i].size() != vectors[i - 1].size()) {
				System.err.println("Lengths' Vectors are not identical");
				return result;
			}

			for (int j = 0; j < vectors[i].size(); j++) {
				if (result.get(j) == 0) {
					continue;
				}
				if (vectors[i].get(j) == 0) {
					result.set(j, 0);
				}
			}
		}
		return result;
	}

	public static ArrayList<Integer> unionVertors(ArrayList<Integer>... vectors) {
		ArrayList<Integer> result = new ArrayList<>();
		if (vectors.length < 1) {
			System.err.println("Union Length = 0");
			return result;
		}
		if (vectors.length == 1) {
			System.err.println("Union Length = 1");
			return vectors[0];
		}
		if (vectors[0].size() < 1) {
			return vectors[1];
		}

		for (int i = 0; i < vectors[0].size(); i++) {
			result.add(vectors[0].get(i));
		}

		for (int i = 1; i < vectors.length; i++) {
			if (vectors[i].size() != vectors[i - 1].size()) {
				System.err.println("Lengths' Vectors are not identical");
				return result;
			}

			for (int j = 0; j < vectors[i].size(); j++) {
				if (vectors[i].get(j) == 1) {
					result.set(j, 1);
				}
			}
		}
		return result;
	}

	public static double getSlope(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("array lengths are not equal");
		}
		int n = x.length;

		// first pass
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
		for (int i = 0; i < n; i++) {
			sumx += x[i];
			sumx2 += x[i] * x[i];
			sumy += y[i];
		}
		double xbar = sumx / n;
		double ybar = sumy / n;

		// second pass: compute summary statistics
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = 0; i < n; i++) {
			xxbar += (x[i] - xbar) * (x[i] - xbar);
			yybar += (y[i] - ybar) * (y[i] - ybar);
			xybar += (x[i] - xbar) * (y[i] - ybar);
		}
		double slope = xybar / xxbar;

		return slope;
	}
}
