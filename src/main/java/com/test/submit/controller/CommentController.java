package com.test.submit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.*;
import java.sql.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import jakarta.servlet.http.HttpSession;


@Controller
public class CommentController {
    private final String URL = "jdbc:mysql://localhost:3306/submitDB";
    private final String USER = "root";
    private final String PASSWORD = "1234";

    @PostMapping("/comment")
    public String postComment(Model model, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam int postId ,@RequestParam String comment) { // 여기서 id는 posts 테이블의 id.
        String commentQuery = "INSERT INTO comments (postId, memberId, comment) VALUES (?, ?, ?)"; 
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(commentQuery);
            System.out.println("comment 과정에서의 id 값: " + session.getAttribute("id"));
            pstmt.setInt(1, postId);
            pstmt.setString(2, (String) session.getAttribute("id"));
            pstmt.setString(3, comment);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("redirect:/openPost?id=%d", postId);
    }

    @GetMapping("/commentDelete")
    public String getCommentDelete(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam int commentId, @RequestParam int postId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // 트랜잭션 시작

            try (PreparedStatement deleteCommentLikes = connection.prepareStatement(
                        "DELETE FROM commentlikes WHERE commentId = ?");
                PreparedStatement deleteComment = connection.prepareStatement(
                        "DELETE FROM comments WHERE id = ?")) {

                // 댓글의 좋아요 삭제
                deleteCommentLikes.setInt(1, commentId);
                deleteCommentLikes.executeUpdate();

                // 댓글 삭제
                deleteComment.setInt(1, commentId);
                deleteComment.executeUpdate();

                connection.commit(); // 트랜잭션 커밋

            } catch (SQLException e) {
                connection.rollback(); // 오류 발생 시 트랜잭션 롤백
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.format("redirect:/openPost?id=%d", postId);
    }

    
    
}
