package re.restauran_manager.presentation.view;

import re.restauran_manager.business.service.Impl.IAccountServiceImpl;

import java.util.Scanner;
public class RestaurantApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MainMenu.viewAuthor(sc);
    }
}
