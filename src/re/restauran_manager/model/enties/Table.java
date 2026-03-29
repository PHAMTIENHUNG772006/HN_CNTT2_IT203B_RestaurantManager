package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.TableStatus;
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

    public void displayData() {
        System.out.printf("║ %-8d ║ %-15d ║ %-15s ║\n",
                this.table_id,
                this.number_seats,
                this.status);
    }

    public static void getFooter() {
        System.out.println("╚══════════╩═════════════════╩═════════════════╝");
    }

    public static Table inputData() {
        Table table = new Table();

        table.setNumber_seats(InputMethod.getInputInt("Nhập số chỗ ngồi cho bàn: "));

        System.out.println("Chọn trạng thái bàn:");
        System.out.println("1. Trống (EMPTY)");
        System.out.println("2. Đang có khách (OCCUPIED)");

        int statusChoice = InputMethod.getInputInt("Lựa chọn của bạn: ");
        switch (statusChoice) {
            case 1:
                table.setStatus(TableStatus.EMPTY);
                break;
            case 2:
                table.setStatus(TableStatus.OCCUPIED);
                break;
            default:
                table.setStatus(TableStatus.EMPTY);
                break;
        }

        return table;
    }
}
