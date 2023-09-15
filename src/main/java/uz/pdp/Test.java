package uz.pdp;

import com.github.javafaker.Faker;
import uz.pdp.config.ObjectsConfig;
import uz.pdp.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Test {
    private Connection con = new ObjectsConfig().getConnection();

    public static void main(String[] args) {
        try {
            new Test().test();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void test() throws SQLException {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            User user = User.builder()
                    .email(faker.internet().emailAddress())
                    .password(faker.internet().password())
                    .fullName(faker.name().fullName())
                    .build();
            PreparedStatement ps = con.prepareStatement("INSERT INTO users(full_name,username,password) VALUES (?,?,?)");
            ps.setString(1, user.fullName);
            ps.setString(2, user.email);
            ps.setString(3, user.password);
            ps.executeUpdate();
        }
    }
}
