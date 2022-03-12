package fzu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    private Integer id;
    private String account;
    private String name;
    private String id_number;
    private String password;
    private double balance;

    @Override
    public String toString() {
        return "账号："+ account+"\n"+
                "姓名："+name+'\n'+
                "身份证号："+id_number+'\n'+
                "密码："+password+'\n'+
                "余额："+balance+'\n';
    }
}
