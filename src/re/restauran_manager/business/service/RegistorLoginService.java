package re.restauran_manager.business.service;

import re.restauran_manager.business.dao.RegistorLoginDao;
import re.restauran_manager.model.enties.Account;
import re.restauran_manager.utils.PasswordHasher;

public class RegistorLoginService {

    public boolean register(Account account){
        RegistorLoginDao registorLoginDao = RegistorLoginDao.getInstance();


        if (account == null){
            throw new IllegalArgumentException("Tài khoản muốn đăng kí không tồn tại");
        }

        if (account.getEmail() == null || account.getPassword() == null){
            throw new IllegalArgumentException("Email và mật khẩu không được để trống.");
        }

        account.setPassword(PasswordHasher.hashPassword(account.getPassword()));

       return  registorLoginDao.registor(account);
    }



//    public boolean login(String email, String pass){
//        RegistorLoginDao registorLoginDao = RegistorLoginDao.getInstance();
//
//
//        if (email == null || pass == null){
//            throw new IllegalArgumentException("Email hoặc mật khẩu không hợp lệ");
//        }
//
//        if (account.getEmail() == null || account.getPassword() == null){
//            throw new IllegalArgumentException("Email và mật khẩu không được để trống.");
//        }
//
//        account.setPassword(PasswordHasher.hashPassword(account.getPassword()));
//
//        return  registorLoginDao.registor(account);
//    }
}
