<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*, java.sql.*, javax.servlet.http.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My SNS</title>
    <link href="${pageContext.request.contextPath}/css/commonstyle.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/mainstyle.css" type="text/css" rel="stylesheet">
</head>
<body id="container">
    <%
        HttpSession existingSession = request.getSession(false); // 세션 가져오기
        String name = (String) existingSession.getAttribute("name"); // 세션에서 name 가져오기
        int follower = (int) existingSession.getAttribute("follower"); // 세션에서 follower 가져오기

        if (existingSession != null && name != null) {
    %>
            <header class="logo">
                <h1 id="mainTitle">이거 Merge?</h1>
                <ol id="menu">
                    <li><a href="/main">HOME</a></li>
                </ol>
            </header>
            <aside>
                <div class="profile">
                    <table border="2" style="border-collapse: collapse;">
                        <tr>
                            <td rowspan="2"><img src="file/profile.jpg"></td>
                            <td colspan="2">
                                <!-- 로그인한 아이디의 이름 출력하기. -->
                                <h3 id="log-id">${name} 님<br> follower: ${follower}명<br> following: ${following}명</h3>
                            </td>
                        </tr>
                        <tr>
                            <form action="/logout">
                                <td style="text-align: center;"><input type="submit" id="logout" name="logout" value="로그아웃"></td>
                            </form>
                        </tr>
                    </table>
                </div>
            </aside>
            <section>
                <article>
                    <button><a href="/writePost">글 쓰기</a></button>
                </article>
                <article class="normalList">
                    <h2>일반 게시물 목록</h2>
                    <table>
                        <tr>
                            <th>작성자</th>
                            <th>제목</th>
                            <th>작성 시간</th>
                        </tr>
                        <c:forEach var="post" items="${posts}">
                            <tr>
                                <td>${post.memberId}</td>
                                <td><a href="openPost?id=${post.id}">${post.title}</a></td>
                                <td>${post.createdTime}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </article>
                <article class="followerList">
                    <h2>본인 및 팔로잉하는 게시물 목록</h2>
                    <table>
                        <tr>
                            <th>작성자</th>
                            <th>제목</th>
                            <th>작성 시간</th>
                        </tr>
                        <c:forEach var="post" items="${followingPosts}">
                            <tr>
                                <td>${post.memberId}</td>
                                <td><a href="openPost?id=${post.id}">${post.title}</a></td>
                                <td>${post.createdTime}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </article>
            </section>
    <%
        } else {
    %>
            <h2>로그인되지 않았습니다.</h2>
            <a href="/Mainlogin">로그인 하러가기</a>
    <%
        }
    %>
</body>
</html>