<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시물</title>
    <link href="${pageContext.request.contextPath}/css/commonstyle.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/openpoststyle.css" type="text/css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="subject"><h2>${post.title}</h2></div>
        <div class="info">
            <div class="writer">
                <span class="title">작성자: </span>${post.memberId}
                <c:choose>
                    <c:when test="${sessionScope.id == post.memberId}"></c:when>
                    <c:when test="${not empty post && not empty post.memberId && not empty sessionScope.id && sessionScope.id != post.memberId && !sessionScope.followingList.contains(post.memberId)}">
                        <button><a href="/follow?postMemberId=${post.memberId}">팔로우</a></button>
                    </c:when>
                    <c:otherwise>
                        <button><a href="/followCancel?postMemberId=${post.memberId}">팔로우 취소</a></button>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="date">
                <span class="title">작성일: </span>${post.createdTime}
            </div>
        </div>
        <div class="file">
            <span class="title">첨부 파일: </span><a href="downloadFile?id=${post.id}">${post.fileName}</a>
        </div><br>
        <div class="content">
            <span class="content">${post.content}</span>
        </div>
        <div class="btn">
            <c:choose>
                <c:when test="${not empty post && not empty sessionScope.id && !sessionScope.postLikesList.contains(sessionScope.id)}">
                    <button><a href="/like?postId=${post.id}"><img src="../../file/emptyHeart.png" width="15px" height="15px;">좋아요</a></button>
                </c:when>
                <c:otherwise>
                    <button><a href="likeCancel?postId=${post.id}"><img src="../../file/fullHeart.png" width="15px" height="15px;">좋아요 취소</a></button>
                </c:otherwise>
            </c:choose>

            <c:if test="${not empty post && not empty post.memberId && not empty sessionScope.id && sessionScope.id == post.memberId}">
                <button><a href="editPost?id=${post.id}">수정</a></button>
                <button><a href="deletePost?id=${post.id}">삭제</a></button>
            </c:if>
            <span class="title">&nbsp;좋아요 수 : ${postLikesCount}</span>
        </div>
    </div>
    <hr>
    <h3>댓글</h3>
    <div class="comment_container">
        <div class="comment_list">
            <table>
                <c:forEach var="com" items="${sessionScope.postCommentsList}">
                    <tr>
                        <td rowspan="2"><img width="40px" heigth="40px" src="../../file/profile.jpg"></td>
                        <td>${com.memberId}</td>
                        <td>${com.createdTime}</td>
                        <c:choose>
                            <c:when test="${com.memberId == sessionScope.id}">
                                <td><button><a href="/commentDelete?commentId=${com.id}&postId=${post.id}">삭제</a></button><td></td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${!com.commentLikeList.contains(sessionScope.id)}">
                                <td>
                                    <form action="/commentLike" method="post" style="display:inline;">
                                        <input type="hidden" name="id" value="${com.id}">
                                        <input type="hidden" name="postId" value="${post.id}">
                                        <input type="hidden" name="commentId" value="${com.commentId}">
                                        <input type="hidden" name="commentLikeList" value="${com.commentLikeList}">
                                        <input type="hidden" name="postId" value="${post.id}">
                                        <button type="submit">
                                            <img src="../../file/emptyHeart.png" width="15px" height="15px;"> 좋아요
                                        </button>
                                    </form>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>
                                    <form action="/commentLikeCancel" method="post" style="display:inline;">
                                        <input type="hidden" name="id" value="${com.id}">
                                        <input type="hidden" name="postId" value="${post.id}">
                                        <input type="hidden" name="commentId" value="${com.commentId}">
                                        <input type="hidden" name="commentLikeList" value="${com.commentLikeList}">
                                        <input type="hidden" name="postId" value="${post.id}">
                                        <button type="submit">
                                            <img src="../../file/fullHeart.png" width="15px" height="15px;"> 좋아요 취소
                                        </button>
                                    </form>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <tr>
                        <td>${com.comment}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="comment_form">
            <form method="post" action="/comment">
                <input type="hidden" name="postId" value="${post.id}">
                <div class="comment_area">
                    <textarea name="comment" rows="4" cols="50" placeholder="댓글" required></textarea>
                </div>
                <div class="comment_button">
                    <input type="submit" value="저장">
                    <input type="reset" value="취소">
                </div>
            </form>
        </div>
    </div><br>
    <div class="button_area">
        <button><a href="/main">목록으로</a></button>
    </div>
</body>
</html>