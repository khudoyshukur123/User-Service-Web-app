package uz.pdp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.dao.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Controller
@RequiredArgsConstructor

public class UserService {
    private final Connection connection;

    @RequestMapping("/users")
    public ModelAndView getUsers(@RequestParam(value = "message", required = false) String message) throws SQLException {
        ModelAndView modelAndView = new ModelAndView("Users");
        modelAndView.addObject("users", new UserDAO().getAllUsers());
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping(value = "delete")
    public ModelAndView deleteUser(@RequestParam("email") String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE username=?");
        ps.setString(1, email);
        ps.executeUpdate();
        ModelAndView mv = new ModelAndView("redirect:" + "http://localhost:8080/users");
        mv.addObject("message", "Deleted successfully");
        return mv;
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    public ModelAndView updateUser(@RequestParam("email") String email,
                                   HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("UserUpdate");
        mv.addObject("email", email);
        String referer = request.getHeader("Referer");
        mv.addObject("referer", referer);
        return mv;
    }

    @RequestMapping(value = "updated", method = RequestMethod.POST)
    public ModelAndView updateUser(@RequestParam("fullName") String fullName,
                                   @RequestParam("email") String username,
                                   @RequestParam("password") String password
            /* @RequestParam("referer") String referer*/) throws SQLException {

        /*System.out.println(referer);*/
        ModelAndView modelAndView = new ModelAndView("redirect:" + "http://localhost:8080/users");
        PreparedStatement ps;
        if (password != null && !password.isBlank() && fullName != null && !fullName.isBlank()) {
            ps = connection.prepareStatement("UPDATE users SET full_name=?,password=? WHERE username=?");
            ps.setString(1, fullName);
            ps.setString(2, password);
            ps.setString(3, username);
        } else if (fullName != null && !fullName.isBlank()) {
            ps = connection.prepareStatement("UPDATE users SET full_name=? WHERE username=?");
            ps.setString(1, fullName);
            ps.setString(2, username);
        } else if (password != null && !password.isBlank()) {
            ps = connection.prepareStatement("UPDATE users SET password=? WHERE username=?");
            ps.setString(1, password);
            ps.setString(2, username);
        } else {
            modelAndView.addObject("message", "Unsuccessfull");
            return modelAndView;
        }
        ps.executeUpdate();
        modelAndView.addObject("message", "Successfully changed!");
        modelAndView.addObject("users", new UserDAO().getAllUsers());
        return modelAndView;
    }
}
