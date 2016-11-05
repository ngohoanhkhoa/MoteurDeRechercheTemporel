package demo.bob.circle_packing;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

	String name;
	List<Child> children;

	public Cluster(String name, List<Child> children) {
		super();
		this.name = name;
		this.children = children;
	}

	public Cluster(String name) {
		super();
		this.name = name;
		this.children = new ArrayList<Child>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Child> getChildren() {
		return children;
	}

	public void setChildren(List<Child> children) {
		this.children = children;
	}

	public String getResultAsJson() {
		String result = "{\"name\": \"" + name + "\"";

		if (children != null && !children.isEmpty()) {
			result += ", \n\"children\": [\n";

			for (int i = 0, c = children.size(); i < c; i++) {
				result += "{\"name\": \"" + children.get(i).getName() + "\", \"size\": " + children.get(i).getSize()
						+ "}";
				result += (c > i + 1) ? "," : "";
				result += "\n";
			}
			result += "\n ] }";
		}

		return result;
	}

}
