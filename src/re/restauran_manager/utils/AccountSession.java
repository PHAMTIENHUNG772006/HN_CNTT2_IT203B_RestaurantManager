package re.restauran_manager.utils;


import re.restauran_manager.model.enties.Account;

public class AccountSession {
    private static AccountSession instance;
    private int currentOrder;
    private Account currentUser;
    private AccountSession() {}

    public static AccountSession getInstance() {
        if (instance == null) {
            instance = new AccountSession();
        }
        return instance;
    }

    public void login(Account account) {
        this.currentUser = account;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Account currentUser) {
        this.currentUser = currentUser;
    }

    public int getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(int order_id) {
        this.currentOrder = order_id;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
