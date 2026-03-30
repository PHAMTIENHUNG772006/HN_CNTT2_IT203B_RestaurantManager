package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.ITableService;
import re.restauran_manager.business.service.Impl.ITableServiceImpl;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;


public class TableMenu {
    public static void viewMenuManagerTable() {
        ITableService tableService = ITableServiceImpl.getInstance();
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU TABLE                   |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách bàn                     |");
            System.out.println("|  2. Thêm bàn mới                          |");
            System.out.println("|  3. Cập nhật thông tin bàn                |");
            System.out.println("|  4. Xóa bàn                               |");
            System.out.println("|  5. Tìm kiếm bàn theo ID                  |");
            System.out.println("|  6. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    tableService.displayAll();
                    break;
                case 2:
                    Table newTable = Table.inputData();
                    if (tableService.add(newTable)) {
                        System.out.println(ColorConstants.SUCCESS + "Thêm bàn thành công!" + ColorConstants.RESET);
                    }
                    break;
                case 3:
                    handleUpdateTable(tableService);
                    break;
                case 4:
                    int idDelete = InputMethod.getInputInt("Nhập ID bàn cần xóa: ");
                    if (tableService.deleteTable(idDelete)) {
                        System.out.println(ColorConstants.SUCCESS + "Xóa bàn thành công!" + ColorConstants.RESET);
                    }
                    break;
                case 5:
                    int idFind = InputMethod.getInputInt("Nhập ID cần tìm: ");
                    Table table = tableService.findById(idFind);
                    if (table == null) {
                        System.out.println(ColorConstants.ERROR + "Không tìm thấy bàn có ID: " + idFind + ColorConstants.RESET);
                    } else {
                        Table.getHeader();
                        table.displayData();
                        Table.getFooter();
                    }
                    break;
                case 6:
                    System.out.println("Quay lại menu quản lý...");
                    break;
                default:
                    System.out.println(ColorConstants.WARNING + "Lựa chọn không hợp lệ!" + ColorConstants.RESET);
            }
        } while (choice != 6);
    }

    private static void handleUpdateTable(ITableService tableService) {
        System.out.println("+===========================================+");
        System.out.println("|           CHỌN LOẠI CẬP NHẬT              |");
        System.out.println("+===========================================+");
        System.out.println("|  1. Cập nhật số ghế (Seat)                |");
        System.out.println("|  2. Cập nhật trạng thái (Status)          |");
        System.out.println("|  0. Quay lại                              |");
        System.out.println("+-------------------------------------------+");

        int updateChoice = InputMethod.getInputInt("Lựa chọn của bạn: ");
        if (updateChoice == 0) return;

        int id = InputMethod.getInputInt("Nhập ID bàn cần cập nhật: ");

        switch (updateChoice) {
            case 1:
                int numberSeat = InputMethod.getInputInt("Nhập số lượng ghế mới: ");
                if (tableService.updateSeat(id, numberSeat)) {
                    System.out.println(ColorConstants.SUCCESS + "Cập nhật số ghế thành công!" + ColorConstants.RESET);
                }
                break;
            case 2:
                System.out.println("Chọn trạng thái mới:");
                System.out.println("1. FREE (Trống)");
                System.out.println("2. OCCUPIED (Có khách)");
                System.out.println("3. RESERVED (Đã đặt)");
                System.out.println("4. DAMAGED (Hỏng)");
                int statusSelect = InputMethod.getInputInt("Nhập lựa chọn (1-4): ");
                if (tableService.updateStatus(id, statusSelect)) {
                    System.out.println(ColorConstants.SUCCESS + "Cập nhật trạng thái thành công!" + ColorConstants.RESET);
                }
                break;
            default:
                System.out.println(ColorConstants.WARNING + "Lựa chọn không hợp lệ!" + ColorConstants.RESET);
        }
    }
}