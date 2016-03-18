<html>
<head>
    <title>Store</title>
</head>
<body>
<h2>Input the information for the data to be stored</h2>
<form name="uploadForm" method="post" enctype="multipart/form-data">
    <p>please select your file :<input type="file" name="uploadedFile"/></p>
    <select name="type">
        <option value="data">Research data</option>
        <option value="tool">Tool</option>
    </select>
    <p><input type="submit" value="upload"></p>
</form>
</body>
</html>
