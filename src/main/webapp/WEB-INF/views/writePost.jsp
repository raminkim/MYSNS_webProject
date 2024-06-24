<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>글 쓰기</title>
    <link href="${pageContext.request.contextPath}/css/commonstyle.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/writepoststyle.css" type="text/css" rel="stylesheet">
</head>
<body>
    <h1>글 쓰기</h1>
    <form action="/uploadFile" method="post"  enctype="multipart/form-data">
        제목: <input type="text" name="title" required><br>
        내용: <textarea name="content" rows="4" cols="50"></textarea><br>
        파일 선택: <input type="file" name="file"><br>
        <input type="submit" value="업로드">
    </form><br>
    <button><a href="/main">목록으로</a></button>
</body>
</html>