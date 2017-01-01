<%@page import="java.util.ArrayList"%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"
	import="bob.paper3.controller.*, bob.paper3.models.*"%>
<%
	String keyword = (String) request.getParameter("keyword");
	String type = (String) request.getParameter("sort");
	
	ArrayList<Cluster> clusters = new ArrayList<>();
	if (type != null && !type.equalsIgnoreCase("1")) {
		type = 2 + "";
		clusters = MainController.calculateEvent_Khoa(keyword);
	} else {
		clusters = MainController.calculateEvent(keyword);
	}

	if (clusters != null || !clusters.isEmpty()) {
		ArrayList<ArrayList<Document>> docs = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			Cluster cluster = clusters.get(i);
			docs.add(cluster.getChildren());
		}
%>

<%=MainController.exportJSON(docs)%>

<%
	}
%>
