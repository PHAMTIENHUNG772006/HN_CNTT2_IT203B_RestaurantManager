package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.IFoodService;
import re.restauran_manager.business.service.Impl.IFoodServiceImpl;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.Scanner;

public class FoodMenu {
    public static void viewMenuFood() {
        IFoodService foodService = IFoodServiceImpl.getInstance();
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU FOOD                    |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách món ăn                  |");
            System.out.println("|  2. Thêm mói ăn                           |");
            System.out.println("|  3. Cập nhật (giá/số lượng)               |");
            System.out.println("|  4. Xóa món ăn                            |");
            System.out.println("|  5. Tìm kiếm món theo tên                 |");
            System.out.println("|  6. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn : ");

            switch (choice){
                case 1:
                    foodService.displayAll();
                    break;
                case 2:
                   MenuItems items = MenuItems.inputData();
                    boolean isAdd = foodService.add(items);

                    if(isAdd){
                        System.out.println(ColorConstants.SUCCESS + "Thêm thành công món ăn" + ColorConstants.RESET);
                    }
                    break;
                case 3:
                    System.out.println("+===========================================+");
                    System.out.println("|           CHỌN LOẠI CẬP NHẬT              |");
                    System.out.println("+===========================================+");
                    System.out.println("|  1. Cập nhật Số lượng (Stock)             |");
                    System.out.println("|  2. Cập nhật Giá (Price)                  |");
                    System.out.println("|  0. Quay lại                              |");
                    System.out.println("+-------------------------------------------+");

                    int updateChoice = InputMethod.getInputInt("Lựa chọn của bạn: ");

                    if (updateChoice == 1) {
                        String name = InputMethod.getInputString("Nhập tên món ăn cần tìm: ");
                        int stock = InputMethod.getInputInt("Nhập số lượng cần cập nhật : ");
                        foodService.updateStock(name,stock);
                    } else if (updateChoice == 2) {
                        String name = InputMethod.getInputString("Nhập tên món ăn cần tìm: ");
                        double price = InputMethod.getInputDouble("Nhập giá cần cập nhật : ");
                        foodService.updatePrice(name,price);
                    } else if (updateChoice == 0) {
                        System.out.println("Đã ra menu ngoài..");
                        break;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Lựa chọn không hợp lệ!" + ColorConstants.RESET);
                    }
                    break;
                case 4:
                    String name = InputMethod.getInputString("Nhập tên món ăn cần xóa: ");

                    boolean result = foodService.delete(name.trim());

                    if (result) {
                        System.out.println(ColorConstants.SUCCESS + "Xóa món ăn thành công!" + ColorConstants.RESET);
                    }
                    break;
                case 5:
                    String findFood = InputMethod.getInputString("Nhập tên món ăn cần tìm: ");

                    MenuItems itemsFound = foodService.findByName(findFood);
                    System.out.println("Món ăn được tìm thấy : ");
                    itemsFound.displayData();
                    break;
                case 6:
                    System.out.println("Đã ra menu ngoài");
                    break;
                default:
                    System.out.println(ColorConstants.WARNING + "Lựa chọn sai vui lòng chọn lại" + ColorConstants.RESET);
            }
        }while (choice != 6);
    }
}
