package re.restauran_manager.business.service.IService;

import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Orders;
import re.restauran_manager.model.enties.Table;
import java.util.List;

public interface ICustomerService {
    List<MenuItems> getAllAvailableFood();
    List<Table> getFreeTables();
    boolean selectTable(int accountId, int tableId);
    boolean placeOrder(int orderId, List<MenuItems> foods);
    void trackOrderStatus(int orderId);
    List<Orders> getActiveOrders(int account_id);
    double checkout(int orderId, int tableId);
    boolean cancelFood(int orderId, int foodId, int quantity);
    void displayPagination(List<MenuItems> listFood);
}