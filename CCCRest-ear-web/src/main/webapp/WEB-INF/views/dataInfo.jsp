<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Data</title>
</head>
<body>
<table border="1">
    <tr>
        <th>Name</th>
        <th>Size</th>
        <th>Owner</th>
        <th>Publication Date</th>
        <th>Last visit Date</th>
        <th>Access key</th>
    </tr>
    <tr>
        <td>${data.name}</td>
        <td>${data.size}</td>
        <td>${data.owner}</td>
        <td>${data.publish}</td>
        <td>${data.lastVisit}</td>
        <td>${data.key}</td>
    </tr>
</table>
<table border="2">
    <tr>
        <th>Raw Data</th>
    </tr>
    <c:forEach items="${data.rawData}" var="rawData">
        <tr>
            <td>
                <c:out value="${rawData}"/>
            </td>
        </tr>
    </c:forEach>
</table>

<table border="3">
    <tr>
        <th>Tool</th>
    </tr>
    <c:forEach items="${data.tool}" var="tool">
        <tr>
            <td>
                <c:out value="${tool}"/>
            </td>
        </tr>
    </c:forEach>
</table>


<form name="updateForm" method="post">
    <h2>Update the publish date for your data</h2>
    <p><input type="hidden" name="key" value="${data.key}"/></p>
    <p>please input your date:<input type="text" name="date"/></p>
    <p><input type="submit" value="update"></p>
</form>
<p>Click on the link to download: <a href="download/${data.name}">Download Link</a></p>
</body>
</html>