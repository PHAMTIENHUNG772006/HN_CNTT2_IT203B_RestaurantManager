package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.IService.IAccountService;
import re.restauran_manager.business.service.Impl.IAccountServiceImpl;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;

import java.util.List;

public class AccountManager {

    private static final IAccountService accountService = IAccountServiceImpl.getInstance();

    public static void viewMenuAccount() {
        int choice;
        do {
            System.out.println("\n+===========================================+");
            System.out.println("|            QUẢN LÝ TÀI KHOẢN              |");
            System.out.println("+===========================================+");
            System.out.println("|  1. Danh sách tất cả tài khoản            |");
            System.out.println("|  2. Thêm mới tài khoản (Nhân viên/Bếp)    |");
            System.out.println("|  3. Khóa tài khoản người dùng             |");
            System.out.println("|  4. Tìm kiếm tài khoản theo tên           |");
            System.out.println("|  5. Quay lại                              |");
            System.out.println("+-------------------------------------------+");

            choice = InputMethod.getInputInt("Lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                   List<Account> accounts =  accountService.getAllAccounts();

                    if (accounts != null && !accounts.isEmpty()) {
                        Account.getHeader();
                            accounts.stream().forEach(account -> account.displayData());
                        Account.getFooter();
                    }
                    break;
                case 2:
                    addNewAccount();
                    break;
                case 3:
                    banUserAccount();
                    break;
                case 4:
                    searchAccountByUsername();
                    break;
                case 5:
                    System.out.println("Đang quay lại menu chính...");
                    break;
                default:
                    System.out.println(ColorConstants.WARNING + "Lựa chọn không hợp lệ!" + ColorConstants.RESET);
            }
        } while (choice != 5);
    }

    private static void displayAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            System.out.println("\n" + ColorConstants.SUCCESS + "--- DANH SÁCH TÀI KHOẢN ---" + ColorConstants.RESET);
            System.out.printf("%-5s | %-20s | %-12s | %-10s\n", "ID", "Tên đăng nhập", "Chức vụ", "Trạng thái");
            System.out.println("------------------------------------------------------------");
            for (Account acc : accounts) {
                String status = acc.isBan() ? ColorConstants.ERROR + "Bị khóa" : ColorConstants.SUCCESS + "Hoạt động";
                System.out.printf("%-5d | %-20s | %-12s | %-10s" + ColorConstants.RESET + "\n",
                        acc.getAccount_id(),
                        acc.getAccount_name(),
                        acc.getRole(),
                        status);
            }
        }
    }

    private static void addNewAccount() {
        System.out.println("\n--- THÊM TÀI KHOẢN MỚI ---");
        String username = InputMethod.getInputString("Nhập tên đăng nhập: ");
        String password = InputMethod.getInputString("Nhập mật khẩu: ");

        AccountRole role = null;
        while (true) {
            System.out.println("Chọn chức vụ:");
            System.out.println("1. CUSTOMER (Khách hàng)");
            System.out.println("2. CHEF (Đầu bếp)");
            System.out.println("3. MANAGER (Quản lý)");

            int roleChoice = InputMethod.getInputInt("Nhập lựa chọn (1-3): ");

            if (roleChoice >= 1 && roleChoice <= 3) {
                role = switch (roleChoice) {
                    case 1 -> AccountRole.CUSTOMER;
                    case 2 -> AccountRole.CHEF;
                    case 3 -> AccountRole.MANAGER;
                    default -> AccountRole.CUSTOMER;
                };
                break;
            } else {
                System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ. Vui lòng nhập từ 1 đến 3!" + ColorConstants.RESET);
            }
        }

        if (accountService.addAccount(username, password, role)) {
            System.out.println(ColorConstants.SUCCESS + "Tạo tài khoản thành công!" + ColorConstants.RESET);
        } else {
            System.out.println(ColorConstants.ERROR + "Tạo tài khoản thất bại (Tên đăng nhập có thể đã tồn tại)." + ColorConstants.RESET);
        }
    }
    private static void banUserAccount() {
        displayAllAccounts();
        int id = InputMethod.getInputInt("Nhập ID tài khoản muốn khóa: ");
        if (accountService.banAccount(id)) {
            System.out.println(ColorConstants.SUCCESS + "Đã khóa tài khoản thành công!" + ColorConstants.RESET);
        } else {
            System.out.println(ColorConstants.ERROR + "Không thể khóa tài khoản này!" + ColorConstants.RESET);
        }
    }

    private static void searchAccountByUsername() {
        String username = InputMethod.getInputString("Nhập tên tài khoản cần tìm: ");
        Account acc = accountService.findByUsername(username);
        if (acc != null) {
            System.out.println(ColorConstants.SUCCESS + "Thông tin tìm thấy:" + ColorConstants.RESET);
            System.out.println("ID: " + acc.getAccount_id());
            System.out.println("Tên: " + acc.getAccount_name());
            System.out.println("Quyền: " + acc.getRole());
            System.out.println("Trạng thái: " + (acc.isBan() ? "Đang bị khóa" : "Đang hoạt động"));
        } else {
            System.out.println(ColorConstants.WARNING + "Không tìm thấy tài khoản: " + username + ColorConstants.RESET);
        }
    }
}