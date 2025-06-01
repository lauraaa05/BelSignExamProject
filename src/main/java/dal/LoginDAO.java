package dal;

import be.Admin;
import be.Operator;
import be.QualityControl;
import be.User;
import dal.interfaceDAO.ILoginDAO;
import exceptions.DALException;
import utilities.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO implements ILoginDAO {

    public boolean validateUser(String username, String password, UserRole expectedRole) throws DALException {
        String sql = "SELECT ur.RoleName FROM UserLogin ul JOIN UserRoles ur ON ul.Role = ur.Id WHERE ul.Username = ? AND ul.Password = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("RoleName");
                return UserRole.fromString(roleFromDB) == expectedRole;
            }
        } catch (SQLException e) {
            throw new DALException("Failed to validate user", e);
        }
        return false;
    }

    public List<User> getAllUsers() throws DALException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT ul.UserId, ul.Username, ul.Password, ur.RoleName, ul.FirstName, ul.LastName, ul.Email FROM UserLogin ul JOIN UserRoles ur ON ul.Role = ur.Id";

        try (Connection conn = DBAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("UserId");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String role = rs.getString("RoleName");
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
            throw new DALException("Failed to fetch all users", e);
        }
        return users;
    }

    public User addUser(User user, String password) throws DALException{
        try(Connection conn = DBAccess.DBConnection()) {
            String sql = "INSERT INTO UserLogin (Username, Password, Role, FirstName, LastName, Email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getName());
            stmt.setString(2, password);
            int roleId = getRoleIdByName(user.getRole());
            stmt.setInt(3, roleId);
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getEmail());

            stmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUser(User user) throws DALException{
        String sql = "UPDATE UserLogin SET Username = ?, Password = ?, Role = ?, FirstName = ?, LastName = ?, Email = ? WHERE UserId = ?";

        try(Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, getRoleIdByName(user.getRole()));
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

    public User deleteUser(User user) {
        try(Connection conn = DBAccess.DBConnection()) {
            String sql = "DELETE FROM UserLogin WHERE UserId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Operator getOperatorByUsername(String username) throws DALException {
        return DBHelper.getUserByUsername(username, rs -> {
            try {
                Operator operator = new Operator(
                        rs.getInt("UserId"),
                        rs.getString("Username"),
                        rs.getString("RoleName"),
                        null,
                        rs.getString("FirstName"),
                        rs.getString("LastName")
                );
                operator.setPassword(rs.getString("Password"));
                return operator;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public QualityControl getQCUByUsername(String username) throws DALException {
        return DBHelper.getUserByUsername(username, rs -> {
            try {
                return new QualityControl(
                        rs.getInt("UserId"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("RoleName"),
                        rs.getString("FirstName"),
                        rs.getString("LastName")
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Admin getAdminByUsername(String username) throws DALException {
        return DBHelper.getUserByUsername(username, rs -> {
            try {
                return new Admin(
                        rs.getInt("UserId"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("RoleName"),
                        rs.getString("FirstName")
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int getRoleIdByName(String roleName) throws DALException {
        String sql = "SELECT Id FROM UserRoles WHERE RoleName = ?";
        try (Connection conn = DBAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id");
            } else  {
                throw new DALException("Role not found: " + roleName);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to fetch role id by name: " + roleName, e);
        }
    }

    public UserRole getUserRole(String username, String password) throws DALException {
        String sql = "SELECT ur.RoleName FROM UserLogin ul JOIN UserRoles ur ON ul.Role = ur.Id WHERE ul.Username = ? AND ul.Password = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("RoleName");
                return UserRole.fromString(roleFromDB);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to validate user", e);
        }
        return null;
    }
}