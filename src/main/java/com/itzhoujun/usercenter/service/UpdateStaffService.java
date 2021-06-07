package com.itzhoujun.usercenter.service;

import com.itzhoujun.usercenter.dao.staff.StaffMapper;
import com.itzhoujun.usercenter.domain.staff.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;









//修改员工信息
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateStaffService {

    private final StaffMapper staffMapper;
    public final int userUpdate(Staff staff) {
        System.out.println("开始修改");
        Example example = new Example(Staff.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("executorname",staff.getExecutorname());
        System.out.println(staffMapper.updateByExampleSelective(staff,example));
        return  staffMapper.updateByExampleSelective(staff,example);
    }

    public final int userInsert(Staff staff) {
        System.out.println("开始新增");
        Example example = new Example(Staff.class);
        Example.Criteria criteria = example.createCriteria();
        return  staffMapper.insert(staff);
    }

    public final int userDelete(Staff staff) {
        System.out.println("开始删除");
        Example example = new Example(Staff.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("executorname",staff.getExecutorname());
        return  staffMapper.updateByExampleSelective(staff,example);
    }





}
