package com.creators.unit.test.mockmvc;

import com.creators.unit.test.mockito.User;
import com.creators.unit.test.mockito.UserController;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;



    @Before
    public void setUp() throws Exception {
        /*实例化方式一*/
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
        /*实例化方式二*/
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();


              /*全局配置*/
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultRequest(MockMvcRequestBuilders.get("/user/1").requestAttr("default",true))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysExpect(MockMvcResultMatchers.request().attribute("default",true))
                .build();
    }

    @Test
    public void testHello() throws Exception {
/*
        1.mockMvc.perform 执行一个请求
        2.MockMvcRequestBuilders.get("XXX")构造一个请求
        3.ResultActions.param()
        4.ResultActions.accept()
        5.ResultActions.andExpect
        6.ResultActions.andDo 添加一个结果处理器，表示要对结果做点什么事情。
        7.ResultActions.andReturn 表示执行完成后，返回响应的结果。
*/
        mockMvc.perform(MockMvcRequestBuilders.get("/mock-mvc/test-get")
                /*设置返回类型为utf-8,否则默认为ISO-8859-1*/
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .param("name","tom"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hello"))
                .andDo(MockMvcResultHandlers.print());
    }


    public void demo() throws Exception {
        /*常用测试用例*/

        /*测试普通控制器*/
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}",1))
                /*验证存储模型数据*/
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                /*验证viewName*/
                .andExpect(MockMvcResultMatchers.view().name("user/view"))
                /*验证视图渲染时forward到的jsp*/
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/user/view/jsp"))
                /*验证状态码*/
                .andExpect(MockMvcResultMatchers.status().isOk())
                /*输出MvcResult到控制台*/
                .andDo(MockMvcResultHandlers.print());


        /*得到MvcResult自定义验证*/
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get( "/user/{id}",1)) .andReturn();
        Assert.assertNotNull(result.getModelAndView().getModel().get("user"));

        /*验证请求参数绑定到模型数据及flash属性*/
        mockMvc.perform(MockMvcRequestBuilders.post("/user").param("name","wang"))
                /*验证执行控制器类*/
                .andExpect(MockMvcResultMatchers.handler().handlerType(UserController.class))
                /*验证执行控制器方法名*/
                .andExpect(MockMvcResultMatchers.handler().methodName("create"))
                /*验证页面没有错误*/
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                /*验证存在flash属性*/
                .andExpect(MockMvcResultMatchers.flash().attributeExists("success"))
                /*验证视图名称*/
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user"));


        /*文件上传*/
        byte[] bytes = new byte[]{1,2};
        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/{id}/icon",1L).file("icon",bytes))
                .andExpect(MockMvcResultMatchers.model().attribute("icon",bytes))
                .andExpect(MockMvcResultMatchers.view().name("success"));


        /*JSON请求/响应验证*/
        String requestBody = "{\"id\":1,\"name\":\"wang\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            /*检查返回JSON数据中某个值的内容： 请参考http://goessner.net/articles/JsonPath/*/
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        String errorBody = "{id:1,name:wang}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON).content(errorBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        Assert.assertTrue(HttpMessageNotReadableException.class.isAssignableFrom(mvcResult.getResolvedException().getClass()));



        /*异步测试*/
        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.get("/user/async?id=1&name=wang"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andExpect(MockMvcResultMatchers.request().asyncResult(CoreMatchers.instanceOf(User.class)))
                .andReturn();
        mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(mvcResult1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));



        /*使用MultiValueMap构建参数*/
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("name","wang");
        params.add("hobby","sleep");
        params.add("hobby","eat");
        mockMvc.perform(MockMvcRequestBuilders.post("/user").params(params));

        /*模拟session和cookie*/
        mockMvc.perform(MockMvcRequestBuilders.get("/index").sessionAttr("name", "value"));
        mockMvc.perform(MockMvcRequestBuilders.get("/index").cookie(new Cookie("name", "value")));

    }



}