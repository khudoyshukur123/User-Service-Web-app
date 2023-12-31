package uz.pdp.dao;

import uz.pdp.config.ObjectsConfig;
import uz.pdp.dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    Connection con = new ObjectsConfig().getConnection();

    public List<User> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            Statement statement = con.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM users ORDER BY id");
            while (set.next()) {
                User user = User.builder()
                        .email(set.getString("username"))
                        .fullName(set.getString("full_name"))
                        .password(set.getString("password"))
                        .build();
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
