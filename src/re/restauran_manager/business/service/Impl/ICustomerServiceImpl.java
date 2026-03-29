package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.service.IService.ICustomerService;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Table;

import java.util.List;

public class ICustomerServiceImpl implements ICustomerService {
    @Override
    public List<MenuItems> getAllAvailableFood() {
        return List.of();
    }

    @Override
    public List<Table> getFreeTables() {
        return List.of();
    }

    @Override
    public boolean selectTable(int userId, int tableId) {
        return false;
    }

    @Override
    public boolean orderItems(int userId, int tableId, int foodId, int quantity) {
        return false;
    }

    @Override
    public void trackOrderStatus(int userId) {

    }

    @Override
    public double checkout(int userId, int tableId) {
        return 0;
    }
}
