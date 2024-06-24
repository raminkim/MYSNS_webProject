<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입</title>
    <script>
        function pwck() {
            let form = document.getElementById("join_form");
            let pw = form["passwd"].value;
            let repeat_pw = form["repeat_pw"].value;
            let pwck_result = document.getElementById("pwck_result");
            if (pw=="" || repeat_pw=="") {
                pwck_result.innerHTML="비밀번호나 비밀번호 확인란이 입력되지 않았습니다.";
                pwck_result.style.color="red";
                return false;
            } else if (pw==repeat_pw) {
                pwck_result.innerHTML="비밀번호가 일치합니다.";
                pwck_result.style.color="green";
            } else {
                pwck_result.innerHTML="비밀번호가 일치하지 않습니다.";
                pwck_result.style.color="red";
            }
        }
    </script>
    <link href="${pageContext.request.contextPath}/css/commonstyle.css" type="text/css" rel="stylesheet"> 
    <link href="${pageContext.request.contextPath}/css/registerstyle.css" type="text/css" rel="stylesheet"> 
</head>
<body>
    <header>
        <h1 id="mainTitle">이거 Merge?</h1>
    </header>
    <section id="container">
        <div id="block">
            <form id="join_form" method="post" action="/register">
                <table style="text-align: center; border: solid 1px black; width: 300px;">
                    <p style="text-align: center;">Join the member in site</p>
                    <tr class="space">
                        <td style="width:100px;"><h5>이름</h5></td>
                        <td><input type="text" name="name" placeholder="이름을 입력하세요" required></td>
                    </tr>
                    <tr class="space">
                        <td style="width:100px;"><h5>아이디</h5></td>
                        <td><input type="text" name="id" placeholder="아이디를 입력하세요" required></td>
                    </tr>
                    <tr class="space">
                        <td><h5>password</h5></td>
                        <td><input type="password" name="passwd" oninput="pwck()" placeholder="비밀번호를 입력하세요" required></td>
                    </tr>
                    <tr class="space">
                        <td><h5>password check</h5></td>
                        <td><input type="password" name="repeat_pw" oninput="pwck()" placeholder="비밀번호를 입력하세요" required></td>
                    </tr>
                    <tr class="space">
                        <td colspan="3"><p id="pwck_result">비밀번호를 입력해주세요.</p></td>
                    </tr>
                </table>
                <div class="join"> <input style="width: 300px; background-color: lightgreen;" class="join" onclick="" type="submit" value="회원가입"> </div>
            </form>
        </div>
        <div>
            <p style="color: red;">${registerError}</p>
        </div>
    </section>
    <script>
        document.getElementById("join_form").addEventListener("submit", function(event) {
            let form = document.getElementById("join_form");
            let pw = form["passwd"].value;
            let repeat_pw = form["repeat_pw"].value;
            let pwck_result = document.getElementById("pwck_result");
            if (pw != repeat_pw) {
                pwck_result.innerHTML="비밀번호가 일치하지 않습니다. 다시 입력해주세요.";
                pwck_result.style.color="red";
                event.preventDefault();
            }
        } );
    </script>
</body>
</html>