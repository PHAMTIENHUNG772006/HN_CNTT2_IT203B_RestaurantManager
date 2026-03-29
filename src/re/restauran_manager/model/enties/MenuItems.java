package re.restauran_manager.model.enties;
import re.restauran_manager.model.enums.Category;
import re.restauran_manager.model.enums.FoodEnum;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.List;
import java.util.Scanner;

public class MenuItems {
    private int food_id;
    private String food_name;
    private double price;
    private int stock;
    private Category category;
    private FoodEnum status;


    public MenuItems() {
    }

    public MenuItems(int food_id, String food_name, double price, int stock, Category category, FoodEnum status) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.status = status;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FoodEnum getStatus() {
        return status;
    }

    public void setStatus(FoodEnum status) {
        this.status = status;
    }

    public static MenuItems inputData() {
        MenuItems item = new MenuItems();

        item.setFood_name(InputMethod.getInputString("Nhập tên món ăn: "));
        item.setPrice(InputMethod.getInputDouble("Nhập giá món ăn: "));
        item.setStock(InputMethod.getInputInt("Nhập số lượng tồn kho: "));

        System.out.println("Chọn danh mục:");
        System.out.println("1. FOOD");
        System.out.println("2. DRINK");

        while (true) {
            int choice = InputMethod.getInputInt("Lựa chọn của bạn: ");
            if (choice == 1) {
                item.setCategory(Category.FOOD);
                break;
            } else if (choice == 2) {
                item.setCategory(Category.DRINK);
                break;
            }
            System.out.print(ColorConstants.ERROR + "Lựa chọn không hợp lệ (1 hoặc 2): " + ColorConstants.RESET);
        }

        item.setStatus(FoodEnum.PENDING);

        return item;
    }

    public static void getHeader() {
        System.out.println("╔══════╦══════════════════════╦══════════════╦══════════╦══════════════╦════════════╗");
        System.out.printf("║ %-4s ║ %-20s ║ %-12s ║ %-8s ║ %-12s ║ %-10s ║\n",
                "ID", "Tên món", "Giá", "SL", "Danh mục", "Trạng thái");
        System.out.println("╠══════╬══════════════════════╬══════════════╬══════════╬══════════════╬════════════╣");
    }

    public void displayData() {
        System.out.printf("║ %-4d ║ %-20s ║ %,12.0f ║ %-8d ║ %-12s ║ %-10s ║\n",
                this.food_id, this.food_name, this.price, this.stock, this.category, this.status);
    }

    public static void getFooter() {
        System.out.println("╚══════╩══════════════════════╩══════════════╩══════════╩══════════════╩════════════╝");
    }
}
