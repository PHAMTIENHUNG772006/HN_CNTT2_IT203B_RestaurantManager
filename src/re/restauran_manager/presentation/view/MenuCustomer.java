package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.ICustomerService;
import re.restauran_manager.business.service.IService.IFoodService;
import re.restauran_manager.business.service.IService.ITableService;
import re.restauran_manager.business.service.Impl.ICustomerServiceImpl;
import re.restauran_manager.business.service.Impl.IFoodServiceImpl;
import re.restauran_manager.business.service.Impl.ITableServiceImpl;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Orders;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.utils.AccountSession;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.ArrayList;
import java.util.List;

public class MenuCustomer {
    static ICustomerService customerService = ICustomerServiceImpl.getInstance();
    static IFoodService foodService = IFoodServiceImpl.getInstance();
    static ITableService tableService = ITableServiceImpl.getInstance();

    public static void viewMenuCustomer() {
        AccountSession accountSession = AccountSession.getInstance();
        int choice = 0;

        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU CUSTOMER                |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách món ăn                  |");
            System.out.println("|  2. Chọn bàn (Mở hóa đơn)                 |");
            System.out.println("|  3. Gọi món                               |");
            System.out.println("|  4. Hủy món                               |");
            System.out.println("|  5. Theo dõi trạng thái món ăn            |");
            System.out.println("|  6. Thanh toán hóa đơn                    |");
            System.out.println("|  7. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn : ");

            int currentOrderId = accountSession.getCurrentOrder();

            switch (choice) {
                case 1:
                    customerService.displayPagination(customerService.getAllAvailableFood());
                    break;
                case 2:
                    handleSelectTable(accountSession);
                    break;
                case 3:
                    int idAfterOrder = orderFood(accountSession.getCurrentUser().getAccount_id());
                    if (idAfterOrder > 0) {
                        accountSession.setCurrentOrder(idAfterOrder);
                    }
                    break;
                case 4:
                    if (currentOrderId == 0) {
                        System.out.println(ColorConstants.WARNING + "Vui lòng chọn đơn hàng (trong mục Gọi món) trước khi hủy!" + ColorConstants.RESET);
                    } else {
                        int food_id = InputMethod.getInputInt("Nhập ID món muốn hủy : ");
                        int quantity = InputMethod.getInputInt("Nhập số lượng muốn hủy : ");
                        customerService.cancelFood(currentOrderId, food_id, quantity);
                    }
                    break;
                case 5:
                    if (currentOrderId == 0) {
                        System.out.println(ColorConstants.WARNING + "Vui lòng vào mục 'Gọi món' và chọn ID đơn hàng trước!" + ColorConstants.RESET);
                    } else {
                        customerService.trackOrderStatus(currentOrderId);
                    }
                    break;
                case 6:
                    int orderToPay = accountSession.getCurrentOrder();

                    if (orderToPay <= 0) {
                        System.out.println(ColorConstants.WARNING + "Không có đơn hàng nào đang hoạt động để thanh toán." + ColorConstants.RESET);
                    } else {
                        Orders currentOrder = null;
                        List<Orders> myOrders = customerService.getActiveOrders(accountSession.getCurrentUser().getAccount_id());

                        for(Orders o : myOrders) {
                            if(o.getId() == orderToPay) {
                                currentOrder = o;
                                break;
                            }
                        }

                        if (currentOrder != null) {
                            double total = customerService.checkout(orderToPay, currentOrder.getTableId());

                            if (total >= 0) {
                                accountSession.setCurrentOrder(0);
                            }
                        } else {
                            System.out.println(ColorConstants.ERROR + "Không tìm thấy thông tin bàn của hóa đơn này!" + ColorConstants.RESET);
                        }
                    }
                    break;
                case 7:
                    System.out.println("Đã quay lại menu chính.");
                    break;
                default:
                    System.out.println(ColorConstants.WARNING + "Lựa chọn không hợp lệ." + ColorConstants.RESET);
            }
        } while (choice != 7);
    }

    private static void handleSelectTable(AccountSession session) {
        List<Table> freeTables = customerService.getFreeTables();
        if (freeTables == null || freeTables.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Xin lỗi, hiện tại không còn bàn nào trống!" + ColorConstants.RESET);
            return;
        }

        Table.getHeader();
        freeTables.forEach(Table::displayData);
        Table.getFooter();

        int tableId = InputMethod.getInputInt("Nhập ID bàn muốn ngồi (0 để quay lại): ");
        if (tableId == 0) return;

        if (freeTables.stream().noneMatch(t -> t.getTable_id() == tableId)) {
            System.out.println(ColorConstants.ERROR + "ID bàn không hợp lệ!" + ColorConstants.RESET);
            return;
        }

        customerService.selectTable(session.getCurrentUser().getAccount_id(), tableId);
    }

    private static int orderFood(int currentAccountId) {
        List<Orders> activeOrders = customerService.getActiveOrders(currentAccountId);

        if (activeOrders == null || activeOrders.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Bạn chưa đặt bàn hôm nay! Vui lòng thực hiện 'Chọn bàn' trước." + ColorConstants.RESET);
            return 0;
        }

        System.out.println("\n--- DANH SÁCH ĐƠN HÀNG CỦA BẠN ---");
        Orders.getHeader();
        activeOrders.forEach(Orders::displayData);
        Orders.getFooter();

        int selectedOrderId = InputMethod.getInputInt("Nhập Mã ID Đơn hàng để tiếp tục (ID ở cột đầu tiên): ");

        // Kiểm tra xem ID người dùng nhập có nằm trong danh sách đơn hàng của họ không
        boolean isValid = activeOrders.stream().anyMatch(o -> o.getId() == selectedOrderId);

        if (!isValid) {
            System.out.println(ColorConstants.ERROR + "ID không hợp lệ hoặc không phải đơn hàng của bạn!" + ColorConstants.RESET);
            return 0;
        }

        List<MenuItems> cart = new ArrayList<>();
        List<MenuItems> allFood = customerService.getAllAvailableFood();
        customerService.displayPagination(allFood);

        while (true) {
            int foodId = InputMethod.getInputInt("Nhập ID món ăn (0 để dừng chọn): ");
            if (foodId == 0) break;

            MenuItems foodOriginal = foodService.findById(foodId);
            if (foodOriginal == null || foodOriginal.getStock() <= 0) {
                System.out.println(ColorConstants.ERROR + "Món không tồn tại hoặc đã hết hàng!" + ColorConstants.RESET);
                continue;
            }

            int quantity = InputMethod.getInputInt("Số lượng đặt [" + foodOriginal.getFood_name() + "]: ");
            if (quantity <= 0 || quantity > foodOriginal.getStock()) {
                System.out.println(ColorConstants.ERROR + "Số lượng không hợp lệ hoặc vượt quá kho!");
                continue;
            }

            // Tạo bản sao món ăn để cho vào giỏ hàng (tránh ghi đè stock gốc trong list)
            MenuItems itemInCart = new MenuItems();
            itemInCart.setFood_id(foodOriginal.getFood_id());
            itemInCart.setFood_name(foodOriginal.getFood_name());
            itemInCart.setPrice(foodOriginal.getPrice());
            itemInCart.setStock(quantity); // Ở đây stock đóng vai trò là số lượng đặt

            cart.add(itemInCart);

            String cont = InputMethod.getInputString("Tiếp tục chọn món khác? (Y/N): ");
            if (cont.equalsIgnoreCase("N")) break;
        }

        if (!cart.isEmpty()) {
            System.out.println("\n--- XÁC NHẬN GỬI BẾP ---");
            cart.forEach(i -> System.out.printf("- %-20s x %d\n", i.getFood_name(), i.getStock()));

            if (InputMethod.getInputBoolean("Xác nhận gửi yêu cầu? (true/false): ")) {
                if (customerService.placeOrder(selectedOrderId, cart)) {
                    System.out.println(ColorConstants.SUCCESS + "Yêu cầu đã được gửi đến nhà bếp!" + ColorConstants.RESET);
                    return selectedOrderId;
                }
            }
        }
        return selectedOrderId;
    }
}