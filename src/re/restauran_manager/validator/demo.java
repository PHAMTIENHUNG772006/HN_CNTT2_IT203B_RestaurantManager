package re.restauran_manager.validator;

import re.restauran_manager.utils.ColorConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class demo {
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
}
