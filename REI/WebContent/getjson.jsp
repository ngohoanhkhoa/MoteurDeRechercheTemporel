<%@page import="MoteurRecherche.MoteurRecherche"%>

<%@ page trimDirectiveWhitespaces="true" %>
<%@page language="java" contentType="application/json;charset=UTF-8" %>
<%    
		String search = request.getParameter("word");
		MoteurRecherche mtr = new MoteurRecherche();
		String result = mtr.getdata(search);
 %>
<%=result%>