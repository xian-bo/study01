package cn.fzu.service.imp;

import cn.fzu.dao.UserDao;
import cn.fzu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    public void save() {

        userDao.inBalance(1);
        int i =1/0;
        userDao.outBalance(2);
    }
}
