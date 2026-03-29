package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.model.enums.TableStatus;

import java.util.List;

public class CustomerDao {

    public static List<Table> listTable(int id){
        List<Table> tables = TableDao.getInstance().findByStatus(TableStatus.FREE);

        if(tables.isEmpty()){
            return null;
        }

        return tables;
    }


    public static Table createOrder(int customer_id,int table_id){
        Table table = TableDao.getInstance().findById(table_id);

        Account customer = AccountDao.getInstance().findById(customer_id);
        if(table == null){
            return null;
        }

        if(customer != null){
            return null;
        }

        String sql = "INSERT INTO order(user_id,table_id,order_date,status) values(?,?,?,?,'PENDING')";

        return null;
    }
}














