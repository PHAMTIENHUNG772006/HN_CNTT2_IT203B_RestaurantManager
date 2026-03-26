package re.restauran_manager.model.enties;

import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.model.enums.AccountStatus;

public class Account {
    private int account_id;
    private String account_name;
    private String email;
    private String password;
    private AccountRole role ;
    private AccountStatus status ;


    public Account() {
    }

    public Account(int account_id, String account_name, String email, String password, AccountRole role, AccountStatus status) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public static void getHeader() {
        System.out.printf("| %-5s | %-15s | %-10s | %-7s | %-7s |\n", "ID", "Email", "Role", "Status Accunt");
        System.out.println("-------------------------------------------------------------------");
    }

    public void displayData() {
        System.out.printf("| %-5d | %-8d | %-10d | %-6d | %10d |\n", this.account_id, this.email, this.role, this.status);
    }
}


