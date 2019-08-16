package com.creators.unit.test.mockito;

public interface IUserService {

    User findOne(Long id);

    boolean updateUsername(Long id ,String username);

}
