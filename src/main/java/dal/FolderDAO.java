package dal;

import gui.controllers.models.Folder;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class FolderDAO {

        private static final String DB_URL = "jdbc:sqlite:path/to/your/database.db"; // Cambia esto a tu ruta

        public static List<Folder> getAllFolders() {
            List<Folder> folders = new ArrayList<>();

            String query = "SELECT orderNumber, month, year FROM folders"; // Asegúrate de que los nombres coincidan con tu tabla

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String orderNumber = rs.getString("orderNumber");
                    String month = rs.getString("month");
                    String year = rs.getString("year");

                    Folder folder = new Folder(orderNumber, month, year);
                    folders.add(folder);
                }

            } catch (SQLException e) {
                e.printStackTrace(); // o usa Logger
            }

            return folders;
        }
    }

