package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.AccountDao;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.business.service.IService.IAccountService;
import re.restauran_manager.model.enums.AccountRole;
import re.restauran_manager.utils.AccountSession;
import re.restauran_manager.utils.ColorConstants;

import java.util.List;

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

    @Override
    public boolean addAccount(String username, String password, AccountRole role) {

        if (username.trim().isEmpty() || username == null || password.trim().isEmpty() || password == null){
            System.out.println(ColorConstants.ERROR + "Tên hoặc mật khẩu truyền vào không hợp lệ" + ColorConstants.RESET);
            return false;
        }

        if (role == null){
            System.out.println(ColorConstants.ERROR + "Chức vụ truyền vào không hợp lệ" + ColorConstants.RESET);
            return false;
        }
        return accountDao.addAccount(username,password,role);
    }

    @Override
    public List<Account> getAllAccounts() {

        List<Account> accounts = accountDao.getAllAccount();

        if (accounts.isEmpty()){
            System.out.println(ColorConstants.ERROR + "Danh sách tài khoản trống" + ColorConstants.RESET);
            return null;
        }

        return accounts;
    }

    @Override
    public boolean banAccount(int id) {
        if (id <= 0) {
            System.out.println(ColorConstants.ERROR + "ID tài khoản không hợp lệ!" + ColorConstants.RESET);
            return false;
        }
        Account acc = accountDao.findById(id);
        if (acc == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy tài khoản có ID: " + id + ColorConstants.RESET);
            return false;
        }

        Account current = accountSession.getCurrentUser();
        if (current != null && current.getAccount_id() == id) {
            System.out.println(ColorConstants.ERROR + "Bạn không thể tự khóa tài khoản của chính mình!" + ColorConstants.RESET);
            return false;
        }

        return accountDao.bandAccount(id);
    }

    @Override
    public Account findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return accountDao.findByName(username.trim());
    }
}
