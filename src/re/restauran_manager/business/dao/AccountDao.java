package re.restauran_manager.business.dao;

import org.mindrot.jbcrypt.BCrypt;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.utils.DB_Connection;
import re.restauran_manager.utils.ColorConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private static AccountDao instance;

    private AccountDao() {
    }

    public static AccountDao getInstance() {
        if (instance == null) instance = new AccountDao();
        return instance;
    }

    public Account findByName(String name) {
        String sql = "SELECT account_id, account_name, password, role, isban FROM account WHERE account_name = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, name.trim());
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi Database: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public Account findById(int id) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi Database: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public Account login(String name, String password) {
        Account account = findByName(name.trim());

        if (account == null) {
            System.out.println(ColorConstants.ERROR + "Tên đăng nhập hoặc mật khẩu không đúng." + ColorConstants.RESET);
            return null;
        }

        if (account.isBan()) {
            System.out.println(ColorConstants.ERROR + "Tài khoản đã bị khóa!" + ColorConstants.RESET);
            return null;
        }

        try {
            if (BCrypt.checkpw(password, account.getPassword())) {
                return account;
            }
        } catch (Exception e) {
            System.out.println(ColorConstants.ERROR + "Lỗi xác thực hệ thống: " + e.getMessage() + ColorConstants.RESET);
        }

        System.out.println(ColorConstants.ERROR + "Tên đăng nhập hoặc mật khẩu không đúng." + ColorConstants.RESET);
        return null;
    }

    public boolean addAccount(String username, String password, AccountRole role) {
        if (username == null || username.trim().isEmpty() || password == null || role == null) {
            System.out.println(ColorConstants.ERROR + "Dữ liệu không hợp lệ." + ColorConstants.RESET);
            return false;
        }

        if (findByName(username.trim()) != null) {
            System.out.println(ColorConstants.WARNING + "Tên tài khoản đã tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "INSERT INTO account (account_name, password, role, isban) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, username.trim());
            pre.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            pre.setString(3, role.name());
            pre.setBoolean(4, false);

            return pre.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi lưu tài khoản: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }


    public List<Account> getAllAccount() {

        String sql = "SELECT account_id, account_name, password, role, isban FROM account";
        List<Account> accounts = new ArrayList<>();

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pre = conn.prepareStatement(sql)
        ) {

            ResultSet rs = pre.executeQuery();


            while (rs.next()) {
                Account account = mapResultSetToAccount(rs);

                accounts.add(account);
            }

            return accounts;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi truy vấn: " + e.getMessage() + ColorConstants.RESET);
            return null;
        }
    }

    public boolean bandAccount(int id) {
        String sql = "UPDATE account SET isBan = true where account_id = ?";


        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pre = conn.prepareStatement(sql)
        ) {
            pre.setInt(1, id);
            return pre.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi cập nhật tài khoản: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public boolean register(String username, String password) {
        return addAccount(username, password, AccountRole.CUSTOMER);
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