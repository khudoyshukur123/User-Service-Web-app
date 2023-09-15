package uz.pdp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
@RequestMapping(value = "reg")
@RequiredArgsConstructor
public class Register {
    private final Connection con;

    @GetMapping
    public String getReg() {
        return "RegisterForm";
    }

    @RequestMapping(method = {RequestMethod.POST})
    public ModelAndView postReg(@RequestParam("fullName") String fullName,
                                @RequestParam("email") String username,
                                @RequestParam("password") String password) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=?");
            ps.setString(1, username);
            ResultSet set = ps.executeQuery();
            if (set.next()) {
                modelAndView.setViewName("RegisterForm");
                modelAndView.addObject("message", "This username already exists");
            } else {
                modelAndView.addObject("message", fullName);
                modelAndView.setViewName("Cabinet");
                PreparedStatement ps1 = con.prepareStatement("INSERT INTO users(full_name,username,password) VALUES (?,?,?)");
                ps1.setString(1, fullName);
                ps1.setString(2, username);
                ps1.setString(3, password);
                ps1.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modelAndView;
    }
}
