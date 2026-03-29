package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.model.enums.TableStatus;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CustomerDao {

    public static List<Table> listTableFree() {
        List<Table> tables = TableDao.getInstance().findByStatus(TableStatus.FREE);
        if (tables.isEmpty()) {
            return null;
        }
        return tables;
    }

    public static boolean createOrder(int customerId, int tableId) {
        Table table = TableDao.getInstance().findById(tableId);
        Account customer = AccountDao.getInstance().findById(customerId);

        if (table == null || table.getStatus() != TableStatus.FREE) {
            System.out.println(ColorConstants.ERROR + "Bàn không tồn tại hoặc đã có khách!" + ColorConstants.RESET);
            return false;
        }

        if (customer == null) {
            System.out.println(ColorConstants.ERROR + "Tài khoản khách hàng không tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "INSERT INTO orders (user_id, table_id, order_date, status) VALUES (?, ?, ?, 'PENDING')";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, tableId);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                TableDao.getInstance().updateStatus(tableId, TableStatus.OCCUPIED);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi tạo đơn hàng: " + e.getMessage() + ColorConstants.RESET);
        }
        return false;
    }
}