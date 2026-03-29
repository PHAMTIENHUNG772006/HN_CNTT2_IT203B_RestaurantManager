package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.FoodDao;
import re.restauran_manager.business.service.IService.IFoodService;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enums.FoodEnum;
import re.restauran_manager.utils.ColorConstants;

import java.util.List;

public class IFoodServiceImpl implements IFoodService {
    private FoodDao foodDao = FoodDao.getInstance();
    private static IFoodServiceImpl instance;

    private IFoodServiceImpl() {}

    public static IFoodServiceImpl getInstance() {
        if (instance == null) {
            instance = new IFoodServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean add(MenuItems item) {
        if (item == null || item.getFood_name() == null || item.getFood_name().trim().isEmpty() || item.getPrice() <= 0) {
            System.out.println(ColorConstants.ERROR + "Dữ liệu không hợp lệ." + ColorConstants.RESET);
            return false;
        }
        return foodDao.insertFood(item);
    }

    @Override
    public boolean delete(String name) {
        String searchName = name.trim();
        MenuItems item = foodDao.findByName(searchName);

        if (item == null) {

            System.out.println(ColorConstants.ERROR + "Không tìm thấy món: [" + searchName + "]" + ColorConstants.RESET);
            return false;
        }
        return foodDao.deleteByName(item.getFood_name());
    }

    @Override
    public boolean updateStock(String name, int stock) {
        if (stock < 0) {
            System.out.println(ColorConstants.ERROR + "Số lượng tồn kho không được âm." + ColorConstants.RESET);
            return false;
        }
        if (foodDao.findByName(name.trim()) == null) {
            return false;
        }
        return foodDao.updatestock(name, stock);
    }

    @Override
    public boolean updatePrice(String name, double price) {
        if (price < 0) {
            System.out.println(ColorConstants.ERROR + "Số lượng tồn kho không được âm." + ColorConstants.RESET);
            return false;
        }
        if (foodDao.findByName(name.trim()) == null) {
            return false;
        }
        return foodDao.updatePrice(name, price);
    }

    @Override
    public void displayAll() {
        List<MenuItems> listMenu = foodDao.displayAll();
        if (listMenu == null || listMenu.isEmpty()) {
            System.out.println(ColorConstants.ERROR + "Danh sách trống." + ColorConstants.RESET);
            return;
        }

        System.out.println("\n" + ColorConstants.SUCCESS + "                      DANH SÁCH MÓN ĂN" + ColorConstants.RESET);
        MenuItems.getHeader();
        for (MenuItems items : listMenu) {
            items.displayData();
        }
        MenuItems.getFooter();
    }

    @Override
    public MenuItems findById(int id) {
        if (id <= 0) return null;
        return foodDao.findById(id);
    }

    @Override
    public MenuItems findByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        return foodDao.findByName(name);
    }
}