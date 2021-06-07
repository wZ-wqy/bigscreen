package com.itzhoujun.usercenter.service;


import com.itzhoujun.usercenter.dao.user.UserMapper;
import com.itzhoujun.usercenter.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    public final boolean UserLogin(String username,String password) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);
        List<User> users =  userMapper.selectByExample(example);
        System.out.println(users.size());
        if(users.size()==0){
            return false;
        }
        return  true;


    }
}
