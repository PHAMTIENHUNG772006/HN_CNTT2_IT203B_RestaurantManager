package re.restauran_manager.presentation.view;

import re.restauran_manager.utils.InputMethod;

import java.util.Scanner;

public class MenuManager {
    public static void viewMenuManager() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU MANAGER                 |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Quản lý danh sách bàn                 |");
            System.out.println("|  2. Quản lý danh sách(món ắn, đồ uống)    |");
            System.out.println("|  3. Quản lý tài khoản                     |");
            System.out.println("|  4. Duyệt món ăn                          |");
            System.out.println("|  5. Thống kê và báo cáo                   |");
            System.out.println("|  6. Đăng xuất                             |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn : ");

            switch (choice) {
                case 1:
                    viewMenuManagerTable();
                    break;
                case 2:
                    viewMenuManagerFood();
                    break;
                case 3:
                    AccountManager.viewMenuAccount();
                    break;
                case 4:
                    browseDishesMenu.browseDishes();
                    break;
                case 5:
                    StatisticsMenu.displayMenu();
                    break;
                case 6:
                    System.out.println("Đã đăng xuất");
                    break;
                default:
                    System.out.println("Lựa chọn sai vui lòng chọn lại..");
            }
        } while (choice != 6);
    }


    public static void viewMenuManagerTable() {
        TableMenu.viewMenuManagerTable();
    }


    public static void viewMenuManagerFood() {
        FoodMenu.viewMenuFood();
    }

}
