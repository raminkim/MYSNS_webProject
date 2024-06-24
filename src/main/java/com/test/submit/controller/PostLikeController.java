package com.test.submit.controller;

import org.springframework.stereotype.Controller;
import java.io.*;
import java.sql.*;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;


@Controller
public class PostLikeController {
    private final String URL = "jdbc:mysql://localhost:3306/submitDB";
    private final String USER = "root";
    private final String PASSWORD = "1234";

    @GetMapping("/like")
    public String getLike(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam int postId) {
        String likeQuery = "INSERT INTO postLikes (postId, memberId) VALUES (?, ?)"; 
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(likeQuery);
            System.out.println("like 과정에서의 id 값: " + session.getAttribute("id"));
            pstmt.setInt(1, postId);
            pstmt.setString(2, (String) session.getAttribute("id"));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("already like.");
        }
        List<String> postLikesList = (List<String>) session.getAttribute("postLikesList");
        postLikesList.add((String) session.getAttribute("id"));
        System.out.println("postLikesList: " + String.join(",",postLikesList));

        session.setAttribute("postLikesList", postLikesList);
        session.setAttribute("postLikesCount", postLikesList.size());
        redirectAttributes.addAttribute("id", postId);
        return "redirect:/openPost";
    }
    
    @GetMapping("/likeCancel")
    public String getLikeCancel(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam int postId) {
        String likeCancelQuery = "DELETE FROM postLikes WHERE postId = ? AND memberId = ?"; 
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(likeCancelQuery);
            System.out.println("likeCancel 과정에서의 id 값: " + session.getAttribute("id"));
            pstmt.setInt(1, postId);
            pstmt.setString(2, (String) session.getAttribute("id"));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("already like cancel.");
        }
        List<String> postLikesList = (List<String>) session.getAttribute("postLikesList");
        postLikesList.remove((String) session.getAttribute("id"));

        session.setAttribute("postLikesList", postLikesList);
        session.setAttribute("postLikesCount", postLikesList.size());
        redirectAttributes.addAttribute("id", postId);
        return "redirect:/openPost";
    }
}
