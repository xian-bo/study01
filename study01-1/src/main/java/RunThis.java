import fzu.dao.AccountDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import fzu.pojo.Account;
import fzu.pojo.Tran;
import fzu.pojo.TranVo;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class RunThis {
    //        读取配置文件
    static InputStream rs;

    static {
        try {
            rs = Resources.getResourceAsStream("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载SqlSession工厂
    static SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(rs);
    // 创建会话
    static SqlSession sqlSession = sf.openSession();
    //
    static AccountDao mapper = sqlSession.getMapper(AccountDao.class);
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        task2();
    }

    //需求一：开户
    public static void task1() throws NoSuchAlgorithmException {
        AccountDao iAccount = sqlSession.getMapper(AccountDao.class);
        Scanner sc = new Scanner(System.in);

        Account Account = new Account();

        System.out.println("请输入姓名：");
        String name = sc.next();

        System.out.println("请输入身份证号：");
        String IDNumber = sc.next();

        List<Account> l = iAccount.getIdNumber();

        int repeat = 0;
        Iterator<Account> iter = l.iterator();
        while (iter.hasNext()) {
            Account content = (Account) iter.next();
            if (content.getId_number().equals(IDNumber)) {
                repeat = 1;
            }
        }

        if (repeat == 1) {
            System.out.println("这个用户已经开户！");
            return;
        }

        int psw = 1;

        while(true) {
            System.out.println("请设置密码：");
            try {
                psw = sc.nextInt();
            }catch (Exception e) {
                sc.next();
            }

            if (psw / 100000 != 0 && psw / 1000000 == 0 ) {
                break;
            }
            System.out.println("请输入六位数字密码！");
        }

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update((byte) psw);

        String newPsw = new BigInteger(1, messageDigest.digest()).toString(16);

        Account.setAccount("1234567890123456789");
        Account.setName(name);
        Account.setId_number(IDNumber);
        Account.setPassword(newPsw);
        Account.setBalance(10.00);

        try {
            iAccount.insertAccountInfo(Account);
            sqlSession.commit();
        }catch (Exception e) {
            System.out.println("未知的情况，请联系管理员");
        }

    }


    // 需求二：存款
    public static void task2(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您的身份证号：");
        String id_number = scanner.nextLine();
        System.out.println("请输入您的六位数密码：");
        int password = scanner.nextInt();
        Account account = mapper.selectAccountInfoById(id_number, password);
        int count = 5;
        // 1）：根据账号或身份证查询账号信息
        // 2）：输入密码验证
        while(account==null){
            if(count == 0) {
                System.out.println("您今天已输错6次，请到柜台处理");
                return;
            }
            System.out.println("身份证号或密码错误,今天还剩"+count+"次机会，请重新输入");
            System.out.println("请输入您的身份证号：");
            id_number = scanner.next();
            System.out.println("请输入您的六位数密码：");
            password = scanner.nextInt();
            account = mapper.selectAccountInfoById(id_number, password);
            count --;

        }
        System.out.println(account);

        // 3）：输入取款金额
        // 4）：验证余额（账户至少保留10元）
        System.out.println("输入您转入的金额：");
        double tran_amount = scanner.nextDouble();

        // 5）：写入流水表并且修改余额
        Tran tran = new Tran();
        tran.setAccount_info_id(account.getAccount());
        tran.setPre_tran_balance(account.getBalance());
        tran.setTran_amount(tran_amount);
        tran.setPost_tran_balance(account.getBalance()+tran_amount);
        tran.setDate(new Date());
        tran.setType(2);
        mapper.insertTranInfo(tran);
        mapper.updateAccountInfoById(account.getId_number(),account.getBalance()+tran_amount);
        sqlSession.commit();
        System.out.println("存之前的金额为"+tran.getPre_tran_balance());
        System.out.println("您已存入："+tran_amount);
        System.out.println("剩余余额："+tran.getPost_tran_balance());
    }
    // 需求三：取款
    public static void task3(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您的身份证号：");
        String id_number = scanner.nextLine();
        System.out.println("请输入您的六位数密码：");
        int password = scanner.nextInt();
        Account account = mapper.selectAccountInfoById(id_number, password);
        int count = 5;
        // 1）：根据账号或身份证查询账号信息
        // 2）：输入密码验证
        while(account==null){
            if(count == 0) {
                System.out.println("您今天已输错6次，请到柜台处理");
                return;
            }
            System.out.println("身份证号或密码错误,今天还剩"+count+"次机会，请重新输入");
            System.out.println("请输入您的身份证号：");
            id_number = scanner.next();
            System.out.println("请输入您的六位数密码：");
            password = scanner.nextInt();
            account = mapper.selectAccountInfoById(id_number, password);
            count --;

        }
        System.out.println(account);

        // 3）：输入取款金额
        // 4）：验证余额（账户至少保留10元）
        System.out.println("输入您要取出的金额：");
        double tran_amount = scanner.nextDouble();
        while (tran_amount + 10 > account.getBalance()){
            System.out.println("您输入的金额为："+tran_amount);
            System.out.println("您现在的余额为："+account.getBalance());
            System.out.println("账户至少保留10元");
            System.out.println("请重新输入余额或按000退出");
            String option = scanner.next();
            if(option.equals("000")){
                return;
            }
            // tran ==

        }
        // 5）：写入流水表并且修改余额
        Tran tran = new Tran();
        tran.setAccount_info_id(account.getAccount());
        tran.setPre_tran_balance(account.getBalance());
        tran.setTran_amount(tran_amount);
        tran.setPost_tran_balance(account.getBalance()-tran_amount);
        tran.setDate(new Date());
        tran.setType(3);
        mapper.insertTranInfo(tran);
        mapper.updateAccountInfoById(account.getId_number(),account.getBalance()-tran_amount);
        sqlSession.commit();
        System.out.println("取出前的金额为"+tran.getPre_tran_balance());
        System.out.println("您已取出："+tran_amount);
        System.out.println("剩余余额："+tran.getPost_tran_balance());
    }
    // 需求四-1：查询账户个数
    public static void task4_1(){
        System.out.println(mapper.selectCounts());
    }
    // 需求四-2：查询两个日期之间的交易信息
    public static void task4_2(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<TranVo> tranVos = mapper.selectAccountTran("2000-11-11", "2033-12-11");
        for (int i = 0; i < tranVos.size(); i++) {
            System.out.println("账号："+tranVos.get(i).getAccount().getAccount()+
            ",     姓名："+tranVos.get(i).getAccount().getName()+
            ",     交易金额："+tranVos.get(i).getTran_amount()+
            ",     交易日期："+df.format(tranVos.get(i).getDate())+
            ",     交易类型："+changeType(tranVos.get(i).getType())
            );
        }
    }
    public static String changeType(int type){
        switch (type){
            case 1: return  "开户";
            case 2: return "存款";
            case 3:return "取款";

        }
        return null;
    }



}
