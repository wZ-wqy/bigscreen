package com.itzhoujun.usercenter.controller;


import com.itzhoujun.usercenter.domain.staff.Staff;
import com.itzhoujun.usercenter.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/updateStaffController")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UpdateStaffController {

    private final UpdateStaffService updateStaffService;
    private final Staff staff;

    @PostMapping(value = "/staff")
    public int test(@RequestParam(value = "ecnid") String ecnid,@RequestParam(value = "user_name") String user_name,@RequestParam(value = "select_value") String select_value ,   @RequestParam(value = "state",defaultValue = "正常") String state, @RequestParam(value = "check_type") String check_type) throws Exception {
        System.out.println("开始修改staff");
        int fenzuid = Integer.parseInt(select_value)+1;
        staff.setFenzuid(fenzuid);
        staff.setExecutorname(user_name);
        staff.setEcnid(ecnid);
        staff.setState(state);
        int count = 0;
        if(check_type.equals("1")){
            count = updateStaffService.userUpdate(staff);
        }else if(check_type.equals("2")){
            staff.setState("已删除");
            count = updateStaffService.userDelete(staff);
        }else if(check_type.equals("3")){
            count = updateStaffService.userInsert(staff);
        }

        return count;
    }

}
