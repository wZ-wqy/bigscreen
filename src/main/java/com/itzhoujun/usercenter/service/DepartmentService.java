package com.itzhoujun.usercenter.service;

import com.itzhoujun.usercenter.dao.department.DepartmentMapper;
import com.itzhoujun.usercenter.domain.department.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentService {

    private final DepartmentMapper departmentMapper;
    public final List<Department> SelectDepartment() {
        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        List<Department> Equ=departmentMapper.selectByExample(example);
        return Equ;
    }

    public final int departmentUpdate(Department department) {
        System.out.println("开始修改");
        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",department.getId());
        System.out.println(departmentMapper.updateByExampleSelective(department,example));
        return  departmentMapper.updateByExampleSelective(department,example);
    }
    
    public final int softwareInsert(Department department) {
        System.out.println("开始新增");
        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        return  departmentMapper.insert(department);
    }

    public final int softwareDelete(Department department) {
        System.out.println("开始删除");
        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",department.getId());
        System.out.println("删除成功");
        return  departmentMapper.updateByExampleSelective(department,example);
    }

}
