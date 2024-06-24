package com.test.submit.controller;

import java.io.*;
import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.submit.model.CommentBean;

import jakarta.servlet.http.HttpSession;


@Controller
public class CommentLikeController {
    private final String URL = "jdbc:mysql://localhost:3306/submitDB";
    private final String USER = "root";
    private final String PASSWORD = "1234";

    @PostMapping("/commentLike")
    public String getCommentLike(HttpSession session, @RequestParam int id, @RequestParam int commentId, @RequestParam List<String> commentLikeList, @RequestParam int postId) {
        System.out.println("comment의 id : "+ id);
        String commentLikeQuery = "INSERT INTO commentLikes (postId, commentId, memberId) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(commentLikeQuery);
            System.out.println("commentLike 과정에서의 id 값: " + session.getAttribute("id"));
            pstmt.setInt(1, postId);
            pstmt.setInt(2, id);
            pstmt.setString(3, (String) session.getAttribute("id"));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("commentLike insert before : " + String.join(",", commentLikeList));
        commentLikeList.add((String) session.getAttribute("id"));
        System.out.println("commentLike insert after : " + String.join(",", commentLikeList));

        return String.format("redirect:/openPost?id=%d", postId);
    }

    @PostMapping("/commentLikeCancel")
    public String getCommentLikeCancel(HttpSession session, @RequestParam int id, @RequestParam int commentId, @RequestParam List<String> commentLikeList, @RequestParam int postId) {
        String commentLikeCancelQuery = "DELETE FROM commentLikes WHERE commentId = ? AND memberId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(commentLikeCancelQuery);
            System.out.println("commentLikeCancel 과정에서의 id 값: " + session.getAttribute("id"));
            pstmt.setInt(1, id);
            pstmt.setString(2, (String) session.getAttribute("id"));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("commentLike insert before : " + String.join(",", commentLikeList));
        commentLikeList.remove((String) session.getAttribute("id"));
        System.out.println("commentLike insert after : " + String.join(",", commentLikeList));

        return String.format("redirect:/openPost?id=%d", postId);
    }
}
