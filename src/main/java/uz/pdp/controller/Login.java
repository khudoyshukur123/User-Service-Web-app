package uz.pdp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequestMapping(value = "/login")
@Controller
@RequiredArgsConstructor
public class Login {
    private final Connection con;

    @Autowired
    public Login(JdbcTemplate jdbcTemplate) throws SQLException {
        con = jdbcTemplate.getDataSource().getConnection();
    }

    @GetMapping
    public String getLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "LoginForm";
    }

    @PostMapping
    public ModelAndView postLogin(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? and password=?");
            ps.setString(1, user.email);
            ps.setString(2, user.password);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                String name = rs.getString("full_name");
                modelAndView.setViewName("Cabinet");
                modelAndView.addObject("message", name);
            } else {
                modelAndView.setViewName("LoginForm");
                modelAndView.addObject("message", "Something went wrong.Please try again!");
            }
            return modelAndView;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
