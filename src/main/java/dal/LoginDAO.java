package dal;

import be.Admin;
import be.Operator;
import be.QualityControl;
import be.User;
import utilities.DBHelper;

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
        String sql = "SELECT UserId, Username, Password, Role, FirstName, LastName, Email FROM LoginInfo";

        try (Connection conn = DBAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("UserId");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String role = rs.getString("Role");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");

                switch (role.toLowerCase()) {
                    case "operator":
                        Operator operator = new Operator(id, username, role, null, firstName, lastName);
                        operator.setPassword(password);
                        operator.setEmail(email);
                        users.add(operator);
                        break;
                    case "quality control":
                        QualityControl qc = new QualityControl(id, username, role, firstName, lastName);
                        qc.setPassword(password);
                        qc.setEmail(email);
                        users.add(qc);
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

    public boolean updateUser(User user) {
        String sql = "UPDATE LoginInfo SET Username = ?, Password = ?, Role = ?, FirstName = ?, LastName = ?, Email = ? WHERE UserId = ?";

        try(Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getEmail());
            stmt.setInt(7, user.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteUser(User user) {
        try(Connection conn = DBAccess.DBConnection()) {
            String sql = "DELETE FROM LoginInfo WHERE UserId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Operator getOperatorByUsername(String username) {
        return DBHelper.getUserByUsername(username, rs -> {
            try {
                return new Operator(
                        rs.getInt("UserId"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getString("FirstName"),
                        null
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public QualityControl getQCUByUsername(String username) {
        return DBHelper.getUserByUsername(username, rs -> {
            try {
                return new QualityControl(
                        rs.getInt("UserId"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getString("FirstName"),
                        rs.getString("LastName")
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Admin getAdminByUsername(String username) {
        return DBHelper.getUserByUsername(username, rs -> {
            try {
                return new Admin(
                        rs.getInt("UserId"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getString("FirstName")
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
