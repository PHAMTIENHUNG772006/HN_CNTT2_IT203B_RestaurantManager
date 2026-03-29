package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.model.enums.AccountStatus;

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
        System.out.printf("| %-5s | %-10s | %-7s | %-7s |\n", "ID", "Role", "Status Account");
        System.out.println("-------------------------------------------------------------------");
    }

    public void displayData() {
        System.out.printf("| %-5d | %-10d | %-6d | %5b |\n", this.account_id, this.role, this.isBan ? "Bị khóa" : "Hoạt động");
    }
}


