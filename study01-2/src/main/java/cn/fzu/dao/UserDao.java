package cn.fzu.dao;


import cn.fzu.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component()
public interface UserDao {
    public List<User> save();

    @Update("update user set balance=balance-500 where id = #{id}")
    public void inBalance(int id);

    @Update("update user set balance = balance+500 where id=#{id}")
    public void outBalance(int id);

}
