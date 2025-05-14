package dal;

import be.Operator;
import be.QualityControl;
import be.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO {

    public boolean validateQualityControlUser(String username, String password) {
        String sql = "SELECT Role FROM LoginInfo WHERE Username = ? AND Password = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("Role");

                try {
                    UserRole role = UserRole.fromString(roleFromDB);
                    return role == UserRole.QUALITY_CONTROL;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean validateOperatorUser(String username, String password) {
        String sql = "SELECT Role FROM LoginInfo WHERE Username = ? AND Password = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("Role");
                try {
                    UserRole role = UserRole.fromString(roleFromDB);
                    return role == UserRole.OPERATOR;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean validateAdminUser(String username, String password) {
        String sql = "SELECT Role FROM LoginInfo WHERE Username = ? AND Password = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("Role");

                try {
                    UserRole role = UserRole.fromString(roleFromDB);
                    return role == UserRole.ADMIN;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT UserId, Username, Role, FirstName, LastName FROM LoginInfo";

        try (Connection conn = DBAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("UserId");
                String username = rs.getString("Username");
                String role = rs.getString("Role");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");

                switch (role.toLowerCase()) {
                    case "operator":
                        users.add(new Operator(id, username, role, null, firstName, lastName));
                        break;
                    case "quality control":
                        users.add(new QualityControl(id, username, role, firstName, lastName));
                        break;
                    case "admin":
                        break;
                    default:
                        System.out.println("Unknown role: " + role);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void addUser(User user, String password) {
        try(Connection conn = DBAccess.DBConnection()) {
            String sql = "INSERT INTO LoginInfo (Username, Password, Role, FirstName, LastName, Email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getName());
            stmt.setString(2, password);
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
