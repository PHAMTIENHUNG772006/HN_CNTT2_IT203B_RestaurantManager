package re.restauran_manager.business.dao;

import re.restauran_manager.model.dto.OrderDetailDisplay;
import re.restauran_manager.model.enties.OrderDetails;
import re.restauran_manager.model.enums.OrderDetailStatus;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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



    public OrderDetails findOrderDetailById(int id) {
        String sql = "SELECT id, order_id, food_id, quantity, status FROM order_details WHERE id = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    OrderDetails item = new OrderDetails();

                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setItemId(rs.getInt("food_id"));
                    item.setQuantity(rs.getInt("quantity"));

                    String statusStr = rs.getString("status");
                    if (statusStr != null) {
                        item.setStatus(OrderDetailStatus.valueOf(statusStr.toUpperCase()));
                    }
                    return item;
                }
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi khi lấy thông tin chi tiết: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }


    public List<OrderDetailDisplay> showOrderQueue() {
        List<OrderDetailDisplay> list = new ArrayList<>();

        String sql = "SELECT MIN(od.id) as display_id, od.order_id, o.table_id, m.food_name, " +
                "SUM(od.quantity) as total_qty, od.status " +
                "FROM order_details od " +
                "JOIN orders o ON od.order_id = o.order_id " +
                "JOIN menu_items m ON od.food_id = m.food_id " +
                "WHERE od.status IN ('PENDING', 'COOKING', 'READY') " +
                "GROUP BY od.order_id, m.food_id, od.status " +
                "ORDER BY o.order_date ASC";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {

            while (rs.next()) {
                OrderDetailDisplay item = new OrderDetailDisplay();

                item.setOrderId(rs.getInt("display_id"));

                item.setTableId(rs.getInt("table_id"));
                item.setFoodName(rs.getString("food_name"));

                // QUAN TRỌNG: Lấy từ alias "total_qty"
                item.setQuantity(rs.getInt("total_qty"));

                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    item.setStatus(OrderDetailStatus.valueOf(statusStr.toUpperCase()));
                }
                list.add(item);
            }

            if (list.isEmpty()) {
                System.out.println(ColorConstants.WARNING + "Hiện tại không có món nào đang chờ!" + ColorConstants.RESET);
            }

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi truy vấn hàng đợi: " + e.getMessage() + ColorConstants.RESET);
        }
        return list;
    }

    public boolean updateStatus(int order_detail_id, String newStatus) {
        String sql = "UPDATE Order_Details SET status = ? WHERE id = ?";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, order_detail_id);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Update Status: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }


    public boolean updateStock(int id, int stock) {
        String sql = "UPDATE menu_items SET stock = ? WHERE food_id = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stock);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }


}
