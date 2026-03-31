package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.Impl.IAccountServiceImpl;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.business.service.IService.IAccountService;
import re.restauran_manager.utils.ColorConstants;
import re.restauran_manager.utils.InputMethod;


public class AuthenticationMenu {
    public static void printLoginMenu() {
        IAccountService accountService = IAccountServiceImpl.getInstance();
        int choice;
        do {
            System.out.println("\n+===========================================+");
            System.out.println("║              ĐĂNG NHẬP                    ║");
            System.out.println("+===========================================+");
            System.out.println("║  1. Tiến hành đăng nhập                   ║");
            System.out.println("║  0. Quay lại                              ║");
            System.out.println("+-------------------------------------------+");
            choice = InputMethod.getInputInt("Lựa chọn của bạn : ");

            switch (choice) {
                case 1:
                    String username = InputMethod.getInputString("Tên đăng nhập: ");
                    String password = InputMethod.getInputString("Mật khẩu: ");

                    Account account = accountService.login(username, password);

                    if (account != null) {
                        redirectByRole(account.getRole());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println(ColorConstants.WARNING + "Lựa chọn không phù hợp!" + ColorConstants.RESET);
            }
        } while (choice != 0);
    }

    public static void printRegisterMenu() {
        IAccountService accountService = IAccountServiceImpl.getInstance();
        int choice;
        do {
            System.out.println("\n+===========================================+");
            System.out.println("║              ĐĂNG KÝ                      ║");
            System.out.println("+===========================================+");
            System.out.println("║  1. Tạo tài khoản mới                     ║");
            System.out.println("║  0. Quay lại                              ║");
            System.out.println("+-------------------------------------------+");
            choice = InputMethod.getInputInt("Lựa chọn của bạn : ");

            switch (choice) {
                case 1:
                    String username = InputMethod.getInputString("Nhập tên đăng nhập mới: ").trim();
                    String password = InputMethod.getInputString("Nhập mật khẩu: ").trim();

                    if (accountService.register(username, password)) {
                        System.out.println(ColorConstants.SUCCESS + "Đăng ký thành công! Vui lòng đăng nhập." + ColorConstants.RESET);
                        return;
                    } else {
                        System.out.println(ColorConstants.ERROR + "Đăng ký thất bại (Tên có thể đã tồn tại)!" + ColorConstants.RESET);
                    }
                    break;
                case 0:
                    return;
            }
        } while (choice != 0);
    }


    private static void redirectByRole(AccountRole role) {
        switch (role) {
            case CHEF:
                System.out.println(ColorConstants.SUCCESS + "Chào mừng đầu bếp!" + ColorConstants.RESET);
                MenuChef.viewMenuChef();
                break;
            case MANAGER:
                System.out.println(ColorConstants.SUCCESS + "Chào mừng quản lý...!" + ColorConstants.RESET);
                MenuManager.viewMenuManager();
                break;
            case CUSTOMER:
                System.out.println(ColorConstants.SUCCESS + "Chào mừng khách hàng...!" + ColorConstants.RESET);
                MenuCustomer.viewMenuCustomer();
                break;
        }
    }
}