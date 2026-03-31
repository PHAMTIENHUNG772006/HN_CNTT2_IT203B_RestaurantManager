package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.IChefService;
import re.restauran_manager.business.service.Impl.IChefServiceImpl;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.Scanner;

public class MenuChef {
    public static void viewMenuChef() {
        Scanner sc = new Scanner(System.in);
        IChefService chefService = IChefServiceImpl.getInstance();
        int choice = 0;
        do {
            System.out.println("\n+===========================================+");
            System.out.println("|                MENU CHEF                  |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách món được đặt (Queue)    |");
            System.out.println("|  2. Cập nhật trạng thái chế biến món ăn   |");
            System.out.println("|  3. Cập nhật số lượng hàng trong kho      |");
            System.out.println("|  4. Đăng xuất                             |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    chefService.getAll();
                    break;
                case 2:
                    int id = InputMethod.getInputInt("Nhập vào ID cần cập nhật : ");

                    boolean isUpdate = chefService.updateStatus(id);

                    if (isUpdate){
                        System.out.println(ColorConstants.SUCCESS + "Đã cập nhật thành công trạng thái" + ColorConstants.RESET);
                    }else {
                        System.out.println(ColorConstants.ERROR + "Đã cập nhật trạng thái không thành công" + ColorConstants.RESET);
                    }
                    break;
                case 3:
                    int idUpdate = InputMethod.getInputInt("Nhập vào ID cần cập nhật : ");
                    int stock = InputMethod.getInputInt("Nhập vào số lượng cần cập nhật : ");

                    boolean isUpdateStock = chefService.updateStock(idUpdate,stock);

                    if (isUpdateStock){
                        System.out.println(ColorConstants.SUCCESS + "Đã cập nhật thành công số lượng" + ColorConstants.RESET);
                    }else {
                        System.out.println(ColorConstants.ERROR + "Cập nhật số lượng không thành công" + ColorConstants.RESET);
                    }
                    break;
                case 4:
                    System.out.println(ColorConstants.SUCCESS + "Đang đăng xuất quyền Đầu bếp..." + ColorConstants.RESET);
                    break;
                default:
                    System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ!" + ColorConstants.RESET);
            }
        } while (choice != 4);
    }
}
