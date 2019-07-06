package com.atnanx.atcrowfunding.app;

import org.junit.Test;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class AtcrowfundingAppApplicationTests {

    public static int info(int x,double y){
        return 0;
    }

    public static int info(int x,int y){
        return 1;
    }

    @Test
    public void contextLoads() {
        float f=1.5f;
        int num =2147483647;
        num+=2L;
        System.out.println(num);
    }
    @Test
    public void fun2() {
        int num =2147483647;
        num+=5;
        System.out.println(num);
    }
    @Test
    public void fun12() {
        int num =2147483647;
        long temp = num +2L;
        System.out.println(num);
        System.out.println(temp);
    }
    @Test
    public void fun13() {
        long num =100;
        long temp = num +2L;
        System.out.println(num);
        System.out.println(temp);
    }

    @Test
    public void fun3(){
        char c ='A';
        int num =10;
        switch (c) {
            case 'B':
                num++;
            case 'A':
                num++;
            case 'Y':
                num++;
                break;
            default:
                num--;
        }
        System.out.println(num);
    }

    @Test
    public void fun4(){
        System.out.println(inc(10)+inc(8)
            +inc(-10));
    }

    public int inc(int temp){
        if (temp>0){
            return temp*2;
        }
        return -1;
    }

    @Test
    public void fun5(){
        boolean flag = 10%2==1&&10/3==0&&1/0==0;
        System.out.println(flag?'a':'b');
    }

    public static void main(String[] args) {
        int num =68;
        char c=(char)num;
        System.out.println(c);
    }
}

