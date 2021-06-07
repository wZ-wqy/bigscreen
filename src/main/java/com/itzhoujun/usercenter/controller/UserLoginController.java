package com.itzhoujun.usercenter.controller;

import com.itzhoujun.usercenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/userLoginController")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserLoginController {
    private final UserService userService;
    @PostMapping(value = "/login")
    public boolean test(@RequestParam(value = "username") String username , @RequestParam(value = "password") String password, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("开始执行登录");
        boolean login = userService.UserLogin(username,password);
        return login;
    }


}
