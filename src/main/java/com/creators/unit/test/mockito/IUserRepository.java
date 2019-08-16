package com.creators.unit.test.mockito;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ===========================
 * author:       wangjialong
 * date:         2019/8/15
 * time:         11:14
 * description:  请输入描述
 * ============================
 */
public interface IUserRepository extends JpaRepository<User,Long>{
    boolean updateUser(User user);

    void addUser(User user);
}
