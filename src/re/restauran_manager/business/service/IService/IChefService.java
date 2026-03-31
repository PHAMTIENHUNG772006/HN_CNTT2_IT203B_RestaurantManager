package re.restauran_manager.business.service.IService;

import re.restauran_manager.model.enties.MenuItems;

import java.util.List;

public interface IChefService {
    void getAll();
    boolean updateStatus(int id);
    boolean updateStock(int id, int stock);
    void displayPagination(List<MenuItems> listFood);
}
