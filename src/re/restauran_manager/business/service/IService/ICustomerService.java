package re.restauran_manager.business.service.IService;

import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Table;
import java.util.List;

public interface ICustomerService {

    List<MenuItems> getAllAvailableFood();

    List<Table> getFreeTables();

    boolean selectTable(int userId, int tableId);

    boolean orderItems(int userId, int tableId, int foodId, int quantity);

    void trackOrderStatus(int userId);

    double checkout(int userId, int tableId);

}