package re.restauran_manager.presentation.view;

import re.restauran_manager.business.dao.FoodDao;
import re.restauran_manager.business.service.IService.IFoodService;
import re.restauran_manager.business.service.Impl.IFoodServiceImpl;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.Scanner;

public class MenuCustomer {
    public static void viewMenuCustomer() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU CUSTOMER                |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách món ăn                  |");
            System.out.println("|  2. Chọn bàn                              |");
            System.out.println("|  3. Gọi món                               |");
            System.out.println("|  4. Theo dõi trạng thái món ăn            |");
            System.out.println("|  5. Thanh toán hóa đơn                    |");
            System.out.println("|  6. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn : ");
            switch (choice){
                case 1:
                    IFoodService foodService = IFoodServiceImpl.getInstance();
                    foodService.displayAll();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.out.println("Đã ra menu ngoài");
                    break;
                default:
                    System.out.println(ColorConstants.WARNING + "Lựa chọn không hợp lệ. Chọn lại" + ColorConstants.RESET);
            }
        }while (choice != 6);
    }
}
