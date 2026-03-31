package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.Impl.IAccountServiceImpl;
import re.restauran_manager.utils.InputMethod;

public class StatisticsMenu {
    private static final IAccountServiceImpl accountService = IAccountServiceImpl.getInstance();

    public static void displayMenu() {
        int choice = 0;
        do {
            System.out.println("\n+===========================================+");
            System.out.println("|            HỆ THỐNG THỐNG KÊ              |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Thống kê doanh thu theo ngày          |");
            System.out.println("|  2. Thống kê doanh thu theo tháng         |");
            System.out.println("|  3. Quay lại                              |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    accountService.statsByDay();
                    break;
                case 2:
                    accountService.statsByMonth();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 3);
    }
}
