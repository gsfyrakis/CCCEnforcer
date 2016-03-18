<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<table border="1">
    <tr>
        <th>Right name</th>
        <th>State</th>
        <th>Fulfilled times</th>
    </tr>
    <c:forEach items="${userRight}" var="right">
        <tr>
            <td>
                <c:out value="${right.name}"/>
            </td>
            <td>
                <c:out value="${right.state}"/>
            </td>
            <td>
                <c:out value="${right.counter}"/>
            </td>
        </tr>
    </c:forEach>
</table>
<p>
<table border="1">
    <tr>
        <th>Obligation name</th>
        <th>State</th>
        <th>Deadline</th>
    </tr>
    <c:forEach items="${userObligation}" var="obligation">
        <tr>
            <td>
                <c:out value="${obligation.name}"/>
            </td>
            <td>
                <c:out value="${obligation.state}"/>
            </td>
            <td>
                <c:out value="${obligation.deadline}"/>
            </td>
        </tr>
    </c:forEach>
</table>
<p>
<table border="1">
    <tr>
        <th>Prohibition name</th>
        <th>State</th>
    </tr>
    <c:forEach items="${userProhibition}" var="prohibition">
        <tr>
            <td>
                <c:out value="${prohibition.name}"/>
            </td>
            <td>
                <c:out value="${prohibition.state}"/>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>