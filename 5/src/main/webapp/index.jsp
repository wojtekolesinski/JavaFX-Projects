<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1>Wybierz rodzaj samochodu</h1>
<form method="get" action="form">
    <label for="type">Type:</label>
    <select id="type" name="type">
        <option value="">None</option>
        <option value="sportowy">Sportowy</option>
        <option value="osobowy">Osobowy</option>
        <option value="ciezarowy">Ciężarowy</option>
    </select>
    <br/>
    <input type="submit" value="Submit">
</form>
</body>
</html>