package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.OrderDetailStatus;

public class OrderDetailDisplay {
    private int orderId;
    private int tableId;
    private String foodName;
    private int quantity;
    private double unitPrice;
    private OrderDetailStatus status;

    public OrderDetailDisplay() {
    }

    public OrderDetailDisplay(int orderId, int tableId, String foodName, int quantity, double unitPrice, OrderDetailStatus status) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = status;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public OrderDetailStatus getStatus() {
        return status;
    }

    public void setStatus(OrderDetailStatus status) {
        this.status = status;
    }

    public void display() {
        System.out.printf("║ %-10d ║ %-10d ║ %-20s ║ %-8d ║ %-10s ║\n",
                orderId, tableId, foodName, quantity, status);
    }

    public static void getHeader() {
        System.out.println("╔════════════╦════════════╦══════════════════════╦══════════╦════════════╗");
        System.out.println("║ Mã Đơn     ║ Bàn        ║ Tên Món              ║ SL       ║ Trạng Thái ║");
        System.out.println("╠════════════╬════════════╬══════════════════════╬══════════╬════════════╣");
    }

    public static void getFooter() {
        System.out.println("╚════════════╩════════════╩══════════════════════╩══════════╩════════════╝");
    }
}