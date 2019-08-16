package com.creators.unit.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * ===========================
 * author:       wangjialong
 * date:         2019/8/14
 * time:         20:53
 * description:  请输入描述
 * ============================
 */
/*1. 更改测试运行方式为 Suite*/
@RunWith(Suite.class)
/*2. 将测试类传入进来*/
@Suite.SuiteClasses({TaskOneTest.class,TaskTwoTest.class,TaskThreeTest.class})
public class SuitTest {
    /*测试套件的入口类知识组织测试类一起进行测试，无任何测试方法*/
}
