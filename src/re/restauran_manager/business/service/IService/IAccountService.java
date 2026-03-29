package re.restauran_manager.business.service.IService;

import re.restauran_manager.model.enties.Account;

public interface IAccountService {
    Account login(String username, String password);
    boolean register(String username, String password);
}
