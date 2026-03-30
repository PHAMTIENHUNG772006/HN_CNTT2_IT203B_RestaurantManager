package re.restauran_manager.business.service.IService;

import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Table;
import java.util.List;

public interface ICustomerService {

    List<MenuItems> getAllAvailableFood();

    List<Table> getFreeTables();

    void displayPagination(List<MenuItems> listFood);

    boolean selectTable(int userId, int tableId);

    boolean placeOrder(int order_id, List<MenuItems> foods);

    void trackOrderStatus(int accountId);

    double checkout(int accountId, int tableId);

}