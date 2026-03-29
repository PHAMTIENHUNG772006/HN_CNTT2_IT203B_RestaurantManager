package re.restauran_manager.utils;

import java.util.Scanner;

public class InputMethod {
    private static final Scanner sc = new Scanner(System.in);

    public static String getInputString(String message) {
        System.out.print(message);
        while (true) {
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.print(ColorConstants.ERROR + "Không được để trống. Nhập lại: " + ColorConstants.RESET);
        }
    }

    public static int getInputInt(String message) {
        System.out.print(message);
        while (true) {
            try {
                String input = sc.nextLine();
                int n = Integer.parseInt(input);
                if (n >= 0) return n;
                System.out.print(ColorConstants.ERROR + "Vui lòng nhập số dương. Nhập lại: " + ColorConstants.RESET);
            } catch (NumberFormatException e) {
                System.out.print(ColorConstants.ERROR + "Nhập sai định dạng (phải là số nguyên). Nhập lại: " + ColorConstants.RESET);
            }
        }
    }

    public static double getInputDouble(String message) {
        System.out.print(message);
        while (true) {
            try {
                String input = sc.nextLine();
                double n = Double.parseDouble(input);
                if (n >= 0) return n;
                System.out.print(ColorConstants.ERROR + "Vui lòng nhập số dương. Nhập lại: " + ColorConstants.RESET);
            } catch (NumberFormatException e) {
                System.out.print(ColorConstants.ERROR + "Nhập sai định dạng (phải là số thực). Nhập lại: " + ColorConstants.RESET);
            }
        }
    }
}