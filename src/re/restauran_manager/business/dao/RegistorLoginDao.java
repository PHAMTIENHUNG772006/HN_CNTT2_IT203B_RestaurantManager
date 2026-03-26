package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.Account;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistorLoginDao implements IRegistorLogin {

    private static RegistorLoginDao instance;

    private RegistorLoginDao() {
    }

    public static RegistorLoginDao getInstance(){
        if (instance == null) {
            instance = new RegistorLoginDao();
        }
        return instance;
    }

    @Override
    public boolean registor(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản cần đăng ký.");
        }

        String sql = "INSERT INTO account(account_name, email, password, role, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, account.getAccount_name());
            pre.setString(2, account.getEmail());
            pre.setString(3, account.getPassword());
            pre.setString(4, account.getRole().name());
            pre.setString(5, account.getStatus().name());

            int affectedRows = pre.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Đăng ký thất bại, không có dòng nào được thêm.");
            }


            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account login(String email, String password) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;


        try {
            conn = DB_Connection.openConnection();
            String sql = "select account_name,email,password,role,status from account where email = ? and password = ?";
            pre = conn.prepareStatement(sql);

            pre.setString(1, email);
            pre.setString(2, password);

            rs = pre.executeQuery();

            if (rs.next()) {
                Account account = new Account();

                account.setAccount_id(rs.getInt("account_id"));
                account.setAccount_name(rs.getString("account_name"));
                account.setAccount_name(rs.getString("email"));
                account.setAccount_name(rs.getString("role"));
                account.setAccount_name(rs.getString("status"));

                return account;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pre != null) pre.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Account findById(int id) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;

        if (id <= 0) {
            throw new IllegalArgumentException("ID không tồn tại..");
        }

        try {
            conn = DB_Connection.openConnection();
            String sql = "SELECT account_id, account_name, email, email, role, status FROM account WHERE account_id = ?";
            pre.setInt(1, id);
            pre = conn.prepareStatement(sql);

            rs = pre.executeQuery();

            if (rs.next()) {
                Account account = new Account();

                account.setAccount_id(rs.getInt("account_id"));
                account.setAccount_name(rs.getString("account_name"));
                account.setAccount_name(rs.getString("email"));
                account.setAccount_name(rs.getString("role"));
                account.setAccount_name(rs.getString("status"));
                return account;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pre != null) pre.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
