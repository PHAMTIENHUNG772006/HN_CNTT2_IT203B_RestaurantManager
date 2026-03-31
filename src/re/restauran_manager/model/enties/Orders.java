package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.OrderDetailStatus;
import re.restauran_manager.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Orders {
    private int id;
    private int userId;
    private int tableId;
    private double totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public Orders() {
    }

    public Orders(int id, int userId, int tableId, double totalAmount, LocalDateTime orderDate, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.tableId = tableId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public static void getHeader() {
        System.out.println("╔══════╦══════════╦══════════╦═════════════════╦══════════════════╦════════════╗");
        System.out.printf("║ %-4s ║ %-8s ║ %-8s ║ %-15s ║ %-16s ║ %-10s ║\n",
                "ID", "User ID", "Bàn ID", "Tổng tiền", "Ngày đặt", "Trạng thái");
        System.out.println("╠══════╬══════════╬══════════╬═════════════════╬══════════════════╬════════════╣");
    }

    public void displayData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        String dateStr = (this.orderDate != null) ? this.orderDate.format(formatter) : "N/A";
        System.out.printf("║ %-4d ║ %-8d ║ %-8d ║ %,15.2f ║ %-16s ║ %-10s ║\n",
                this.id, this.userId, this.tableId, this.totalAmount, dateStr, this.status);
    }

    public static void getFooter() {
        System.out.println("╚══════╩══════════╩══════════╩═════════════════╩══════════════════╩════════════╝");
    }
}
