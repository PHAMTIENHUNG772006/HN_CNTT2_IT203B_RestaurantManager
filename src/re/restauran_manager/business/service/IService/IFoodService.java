package re.restauran_manager.business.service.IService;


import re.restauran_manager.model.enties.MenuItems;

public interface IFoodService {
    boolean add(MenuItems item);
    boolean delete(String name);
    boolean updateStock(String name, int stock);
    boolean updatePrice(String name, double price);
    void displayAll();
    MenuItems findById(int id);
    MenuItems findByName(String name);
}
