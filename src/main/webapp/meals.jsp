<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
    <style>
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1" cellpadding="2">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <%--@elvariable id="mealsTo" type="java.util.List"--%>
    <c:forEach var="mealTo" items="${mealsTo}">
        <jsp:useBean id="mealTo" class="ru.javawebinar.topjava.model.MealTo"/>
        <c:set var="textColor" value="${mealTo.excess ? 'red' : 'green'}"/>
        <tr style="color: ${textColor} ">
            <td><%=mealTo.getDate()%></td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=update">Update</a></td>
            <td><a href="meals?action=delete">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
