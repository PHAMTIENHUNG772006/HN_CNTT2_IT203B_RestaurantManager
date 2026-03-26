package re.restauran_manager.business.dao;

import re.restauran_manager.model.enties.Account;

public interface IRegistorLogin {
    boolean registor(Account account);
    Account login(String email, String password);
    Account findById(int id);

}
