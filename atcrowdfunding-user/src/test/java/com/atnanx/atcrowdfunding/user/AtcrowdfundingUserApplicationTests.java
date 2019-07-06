package com.atnanx.atcrowdfunding.user;

import com.atnanx.atcrowdfunding.core.bean.TMember;
import com.atnanx.atcrowdfunding.user.component.AliSmsTemplate;
import com.atnanx.atcrowdfunding.user.mapper.TMemberMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtcrowdfundingUserApplicationTests {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    DataSource dataSource;

    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    AliSmsTemplate aliSmsTemplate;

    @Test
    public void fun5() {
//        aliSmsTemplate.sendCodeSms("13065708090","1000");
    }

    /* @Autowired
     JedisPool jedisPool;*/
    @Test
    public void contextLoads() {
        System.out.println(redisConnectionFactory.getClass());
       /* System.out.println(jedisPool.getClass());
        System.out.println(jedisPool);*/
//        Jedis jedis = jedisPool.getResource();
    }

    @Test
    public void fun2() {
        System.out.println(memberMapper);
        List<TMember> tMembers = memberMapper.selectByExample(null);
        System.out.println(dataSource);
        System.out.println(tMembers);
    }

    @Test
    public void fun3() {
        System.out.println("\u6CE8\u518C\u4E2D\u5FC3\u5B9E\u4F8B\u4E3B\u673A");
        System.out.println("\u4EE5\u540E\u670D\u52A1\u90FD\u6CE8\u518C\u5230\u9ED8\u8BA4\u7684\u533A\u57DF\u4E2D\uFF0C\u56E0\u4E3A\u53EA\u67091\u4E2A\u533A\u57DF\uFF0C");
        System.out.println("\u539F\u7406\uFF0C\u76F8\u540C\u7684\u670D\u52A1\u5EFA\u7ACB\u96C6\u7FA4\u53EF\u4EE5\u53D1\u5E03\u5230\u4E0D\u540C\u7684\u533A\u57DF\uFF0Cip\u4E5F\u4E0D\u540C\uFF0C\u5F53\u8BBF\u95EE\u8BE5\u96C6\u7FA4\u7684\u670D\u52A1\u65F6\uFF0C\u6CE8\u518C\u4E2D\u5FC3\u81EA\u52A8\u5339\u914D\u901F\u5EA6\u6700\u8FD1\u7684\u533A\u57DF\u7684\u670D\u52A1(\u4E00\u822C\u6700\u8FD1\u6700\u5FEB)");
        System.out.println("\u8BBE\u7F6E\u4EE5\u540E\u6240\u6709\u7684\u670D\u52A1\u90FD\u6CE8\u518C\u5230\u8FD9\u4E2A\u5730\u5740\u4E0B");
        System.out.println("\u81EA\u5DF1\u4E0D\u4ECE\u6CE8\u518C\u4E2D\u5FC3\u4E2D\u83B7\u53D6\u4EC0\u4E48\u6570\u636E");
        System.out.println("\u6CE8\u518C\u4E2D\u5FC3\u81EA\u5DF1\u4E0D\u7528\u6CE8\u518C\u8FDB\u6CE8\u518C\u4E2D\u5FC3");
    }
}
