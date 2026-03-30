package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.model.enums.Category;
import re.restauran_manager.model.enums.TableStatus;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;
import re.restauran_manager.utils.InputMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private static CustomerDao instance;

    private CustomerDao() {
    }

    public static CustomerDao getInstance() {
        if (instance == null) {
            instance = new CustomerDao();
        }
        return instance;
    }

    public List<Table> listTableFree() {
        List<Table> tables = TableDao.getInstance().findByStatus(TableStatus.FREE);
        if (tables.isEmpty()) {
            return null;
        }
        return tables;
    }

    public List<MenuItems> getAllAvailableFood() {
        String sql = "SELECT food_id, food_name, price, category, stock FROM menu_items WHERE stock > 0";
        List<MenuItems> foods = new ArrayList<>();

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {

            while (rs.next()) {
                MenuItems item = new MenuItems();
                item.setFood_id(rs.getInt("food_id"));
                item.setFood_name(rs.getString("food_name"));
                item.setPrice(rs.getDouble("price"));

                String categoryStr = rs.getString("category");
                if (categoryStr != null) {
                    try {
                        item.setCategory(Category.valueOf(categoryStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        item.setCategory(null);
                    }
                }

                item.setStock(rs.getInt("stock"));
                foods.add(item);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi lấy danh sách món: " + e.getMessage() + ColorConstants.RESET);
        }

        return foods;
    }

    public void displayPagination(List<Table> listTable) {

        if (listTable.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Danh sách bàn đang trống!" + ColorConstants.RESET);
            return;
        }

        int pageSize = 3; // Hiển thị 2 sinh viên/trang để dễ test với 5 dữ liệu mẫu
        int totalPages = (int) Math.ceil((double) listTable.size() / pageSize);
        int currentPage = 1;

        while (true) {
            // Cập nhật lại tổng số trang phòng trường hợp danh sách bị thay đổi ở thread khác (tuy ít xảy ra ở console app này)
            totalPages = (int) Math.ceil((double) listTable.size() / pageSize);
            if (currentPage > totalPages) currentPage = totalPages;

            System.out.println(ColorConstants.SUCCESS + "\n--- HIỂN THỊ DANH SÁCH (TRANG " + currentPage + "/" + totalPages + ") ---" + ColorConstants.RESET);
            Table.getHeader();

            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, listTable.size());

            for (int i = startIndex; i < endIndex; i++) {
                listTable.get(i).displayData();
            }
            Table.getFooter();

            System.out.println("Tùy chọn: [N] Next | [P] Prev | [S] Chọn trang | [E] Thoát");
            String choice = InputMethod.getInputString("Nhập lựa chọn của bạn: ").toUpperCase();

            switch (choice) {
                case "N":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Bạn đang ở trang cuối cùng!" + ColorConstants.RESET);
                    }
                    break;
                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Bạn đang ở trang đầu tiên!" + ColorConstants.RESET);
                    }
                    break;
                case "S":
                    int targetPage = InputMethod.getInputInt("Nhập số trang muốn đến (1 - " + totalPages + "): ");
                    if (targetPage >= 1 && targetPage <= totalPages) {
                        currentPage = targetPage;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Số trang không hợp lệ!" + ColorConstants.RESET);
                    }
                    break;
                case "E":
                    System.out.println(ColorConstants.SUCCESS + "Đã thoát chế độ phân trang." + ColorConstants.RESET);
                    return;
                default:
                    System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ, vui lòng nhập N, P, S hoặc E." + ColorConstants.RESET);
            }
        }
    }

    public int createOrder(int customerId, int tableId) {
        Table table = TableDao.getInstance().findById(tableId);
        if (table == null || table.getStatus() != TableStatus.FREE) {
            System.out.println(ColorConstants.ERROR + "Bàn không tồn tại hoặc đã có khách!" + ColorConstants.RESET);
            return -1;
        }

        String sqlOrder = "INSERT INTO orders (user_id, table_id, total_amount, order_date, status) VALUES (?, ?, 0.0, NOW(), 'PENDING')";
        String sqlUpdateTable = "UPDATE Tables SET status = 'OCCUPIED' WHERE table_id = ?";
        int generatedId = -1;

        Connection conn = null;
        try {
            conn = DB_Connection.openConnection();
            conn.setAutoCommit(false);


            try (PreparedStatement pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOrder.setInt(1, customerId);
                pstmtOrder.setInt(2, tableId);
                pstmtOrder.executeUpdate();

                try (ResultSet rs = pstmtOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement pstmtTable = conn.prepareStatement(sqlUpdateTable)) {
                pstmtTable.setInt(1, tableId);
                pstmtTable.executeUpdate();
            }

            conn.commit();
            return generatedId;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.out.println(ColorConstants.ERROR + "Lỗi hệ thống: " + e.getMessage() + ColorConstants.RESET);
            return -1;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }


    public boolean insertFood(int orderId, List<MenuItems> foods) {
        if (orderId <= 0 || foods == null || foods.isEmpty()) {
            System.out.println(ColorConstants.ERROR + "Dữ liệu đầu vào không hợp lệ!" + ColorConstants.RESET);
            return false;
        }

        String sqlInsert = "INSERT INTO order_details (order_id, food_id, quantity, unit_price, status) VALUES (?, ?, ?, ?, 'PENDING')";
        String sqlUpdateStock = "UPDATE menu_items SET stock = stock - ? WHERE food_id = ?";

        try (Connection conn = DB_Connection.openConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement preInsert = conn.prepareStatement(sqlInsert);
                 PreparedStatement preUpdateStock = conn.prepareStatement(sqlUpdateStock)) {

                for (MenuItems item : foods) {
                    preInsert.setInt(1, orderId);
                    preInsert.setInt(2, item.getFood_id());
                    preInsert.setInt(3, item.getStock());
                    preInsert.setDouble(4, item.getPrice());
                    preInsert.addBatch();


                    preUpdateStock.setInt(1, item.getStock());
                    preUpdateStock.setInt(2, item.getFood_id());
                    preUpdateStock.addBatch();
                }

                preInsert.executeBatch();
                preUpdateStock.executeBatch();

                updateOrderTotalAmount(conn, orderId);

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println(ColorConstants.ERROR + "Lỗi thực thi: " + e.getMessage() + ColorConstants.RESET);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi kết nối: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }


    private void updateOrderTotalAmount(Connection conn, int orderId) throws SQLException {
        String sqlUpdate = "UPDATE orders o SET total_amount = " +
                "(SELECT SUM(quantity * unit_price) FROM order_details WHERE order_id = ?) " +
                "WHERE o.order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }
    }

}