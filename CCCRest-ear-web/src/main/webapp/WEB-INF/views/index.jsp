<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
Hello!
<%
    if (session.getAttribute("user") == null) {
%>
<p><a href="signin.jsp">Sign in</a></p>
<p><a href="signup.jsp">Sign up</a></p>
<%
} else {
%>
<p><a href="data/upload.jsp">Upload file</a></p>
<p><a href="data/remove.jsp">Remove file</a></p>
<p><a href="signout.jsp">Sign out</a></p>
<p><a href="data/retrieve.jsp">Look up data</a></p>
<p><a href="ropInfo.jsp">ROP information</a>
        <%
}
%>
</body>
</html>