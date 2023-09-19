package uz.pdp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
@RequestMapping(value = "reg")
@RequiredArgsConstructor
public class Register {
    private final Connection con;

    @Autowired
    public Register(JdbcTemplate jdbcTemplate) throws SQLException {
        this.con = jdbcTemplate.getDataSource().getConnection();
    }

    @GetMapping
    public String getReg(Model model) {
        model.addAttribute("user", new User());
        return "RegisterForm";
    }

    @PostMapping
    public ModelAndView postReg(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(user);
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=?");
            ps.setString(1, user.email);
            ResultSet set = ps.executeQuery();
            if (set.next()) {
                modelAndView.setViewName("RegisterForm");
                modelAndView.addObject("message", "This username already exists");
            } else {
                modelAndView.addObject("message", user.fullName);
                modelAndView.setViewName("Cabinet");
                PreparedStatement ps1 = con.prepareStatement("INSERT INTO users(full_name,username,password) VALUES (?,?,?)");
                ps1.setString(1, user.fullName);
                ps1.setString(2, user.email);
                ps1.setString(3, user.password);
                ps1.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modelAndView;
    }
}
