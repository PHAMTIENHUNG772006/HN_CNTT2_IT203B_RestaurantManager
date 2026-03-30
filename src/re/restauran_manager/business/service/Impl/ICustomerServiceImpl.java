package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.CustomerDao;
import re.restauran_manager.business.service.IService.ICustomerService;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.utils.AccountSession;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.ArrayList;
import java.util.List;

public class ICustomerServiceImpl implements ICustomerService {
    CustomerDao customerDao = CustomerDao.getInstance();
    private static ICustomerServiceImpl instance;

    private ICustomerServiceImpl() {
    }

    public static ICustomerServiceImpl getInstance() {
        if (instance == null) {
            instance = new ICustomerServiceImpl();
        }
        return instance;
    }


    @Override
    public List<MenuItems> getAllAvailableFood() {
        List<MenuItems> foods = customerDao.getAllAvailableFood();

        if (foods == null || foods.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Hiện tại nhà hàng chưa phục vụ" + ColorConstants.RESET);
            return new ArrayList<>();
        }
        return foods;
    }

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
    public void displayPagination(List<MenuItems> listFood) {

        if (listFood.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Danh món ăn đang trống!" + ColorConstants.RESET);
            return;
        }

        int pageSize = 5; // Hiển thị 2 sinh viên/trang để dễ test với 5 dữ liệu mẫu
        int totalPages = (int) Math.ceil((double) listFood.size() / pageSize);
        int currentPage = 1;

        while (true) {
            // Cập nhật lại tổng số trang phòng trường hợp danh sách bị thay đổi ở thread khác (tuy ít xảy ra ở console app này)
            totalPages = (int) Math.ceil((double) listFood.size() / pageSize);
            if (currentPage > totalPages) currentPage = totalPages;

            System.out.println(ColorConstants.SUCCESS + "\n--- HIỂN THỊ DANH SÁCH (TRANG " + currentPage + "/" + totalPages + ") ---" + ColorConstants.RESET);
            MenuItems.getHeader();

            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, listFood.size());

            for (int i = startIndex; i < endIndex; i++) {
                listFood.get(i).displayData();
            }
            MenuItems.getFooter();

            System.out.println("Tùy chọn: [N] Next | [P] Prev | [S] Chọn trang | [E] Thoát");
            String choice = InputMethod.getInputString("Nhập lựa chọn của bạn: ").toUpperCase();

            switch (choice) {
                case "N":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Bạn đang ở trang cuối cùng!" + ColorConstants.RESET);
                    }
                    break;
                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Bạn đang ở trang đầu tiên!" + ColorConstants.RESET);
                    }
                    break;
                case "S":
                    int targetPage = InputMethod.getInputInt("Nhập số trang muốn đến (1 - " + totalPages + "): ");
                    if (targetPage >= 1 && targetPage <= totalPages) {
                        currentPage = targetPage;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Số trang không hợp lệ!" + ColorConstants.RESET);
                    }
                    break;
                case "E":
                    System.out.println(ColorConstants.SUCCESS + "Đã thoát chế độ phân trang." + ColorConstants.RESET);
                    return;
                default:
                    System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ, vui lòng nhập N, P, S hoặc E." + ColorConstants.RESET);
            }
        }
    }


    @Override
    public boolean selectTable(int accountId, int tableId) {

        if (tableId <= 0) {
            System.out.println(ColorConstants.ERROR + "ID bàn không hợp lệ!" + ColorConstants.RESET);
            return false;
        }

        int orderId = customerDao.createOrder(accountId, tableId);

        if (orderId != -1) {
            AccountSession currentAccount =  AccountSession.getInstance();
            currentAccount.setCurrentOrder(orderId);
            System.out.println(ColorConstants.SUCCESS + "Đã mở hóa đơn mới (ID: " + orderId + ") cho bàn " + tableId + ColorConstants.RESET);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean placeOrder(int order_id, List<MenuItems> foods) {
        if (order_id <= 0 ){
            System.out.println(ColorConstants.WARNING + "ID order không hợp kệ" + ColorConstants.RESET);
            return false;
        }

        if (foods.isEmpty()){
            System.out.println(ColorConstants.WARNING + "Danh sách truyền vào rỗng" + ColorConstants.RESET);
            return false;
        }

        customerDao.insertFood(order_id,foods);
        return false;
    }

    @Override
    public void trackOrderStatus(int order_id) {

    }

    @Override
    public double checkout(int accountId, int tableId) {
        return 0;
    }



}
