package re.restauran_manager.model.enties;
import re.restauran_manager.model.enums.Category;
import re.restauran_manager.model.enums.ItemStatus;

public class MenuItems {
    private int food_id;
    private String food_name;
    private double price;
    private int quantity;
    private Category category;
    private ItemStatus status;


    public MenuItems() {
    }

    public MenuItems(int food_id, String food_name, double price, int quantity, Category category, ItemStatus status) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.status = status;
    }


    public static void getHeader() {
        // Căn chỉnh độ rộng các cột cho phù hợp với dữ liệu thực tế
        System.out.printf("| %-5s | %-20s | %-12s | %-8s | %-12s | %-10s |\n",
                "ID", "Tên món", "Giá", "SL", "Danh mục", "Trạng thái");
        System.out.println("---------------------------------------------------------------------------------------");
    }

    public void displayData() {
        System.out.printf("| %-5d | %-20s | %,12.2f | %-8d | %-12s | %-10s |\n", this.food_id,this.food_name, this.price, this.quantity, this.category, this.status);
    }
}
