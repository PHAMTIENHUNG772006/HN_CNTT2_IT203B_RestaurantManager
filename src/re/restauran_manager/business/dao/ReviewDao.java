package re.restauran_manager.business.dao;

import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReviewDao {

    private static ReviewDao instance;

    private ReviewDao() {
    }

    public static ReviewDao getInstance() {
        if (instance == null) {
            instance = new ReviewDao();
        }

        return instance;
    }


    public static boolean insertReview(int account_id, int order_id, int rate, String comment) {


        String sql = "insert into reviews( account_id , order_id,rate,comment) values(?,?,?,?)";

        try (
                Connection conn = DB_Connection.openConnection();
                PreparedStatement pre = conn.prepareStatement(sql);
        ) {


            return pre.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(ColorConstants.ERROR + "Lỗi thêm reviews: " + e.getMessage() + ColorConstants.RESET);
        }
        return false;
    }


}
