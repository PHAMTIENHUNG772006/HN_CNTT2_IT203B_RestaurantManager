package re.restauran_manager.view;

import java.util.Scanner;

public class MenuManager {
    public static void viewMenuManager(Scanner sc) {

        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU MANAGER                 |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Quản lý danh sách bàn                 |");
            System.out.println("|  2. Quản lý danh sách(món ắn, đồ uống)    |");
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


    public static void viewMenuManagerTable(Scanner sc) {
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU TABLE                   |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách bàn                     |");
            System.out.println("|  2. Cập nhật thông tin bàn                |");
            System.out.println("|  3. Xóa bàn                               |");
            System.out.println("|  4. Tìm kiếm bàn                          |");
            System.out.println("|  5. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.out.println("Đã ra menu ngoài");
                    break;
                default:
                    System.out.println("Lựa chọn sai vui lòng chọn lại..");
            }
        } while (choice != 5);
    }


    public static void viewMenuManagerFood(Scanner sc) {
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU TABLE                   |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách món ăn                  |");
            System.out.println("|  2. Cập nhật thông tin món ăn             |");
            System.out.println("|  3. Xóa món ăn                            |");
            System.out.println("|  4. Tìm kiếm món ăn                       |");
            System.out.println("|  5. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.out.println("Đã ra menu ngoài");
                    break;
                default:
                    System.out.println("Lựa chọn sai vui lòng chọn lại..");
            }
        } while (choice != 5);
    }
}
