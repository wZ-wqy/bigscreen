package com.itzhoujun.usercenter.controller;


import com.itzhoujun.usercenter.domain.department.Department;
import com.itzhoujun.usercenter.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/departmentController")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j

public class DepartmentController {

    private final DepartmentService departmentService;
    private final Department department;
    @GetMapping(value = "/department")
    public List<Department> selectdepartment(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("开始执行查询");
        List<Department> depa = departmentService.SelectDepartment();
        JSONArray json = JSONArray.fromObject(depa);
        System.out.println(json);
        return depa;
    }
    @PostMapping(value = "/update")
    public int software(@RequestParam(value = "id" ,defaultValue = "") String id,@RequestParam(value = "Fenzuname") String Fenzuname, @RequestParam(value = "check_type") String check_type) throws Exception {
        System.out.println("开始修改");
        department.setId(Integer.parseInt(id));
        department.setFenzuname(Fenzuname);
        int count = 0;
        if(check_type.equals("update")){
            count = departmentService.departmentUpdate(department);
        }else if(check_type.equals("delete")){
            count = departmentService.departmentUpdate(department);
        }else if(check_type.equals("insert")){
            count = departmentService.departmentUpdate(department);
        }

        return count;
    }

}
