package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.model.enums.Category;
import re.restauran_manager.model.enums.FoodEnum;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChefDao {

    private static ChefDao instance;

    private ChefDao() {
    }

    public static ChefDao getInstance() {
        if (instance == null) {
            instance = new ChefDao();
        }
        return instance;
    }

    public Account findByName(String name, AccountRole role) {
        String sql = "SELECT account_id, account_name, password, role, isban FROM account WHERE account_name = ? AND role = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, name);
            pre.setString(2, role.name());
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

    public boolean updateStatus(int foodId, String newStatus) {
        String sql = "UPDATE Menu_items SET status = ? WHERE food_id = ?";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, foodId);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Update Status: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
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
