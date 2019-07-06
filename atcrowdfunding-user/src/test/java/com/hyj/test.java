package com.hyj;

import com.atnanx.atcrowdfunding.core.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;

public class test {
    ObjectMapper objectMapper =new ObjectMapper();

    @Test
    public void fun1(){
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code","111222");
        try {
            String s = JsonUtil.obj2Json(map);
            String s1 = JsonUtil.obj2PrettyJson(map);
            System.out.println(s);
            System.out.println(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
