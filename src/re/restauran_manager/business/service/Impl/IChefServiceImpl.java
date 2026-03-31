package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.ChefDao;
import re.restauran_manager.business.dao.FoodDao;
import re.restauran_manager.business.service.IService.IChefService;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.OrderDetailDisplay;
import re.restauran_manager.model.enties.OrderDetails;
import re.restauran_manager.model.enums.OrderDetailStatus;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.List;

public class IChefServiceImpl implements IChefService {
    ChefDao chefDao = ChefDao.getInstance();
    private static IChefServiceImpl instance;

    private IChefServiceImpl() {
    }

    public static IChefServiceImpl getInstance() {
        if (instance == null) {
            instance = new IChefServiceImpl();
        }
        return instance;
    }

    @Override
    public void getAll() {

        List<OrderDetailDisplay> list = chefDao.showOrderQueue();

        if (list.isEmpty()){
            System.out.println(ColorConstants.WARNING + "Danh sách trống. Hãy dọn dẹp qua bếp" + ColorConstants.RESET);
        }

        System.out.println(ColorConstants.SUCCESS + "\n--- Danh Sách Món Ăn Cần Làm ---" + ColorConstants.RESET);

        OrderDetailDisplay.getHeader();
        for (OrderDetailDisplay item : list) {
            item.display();
        }
        OrderDetailDisplay.getFooter();
    }

    @Override
    public boolean updateStatus(int id) {
        OrderDetails orderDetails = chefDao.findOrderDetailById(id);

        if (orderDetails == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy món ăn có ID này!" + ColorConstants.RESET);
            return false;
        }

        OrderDetailStatus status = orderDetails.getStatus();


        if (status == OrderDetailStatus.CANCEL) {
            System.out.println(ColorConstants.ERROR + "Món này có thể đã bị hủy!" + ColorConstants.RESET);
            return false;
        }
        if (status == OrderDetailStatus.SERVED) {
            System.out.println(ColorConstants.WARNING + "Món này đã phục vụ xong." + ColorConstants.RESET);
            return false;
        }

        String nextStatus = "";
        if (status == OrderDetailStatus.PENDING) {
            nextStatus = "COOKING";
        } else if (status == OrderDetailStatus.COOKING) {
            nextStatus = "READY";
        } else if (status == OrderDetailStatus.READY) {
            nextStatus = "SERVED";
            System.out.println(ColorConstants.SUCCESS + "Món ăn đã được phục vụ" + ColorConstants.RESET);
        }

        if (!nextStatus.isEmpty()) {
            return chefDao.updateStatus(id, nextStatus);
        }

        return false;
    }

    @Override
    public boolean updateStock(int id, int stock) {
        if (stock < 0) {
            System.out.println(ColorConstants.ERROR + "Số lượng tồn kho không được âm." + ColorConstants.RESET);
            return false;
        }

        FoodDao foodDao = FoodDao.getInstance();

        displayPagination(foodDao.displayAll());

        MenuItems items = foodDao.findById(id);

        if (items == null){
            System.out.println(ColorConstants.ERROR + "Không thấy ID cần cập nhật" + ColorConstants.RESET);
            return false;
        }

        return chefDao.updateStock(id, stock);
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

            // Kiểm tra và gọi header/footer từ class MenuItems
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
}














