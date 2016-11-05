package demo.bob.circle_packing;

import java.util.List;

public class UtilsCommon {

	public static String getResultJson(String rootName, List<Cluster> clusters) {
		String result = "{ \"name\": \" " + rootName + " \", \n \"children\": [\n";
		for (int i = 0, c = clusters.size(); i < c; i++) {
			result += clusters.get(i).getResultAsJson();
			result += (c > i + 1) ? "," : "";
			result += "\n";
		}
		
		result += "\n] \n}";
		return result;
	}
}
