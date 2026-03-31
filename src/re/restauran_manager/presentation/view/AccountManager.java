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
            System.out.println("|  2. Thêm mới tài khoản CHEF               |");
            System.out.println("|  3. Khóa / Mở tài khoản                   |");
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
        System.out.println("\n--- THÊM TÀI KHOẢN ĐẦU BẾP (CHEF) ---");
        String username = InputMethod.getInputString("Nhập tên đăng nhập: ");
        String password = InputMethod.getInputString("Nhập mật khẩu: ");

        AccountRole role = AccountRole.CHEF;

        if (accountService.addAccount(username, password, role)) {
            System.out.println(ColorConstants.SUCCESS + "Tạo tài khoản ĐẦU BẾP thành công!" + ColorConstants.RESET);
        } else {
            System.out.println(ColorConstants.ERROR + "Tạo tài khoản thất bại (Tên đăng nhập có thể đã tồn tại)." + ColorConstants.RESET);
        }
    }

    private static void banUserAccount() {
        displayAllAccounts();
        int id = InputMethod.getInputInt("Nhập ID tài khoản muốn (Khóa/Mở): ");
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
            System.out.println(ColorConstants.SUCCESS + "\n[ KẾT QUẢ TÌM KIẾM ]" + ColorConstants.RESET);
            System.out.println("╔══════════════════════╦══════════════════════════════════╗");
            System.out.printf("║ %-20s ║ %-32s ║\n", "THÔNG TIN", "CHI TIẾT");
            System.out.println("╠══════════════════════╬══════════════════════════════════╣");

            System.out.printf("║ %-20s ║ %-32s ║\n", "Mã tài khoản (ID)", acc.getAccount_id());
            System.out.printf("║ %-20s ║ %-32s ║\n", "Tên đăng nhập", acc.getAccount_name());
            System.out.printf("║ %-20s ║ %-32s ║\n", "Quyền hạn", acc.getRole());

            String statusText = acc.isBan() ? "ĐANG BỊ KHÓA" : "ĐANG HOẠT ĐỘNG";
            String statusColor = acc.isBan() ? ColorConstants.ERROR : ColorConstants.SUCCESS;

            System.out.printf("║ %-20s ║ %s%-32s%s ║\n",
                    "Trạng thái", statusColor, statusText, ColorConstants.RESET);

            System.out.println("╚══════════════════════╩══════════════════════════════════╝");
        } else {
            System.out.println(ColorConstants.WARNING + "(!) Không tìm thấy tài khoản nào có tên: " + username + ColorConstants.RESET);
        }
    }
}