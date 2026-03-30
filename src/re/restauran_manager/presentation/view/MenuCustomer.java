package re.restauran_manager.presentation.view;

import re.restauran_manager.business.dao.TableDao;
import re.restauran_manager.business.service.IService.ICustomerService;
import re.restauran_manager.business.service.IService.IFoodService;
import re.restauran_manager.business.service.IService.ITableService;
import re.restauran_manager.business.service.Impl.ICustomerServiceImpl;
import re.restauran_manager.business.service.Impl.IFoodServiceImpl;
import re.restauran_manager.business.service.Impl.ITableServiceImpl;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.utils.AccountSession;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuCustomer {
    static ICustomerService customerService = ICustomerServiceImpl.getInstance();
    static IFoodService foodService = IFoodServiceImpl.getInstance();
    static ITableService tableService = ITableServiceImpl.getInstance();

    public static void viewMenuCustomer() {
        Scanner sc = new Scanner(System.in);

        AccountSession accountSession = AccountSession.getInstance();
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
            switch (choice) {
                case 1:
                    List<MenuItems> foods = customerService.getAllAvailableFood();

                    customerService.displayPagination(foods);
                    break;
                case 2:

                    List<Table> freeTables = customerService.getFreeTables();

                    if (freeTables.isEmpty()) {
                        System.out.println(ColorConstants.WARNING + "Xin lỗi, hiện tại không còn bàn nào trống!" + ColorConstants.RESET);
                        return;
                    }

                    Table.getHeader();
                   freeTables.stream().forEach(table -> table.displayData());
                    Table.getFooter();
                    int tableId = InputMethod.getInputInt("Nhập ID bàn muốn ngồi (0 để thoát): ");
                    if (tableId == 0) return;
                    boolean isAvailable = freeTables.stream().anyMatch(t -> t.getTable_id() == tableId);

                    if (!isAvailable) {
                        System.out.println(ColorConstants.ERROR + "ID bàn không hợp lệ hoặc bàn này vừa có người ngồi!" + ColorConstants.RESET);
                        break;
                    }
                    System.out.println(tableId);
                    boolean success = customerService.selectTable(accountSession.getCurrentUser().getAccount_id(), tableId);

                    if (success) {
                        System.out.println(ColorConstants.SUCCESS + "Chọn bàn thành công! Mời bạn chọn món." + ColorConstants.RESET);
                    } else {
                        System.out.println(ColorConstants.ERROR + "Có lỗi xảy ra, vui lòng thử lại." + ColorConstants.RESET);
                    }
                    break;
                case 3:
                    orderFood(AccountSession.getInstance().getCurrentUser().getAccount_id(),AccountSession.getInstance().getCurrentOrder());
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
        } while (choice != 6);
    }



    private static void orderFood(int currentAccount, int currentOrder) {
        if (currentOrder <= 0) {
            System.out.println(ColorConstants.WARNING + "Vui lòng đặt bàn trước khi gọi món!" + ColorConstants.RESET);
            return;
        }

        List<MenuItems> cart = new ArrayList<>();
        List<MenuItems> availableMenu = customerService.getAllAvailableFood();
        customerService.displayPagination(availableMenu);

        while (true) {
            int foodId = InputMethod.getInputInt("Nhập ID món ăn muốn gọi (0 để dừng chọn): ");
            if (foodId == 0) break;

            MenuItems food = foodService.findById(foodId);
            if (food == null || food.getStock() <= 0) {
                System.out.println(ColorConstants.ERROR + "Món ăn không tồn tại hoặc đã hết hàng!" + ColorConstants.RESET);
                continue;
            }

            int quantity = InputMethod.getInputInt("Nhập số lượng cho món [" + food.getFood_name() + "] (Còn " + food.getStock() + "): ");

            if (quantity <= 0) {
                System.out.println(ColorConstants.ERROR + "Số lượng phải lớn hơn 0!" + ColorConstants.RESET);
                continue;
            }

            if (quantity > food.getStock()) {
                System.out.println(ColorConstants.ERROR + "Số lượng trong kho không đủ!" + ColorConstants.RESET);
                continue;
            }


            food.setStock(quantity);
            cart.add(food);
            System.out.println(ColorConstants.SUCCESS + "Đã thêm " + quantity + " " + food.getFood_name() + " vào danh sách chờ." + ColorConstants.RESET);

            String cont = InputMethod.getInputString("Tiếp tục chọn món? (Y/N): ");
            if (cont.equalsIgnoreCase("N")) break;
        }

        if (!cart.isEmpty()) {
            System.out.println("\n--- XÁC NHẬN ĐƠN HÀNG ---");
            for (MenuItems item : cart) {
                System.out.printf("- %-20s x %d\n", item.getFood_name(), item.getStock());
            }

            boolean confirm = InputMethod.getInputBoolean("Xác nhận gửi yêu cầu đến bếp? (true/false): ");
            if (confirm) {
                boolean success = customerService.placeOrder(currentOrder, cart);
                if (success) {
                    System.out.println(ColorConstants.SUCCESS + "Gọi món thành công! Vui lòng chờ trong giây lát." + ColorConstants.RESET);
                }
            }
        } else {
            System.out.println("Bạn chưa chọn món ăn nào.");
        }
    }
}
