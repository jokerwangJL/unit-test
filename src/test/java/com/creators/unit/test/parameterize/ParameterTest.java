package com.creators.unit.test.parameterize;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;


/**
 * ===========================
 * author:       wangjialong
 * date:         2019/8/14
 * time:         21:07
 * description:  参数化测试
 * ============================
 */
/*更改默认的测试运行器为RunWith(Parameterized.class)*/
@RunWith(Parameterized.class)
public class ParameterTest {

    /*声明变量存放预期值和测试数据*/
    private String firstName;
    private String lastName;

    /*声明一个返回值 为Collection的公共静态方法，并使用@Parameters进行修饰*/
    @Parameterized.Parameters
    public static List<Object[]> param() {
        /*这里给出两个测试用例*/
        return Arrays.asList(new Object[][]{{"Mike","Black"},{"Circle","Smith"}});
    }

    public ParameterTest (String firstName,String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /*进行测试，发现它会将所有的测试用例测试一遍*/
    @Test
    public void test(){
        String name = firstName + " " + lastName;
        System.out.println(name);
    }


}
