package re.restauran_manager.business.dao;

import re.restauran_manager.model.dto.OrderDetailDisplay;
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

    public static CustomerDao getInstance() {
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
        String sqlCheck = "SELECT id, quantity FROM Order_Details WHERE order_id = ? AND food_id = ? " +
                "AND status IN ('PENDING', 'WAITING_APPROVAL')";
        String sqlUpdateDetail = "UPDATE Order_Details SET quantity = ? WHERE id = ?";
        String sqlInsertDetail = "INSERT INTO Order_Details (order_id, food_id, quantity, unit_price, status) VALUES (?, ?, ?, ?, 'WAITING_APPROVAL')";
        String sqlUpdateTotal = "UPDATE Orders SET total_amount = total_amount + ? WHERE order_id = ?";
        String sqlUpdateStock = "UPDATE Menu_items SET stock = stock - ? WHERE food_id = ?";

        try {
            conn = DB_Connection.openConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
                 PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateDetail);
                 PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsertDetail);
                 PreparedStatement pstmtTotal = conn.prepareStatement(sqlUpdateTotal);
                 PreparedStatement pstmtStock = conn.prepareStatement(sqlUpdateStock)) {

                for (MenuItems item : items) {
                    int quantityRequested = item.getStock();

                    pstmtCheck.setInt(1, orderId);
                    pstmtCheck.setInt(2, item.getFood_id());
                    try (ResultSet rs = pstmtCheck.executeQuery()) {
                        if (rs.next()) {
                            int detailId = rs.getInt("id");
                            int currentQty = rs.getInt("quantity");
                            pstmtUpdate.setInt(1, currentQty + quantityRequested);
                            pstmtUpdate.setInt(2, detailId);
                            pstmtUpdate.executeUpdate();
                        } else {
                            pstmtInsert.setInt(1, orderId);
                            pstmtInsert.setInt(2, item.getFood_id());
                            pstmtInsert.setInt(3, quantityRequested);
                            pstmtInsert.setDouble(4, item.getPrice());
                            pstmtInsert.executeUpdate();
                        }
                    }

                    pstmtTotal.setDouble(1, quantityRequested * item.getPrice());
                    pstmtTotal.setInt(2, orderId);
                    pstmtTotal.executeUpdate();

                    pstmtStock.setInt(1, quantityRequested);
                    pstmtStock.setInt(2, item.getFood_id());
                    pstmtStock.executeUpdate();
                }
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    System.out.println(ColorConstants.ERROR + "Lỗi Rollback: " + ex.getMessage() + ColorConstants.RESET);
                }
            }
            System.out.println(ColorConstants.ERROR + "Lỗi thêm món vào đơn hàng: " + e.getMessage() + ColorConstants.RESET);
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {
                System.out.println(ColorConstants.ERROR + "Lỗi đóng kết nối: " + e.getMessage() + ColorConstants.RESET);
            }
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
        String sql = "SELECT COUNT(*) FROM Order_Details \n" +
                "WHERE order_id = ? AND status IN ('WAITING_APPROVAL', 'PENDING');";
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

            int currentQty = 0;
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, orderId);
                psCheck.setInt(2, foodId);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) currentQty = rs.getInt("quantity");
                else return false;
            }

            if (quantityToCancel > currentQty || quantityToCancel <= 0) return false;

            if (quantityToCancel == currentQty) {
                try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                    psDel.setInt(1, orderId);
                    psDel.setInt(2, foodId);
                    psDel.executeUpdate();
                }
            } else {
                try (PreparedStatement psUp = conn.prepareStatement(sqlUpdateQty)) {
                    psUp.setInt(1, quantityToCancel);
                    psUp.setInt(2, orderId);
                    psUp.setInt(3, foodId);
                    psUp.executeUpdate();
                }
            }

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
        Connection conn = null;
        double totalAmount = 0;
        try {
            conn = DB_Connection.openConnection();
            if (conn == null) return -1;
            conn.setAutoCommit(false);

            if (cancelUnfinished) {
                handleUnfinishedItems(conn, orderId);
            }

            totalAmount = calculateAndPrintInvoice(conn, orderId);


            finalizeCheckout(conn, tableId, orderId, totalAmount);

            conn.commit();
            return totalAmount;
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            System.out.println(ColorConstants.ERROR + "Lỗi thanh toán: " + e.getMessage() + ColorConstants.RESET);
            return -1;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    private void handleUnfinishedItems(Connection conn, int orderId) {
        String sqlGet = "SELECT food_id, quantity FROM Order_Details WHERE order_id = ? " +
                "AND status IN ('WAITING_APPROVAL', 'PENDING')";
        String sqlRestore = "UPDATE Menu_items SET stock = stock + ? WHERE food_id = ?";
        String sqlCancel = "UPDATE Order_Details SET status = 'CANCEL' WHERE order_id = ? " +
                "AND status IN ('WAITING_APPROVAL', 'PENDING')";

        try {
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(true);

            try (PreparedStatement psGet = conn.prepareStatement(sqlGet)) {
                psGet.setInt(1, orderId);
                try (ResultSet rs = psGet.executeQuery()) {
                    while (rs.next()) {
                        int fId = rs.getInt("food_id");
                        int qty = rs.getInt("quantity");

                        try (PreparedStatement psRes = conn.prepareStatement(sqlRestore)) {
                            psRes.setInt(1, qty);
                            psRes.setInt(2, fId);
                            int rows = psRes.executeUpdate();
                            if (rows > 0) {
                                System.out.println(ColorConstants.WARNING + "[HOÀN KHO] Đã trả lại " + qty + " món ID " + fId + ColorConstants.RESET);
                            }
                        }
                    }
                }
            }

            try (PreparedStatement psCan = conn.prepareStatement(sqlCancel)) {
                psCan.setInt(1, orderId);
                psCan.executeUpdate();
            }

            conn.setAutoCommit(originalAutoCommit);

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi hoàn stock: " + e.getMessage() + ColorConstants.RESET);
        }
    }

    private double calculateAndPrintInvoice(Connection conn, int orderId) {
        String sql = "SELECT m.food_name, od.quantity, od.unit_price, od.status, (od.quantity * od.unit_price) as sub_total " +
                "FROM Order_Details od" +
                " JOIN Menu_items m ON od.food_id = m.food_id " +
                "WHERE od.order_id = ? AND od.status IN ('COOKING', 'READY', 'SERVED')";

        double total = 0;
        StringBuilder sb = new StringBuilder();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double sub = rs.getDouble("sub_total");
                    total += sub;
                    sb.append(String.format("| %-20s | %-5d | %-12.0f | %-15s | %-12.2f |\n",
                            rs.getString("food_name"), rs.getInt("quantity"),
                            rs.getDouble("unit_price"), rs.getString("status"), sub));
                }
            }

            if (total > 0) {
                System.out.println("\n" + ColorConstants.SUCCESS + "+==================== HÓA ĐƠN CHI TIẾT ====================+" + ColorConstants.RESET);
                System.out.printf("| %-20s | %-5s | %-12s | %-15s | %-12s |\n", "Tên món", "SL", "Đơn giá", "Trạng thái", "Thành tiền");
                System.out.println("+----------------------------------------------------------+");
                System.out.print(sb.toString());
                System.out.printf("| %-59s | %-12.2f |\n", "Tổng tiền đơn hàng:", total);
                System.out.println("+==========================================================+");
            } else {
                System.out.println(ColorConstants.WARNING + "[Thông báo] Không có món ăn nào đã được phục vụ để tính tiền." + ColorConstants.RESET);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi in hóa đơn: " + e.getMessage() + ColorConstants.RESET);
            return -1;
        }
        return total;
    }

    private void finalizeCheckout(Connection conn, int tableId, int orderId, double total) {
        String sqlUpdateOrder = "UPDATE Orders SET total_amount = ?, status = 'PAID' WHERE order_id = ?";
        String sqlFreeTable = "UPDATE Tables SET status = 'FREE' WHERE table_id = ?";

        try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateOrder);
             PreparedStatement ps2 = conn.prepareStatement(sqlFreeTable)) {
            ps1.setDouble(1, total);
            ps1.setInt(2, orderId);
            ps1.executeUpdate();

            ps2.setInt(1, tableId);
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi cập nhật trạng thái bàn và order: " + e.getMessage() + ColorConstants.RESET);
        }
    }

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