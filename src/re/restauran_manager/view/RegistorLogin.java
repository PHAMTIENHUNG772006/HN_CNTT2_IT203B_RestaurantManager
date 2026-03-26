package re.restauran_manager.view;

import java.util.Scanner;

public class RegistorLogin {
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

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println("Đã ra menu ngoài");
                    break;
                default:
                    System.out.println("Lựa chọn sai vui lòng chọn lại..");
            }
        } while (choice != 3);


    }
}
