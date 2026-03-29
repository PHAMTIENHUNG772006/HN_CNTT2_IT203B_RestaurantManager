package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.AccountDao;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.business.service.IService.IAccountService;
import re.restauran_manager.utils.AccountSession;

public class IAccountServiceImpl implements IAccountService {
    AccountDao accountDao = AccountDao.getInstance();
    AccountSession accountSession = AccountSession.getInstance();
    private static IAccountServiceImpl instance ;

    private IAccountServiceImpl() {}

    public static IAccountServiceImpl getInstance() {
        if (instance == null) {
            instance = new IAccountServiceImpl();
        }
        return instance;
    }


    @Override
    public Account login(String username, String password) {
        if(username.isEmpty() || password.isEmpty()){
            return null;
        }
        try{

        Account currentAcccount = accountDao.login(username, password);
        if (currentAcccount != null) {
            accountSession.login(currentAcccount);
            return currentAcccount;
        }
    }catch(Exception e){
        System.out.println(e.getMessage());
    }
        return null;
    }

    @Override
    public boolean register(String username, String password) {
        if(username.isEmpty() || password.isEmpty()){
            return false;
        }
        try{
            return accountDao.register(username, password);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
