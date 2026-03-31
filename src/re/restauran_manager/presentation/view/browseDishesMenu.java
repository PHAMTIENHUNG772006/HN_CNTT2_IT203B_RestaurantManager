package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.Impl.IAccountServiceImpl;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

public class browseDishesMenu {
    private static final IAccountServiceImpl accountService = IAccountServiceImpl.getInstance();

    public static void browseDishes() {
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|             MENU BROWSE DISHES            |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Duyệt theo ID ORDER                   |");
            System.out.println("|  2. Duyệt tất cả                          |");
            System.out.println("|  3. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn: ");
            switch (choice) {
                case 1:
                    handleBrowseById();
                    break;
                case 2:
                    handleBrowseAll();
                    break;
                case 3:
                    System.out.println("Đã quay lại menu trước.");
                    break;
                default:
                    System.out.println(ColorConstants.ERROR + "Lựa chọn sai, vui lòng chọn lại." + ColorConstants.RESET);
            }
        } while (choice != 3);
    }

    private static void handleBrowseById() {
        int orderId = InputMethod.getInputInt("Nhập ID Order cần duyệt: ");
        boolean isSuccess = accountService.browseDishesById(orderId);

        if (isSuccess) {
            System.out.println(ColorConstants.SUCCESS + "Đã duyệt đơn hàng ID " + orderId + " thành công!" + ColorConstants.RESET);
        } else {
            System.out.println(ColorConstants.ERROR + "Duyệt thất bại. Vui lòng kiểm tra lại ID." + ColorConstants.RESET);
        }
    }

    private static void handleBrowseAll() {
        System.out.println("Đang tiến hành duyệt tất cả các đơn hàng...");
        boolean isSuccess = accountService.browseDishesAll();

        if (isSuccess) {
            System.out.println(ColorConstants.SUCCESS + "Tất cả đơn hàng đã được duyệt thành PENDING." + ColorConstants.RESET);
        } else {
            System.out.println(ColorConstants.ERROR + "Không có đơn hàng nào cần duyệt hoặc có lỗi xảy ra." + ColorConstants.RESET);
        }
    }
}