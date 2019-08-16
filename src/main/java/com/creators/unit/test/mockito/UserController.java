package com.creators.unit.test.mockito;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ===========================
 * author:       wangjialong
 * date:         2019/8/16
 * time:         11:05
 * description:  请输入描述
 * ============================
 */
@RestController
@RequestMapping("/mock-mvc")
public class UserController {


    @GetMapping("/test-get")
    public String testHello () {
        return "hello";
    }
}
