package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.TableStatus;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

public class Table {
    private int table_id;
    private int number_seats;
    private TableStatus status;

    public Table() {
    }

    public Table(int table_id, int number_seats, TableStatus status) {
        this.table_id = table_id;
        this.number_seats = number_seats;
        this.status = status;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public int getNumber_seats() {
        return number_seats;
    }

    public void setNumber_seats(int number_seats) {
        this.number_seats = number_seats;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }


    public static void getHeader() {
        System.out.println("╔══════════╦═════════════════╦═════════════════╗");
        System.out.printf("║ %-8s ║ %-15s ║ %-15s ║\n",
                "ID Bàn", "Số chỗ ngồi", "Trạng thái");
        System.out.println("╠══════════╬═════════════════╬═════════════════╣");
    }

    public static void getFooter() {
        System.out.println("╚══════════╩═════════════════╩═════════════════╝");
    }

    public static Table inputData() {
        Table table = new Table();

        int seats;
        while (true) {
            seats = InputMethod.getInputInt("Nhập số chỗ ngồi cho bàn: ");
            if (seats > 0 && seats <= 100) {
                table.setNumber_seats(seats);
                break;
            }
            System.out.println(ColorConstants.ERROR + "Số chỗ ngồi phải từ 1 đến 100!" + ColorConstants.RESET);
        }

        System.out.println("Chọn trạng thái bàn:");
        System.out.println("1. Trống (FREE)");
        System.out.println("2. Đang có khách (OCCUPIED)");
        System.out.println("3. Đã đặt trước (RESERVED)"); // Thêm lựa chọn 3

        while (true) {
            int statusChoice = InputMethod.getInputInt("Lựa chọn của bạn: ");
            if (statusChoice == 1) {
                table.setStatus(TableStatus.FREE);
                break;
            } else if (statusChoice == 2) {
                table.setStatus(TableStatus.OCCUPIED);
                break;
            } else if (statusChoice == 3) {
                table.setStatus(TableStatus.RESERVED);
                break;
            } else {
                System.out.println(ColorConstants.WARNING + "Vui lòng chỉ chọn từ 1 đến 3!" + ColorConstants.RESET);
            }
        }

        return table;
    }

    public void displayData() {

        String statusColor;
        switch (this.status) {
            case FREE: statusColor = ColorConstants.SUCCESS; break;
            case OCCUPIED: statusColor = ColorConstants.ERROR; break;
            case RESERVED: statusColor = ColorConstants.WARNING; break;
            default: statusColor = ColorConstants.RESET;
        }

        System.out.printf("║ %-8d ║ %-15d ║ %s%-15s%s ║\n",
                this.table_id,
                this.number_seats,
                statusColor, this.status, ColorConstants.RESET);
    }
}
