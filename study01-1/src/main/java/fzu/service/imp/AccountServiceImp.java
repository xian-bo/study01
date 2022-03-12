package fzu.service.imp;

import fzu.dao.AccountDao;
import org.springframework.stereotype.Service;
import fzu.pojo.Account;
import fzu.pojo.Tran;
import fzu.pojo.TranVo;
import fzu.service.IAccountService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountServiceImp implements IAccountService {

    @Resource
    private AccountDao accountDao;
    @Override
    public int insertAccountInfo(Account accountInfo) {
        return accountDao.insertAccountInfo(accountInfo);
    }

    @Override
    public List<Account> getIdNumber() {
        return accountDao.getIdNumber();
    }

    @Override
    public Account selectAccountInfoById(String id_number, Integer password) {
        return accountDao.selectAccountInfoById(id_number,password);
    }

    @Override
    public void updateAccountInfoById(String id_number, double balance) {
        accountDao.updateAccountInfoById(id_number, balance);
    }

    @Override
    public void insertTranInfo(Tran tran) {
        accountDao.insertTranInfo(tran);
    }

    @Override
    public int selectCounts() {
        return accountDao.selectCounts();
    }

    @Override
    public List<TranVo> selectAccountTran(String before, String after) {
        return accountDao.selectAccountTran(before,after);
    }
}
