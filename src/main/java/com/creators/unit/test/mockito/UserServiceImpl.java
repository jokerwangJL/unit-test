package com.creators.unit.test.mockito;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ===========================
 * author:       wangjialong
 * date:         2019/8/15
 * time:         11:14
 * description:  请输入描述
 * ============================
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;


    @Override
    public User findOne(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public boolean updateUsername(Long id, String username) {
        User user = findOne(id);
        if(user == null) {
            return false;
        }
        user.setUsername(username);
        return userRepository.updateUser(user);
    }

}
