<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="${pageContext.request.contextPath}/css/commonstyle.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/loginstyle.css" type="text/css" rel="stylesheet">
</head>
<body>
    <header>
        <h1 id="mainTitle">이거 Merge?</h1>
    </header>
    <section id="container"> 
        <div id="block">
            <form id="login_form" method="post" action="/Mainlogin">
                <fieldset">
                    <legend style="text-align: center;">LOGIN</legend>
                    <table>
                        <tr>
                            <td>ID</td>
                            <td><input type="text" name="id" placeholder="ID" required><td>
                        </tr>
                        <tr>
                            <td>password</td>
                            <td><input type="password" name="passwd" placeholder="passwd" required></td>
                        </tr>
                    </table><br>
                    <div class="login"><input class="login" type="submit" value="로그인"></div>
                </fieldset>
            </form>
        <div>
        <div>
            <p style="color: red;">${loginError}</p>
        </div><br><br>
        <div id="join">
            <b>이거 Merge? 가 처음이에요 &nbsp;</b><a href="/register"><b>회원가입<b></b></a>
        </div>
    </section>
</body>
</html>