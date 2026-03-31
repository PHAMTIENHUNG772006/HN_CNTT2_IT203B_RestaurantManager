package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.IAccountService;
import re.restauran_manager.utils.InputMethod;

import java.util.Scanner;

public class MainMenu {
    public static void viewAuthor(Scanner sc) {
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU MANAGER                 |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Đăng kí tài khoản                     |");
            System.out.println("|  2. Đăng nhập                             |");
            System.out.println("|  3. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn :");
            switch (choice) {
                case 1:
                    AuthenticationMenu.printRegisterMenu();
                    break;
                case 2:
                    AuthenticationMenu.printLoginMenu();
                    break;
                case 3:
                    System.out.println("Đã Thoát chương trình thành công..");
                    break;
                default:
                    System.out.println("Lựa chọn sai vui lòng chọn lại..");
            }
        } while (choice != 3);
    }
}
