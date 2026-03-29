package re.restauran_manager.business.dao;

import org.mindrot.jbcrypt.BCrypt;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.utils.DB_Connection;
import re.restauran_manager.utils.ColorConstants;

import java.sql.*;

public class AccountDao {

    private static AccountDao instance;
    private AccountDao() {}

    public static AccountDao getInstance() {
        if (instance == null) instance = new AccountDao();
        return instance;
    }

    public Account findByName(String name) {
        String sql = "SELECT account_id, account_name, password, role, isban FROM account WHERE account_name = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, name);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi khi tìm theo tên: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public Account findById(int id) {
        String sql = "SELECT account_id, account_name, password, role, isban FROM account WHERE account_id = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi khi tìm theo ID: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }


    public boolean register(String username, String password) {
        if (findByName(username) != null) {
            System.out.println(ColorConstants.WARNING + "Tên tài khoản đã tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "INSERT INTO Account (account_name, password, role, isban) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            pstmt.setString(3, AccountRole.CUSTOMER.name());
            pstmt.setBoolean(4, false);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Register: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public Account login(String name, String password) {
        Account account = findByName(name);

        if (account == null) {
            System.out.println(ColorConstants.ERROR + "Tài khoản không tồn tại." + ColorConstants.RESET);
            return null;
        }

        try {
            if (BCrypt.checkpw(password, account.getPassword())) {
                if (account.isBan()) {
                    System.out.println(ColorConstants.ERROR + "Tài khoản đã bị khóa. Vui lòng liên hệ quản lý!" + ColorConstants.RESET);
                    return null;
                }
                return account;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi: Mật khẩu trong hệ thống chưa được mã hóa đúng chuẩn!" + ColorConstants.RESET);
            return null;
        }

        System.out.println(ColorConstants.ERROR + "Tên đăng nhập hoặc mật khẩu không đúng." + ColorConstants.RESET);
        return null;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setAccount_name(rs.getString("account_name"));
        account.setPassword(rs.getString("password"));

        String roleStr = rs.getString("role");
        if (roleStr != null) {
            account.setRole(AccountRole.valueOf(roleStr.toUpperCase()));
        }

        account.setBan(rs.getBoolean("isban"));
        return account;
    }
}