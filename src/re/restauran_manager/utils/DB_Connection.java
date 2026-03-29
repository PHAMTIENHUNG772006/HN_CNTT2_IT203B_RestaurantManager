package re.restauran_manager.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DB_Connection {
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/restaurant_manager?createDatabaseIfNotExist=true";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "admin123";


    public static Connection openConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy Driver: " + e.getMessage() + ColorConstants.RESET);
            throw new RuntimeException("Lỗi nạp Driver JDBC", e);
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi kết nối Database: " + e.getMessage() + ColorConstants.RESET);
            throw new RuntimeException("Lỗi cấu hình URL/User/Pass", e);
        }
    }

    public static void initDB(String filePath) {
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
            content = content.replaceAll("(?m)^--.*", "");

            try (Connection conn = openConnection();
                 Statement stmt = conn.createStatement()) {
                String[] queries = content.split(";");

                StringBuilder currentQuery = new StringBuilder();
                for (String query : queries) {
                    currentQuery.append(query);
                    String sql = currentQuery.toString().trim();

                    if (sql.isEmpty()) continue;
                    if (sql.toUpperCase().contains("BEGIN") && !sql.toUpperCase().contains("END")) {
                        currentQuery.append(";");
                        continue;
                    }

                    stmt.execute(sql);
                    currentQuery.setLength(0);
                }
                System.out.println("Thực thi script " + filePath + " thành công!");
            }
        } catch (Exception e) {
            System.err.println("Lỗi thực thi SQL: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        Connection conn = openConnection();
        initDB("src/script.sql");
    }

}
