package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.Table;
import re.restauran_manager.utils.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDao {

    public static Table choiceTable(int id){
        Table table = TableDao.getInstance().findById(id);

        if(table != null){
            return table;
        }
        return null;
    }


    public static Table orderFood(int id,int quantity){
        Table table = TableDao.getInstance().findById(id);

        if(table != null){
            return table;
        }
        return null;
    }
}
