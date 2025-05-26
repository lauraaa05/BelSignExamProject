package utilities;

import dal.DBAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class DBHelper {

    public static <T> T getUserByUsername(String username, Function<ResultSet, T> mapper) {
        T user = null;
        String sql = "SELECT * FROM LoginInfo WHERE Username = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapper.apply(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}