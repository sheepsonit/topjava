<jsp:useBean id="editMeal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit Meal</h2>
<form method="POST" action='meals'>
    <input type="hidden" name="id" value="${editMeal.id}"/>
    <p>DateTime : <input type="datetime-local" name="dateTime"
    <%--                         value="<javatime:format value="${editMeal.dateTime}" pattern="yyyy-MM-dd hh:mm"/>"></p>--%>
                         value="${editMeal.dateTime}" pattern="yyyy-MM-dd hh:mm"></p>
    <p>Description : <input
            type="text" name="description"
            value="${editMeal.description}"></p>
    <p>Calories : <input
            type="text" name="calories"
            value="${editMeal.calories}"></p>
    <br>
    <input type="submit" value="Save"> <input type="reset" value="Cancel" onclick="window.history.back()">
</form>
</body>
</html>
