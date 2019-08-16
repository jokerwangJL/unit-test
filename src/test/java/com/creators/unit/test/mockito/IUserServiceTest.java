package com.creators.unit.test.mockito;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class IUserServiceTest {
    private IUserService userService;

//    @Mock
    private IUserRepository userRepository;


    @Before
    public void setUp() throws Exception {
        /*对所有注解了@Mock的对象进行模拟*/
//        MockitoAnnotations.initMocks(this);
        /*如果不使用注解，可以对单个对象进行mock*/
        userRepository = Mockito.mock(IUserRepository.class);
        /*构造测试对象*/
        userService = new UserServiceImpl(userRepository);
        /*打桩，构建当userRepository getOne函数执行参数为1的时候，设置返回的结果User*/
        Mockito.when(userRepository.getOne(1L)).thenReturn(new User(1L,"jack"));
         /*打桩，构建当userRepository getOne函数执行参数为1的时候，设置返回的结果null*/
        Mockito.when(userRepository.getOne(2L)).thenReturn(null);
         /*打桩，构建当userRepository getOne函数执行参数为1的时候，设置抛出异常*/
        Mockito.when(userRepository.getOne(3L)).thenThrow(new IllegalArgumentException("the id is not support"));
         /*打桩，构建当userRepository updateUser执行任意User类型的参数，返回的结果都是true*/
        Mockito.when(userRepository.updateUser(Mockito.any(User.class))).thenReturn(true);
        /*打桩，给void方法 */
        Mockito.doAnswer(invocation -> {
            System.out.println("进入Mock");
        return null;
        }).when(userRepository).addUser(Mockito.any());

        /*模拟方法设置返回期望值*/
        List spy = Mockito.spy(new LinkedList<>());
        /*这里会抛出IndexOutOfBoundsException*/
//        Mockito.when(spy.get(0)).thenReturn("foo");
        /*所以要使用下面代码*/
        Mockito.doReturn("foo").when(spy).get(0);
    }



    @Test
    public void testUpdateUsernameSuccess() throws Exception {
        Long userId = 1L;
        String newUsername = "new Jack";
        /*测试service方法*/
        boolean updated = userService.updateUsername(userId,newUsername);
        /*检查结果*/
        Assert.assertThat(updated, Matchers.is(true));

        /*Mock对象一旦创建，就会自动记录自己的交互行为。通过verify(mock).someMethod()方法，来验证方法是否被调用。*/
        /*验证调用上面的service方法后是否 userRepositroy.getOne(1L)调用过。*/
        Mockito.verify(userRepository).getOne(userId);

        /*updateUsername 函数中我们调用了已经打桩了的其他的函数，现在我们来验证进入其他函数中的参数*/
        /*构造参数捕获器，用于捕获方法参数进行验证*/
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        /*验证updateUser方法是否呗调用过，并且捕获入参*/
        Mockito.verify(userRepository).updateUser(userCaptor.capture());
        /*获取参数updateUser*/
        User updateUser = userCaptor.getValue();
        /*验证入参是否是预期的*/
        Assert.assertThat(updateUser.getUsername(),Matchers.is(newUsername));
        /*保证这个测试用例中所有被Mock的对象的相关方法都已经被Verify过了*/
        Mockito.verifyNoMoreInteractions(userRepository);
        /*如果有一个交互没有被verify，则会报错
        org.mockito.exceptions.verification.NoInteractionsWanted:
        No interactions wanted here:
        -> at com.wuwii.service.IUserServiceTest.testUpdateUsernameSuccess(IUserServiceTest.java:74)
        But found this interaction on mock 'iUserRepository':
        -> at com.wuwii.service.impl.UserServiceImpl.findOne(UserServiceImpl.java:21)
        ****/

    }

//    @Test
    public void testUpdateUsernameFailed() throws Exception {
        Long userId = 2L;
        String newUsername = "new Jack";
        /*没有经过mock的updateUser方法，它的返回值是false*/
        boolean updated = userService.updateUsername(userId,newUsername);
        Assert.assertThat(updated,Matchers.is(true));
        /*验证userRepository的getOne(2L)这个方法是否被调用过（这个是被测试过的，此步骤通过）*/
        Mockito.verify(userRepository).getOne(2L);
        /*验证userRepository的updateUser(null)这个方法是否被调用过（这个方法是没有被调用过的）*/
        Mockito.verify(userRepository).updateUser(null);
        Mockito.verifyNoMoreInteractions(userRepository);

    }

}