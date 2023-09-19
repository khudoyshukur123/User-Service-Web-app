package uz.pdp;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uz.pdp.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Component
public class Test {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        try {
            new Test().test();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void test() throws SQLException {
        Connection con = jdbcTemplate.getDataSource().getConnection();
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
