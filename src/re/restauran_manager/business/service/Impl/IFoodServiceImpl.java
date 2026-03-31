package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.FoodDao;
import re.restauran_manager.business.service.IService.IFoodService;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.ArrayList;
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

    // --- SỬA LẠI DELETE THEO ID ---
    @Override
    public boolean delete(int id) {
        MenuItems item = foodDao.findById(id);
        if (item == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy món có ID: [" + id + "]" + ColorConstants.RESET);
            return false;
        }
        return foodDao.deleteById(id); // Gọi hàm deleteById trong DAO
    }

    // --- SỬA LẠI UPDATE STOCK THEO ID ---
    @Override
    public boolean updateStock(int id, int stock) {
        if (stock < 0) {
            System.out.println(ColorConstants.ERROR + "Số lượng tồn kho không được âm." + ColorConstants.RESET);
            return false;
        }
        if (foodDao.findById(id) == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy món có ID: " + id + ColorConstants.RESET);
            return false;
        }
        return foodDao.updateStock(id, stock);
    }

    // --- SỬA LẠI UPDATE PRICE THEO ID ---
    @Override
    public boolean updatePrice(int id, double price) {
        if (price < 0) {
            System.out.println(ColorConstants.ERROR + "Giá tiền không được âm." + ColorConstants.RESET);
            return false;
        }
        if (foodDao.findById(id) == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy món có ID: " + id + ColorConstants.RESET);
            return false;
        }
        return foodDao.updatePrice(id, price);
    }

    @Override
    public List<MenuItems> displayAll() {
        List<MenuItems> foods = foodDao.displayAll();

        if (foods == null || foods.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Hiện tại nhà hàng chưa phục vụ món nào!" + ColorConstants.RESET);
            return new ArrayList<>();
        }
        return foods;
    }

    @Override
    public MenuItems findById(int id) {
        if (id <= 0) return null;
        return foodDao.findById(id);
    }

    @Override
    public  List<MenuItems> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Danh sách món ăn đang trống!" + ColorConstants.RESET);
            return null;
        }


        return foodDao.findByName(name);
    }

    @Override
    public void displayPagination(List<MenuItems> listFood) {
        if (listFood == null || listFood.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Danh sách món ăn đang trống!" + ColorConstants.RESET);
            return;
        }

        int pageSize = 5;
        int currentPage = 1;

        while (true) {
            int totalPages = (int) Math.ceil((double) listFood.size() / pageSize);
            if (currentPage > totalPages) currentPage = totalPages;
            if (currentPage < 1) currentPage = 1;

            System.out.println(ColorConstants.SUCCESS + "\n--- MENU NHÀ HÀNG (TRANG " + currentPage + "/" + totalPages + ") ---" + ColorConstants.RESET);

            MenuItems.getHeader();
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, listFood.size());

            for (int i = startIndex; i < endIndex; i++) {
                listFood.get(i).displayData();
            }
            MenuItems.getFooter();

            System.out.println("Tùy chọn: [N] Tiếp | [P] Trước | [S] Chọn trang | [E] Thoát");
            String choice = InputMethod.getInputString("Nhập lựa chọn của bạn: ").toUpperCase();

            if (choice.equals("N") && currentPage < totalPages) currentPage++;
            else if (choice.equals("P") && currentPage > 1) currentPage--;
            else if (choice.equals("S")) {
                int targetPage = InputMethod.getInputInt("Nhập số trang (1 - " + totalPages + "): ");
                if (targetPage >= 1 && targetPage <= totalPages) currentPage = targetPage;
                else System.out.println(ColorConstants.WARNING + "Số trang không hợp lệ!" + ColorConstants.RESET);
            }
            else if (choice.equals("E")) break;
            else System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ hoặc đã hết trang!" + ColorConstants.RESET);
        }
    }
}