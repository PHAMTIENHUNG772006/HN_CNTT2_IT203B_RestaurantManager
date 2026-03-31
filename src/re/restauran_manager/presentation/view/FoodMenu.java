package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.IFoodService;
import re.restauran_manager.business.service.Impl.IFoodServiceImpl;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.List;

public class FoodMenu {
    public static void viewMenuFood() {
        IFoodService foodService = IFoodServiceImpl.getInstance();
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU FOOD                    |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách món ăn                  |");
            System.out.println("|  2. Thêm mới món ăn                       |");
            System.out.println("|  3. Cập nhật (giá/số lượng)               |");
            System.out.println("|  4. Xóa món ăn                            |");
            System.out.println("|  5. Tìm kiếm món theo tên                 |");
            System.out.println("|  6. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn : ");

            switch (choice) {
                case 1:
                    foodService.displayPagination(foodService.displayAll());
                    break;
                case 2:
                    MenuItems items = MenuItems.inputData();
                    if (foodService.add(items)) {
                        System.out.println(ColorConstants.SUCCESS + "Thêm thành công món ăn!" + ColorConstants.RESET);
                    } else {
                        System.out.println(ColorConstants.ERROR + "Thêm thất bại. Món ăn có thể đã tồn tại!" + ColorConstants.RESET);
                    }
                    break;
                case 3:
                    foodService.displayPagination(foodService.displayAll());

                    System.out.println("+===========================================+");
                    System.out.println("|           CHỌN LOẠI CẬP NHẬT              |");
                    System.out.println("+===========================================+");
                    System.out.println("|  1. Cập nhật Số lượng (Stock)             |");
                    System.out.println("|  2. Cập nhật Giá (Price)                  |");
                    System.out.println("|  0. Quay lại                              |");
                    System.out.println("+-------------------------------------------+");

                    int updateChoice = InputMethod.getInputInt("Lựa chọn của bạn: ");

                    if (updateChoice == 1) {
                        int id = InputMethod.getInputInt("Nhập ID món ăn cần cập nhật kho: ");
                        int stock = InputMethod.getInputInt("Nhập số lượng tồn kho mới: ");
                        if (foodService.updateStock(id, stock)) {
                            System.out.println(ColorConstants.SUCCESS + "Cập nhật kho thành công!" + ColorConstants.RESET);
                        } else {
                            System.out.println(ColorConstants.ERROR + "Thao tác thất bại!" + ColorConstants.RESET);
                        }
                    } else if (updateChoice == 2) {
                        int id = InputMethod.getInputInt("Nhập ID món ăn cần cập nhật giá: ");
                        double price = InputMethod.getInputDouble("Nhập giá mới: ");
                        if (foodService.updatePrice(id, price)) {
                            System.out.println(ColorConstants.SUCCESS + "Cập nhật giá thành công!" + ColorConstants.RESET);
                        } else {
                            System.out.println(ColorConstants.ERROR + "Thao tác thất bại!" + ColorConstants.RESET);
                        }
                    }
                    break;
                case 4:
                    foodService.displayPagination(foodService.displayAll());
                    int idDel = InputMethod.getInputInt("Nhập ID món ăn cần xóa: ");
                    if (foodService.delete(idDel)) {
                        System.out.println(ColorConstants.SUCCESS + "Xóa món ăn thành công!" + ColorConstants.RESET);
                    } else {
                        System.out.println(ColorConstants.WARNING + "Xóa thất bại!" + ColorConstants.RESET);
                    }
                    break;
                case 5:
                    String findFood = InputMethod.getInputString("Nhập tên món ăn cần tìm: ");
                    List<MenuItems> foods = foodService.findByName(findFood);

                    if (foods == null){
                        return;
                    }else {
                        System.out.println(ColorConstants.SUCCESS + "Món ăn được tìm thấy:" + ColorConstants.RESET);
                        MenuItems.getHeader();
                       for (MenuItems item : foods){
                           item.displayData();
                       }
                        MenuItems.getFooter();
                    }
                    break;
                case 6:
                    System.out.println("Quay lại Menu chính...");
                    break;
                default:
                    System.out.println(ColorConstants.WARNING + "Lựa chọn sai, vui lòng chọn lại!" + ColorConstants.RESET);
            }
        } while (choice != 6);
    }
}