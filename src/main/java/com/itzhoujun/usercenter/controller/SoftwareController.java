package com.itzhoujun.usercenter.controller;

import com.itzhoujun.usercenter.service.SoftwareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/softwareController")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SoftwareController {

    @GetMapping(value = "/software")

    public JSONObject selectstaff(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("开始执行查询");
        JSONObject jsa = SoftwareService.softwareSelect();
        return jsa;
    }





}
