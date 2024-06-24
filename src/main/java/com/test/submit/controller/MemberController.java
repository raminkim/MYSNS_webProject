package com.test.submit.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class MemberController {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/submitDB";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "1234";

    @PostMapping("/Mainlogin")
    public String login(@RequestParam("id") String id,
                        @RequestParam("passwd") String passwd,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)) {
            String query = "SELECT * FROM members WHERE id = ? AND passwd = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, id);
                statement.setString(2, passwd);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name"); // name 컬럼의 값을 가져오기.
                        String follower = resultSet.getString("follower"); // follower 컬럼의 값을 가져오기.
                        HttpSession session = request.getSession(); // 로그인 성공시 세션에 유저 네임 삽입
                        session.setAttribute("id", id);
                        session.setAttribute("name", name);
                        session.setAttribute("follower", follower);
                        return "redirect:/main";
                    } else {
                        // 로그인 실패 메시지 추가
                        redirectAttributes.addFlashAttribute("loginError", "아이디 또는 비밀번호가 잘못되었습니다.");
                        // 로그인 페이지로 리다이렉트
                        return "redirect:/Mainlogin"; // 리다이렉트로 login GET 요청 처리 필요.
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 로그인 실패 시 다시 메인 페이지로 이동
        return "redirect:/Mainlogin";
    }
    @GetMapping("/Mainlogin")
    public String getLogin(Model model) {
        return "Mainlogin";
    }
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }
    @PostMapping("/register")
    public String postRegister(@RequestParam("id") String id,
                               @RequestParam("passwd") String passwd,
                               @RequestParam("name") String name,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)) {
            String query = "SELECT * FROM members WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // 사용자 이미 존재
                        redirectAttributes.addFlashAttribute("registerError", "이미 존재하는 아이디입니다.");
                        return "redirect:/register"; // 회원가입 페이지로 리다이렉트
                    } else {
                        // 해당 아이디를 사용하는 사용자가 없다면, 새로운 사용자 삽입
                        query = "INSERT INTO members (id, passwd, name) VALUES(?, ?, ?)";
                        try (PreparedStatement psmt = connection.prepareStatement(query)) {
                            psmt.setString(1, id);
                            psmt.setString(2, passwd);
                            psmt.setString(3, name);
    
                            int result = psmt.executeUpdate();
                            if (result > 0) {
                                return "redirect:/register_success";
                            } else {
                                redirectAttributes.addFlashAttribute("registerError", "서버 오류가 발생했습니다. 다시 시도해주세요.");
                                return "redirect:/register";
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("registerError", "서버 오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/register";
        }
    }
    
    @GetMapping("/register_success")
    public String registerSuccess() {
        return "register_success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/Mainlogin"; // 로그아웃 후 다시 로그인 페이지로 리다이렉트
    }
}
