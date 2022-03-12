package fzu.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import fzu.pojo.Account;
import fzu.pojo.Tran;
import fzu.pojo.TranVo;

import java.util.List;

@Service
public interface AccountDao {

    // 需求一

    /**
     * 新增用户账户
     *
     * @param accountInfo 参数
     * @return 结果
     */
    public int insertAccountInfo(Account accountInfo);


    /**
     * 查询所有账户信息
     *
     *
     * @return 结果
     */
    public List<Account> getIdNumber();

    // 需求二、三
    /**
     * 根据身份证查询账户信息
     */
    public Account selectAccountInfoById(@Param("id_number") String id_number, @Param("password") Integer password);
    //更新账户信息
    public void updateAccountInfoById(@Param("id_number")String id_number, @Param("balance") double balance);
    //根据账户id插入交易信息
    public void insertTranInfo(Tran tran);


//    需求四

    //查询账号个数
    public int selectCounts();
    //查询两个日期之间的交易信息
    public List<TranVo> selectAccountTran(@Param("before") String before, @Param("after") String after);

}
