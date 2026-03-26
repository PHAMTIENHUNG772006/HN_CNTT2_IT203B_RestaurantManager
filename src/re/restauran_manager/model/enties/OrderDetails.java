package re.restauran_manager.model.enties;

public class OrderDetails {
    private int id;
    private int orderId;
    private int itemId;
    private int quantity;
    private double price;

    public OrderDetails() {}

    public OrderDetails(int id, int orderId, int itemId, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static void getHeader() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("| %-5s | %-10s | %-10s | %-8s | %-15s |\n",
                "ID", "Order ID", "Item ID", "Qty", "Price (VND)");
        System.out.println("-----------------------------------------------------------------------");
    }

    public void displayData() {
        System.out.printf("| %-5d | %-10d | %-10d | %-8d | %,15.2f |\n", this.id, this.orderId, this.itemId, this.quantity, this.price);
    }
}
