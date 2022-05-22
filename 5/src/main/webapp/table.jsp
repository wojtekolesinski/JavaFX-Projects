<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
    <title>Table</title>
    <link href="css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <table>
        <thead>
            <tr>
                <th>Id</th>
                <th>Make</th>
                <th>Model</th>
                <th>Type</th>
                <th>Production Year</th>
                <th>Fuel Consumption (l/100km)</th>
            </tr>
        </thead>
        <tbody>
            <jsp:useBean id="cars" scope="request" type="java.util.List<com.example.tpo5.models.Car>"/>
            <c:forEach var="car" items="${cars}">
                <tr>
                    <td><c:out value="${car.id}"/></td>
                    <td><c:out value="${car.make}"/></td>
                    <td><c:out value="${car.model}"/></td>
                    <td><c:out value="${car.type}"/></td>
                    <td><c:out value="${car.productionYear}"/></td>
                    <td><c:out value="${car.fuelConsumption}"/></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <br/>
    <a href="index.jsp">Back to main page</a>
</body>
</html>
