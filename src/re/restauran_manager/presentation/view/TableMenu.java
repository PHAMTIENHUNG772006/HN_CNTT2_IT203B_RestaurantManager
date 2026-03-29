package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.ITableService;
import re.restauran_manager.business.service.Impl.ITableServiceImpl;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.Scanner;

public class TableMenu {
    public static void viewMenuManagerTable() {
        Scanner sc = new Scanner(System.in);
        ITableService tableService = ITableServiceImpl.getInstance();
        int choice = 0;
        do {
            System.out.println("+===========================================+");
            System.out.println("|              MENU TABLE                   |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Xem danh sách bàn                     |");
            System.out.println("|  2. Thêm bàn                              |");
            System.out.println("|  3. Cập nhật thông tin bàn                |");
            System.out.println("|  4. Xóa bàn                               |");
            System.out.println("|  5. Tìm kiếm bàn                          |");
            System.out.println("|  6. Thoát                                 |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Nhập lựa chọn : ");

            switch (choice) {
                case 1:
                    tableService.displayAll();
                    break;
                case 2:
                    Table newTable = Table.inputData();

                    boolean isAdd = tableService.add(newTable);

                    if(isAdd){
                        System.out.println(ColorConstants.SUCCESS + "Thêm thành công bàn" + ColorConstants.RESET);
                    }
                    break;
                case 3:
                    System.out.println("+===========================================+");
                    System.out.println("|           CHỌN LOẠI CẬP NHẬT              |");
                    System.out.println("+===========================================+");
                    System.out.println("|  1. Cập nhật ghế (Seat)                   |");
                    System.out.println("|  2. Cập nhật trạng thái(Status)           |");
                    System.out.println("|  0. Quay lại                              |");
                    System.out.println("+-------------------------------------------+");
                    boolean isUpdate = false;
                    int updateChoice = InputMethod.getInputInt("Lựa chọn của bạn: ");

                    if (updateChoice == 1) {
                        int id = InputMethod.getInputInt("Nhập ID bàn cần tìm: ");
                        int numberSeat = InputMethod.getInputInt("Nhập số lượng ghế cần cập nhật : ");
                        isUpdate = tableService.updateSeat(id,numberSeat);
                        if (isUpdate){
                            System.out.println(ColorConstants.SUCCESS + "Cập nhật bàn thành công!" + ColorConstants.RESET);
                        }
                    } else if (updateChoice == 2) {
                        int id = InputMethod.getInputInt("Nhập ID bàn cần tìm: ");
                        System.out.println("Chọn trạng thái mới: 1. EMMPTY, 2. OCCUPIED");
                        int statusSelect = InputMethod.getInputInt("Nhập lựa chọn: ");

                        isUpdate = tableService.updateStatus(id, statusSelect);
                        if (isUpdate){
                            System.out.println(ColorConstants.SUCCESS + "Cập nhật bàn thành công!" + ColorConstants.RESET);
                        }
                    } else if (updateChoice == 0) {
                        System.out.println("Đã ra menu ngoài..");
                        break;
                    } else {
                        System.out.println(ColorConstants.WARNING + "Lựa chọn không hợp lệ!" + ColorConstants.RESET);
                    }
                    break;
                case 4:

                    int id = InputMethod.getInputInt("Nhập ID bàn cần xóa: ");

                    boolean result = tableService.deleteTable(id);

                    if (result) {
                        System.out.println(ColorConstants.SUCCESS + "Xóa bàn thành công!" + ColorConstants.RESET);
                    }
                    break;
                case 5:
                    int findTable = InputMethod.getInputInt("Nhập ID cần tìm: ");

                    Table table = tableService.findById(findTable);
                    System.out.println("Bàn được tìm thấy : ");
                    table.displayData();
                    break;
                case 6:
                    System.out.println("Đã ra menu ngoài");
                    break;
                default:
                    System.out.println("Lựa chọn sai vui lòng chọn lại..");
            }
        } while (choice != 6);
    }
}
