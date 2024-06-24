package com.test.submit.controller;

import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller
public class FollowController {
    private final String URL = "jdbc:mysql://localhost:3306/submitDB";
    private final String USER = "root";
    private final String PASSWORD = "1234";

    @GetMapping("/follow")
    public String getFollowRequest(HttpSession session, @RequestParam String postMemberId) {
        String followQuery = "INSERT INTO follows (followerId, followeeId) VALUES (?, ?)"; 
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(followQuery);
            System.out.println("follow 과정에서의 id 값: " + session.getAttribute("id"));
            pstmt.setString(1, (String) session.getAttribute("id"));
            pstmt.setString(2, postMemberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int id = (int) session.getAttribute("postId");
        List<String> followingList = (List<String>) session.getAttribute("followingList");
        followingList.add(postMemberId);

        session.setAttribute("followingList", followingList);
        session.setAttribute("following", followingList.size());
        return String.format("redirect:/openPost?id=%d", id);
    }

    @GetMapping("/followCancel")
    public String getFollowCancelRequest(HttpSession session, @RequestParam String postMemberId) {
        String followDeleteQuery = "DELETE FROM follows WHERE followerId = ? AND followeeId = ?"; 
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(followDeleteQuery);
            pstmt.setString(1, (String) session.getAttribute("id"));
            pstmt.setString(2, postMemberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int id = (int) session.getAttribute("postId");
        List<String> followingList = (List<String>) session.getAttribute("followingList");
        followingList.remove(postMemberId);

        session.setAttribute("followingList", followingList);
        session.setAttribute("following", followingList.size());
        return String.format("redirect:/openPost?id=%d", id);
    }
}
