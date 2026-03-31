package re.restauran_manager.business.service.IService;


import re.restauran_manager.model.enties.MenuItems;

import java.util.List;

public interface IFoodService {
    boolean add(MenuItems item);
    boolean delete(int id);
    boolean updateStock(int id, int stock);
    boolean updatePrice(int id, double price);
    List<MenuItems> displayAll();
    MenuItems findById(int id);
    List<MenuItems> findByName(String name);
    void displayPagination(List<MenuItems> listFood);
}
