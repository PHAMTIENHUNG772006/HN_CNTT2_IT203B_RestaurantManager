package re.restauran_manager.presentation.view;

import re.restauran_manager.business.dao.AccountDao;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enums.AccountRole;

import java.util.Scanner;

public class RestaurantApplication {
    public static void main(String[] args) {
        insertFirstManager();

        Scanner sc = new Scanner(System.in);
        MainMenu.viewAuthor(sc);
    }

    private static void insertFirstManager() {
        Account admin = new Account();
        admin.setAccount_name("admin");
        admin.setPassword("admin123");
        admin.setRole(AccountRole.MANAGER);
        admin.setBan(false);

        boolean result = AccountDao.getInstance().initAccountAdmin(admin.getAccount_name(), admin.getPassword());
    }
}