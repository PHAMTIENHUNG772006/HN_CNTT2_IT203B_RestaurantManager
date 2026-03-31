package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enums.Category;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodDao {

    private static FoodDao instance;

    private FoodDao() {}

    public static FoodDao getInstance() {
        if (instance == null) {
            instance = new FoodDao();
        }
        return instance;
    }

    public  List<MenuItems> findByName(String name) {
        String sql = "SELECT food_id, food_name, price, category, stock FROM menu_items WHERE food_name = ?";
        List<MenuItems> foods = new ArrayList<>();
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, name );
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()){
                    MenuItems items =  mapResultSetToFood(rs);

                    foods.add(items);
                }
            }
            return foods;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public MenuItems findById(int id) {
        String sql = "SELECT food_id, food_name, price, category, stock FROM menu_items WHERE food_id = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToFood(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public boolean insertFood(MenuItems item) {
        if (item == null) return false;
        String sql = "INSERT INTO menu_items (food_name, price, category, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getFood_name());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setString(3, item.getCategory().name());
            pstmt.setInt(4, item.getStock());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM menu_items WHERE food_id = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi xóa món: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public boolean updatePrice(int id, double price) {
        String sql = "UPDATE menu_items SET price = ? WHERE food_id = ?";
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, price);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi cập nhật giá: " + e.getMessage() + ColorConstants.RESET);
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
            System.out.println(ColorConstants.ERROR + "Lỗi cập nhật kho: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public List<MenuItems> displayAll() {
        String sql = "SELECT * FROM menu_items";
        List<MenuItems> menuList = new ArrayList<>();
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                menuList.add(mapResultSetToFood(rs));
            }
        } catch (SQLException e) {
            System.err.println(ColorConstants.ERROR + "Lỗi: " + e.getMessage() + ColorConstants.RESET);
        }
        return menuList;
    }

    private MenuItems mapResultSetToFood(ResultSet rs) throws SQLException {
        MenuItems menuItems = new MenuItems();
        menuItems.setFood_id(rs.getInt("food_id"));
        menuItems.setFood_name(rs.getString("food_name"));
        menuItems.setPrice(rs.getDouble("price"));
        String category = rs.getString("category");
        if (category != null) {
            menuItems.setCategory(Category.valueOf(category.toUpperCase()));
        }
        menuItems.setStock(rs.getInt("stock"));
        return menuItems;
    }
}