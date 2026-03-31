package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.CustomerDao;
import re.restauran_manager.business.service.IService.ICustomerService;
import re.restauran_manager.model.enties.*;
import re.restauran_manager.model.enums.OrderDetailStatus;
import re.restauran_manager.utils.AccountSession;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.ArrayList;
import java.util.List;

public class ICustomerServiceImpl implements ICustomerService {

    private final CustomerDao customerDao = CustomerDao.getInstance();
    private static ICustomerServiceImpl instance;

    private ICustomerServiceImpl() {}


    public static synchronized ICustomerServiceImpl getInstance() {
        if (instance == null) {
            instance = new ICustomerServiceImpl();
        }
        return instance;
    }


    @Override
    public List<MenuItems> getAllAvailableFood() {
        List<MenuItems> foods = customerDao.getAllAvailableFood();
        if (foods == null || foods.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Hiện tại nhà hàng chưa phục vụ món nào!" + ColorConstants.RESET);
            return new ArrayList<>();
        }
        return foods;
    }

    @Override
    public void displayPagination(List<MenuItems> listFood) {
        if (listFood == null || listFood.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Danh sách món ăn đang trống!" + ColorConstants.RESET);
            return;
        }

        int pageSize = 5;
        int currentPage = 1;

        while (true) {
            int totalPages = (int) Math.ceil((double) listFood.size() / pageSize);
            if (currentPage > totalPages) currentPage = totalPages;
            if (currentPage < 1) currentPage = 1;

            System.out.println(ColorConstants.SUCCESS + "\n--- MENU NHÀ HÀNG (TRANG " + currentPage + "/" + totalPages + ") ---" + ColorConstants.RESET);


            MenuItems.getHeader();
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, listFood.size());

            for (int i = startIndex; i < endIndex; i++) {
                listFood.get(i).displayData();
            }
            MenuItems.getFooter();

            System.out.println("Tùy chọn: [N] Tiếp | [P] Trước | [S] Chọn trang | [E] Thoát");
            String choice = InputMethod.getInputString("Nhập lựa chọn của bạn: ").toUpperCase();

            if (choice.equals("N") && currentPage < totalPages) currentPage++;
            else if (choice.equals("P") && currentPage > 1) currentPage--;
            else if (choice.equals("S")) {
                int targetPage = InputMethod.getInputInt("Nhập số trang (1 - " + totalPages + "): ");
                if (targetPage >= 1 && targetPage <= totalPages) currentPage = targetPage;
                else System.out.println(ColorConstants.WARNING + "Số trang không hợp lệ!" + ColorConstants.RESET);
            }
            else if (choice.equals("E")) break;
            else System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ hoặc đã hết trang!" + ColorConstants.RESET);
        }
    }

    // ================= TABLE =================
    @Override
    public List<Table> getFreeTables() {
        List<Table> freeTables = customerDao.listTableFree();
        if (freeTables == null || freeTables.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Hiện tại không có bàn nào còn trống!" + ColorConstants.RESET);
            return new ArrayList<>();
        }
        return freeTables;
    }

    @Override
    public boolean selectTable(int accountId, int tableId) {
        if (tableId <= 0) {
            System.out.println(ColorConstants.ERROR + "ID bàn không hợp lệ!" + ColorConstants.RESET);
            return false;
        }

        List<Table> freeTables = customerDao.listTableFree();
        boolean exists = freeTables.stream().anyMatch(t -> t.getTable_id() == tableId);

        if (!exists) {
            System.out.println(ColorConstants.ERROR + "Bàn không hợp lệ hoặc đã có người!" + ColorConstants.RESET);
            return false;
        }

        int orderId = customerDao.createOrder(accountId, tableId);
        if (orderId != -1) {
            AccountSession.getInstance().setCurrentOrder(orderId);
            System.out.println(ColorConstants.SUCCESS + "Mở order thành công ID: " + orderId + ColorConstants.RESET);
            return true;
        }
        return false;
    }

    // ================= ORDER =================
    @Override
    public boolean placeOrder(int orderId, List<MenuItems> foods) {
        if (orderId <= 0 || foods == null || foods.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Dữ liệu đặt món không hợp lệ!" + ColorConstants.RESET);
            return false;
        }

        // Lưu ý: Trong cart, bạn dùng getStock() để lưu số lượng khách đặt
        for (MenuItems item : foods) {
            if (item.getStock() <= 0) {
                System.out.println(ColorConstants.ERROR + "Số lượng món đặt phải > 0!" + ColorConstants.RESET);
                return false;
            }
        }

        boolean result = customerDao.addListFoodToOrder(orderId, foods);
        if (result) {
            System.out.println(ColorConstants.SUCCESS + "Đặt món thành công!" + ColorConstants.RESET);
        } else {
            System.out.println(ColorConstants.ERROR + "Đặt món thất bại!" + ColorConstants.RESET);
        }
        return result;
    }

    @Override
    public List<Orders> getActiveOrders(int account_id) {
        return customerDao.getActiveOrders(account_id);
    }

    // ================= TRACK =================
    @Override
    public void trackOrderStatus(int orderId) {
        if (orderId <= 0) {
            System.out.println(ColorConstants.WARNING + "Bạn chưa chọn đơn hàng để theo dõi!" + ColorConstants.RESET);
            return;
        }

        List<OrderDetailDisplay> details = customerDao.getCurrentOrderDetails(orderId);
        if (details == null || details.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Đơn hàng này chưa có món nào!" + ColorConstants.RESET);
            return;
        }

        OrderDetailDisplay.getHeader();
        for (OrderDetailDisplay d : details) {
            d.display();
        }
        OrderDetailDisplay.getFooter();
    }

    // ================= CHECKOUT =================
    @Override
    public double checkout(int orderId, int tableId) {

        if (orderId <= 0 || tableId <= 0) {
            System.out.println(ColorConstants.ERROR + "Thông tin thanh toán không hợp lệ (ID Bàn hoặc Đơn hàng trống)!" + ColorConstants.RESET);
            return -1;
        }

        int count = customerDao.countUnfinishedDishes(orderId);
        boolean shouldCancel = false;

        if (count > 0) {
            System.out.println(ColorConstants.WARNING + "Có " + count + " món chưa phục vụ xong!" + ColorConstants.RESET);
            String choice = InputMethod.getInputString("Nhập 'Y' để hủy các món này và thanh toán luôn: ");
            if (choice.equalsIgnoreCase("Y")) {
                shouldCancel = true;
            } else {
                return -2;
            }
        }

        double total = customerDao.processCheckout(tableId, orderId, shouldCancel);

        if (total >= 0) {
            System.out.println(ColorConstants.SUCCESS + "Thanh toán thành công. Tổng tiền: " + total + " VND" + ColorConstants.RESET);
            AccountSession.getInstance().setCurrentOrder(0);
        } else {
            System.out.println(ColorConstants.ERROR + "Lỗi hệ thống: Thanh toán thất bại!" + ColorConstants.RESET);
        }

        return total;
    }

    // ================= CANCEL =================
    @Override
    public boolean cancelFood(int orderId, int foodId, int quantity) {
        if (orderId <= 0 || foodId <= 0 || quantity <= 0) {
            System.out.println(ColorConstants.WARNING + "Thông tin không hợp lệ!" + ColorConstants.RESET);
            return false;
        }

        OrderDetails detail = customerDao.findOrderDetail(orderId, foodId);

        if (detail == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy món này trong hóa đơn!" + ColorConstants.RESET);
            return false;
        }

        if (detail.getStatus() != OrderDetailStatus.PENDING) {
            System.out.println(ColorConstants.ERROR + "Món đang được chế biến, không thể hủy!" + ColorConstants.RESET);
            return false;
        }

        if (quantity > detail.getQuantity()) {
            System.out.println(ColorConstants.ERROR + "Số lượng hủy lớn hơn số lượng đã đặt!" + ColorConstants.RESET);
            return false;
        }

        boolean result = customerDao.cancelFood(orderId, foodId, quantity);
        if (result) {
            System.out.println(ColorConstants.SUCCESS + "Hủy món thành công!" + ColorConstants.RESET);
        } else {
            System.out.println(ColorConstants.ERROR + "Hủy món thất bại!" + ColorConstants.RESET);
        }
        return result;
    }
}