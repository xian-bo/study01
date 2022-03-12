import cn.fzu.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test01 {

    @Autowired
    private UserService userService;


    @Autowired
    @Qualifier("jedisPool")
    private JedisPool jedisPool;



//    @Test
//    public void test01(){
//        userService.save();
//    }

    @Test
    public void test02(){

        Jedis jedis = jedisPool.getResource();
        String set = jedis.set("aaa", "000");
        jedis.close();
        System.out.println(set);
    }

}
