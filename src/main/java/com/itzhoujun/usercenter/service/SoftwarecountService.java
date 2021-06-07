package com.itzhoujun.usercenter.service;

import com.itzhoujun.usercenter.dao.softwarecount.SoftwarecountMapper;
import com.itzhoujun.usercenter.domain.softwarecount.Softwarecount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


//修改软件部服务满意度
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SoftwarecountService {

    private final SoftwarecountMapper softwareMapper;
    public final int softwareUpdate(Softwarecount software) {
        System.out.println("开始修改");
        Example example = new Example(Softwarecount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("month",software.getMonth());
        System.out.println(softwareMapper.updateByExampleSelective(software,example));
        return  softwareMapper.updateByExampleSelective(software,example);
    }

    public final int softwareInsert(Softwarecount software) {
        System.out.println("开始新增");
        Example example = new Example(Softwarecount.class);
        Example.Criteria criteria = example.createCriteria();
        return  softwareMapper.insert(software);
    }

    public final int softwareDelete(Softwarecount software) {
        System.out.println("开始删除");
        Example example = new Example(Softwarecount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("month",software.getMonth());
        System.out.println("删除成功");
        return  softwareMapper.updateByExampleSelective(software,example);
    }





}

