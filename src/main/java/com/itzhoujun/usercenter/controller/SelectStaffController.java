package com.itzhoujun.usercenter.controller;

import com.itzhoujun.usercenter.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/selectStaffController")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SelectStaffController {

        @GetMapping(value = "/staff")

        public JSONObject selectstaff(HttpServletRequest req, HttpServletResponse resp) {

            System.out.println("开始执行查询分组");
            JSONObject jsa = StaffService.selectStaffService();
            String[] fenzu = SelectDevice.selectFenZu();
            jsa.accumulate("zu",fenzu);
            System.out.println(jsa);
            return jsa;
        }










    }
