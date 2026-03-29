package re.restauran_manager.business.dao;

import org.mindrot.jbcrypt.BCrypt;

import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.model.enums.Category;
import re.restauran_manager.model.enums.FoodEnum;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FoodDao {

    private static FoodDao instance;

    private FoodDao() {
    }

    public static FoodDao getInstance() {
        if (instance == null) {
            instance = new FoodDao();
        }
        return instance;
    }

    public MenuItems findByName(String name) {
        String sql = "SELECT food_id, food_name, price, category, stock,status  FROM menu_items WHERE food_name = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, name);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToFood(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi khi tìm theo tên: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public MenuItems findById(int id) {
        String sql = "SELECT food_id, food_name, price, category, stock,status  FROM menu_items WHERE food_id = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToFood(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi khi tìm theo ID: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }

    public boolean insertFood(MenuItems item) {
        if (item == null) {
            System.out.println(ColorConstants.WARNING + "Dữ liệu món ăn rỗng" + ColorConstants.RESET);
            return false;
        }

        String sql = "INSERT INTO Menu_items (food_name, price, category, stock, status) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, item.getFood_name());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setString(3, item.getCategory().name());
            pstmt.setInt(4, item.getStock());
            pstmt.setString(5, item.getStatus().name());

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Insert Food: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public boolean deleteByName(String name) {
        if (findByName(name) == null) {
            System.out.println(ColorConstants.WARNING + "Món ăn không tồn tại để xóa!" + ColorConstants.RESET);
            return false;
        }

        String sql = "DELETE FROM Menu_items WHERE food_name = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Delete: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }

    }


    public boolean updatePrice(String name, double price) {

        if (findByName(name) != null) {
            System.out.println(ColorConstants.WARNING + "Món ăn không tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "UPDATE SET price = ? FROM Menu_Items where food_name = ?";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setDouble(1, price);
            pstmt.setString(2, name);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Update: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }



    public boolean updatestock(String name, int stock) {

        if (findByName(name) != null) {
            System.out.println(ColorConstants.WARNING + "Món ăn không tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "UPDATE SET price = ? FROM Menu_Items where stock = ?";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, stock);
            pstmt.setString(2, name);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Update: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }





    public List<MenuItems> displayAll() {
        String sql = "SELECT food_id, food_name, price, category, stock, status FROM Menu_items";
        List<MenuItems> menuList = new ArrayList<>();

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()
        ) {
            while (resultSet.next()) {
                MenuItems item = mapResultSetToFood(resultSet);

                menuList.add(item);
            }
        } catch (SQLException e) {

            System.err.println(ColorConstants.ERROR + "SQL Selection Error: " + e.getMessage() + ColorConstants.RESET);
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

        String status = rs.getString("status");
        if (status != null) {
            menuItems.setStatus(FoodEnum.valueOf(status.toUpperCase()));
        }

        return menuItems;
    }

}
