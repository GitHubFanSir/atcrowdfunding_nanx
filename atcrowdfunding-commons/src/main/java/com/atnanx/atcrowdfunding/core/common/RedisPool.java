package com.atnanx.atcrowdfunding.core.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/*@Component
@ConfigurationProperties(prefix = "spring.redis")*/
public class RedisPool {
    private static JedisPool pool;//jedis连接池
    //最大连接数

    private static Integer maxTotal ;
    //在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer maxIdle ;
    //在jedispool中最小的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle ;

    //当我们从jedis pool中包有一个jedis实例，拿一个jedis实例，即java与redis服务端的通信客户端，是否需要测试
    //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnBorrow =true ;
    //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
    private static Boolean testOnReturn =false;

    private static String host;
    private static Integer port;

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。

        pool = new JedisPool(config,host,port,1000*2);
    }

    /**
     * 为了这个类在加载到jvm的时候,就初始化连接池
     */
    static{
        initPool();
    }

    public static Jedis getJedisResource(){
        return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedisResource();
        jedis.set("hyj","fighting");
        RedisPool.returnResource(jedis);

        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");
    }
}
