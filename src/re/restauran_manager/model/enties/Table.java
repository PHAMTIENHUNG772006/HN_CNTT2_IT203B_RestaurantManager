package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.TableStatus;

public class Table {
    private int table_id;
    private int number_seats;
    private TableStatus status;


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
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-15s |\n", "ID Bàn", "Số chỗ ngồi", "Trạng thái");
        System.out.println("--------------------------------------------------");
    }

    public void displayData() {

        System.out.printf("| %-10d | %-15d | %-15s |\n",
                this.table_id,
                this.number_seats,
                this.status);
    }
}
