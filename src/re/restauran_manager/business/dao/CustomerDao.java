package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.*;
import re.restauran_manager.model.enums.Category;
import re.restauran_manager.model.enums.OrderDetailStatus;
import re.restauran_manager.model.enums.OrderStatus;
import re.restauran_manager.model.enums.TableStatus;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private static CustomerDao instance;

    private CustomerDao() {}

    public static  CustomerDao getInstance() {
        if (instance == null) {
            instance = new CustomerDao();
        }
        return instance;
    }

    // ================= TABLE =================
    public List<Table> listTableFree() {
        return TableDao.getInstance().findByStatus(TableStatus.FREE);
    }

    // ================= MENU (Hỗ trợ phân trang) =================
    public List<MenuItems> getAllAvailableFood() {
        String sql = "SELECT food_id, food_name, price, category, stock FROM menu_items WHERE stock > 0";
        List<MenuItems> foods = new ArrayList<>();
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {

            while (rs.next()) {
                foods.add(mapResultSetToMenuItem(rs));
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi lấy danh sách món: " + e.getMessage() + ColorConstants.RESET);
        }
        return foods;
    }


    // ================= ORDER =================
    public Orders findOrder(int id) {
        String sql = "SELECT order_id, table_id, order_date, status, user_id FROM Orders WHERE order_id = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    Orders order = new Orders();
                    order.setId(rs.getInt("order_id"));
                    order.setTableId(rs.getInt("table_id"));
                    order.setUserId(rs.getInt("user_id"));

                    if (rs.getTimestamp("order_date") != null)
                        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());

                    String statusStr = rs.getString("status");
                    if (statusStr != null)
                        order.setStatus(OrderStatus.valueOf(statusStr.toUpperCase()));

                    return order;
                }
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi tìm đơn: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public List<Orders> getActiveOrders(int account_id) {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT order_id, table_id, order_date, status, user_id FROM Orders " +
                "WHERE status = 'PENDING' AND user_id = ? ORDER BY order_date DESC";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setInt(1, account_id);
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                Orders order = new Orders();
                order.setId(rs.getInt("order_id"));
                order.setTableId(rs.getInt("table_id"));
                order.setUserId(rs.getInt("user_id"));

                if (rs.getTimestamp("order_date") != null)
                    order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());

                order.setStatus(OrderStatus.valueOf(rs.getString("status").toUpperCase()));
                list.add(order);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi lấy danh sách đơn hàng: " + e.getMessage() + ColorConstants.RESET);
        }
        return list;
    }

    public int createOrder(int customerId, int tableId) {
        Table table = TableDao.getInstance().findById(tableId);
        if (table == null || table.getStatus() != TableStatus.FREE) {
            System.out.println(ColorConstants.ERROR + "Bàn không trống!" + ColorConstants.RESET);
            return -1;
        }

        String sqlOrder = "INSERT INTO orders (user_id, table_id, total_amount, order_date, status) VALUES (?, ?, 0.0, NOW(), 'PENDING')";
        String sqlUpdateTable = "UPDATE Tables SET status = 'OCCUPIED' WHERE table_id = ?";

        Connection conn = null;
        try {
            conn = DB_Connection.openConnection();
            conn.setAutoCommit(false);
            int generatedId = -1;

            try (PreparedStatement pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOrder.setInt(1, customerId);
                pstmtOrder.setInt(2, tableId);
                pstmtOrder.executeUpdate();
                ResultSet rs = pstmtOrder.getGeneratedKeys();
                if (rs.next()) generatedId = rs.getInt(1);
            }

            try (PreparedStatement pstmtTable = conn.prepareStatement(sqlUpdateTable)) {
                pstmtTable.setInt(1, tableId);
                pstmtTable.executeUpdate();
            }

            conn.commit();
            return generatedId;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {
                System.out.println(ColorConstants.ERROR + "Lỗi Rollback: " + ex.getMessage() + ColorConstants.RESET);
            }
            System.out.println(ColorConstants.ERROR + "Lỗi tạo đơn: " + e.getMessage() + ColorConstants.RESET);
            return -1;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    public boolean addListFoodToOrder(int orderId, List<MenuItems> items) {
        Connection conn = null;
        PreparedStatement pstmtCheck = null;
        PreparedStatement pstmtUpdate = null;
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtTotal = null;
        PreparedStatement pstmtStock = null;
        ResultSet rs = null;

        String sqlCheck = "SELECT id, quantity FROM Order_Details WHERE order_id = ? AND food_id = ? AND status = 'PENDING'";
        String sqlUpdate = "UPDATE Order_Details SET quantity = ? WHERE id = ?";
        String sqlInsert = "INSERT INTO Order_Details (order_id, food_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String sqlUpdateTotal = "UPDATE Orders SET total_amount = total_amount + ? WHERE order_id = ?";
        String sqlUpdateStock = "UPDATE Menu_items SET stock = stock - ? WHERE food_id = ?";

        try {
            conn = DB_Connection.openConnection();
            conn.setAutoCommit(false);

            pstmtCheck = conn.prepareStatement(sqlCheck);
            pstmtUpdate = conn.prepareStatement(sqlUpdate);
            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtTotal = conn.prepareStatement(sqlUpdateTotal);
            pstmtStock = conn.prepareStatement(sqlUpdateStock);

            for (MenuItems item : items) {
                pstmtCheck.setInt(1, orderId);
                pstmtCheck.setInt(2, item.getFood_id());
                rs = pstmtCheck.executeQuery();

                if (rs.next()) {
                    // Gộp số lượng nếu đang ở trạng thái PENDING
                    int detailId = rs.getInt("id");
                    int currentQty = rs.getInt("quantity");
                    pstmtUpdate.setInt(1, currentQty + item.getStock());
                    pstmtUpdate.setInt(2, detailId);
                    pstmtUpdate.executeUpdate();
                } else {
                    // Tách dòng mới nếu chưa có hoặc trạng thái khác PENDING
                    pstmtInsert.setInt(1, orderId);
                    pstmtInsert.setInt(2, item.getFood_id());
                    pstmtInsert.setInt(3, item.getStock());
                    pstmtInsert.setDouble(4, item.getPrice());
                    pstmtInsert.executeUpdate();
                }
                rs.close();

                // 2. Cập nhật tổng tiền hóa đơn cho món này
                pstmtTotal.setDouble(1, item.getStock() * item.getPrice());
                pstmtTotal.setInt(2, orderId);
                pstmtTotal.executeUpdate();

                // 3. Cập nhật tồn kho thực tế của nhà hàng
                pstmtStock.setInt(1, item.getStock());
                pstmtStock.setInt(2, item.getFood_id());
                pstmtStock.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    System.out.println(ColorConstants.ERROR + "Lỗi Rollback: " + ex.getMessage() + ColorConstants.RESET);
                }
            }
            System.out.println(ColorConstants.ERROR + "Lỗi thêm món: " + e.getMessage() + ColorConstants.RESET);
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtCheck != null) pstmtCheck.close();
                if (pstmtUpdate != null) pstmtUpdate.close();
                if (pstmtInsert != null) pstmtInsert.close();
                if (pstmtTotal != null) pstmtTotal.close();
                if (pstmtStock != null) pstmtStock.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
    }

    private void updateOrderTotalAmount(Connection conn, int orderId) throws SQLException {
        String sqlUpdate = "UPDATE orders SET total_amount = (SELECT COALESCE(SUM(quantity * unit_price), 0) " +
                "FROM order_details WHERE order_id = ? AND status != 'CANCEL') WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }
    }

    public List<OrderDetailDisplay> getCurrentOrderDetails(int orderId) {
        List<OrderDetailDisplay> list = new ArrayList<>();
        // Sử dụng SUM(od.quantity) và GROUP BY để gộp các món trùng trạng thái
        String sql = "SELECT od.order_id, o.table_id, m.food_name, SUM(od.quantity) AS total_quantity, " +
                "od.unit_price, od.status " +
                "FROM order_details od " +
                "JOIN orders o ON od.order_id = o.order_id " +
                "JOIN menu_items m ON od.food_id = m.food_id " +
                "WHERE od.order_id = ? " +
                "GROUP BY m.food_id, od.status, od.unit_price";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, orderId);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                OrderDetailDisplay item = new OrderDetailDisplay();
                item.setOrderId(rs.getInt("order_id"));
                item.setTableId(rs.getInt("table_id"));
                item.setFoodName(rs.getString("food_name"));
                item.setQuantity(rs.getInt("total_quantity"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setStatus(OrderDetailStatus.valueOf(rs.getString("status").toUpperCase()));
                list.add(item);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi lấy chi tiết hóa đơn: " + e.getMessage() + ColorConstants.RESET);
        }
        return list;
    }

    public int countUnfinishedDishes(int orderId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM order_details WHERE order_id = ? AND status IN ('PENDING', 'COOKING', 'READY')";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, orderId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi đếm món: " + e.getMessage() + ColorConstants.RESET);
        }
        return 0;
    }

    public boolean cancelFood(int orderId, int foodId, int quantityToCancel) {
        String sqlCheck = "SELECT quantity FROM order_details WHERE order_id = ? AND food_id = ? AND status = 'PENDING'";
        String sqlDelete = "DELETE FROM order_details WHERE order_id = ? AND food_id = ?";
        String sqlUpdateQty = "UPDATE order_details SET quantity = quantity - ? WHERE order_id = ? AND food_id = ?";
        String sqlUpdateStock = "UPDATE menu_items SET stock = stock + ? WHERE food_id = ?";

        Connection conn = null;
        try {
            conn = DB_Connection.openConnection();
            conn.setAutoCommit(false);

            // 1. Lấy số lượng hiện tại
            int currentQty = 0;
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, orderId);
                psCheck.setInt(2, foodId);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) currentQty = rs.getInt("quantity");
                else return false;
            }

            // 2. Kiểm tra tính hợp lệ
            if (quantityToCancel > currentQty || quantityToCancel <= 0) return false;

            // 3. XỬ LÝ CHÍNH XÁC:
            if (quantityToCancel == currentQty) {
                // Nếu hủy hết thì xóa luôn dòng này, không để quantity về 0
                try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                    psDel.setInt(1, orderId);
                    psDel.setInt(2, foodId);
                    psDel.executeUpdate();
                }
            } else {
                // Nếu chỉ hủy một phần thì mới Update giảm số lượng
                try (PreparedStatement psUp = conn.prepareStatement(sqlUpdateQty)) {
                    psUp.setInt(1, quantityToCancel);
                    psUp.setInt(2, orderId);
                    psUp.setInt(3, foodId);
                    psUp.executeUpdate();
                }
            }

            // 4. Hoàn kho và cập nhật tiền
            try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                psStock.setInt(1, quantityToCancel);
                psStock.setInt(2, foodId);
                psStock.executeUpdate();
            }

            updateOrderTotalAmount(conn, orderId);
            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {
                System.out.println(ColorConstants.ERROR + "Lỗi Rollback: " + ex.getMessage() + ColorConstants.RESET);
            }
            System.out.println(ColorConstants.ERROR + "Lỗi hủy món: " + e.getMessage() + ColorConstants.RESET);
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }

    public OrderDetails findOrderDetail(int orderId, int foodId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ? AND food_id = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, orderId);
            pre.setInt(2, foodId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                OrderDetails od = new OrderDetails();
                od.setOrderId(orderId);
                od.setItemId(foodId);
                od.setQuantity(rs.getInt("quantity"));
                od.setStatus(OrderDetailStatus.valueOf(rs.getString("status").toUpperCase()));
                return od;
            }
        } catch (Exception e) {
            System.out.println(ColorConstants.ERROR + "Lỗi tìm chi tiết món: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public double processCheckout(int tableId, int orderId, boolean cancelUnfinished) {
        // 1. Chỉ hủy các món PENDING hoặc WAITING_APPROVAL
        String sqlCancel = "UPDATE Order_Details SET status = 'CANCEL' " +
                "WHERE order_id = ? AND status IN ('PENDING', 'WAITING_APPROVAL')";

        String sqlPaid = "UPDATE Orders SET status = 'PAID' WHERE order_id = ?";
        String sqlFreeTable = "UPDATE Tables SET status = 'FREE' WHERE table_id = ?";

        // 2. Lấy chi tiết hóa đơn: Bao gồm tất cả các món ĐÃ NẤU hoặc ĐÃ PHỤC VỤ
        String sqlGetDetails = "SELECT m.food_name, od.quantity, od.unit_price, od.status, " +
                "(od.quantity * od.unit_price) as sub_total " +
                "FROM Order_Details od " +
                "JOIN Menu_items m ON od.food_id = m.food_id " +
                "WHERE od.order_id = ? AND od.status NOT IN ('CANCEL')";

        String sqlTotal = "SELECT total_amount FROM Orders WHERE order_id = ?";

        Connection conn = null;
        try {
            conn = DB_Connection.openConnection();
            conn.setAutoCommit(false);

            // THỰC HIỆN HỦY CÁC MÓN CHƯA NẤU
            // THỰC HIỆN HỦY CÁC MÓN CHƯA NẤU
            if (cancelUnfinished) {
                // --- BẮT ĐẦU ĐOẠN BỔ SUNG HOÀN KHO ---
                String sqlGetCancelItems = "SELECT food_id, quantity FROM Order_Details " +
                        "WHERE order_id = ? AND status IN ('PENDING', 'WAITING_APPROVAL')";
                String sqlRestoreStock = "UPDATE Menu_items SET stock = stock + ? WHERE food_id = ?";

                try (PreparedStatement psGet = conn.prepareStatement(sqlGetCancelItems);
                     PreparedStatement psRestore = conn.prepareStatement(sqlRestoreStock)) {

                    psGet.setInt(1, orderId);
                    ResultSet rsCancel = psGet.executeQuery();

                    while (rsCancel.next()) {
                        int fId = rsCancel.getInt("food_id");
                        int qty = rsCancel.getInt("quantity");

                        // Cộng lại số lượng vào bảng Menu_items
                        psRestore.setInt(1, qty);
                        psRestore.setInt(2, fId);
                        psRestore.executeUpdate();
                    }
                }
                try (PreparedStatement pre = conn.prepareStatement(sqlCancel)) {
                    pre.setInt(1, orderId);
                    pre.executeUpdate();
                }
                // Cập nhật lại tổng tiền sau khi đã hủy các món PENDING/WAITING_APPROVAL
                updateOrderTotalAmount(conn, orderId);
            }

            // IN HÓA ĐƠN CHI TIẾT
            System.out.println("\n" + ColorConstants.SUCCESS + "--- CHI TIẾT HÓA ĐƠN ---" + ColorConstants.RESET);
            System.out.printf("%-20s | %-5s | %-12s | %-15s | %-12s\n", "Tên món", "SL", "Đơn giá", "Trạng thái", "Thành tiền");
            System.out.println("----------------------------------------------------------------------------------");

            try (PreparedStatement pre = conn.prepareStatement(sqlGetDetails)) {
                pre.setInt(1, orderId);
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                    System.out.printf("%-20s | %-5d | %-12.2f | %-15s | %-12.2f\n",
                            rs.getString("food_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price"),
                            rs.getString("status"), // Hiển thị trạng thái để khách biết món nào đã nấu
                            rs.getDouble("sub_total"));
                }
            }

            // Lấy tổng tiền cuối cùng (Đã bao gồm các món đang nấu/đã xong)
            double total = 0;
            try (PreparedStatement pre = conn.prepareStatement(sqlTotal)) {
                pre.setInt(1, orderId);
                ResultSet rs = pre.executeQuery();
                if (rs.next()) total = rs.getDouble(1);
            }
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println(ColorConstants.SUCCESS + "TỔNG CỘNG THANH TOÁN: " + total + " VND" + ColorConstants.RESET);
            System.out.println(ColorConstants.WARNING + "(Lưu ý: Bao gồm các món đã vào bếp và đang nấu)" + ColorConstants.RESET + "\n");

            // Cập nhật trạng thái hóa đơn và bàn
            try (PreparedStatement pre = conn.prepareStatement(sqlPaid)) {
                pre.setInt(1, orderId);
                pre.executeUpdate();
            }
            try (PreparedStatement pre = conn.prepareStatement(sqlFreeTable)) {
                pre.setInt(1, tableId);
                pre.executeUpdate();
            }

            conn.commit();
            return total;

        } catch (Exception e) {
            System.out.println(ColorConstants.ERROR + "Lỗi thanh toán: " + e.getMessage() + ColorConstants.RESET);
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {
                System.out.println(ColorConstants.ERROR + "Lỗi Rollback: " + ex.getMessage() + ColorConstants.RESET);
            }
            return -1;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    // Hàm helper để map dữ liệu từ ResultSet sang Object (tránh lặp code)
    private MenuItems mapResultSetToMenuItem(ResultSet rs) throws SQLException {
        MenuItems item = new MenuItems();
        item.setFood_id(rs.getInt("food_id"));
        item.setFood_name(rs.getString("food_name"));
        item.setPrice(rs.getDouble("price"));
        item.setStock(rs.getInt("stock"));

        String categoryStr = rs.getString("category");
        try {
            item.setCategory(categoryStr != null ? Category.valueOf(categoryStr.toUpperCase()) : Category.FOOD);
        } catch (IllegalArgumentException e) {
            item.setCategory(Category.FOOD);
        }
        return item;
    }
}