package fzu;

import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Utils {

    //Redis服务器IP
    private static String ADDR = "127.0.0.1";
    //Redis的端口号
    private static int PORT = 6379;
    //访问密码
    private static String AUTH = "fwl123";
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static long MAX_WAIT = 50000;

    private static int TIMEOUT = 50000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    public static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            config.setTestWhileIdle(true);
            config.setTimeBetweenEvictionRunsMillis(1000 * 120);

            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);

            System.out.println("redis init succ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述: 批量插入redis     hmset
     * @Param: [list]
     * @Description:
     * @Author: chenxin
     * @Date: 2019/9/24 16:34
     */
    public static void intomap(List<Map<String, Object>> list){
        Jedis jedis = getJedis();
        Pipeline pipel = jedis.pipelined();
        try {
            for(Map<String, Object> map: list){
                String key = map.get("key").toString();
                Map<String, String> value = (Map<String, String>)map.get("value");
                pipel.hmset(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            pipel.sync();
            returnResource(jedis);
        }
    }

    /**
     * 获取Jedis实例
     * @return
     */
    synchronized static Jedis getJedis()
    {
        int timeoutCount = 0;
        while (true) // 如果是网络超时则多试几次
        {
            try
            {
                Jedis jedis = jedisPool.getResource();
                return jedis;
            }
            catch (Exception e)
            {
                e.printStackTrace();

                // 底层原因是SocketTimeoutException，不过redis已经捕捉且抛出JedisConnectionException，不继承于前者
                if (e instanceof JedisConnectionException || e instanceof SocketTimeoutException)
                {
                    timeoutCount++;
                    System.out.println("getJedis timeoutCount="+timeoutCount);
                    if (timeoutCount > 3)
                    {
                        break;
                    }
                }
                else
                {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    public static boolean hasKey(String key){
        Jedis redis = getJedis();
        try {
            return redis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(redis);
        }

        return false;
    }

    public static void mset(String key,String value){
        Jedis redis = getJedis();
        try {
            redis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(redis);
        }
    }

    public static void mset(String key,String value, int seconds){
        Jedis redis = getJedis();
        try {
            redis.set(key, value);
            redis.expire(key, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(redis);
        }
    }

//    /**
//     * 注意。是排他的，一旦key存在，则不写入
//     * @param key
//     * @param value
//     * @param time
//     */
//    public static boolean msetOnce(String key,String value,long time){
//        Jedis redis = getJedis();
//        try {
//            if(redis.exists(key)){
//                return false;
//            }
//            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
//            String result = redis.set(key, value,"NX", "EX",time);
//
//            if(("OK").equals(result)){
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            returnResource(redis);
//        }
//
//        return false;
//    }

    public static void mdel(String key){
        Jedis redis = getJedis();
        try {
            redis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(redis);
        }
    }

    public static String setNullToBanlk(String name) {
        if (name == null) {
            name = "";
        }
        return name != null ? name.trim() : "";
    }

    public Utils() {

    }

    public static String mget(String key){
        Jedis redis = getJedis();
        String value="";
        try {

            value=redis.get(key);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(redis);
        }

        return setNullToBanlk(value);
    }

    /**
     * 查询Key
     * @param pattern
     * @return
     */
    public static  Set<String> getKeys(String pattern){
        Jedis redis = getJedis();
        Set<String> value=null;
        try {
            if(pattern.contains("*")){
                value=redis.keys(pattern);
            }else{
                value=redis.keys(pattern+"*");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(redis);
        }

        return value;
    }
}