package com.test.submit.controller;

import com.test.submit.model.CommentBean;
import com.test.submit.model.PostBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    private final String URL = "jdbc:mysql://localhost:3306/submitDB";
    private final String USER = "root";
    private final String PASSWORD = "1234";

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/main")
    public String getMain(Model model, HttpSession session) {
        List<PostBean> followingPosts = new ArrayList<>();
        List<PostBean> posts = new ArrayList<>();

        List<String> followerList = new ArrayList<>(); // 나를 팔로우하는 계정 ID들 리스트. (팔로워 목록)
        List<String> followingList = new ArrayList<>(); // 내가 팔로우하는 계정 ID들 리스트. (팔로잉 목록)


        String memberId = (String) session.getAttribute("id"); // 세션에서 로그인된 유저 ID 가져오기
        if (memberId == null) { return "login"; }
        System.out.println("memberId : " + memberId);

        // 나를 팔로우하는 계정들의 ID를 가져오는 쿼리 (팔로워)
        String followerQuery = "SELECT followerId FROM follows WHERE followeeId = ?";
        // 내가 팔로우한 계정들의 ID를 가져오는 쿼리 (팔로잉)
        String followingQuery = "SELECT followeeId FROM follows WHERE followerId = ?";
        // 모든 계정의 게시글을 가져오는 쿼리
        String postQuery = "SELECT * FROM posts ORDER BY createdTime DESC";
        // 본인 및 본인이 팔로우한 계정들의 게시글을 가져오는 쿼리
        String postQuery2 = "SELECT * FROM posts WHERE memberId = ? OR memberId IN (" + followingQuery + ") ORDER BY createdTime DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(postQuery); // 모든 계정의 게시물 목록을 가져오기 위해 불러옴.
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PostBean post = new PostBean(rs.getInt("id"), rs.getString("memberId"), rs.getString("title"), rs.getString("content"), rs.getString("fileName"), rs.getString("createdTime"));
                    posts.add(post);
                }
            }

            PreparedStatement pstmt2 = conn.prepareStatement(postQuery2); // 내가 팔로우한 계정의 게시물 목록을 가져오기 위해 불러옴.
            pstmt2.setString(1, memberId);
            pstmt2.setString(2, memberId);
            try (ResultSet rs = pstmt2.executeQuery()) {
                while (rs.next()) {
                    PostBean post = new PostBean(rs.getInt("id"), rs.getString("memberId"), rs.getString("title"), rs.getString("content"), rs.getString("fileName"), rs.getString("createdTime"));
                    followingPosts.add(post);
                }
            }

            PreparedStatement pstmt3 = conn.prepareStatement(followerQuery);
            pstmt3.setString(1, memberId); // 로그인한 계정을 팔로워하는 팔로워 수를 계산하기 위해 불러옴. (팔로워)
            try (ResultSet rs = pstmt3.executeQuery()) {
                while (rs.next()) {
                    String follower = rs.getString("followerId");
                    followerList.add(follower);
                }
            }
            
            PreparedStatement pstmt4 = conn.prepareStatement(followingQuery);
            pstmt4.setString(1, memberId); // 로그인한 계정이 팔로우하는 팔로잉 수를 계산하기 위해 불러옴. (팔로잉)
            try (ResultSet rs = pstmt4.executeQuery()) {
                while (rs.next()) {
                    String following = rs.getString("followeeId");
                    followingList.add(following);
                }
            }
            session.setAttribute("followerList",  followerList);
            session.setAttribute("followingList", followingList);
            session.setAttribute("follower",  followerList.size());
            session.setAttribute("following", followingList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("follower: " + followerList.size());
        System.out.println("followerList: " + String.join(",",followerList));
        System.out.println("following: " + followingList.size());
        System.out.println("followingList: " + String.join(",",followingList));
        model.addAttribute("posts", posts);
        model.addAttribute("followingPosts", followingPosts);
        return "main";
    }

    @GetMapping("/writePost")
    public String writePost() {
        return "writePost";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("file") MultipartFile file, HttpSession session) {
        String fileName = "";
        String memberId = (String) session.getAttribute("id"); // 세션에서 로그인된 유저 ID 가져오기

        if (!file.isEmpty()) {
            fileName = file.getOriginalFilename();
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO posts (memberId, title, content, fileName) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, memberId);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setString(4, fileName);
            pstmt.executeUpdate();

            // 파일 업로드 처리 코드
            if (!file.isEmpty()) {
                String uploadDir = servletContext.getRealPath("/") + "uploads";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                file.transferTo(new File(uploadDirFile, fileName));
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return "redirect:/main";
    }

    @GetMapping("/editPost")
    public String editPost(@RequestParam("id") int id, Model model) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM posts WHERE id = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PostBean post = new PostBean(rs.getInt("id"), rs.getString("memberId"), rs.getString("title"), rs.getString("content"), rs.getString("fileName"), rs.getString("createdTime"));
                    model.addAttribute("post", post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "editPost";
    }

    @PostMapping("/updatePost")
    public String updatePost(@RequestParam("id") int id, @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("newFile") MultipartFile newFile) {
        String fileName = newFile.getOriginalFilename();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (newFile.getSize() > 0) {
                String uploadPath = servletContext.getRealPath("/") + "uploads";
                // 기존 파일 삭제
                try (PreparedStatement pstmt = conn.prepareStatement("SELECT fileName FROM posts WHERE id = ?")) {
                    pstmt.setInt(1, id);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            String oldFileName = rs.getString("fileName");
                            File oldFile = new File(uploadPath, oldFileName);
                            oldFile.delete();
                        }
                    }
                }
                // 새 파일 업로드
                newFile.transferTo(new File(uploadPath, fileName));
            } else {
                // 새 파일이 업로드되지 않은 경우 기존 파일 유지
                try (PreparedStatement pstmt = conn.prepareStatement("SELECT fileName FROM posts WHERE id = ?")) {
                    pstmt.setInt(1, id);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            fileName = rs.getString("fileName");
                        }
                    }
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE posts SET title = ?, content = ?, fileName = ? WHERE id = ?")) {
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setString(3, fileName);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return "redirect:/main";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("id") int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // 트랜잭션 시작

            try (PreparedStatement deleteCommentLikes = connection.prepareStatement(
                        "DELETE FROM commentlikes WHERE commentId IN (SELECT id FROM comments WHERE postId = ?)");
                 PreparedStatement deleteComments = connection.prepareStatement(
                        "DELETE FROM comments WHERE postId = ?");
                 PreparedStatement deletePostLikes = connection.prepareStatement(
                        "DELETE FROM postlikes WHERE postId = ?");
                 PreparedStatement deletePost = connection.prepareStatement(
                        "DELETE FROM posts WHERE id = ?")) {

                // 댓글의 좋아요 삭제
                deleteCommentLikes.setInt(1, id);
                deleteCommentLikes.executeUpdate();

                // 댓글 삭제
                deleteComments.setInt(1, id);
                deleteComments.executeUpdate();

                // 게시물의 좋아요 삭제
                deletePostLikes.setInt(1, id);
                deletePostLikes.executeUpdate();

                // 게시물 삭제
                deletePost.setInt(1, id);
                deletePost.executeUpdate();

                connection.commit(); // 트랜잭션 커밋

            } catch (SQLException e) {
                connection.rollback(); // 오류 발생 시 트랜잭션 롤백
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/main";
    }

    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam("id") int id, HttpServletResponse response) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT fileName FROM posts WHERE id = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String fileName = rs.getString("fileName");
                    String uploadPath = servletContext.getRealPath("/") + "uploads";
                    File file = new File(uploadPath, fileName);
                    if (file.exists()) {
                        // 다운로드할 파일 이름 설정
                        response.setContentType("application/octet-stream");
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                        // 파일 내용을 response 스트림으로 전송
                        try (InputStream inStream = new FileInputStream(file); OutputStream outStream = response.getOutputStream()) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inStream.read(buffer)) != -1) {
                                outStream.write(buffer, 0, bytesRead);
                            }
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/openPost")
    public String getopenPost(Model model, HttpSession session, @RequestParam("id") int id){ // 여기서 id는 posts 테이블의 id.
        PostBean post = null;
        CommentBean comment = null;
        List<String> postLikesList = new ArrayList<>();
        List<CommentBean> postCommentsList = new ArrayList<>();


        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement("select * from posts where id = ?");
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) { // 현재 post의 id를 세션에 등록하기 위함.
                if (rs.next()) {
                    post = new PostBean(rs.getInt("id"), rs.getString("memberId"), rs.getString("title"), rs.getString("content"), rs.getString("fileName"), rs.getString("createdtime"));
                    session.setAttribute("postId", rs.getInt("id"));
                    System.out.println("session id: " + (session.getAttribute("id")));
                    System.out.println("post member : " + rs.getString("memberId"));
                }
            }

            PreparedStatement pstmt2 = conn.prepareStatement("SELECT memberId FROM postLikes WHERE postId = ?"); // postId(게시물).
            pstmt2.setInt(1, id);
            try (ResultSet rs = pstmt2.executeQuery()) { // 현재 post에 등록된 좋아요를 알기 위함이다.
                while (rs.next()) {
                    postLikesList.add(rs.getString("memberId"));
                }
            }

            PreparedStatement pstmt3 = conn.prepareStatement("SELECT * FROM comments WHERE postId = ? ORDER BY createdTime DESC"); // postId(게시물).
            pstmt3.setInt(1, id);
            try (ResultSet rs = pstmt3.executeQuery()) { // 현재 post에 등록된 댓글들을 알기 위함이다.
                while (rs.next()) {
                    List<String> postCommentLikesList = new ArrayList<>(); // 각 댓글에 대해 좋아요 누른 인원들의 리스트를 담음
                    int commentId = rs.getInt("id");

                    PreparedStatement pstmt4 = conn.prepareStatement("SELECT * FROM commentLikes WHERE commentId = ?");
                    pstmt4.setInt(1, commentId);
                    try (ResultSet rs2 = pstmt4.executeQuery()) { // 해당 댓글에 대해 좋아요를 누른 인원들을 파악하기 위함이다.
                        while(rs2.next()) {
                            postCommentLikesList.add(rs2.getString("memberId")); // 해당 댓에 대해 좋아요를 누른 인원(memberId) 가져오기
                        }
                    } finally {
                        pstmt4.close();
                    }
                    comment = new CommentBean(rs.getInt("id"), commentId, rs.getString("memberId"), rs.getString("createdTime"), rs.getString("comment"), postCommentLikesList);
                    postCommentsList.add(comment);
                }
            } finally {
                pstmt3.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("postLikesList", postLikesList); // 게시물 좋아요 누른 계정들 리스트.
        session.setAttribute("postLikesCount", postLikesList.size()); // 게시물 좋아요 수.
        session.setAttribute("postCommentsList", postCommentsList); // 게시물의 댓글들 comment 리스트.
        session.setAttribute("postCommentsCount", postCommentsList.size()); // 게시물 댓글 수.
        model.addAttribute("post", post);

        System.out.println("openPost start");
        System.out.println("postCommentsCount : " + session.getAttribute("postCommentsCount"));
        System.out.println("postLikesList : " + session.getAttribute("postLikesList"));
        System.out.println("login : " + session.getAttribute("id"));
        return "openPost";
    }
}