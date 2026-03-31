package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.model.enums.AccountStatus;
import re.restauran_manager.utils.ColorConstants;

public class Account {
    private int account_id;
    private String account_name;
    private String password;
    private AccountRole role ;
    private boolean isBan ;


    public Account() {

    }

    public Account(int account_id, String account_name, String password, AccountRole role, boolean isBan) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.password = password;
        this.role = role;
        this.isBan = isBan;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public boolean isBan() {
        return isBan;
    }

    public void setBan(boolean ban) {
        isBan = ban;
    }

    public static void getHeader() {
        System.out.println("╔══════╦══════════════════════╦══════════════╦══════════════╗");
        System.out.printf("║ %-4s ║ %-20s ║ %-12s ║ %-12s ║\n",
                "ID", "Tên tài khoản", "Chức vụ", "Trạng thái");
        System.out.println("╠══════╬══════════════════════╬══════════════╬══════════════╣");
    }

    public void displayData() {
        String statusLabel = this.isBan ? ColorConstants.ERROR + "Bị khóa" + ColorConstants.RESET
                : ColorConstants.SUCCESS + "Hoạt động" + ColorConstants.RESET;

        System.out.printf("║ %-4d ║ %-20s ║ %-12s ║ %-21s ║\n",
                this.account_id,
                this.account_name,
                this.role != null ? this.role.name() : "N/A",
                statusLabel);
    }

    public static void getFooter() {
        System.out.println("╚══════╩══════════════════════╩══════════════╩══════════════╝");
    }
}


