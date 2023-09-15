package uz.pdp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequestMapping(value = "/login")
@Controller
@RequiredArgsConstructor
public class Login {
    private final Connection con;

    @GetMapping
    public String getLoginForm() {
        return "LoginForm";
    }


    @PostMapping
    public ModelAndView postLogin(@RequestParam("email") String username,
                                  @RequestParam("password") String password) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? and password=?");
            ps.setString(1, username);
            ps.setString(2, password);
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
