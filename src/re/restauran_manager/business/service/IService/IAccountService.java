package re.restauran_manager.business.service.IService;

import re.restauran_manager.model.enties.Account;
import re.restauran_manager.model.enums.AccountRole;

import java.util.List;

public interface IAccountService {

    Account login(String username, String password);

    boolean register(String username, String password);

    boolean addAccount(String username, String password, AccountRole role);

    List<Account> getAllAccounts();

    boolean banAccount(int id);

    Account findByUsername(String username);

    boolean browseDishesById(int order_id);

    boolean browseDishesAll();

    void statsByMonth();

    void statsByDay();
}