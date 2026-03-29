package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.Category;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

public class MenuItems {
    private int food_id;
    private String food_name;
    private double price;
    private int stock;
    private Category category;

    public MenuItems() {
    }

    public MenuItems(int food_id, String food_name, double price, int stock, Category category) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.price = price;
        this.stock = stock;
        this.category = category;
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

    public static MenuItems inputData() {
        MenuItems item = new MenuItems();

        item.setFood_name(InputMethod.getInputString("Nhập tên món ăn: "));

        while (true) {
            double price = InputMethod.getInputDouble("Nhập giá món ăn: ");
            if (price > 0) {
                item.setPrice(price);
                break;
            }
            System.out.println(ColorConstants.ERROR + "Giá món ăn phải lớn hơn 0!" + ColorConstants.RESET);
        }

        while (true) {
            int stock = InputMethod.getInputInt("Nhập số lượng tồn kho: ");
            if (stock >= 0) {
                item.setStock(stock);
                break;
            }
            System.out.println(ColorConstants.ERROR + "Số lượng không được là số âm!" + ColorConstants.RESET);
        }

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
            System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ (Chỉ chọn 1 hoặc 2)!" + ColorConstants.RESET);
        }

        return item;
    }

    public static void getHeader() {
        System.out.println("╔══════╦══════════════════════╦══════════════╦══════════╦══════════════╗");
        System.out.printf("║ %-4s ║ %-20s ║ %-12s ║ %-8s ║ %-12s ║\n",
                "ID", "Tên món", "Giá", "SL", "Danh mục");
        System.out.println("╠══════╬══════════════════════╬══════════════╬══════════╬══════════════╣");
    }

    public void displayData() {
        System.out.printf("║ %-4d ║ %-20s ║ %,12.0f ║ %-8d ║ %-12s ║\n",
                this.food_id, this.food_name, this.price, this.stock, this.category);
    }

    public static void getFooter() {
        System.out.println("╚══════╩══════════════════════╩══════════════╩══════════╩══════════════╝");
    }
}