package re.restauran_manager.business.dao;


import re.restauran_manager.model.enties.Table;
import re.restauran_manager.model.enums.TableStatus;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDao {

    private static TableDao instance;

    private TableDao() {
    }

    public static TableDao getInstance() {
        if (instance == null) {
            instance = new TableDao();
        }
        return instance;
    }

    public Table findById(int id) {
        String sql = "SELECT table_id, number_seats, status FROM Tables WHERE table_id = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) return mapResultSetToTable(rs);
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi khi tìm theo tên: " + e.getMessage() + ColorConstants.RESET);
        }
        return null;
    }


    public List<Table> findByStatus(TableStatus status) {
        String sql = "SELECT table_id, number_seats, status FROM Tables WHERE status = ?";

        List<Table> tables = new ArrayList<>();
        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, status.name());

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                tables.add(mapResultSetToTable(rs));
            }
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi khi tìm theo tên: " + e.getMessage() + ColorConstants.RESET);
        }
        return tables;
    }



    public boolean insertTable(Table table) {
        if (table == null) {
            System.out.println(ColorConstants.WARNING + "Không thấy dữ liệu" + ColorConstants.RESET);
            return false;
        }

        String sql = "INSERT INTO Tables (number_seats,status) VALUES (?, ?)";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, table.getNumber_seats());
            pstmt.setString(2, table.getStatus().name());

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Insert Food: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public boolean deleteById(int id) {
        if (findById(id) == null) {
            System.out.println(ColorConstants.WARNING + "Bàn không tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "DELETE FROM Tables WHERE table_id = ?";

        try (Connection conn = DB_Connection.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Delete: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }

    }


    public boolean updateSeat(int id, int number_seat) {

        if (findById(id) == null) {
            System.out.println(ColorConstants.WARNING + "Bàn không không tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "UPDATE Tables SET number_seats = ? WHERE table_id = ?";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, number_seat);
            pstmt.setInt(2, id);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Update: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }

    public boolean updateStatus(int id, TableStatus status) {

        if (findById(id) == null) {
            System.out.println(ColorConstants.WARNING + "Bàn không không tồn tại!" + ColorConstants.RESET);
            return false;
        }

        String sql = "UPDATE FROM Tables SET status = ?  where table_id = ?";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, id);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi SQL Update: " + e.getMessage() + ColorConstants.RESET);
            return false;
        }
    }



    public List<Table> displayAll() {
        String sql = "SELECT table_id, number_seats,status FROM tables";
        List<Table> tables = new ArrayList<>();

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()
        ) {
            while (resultSet.next()) {
                Table item = mapResultSetToTable(resultSet);

                tables.add(item);
            }
        } catch (SQLException e) {
            System.err.println(ColorConstants.ERROR + "SQL Selection Error: " + e.getMessage() + ColorConstants.RESET);
        }
        return tables;
    }


    private Table mapResultSetToTable(ResultSet rs) throws SQLException {
        Table table = new Table();
        table.setTable_id(rs.getInt("table_id"));
        table.setNumber_seats(rs.getInt("number_seats"));


        String status = rs.getString("status");
        if (status != null) {
            table.setStatus(TableStatus.valueOf(status.toUpperCase()));
        }

        return table;
    }

}
