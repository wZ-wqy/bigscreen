package com.itzhoujun.usercenter.service;


import com.itzhoujun.usercenter.dao.equipment.EquipmentMapper;
import com.itzhoujun.usercenter.domain.equipment.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;



//查询设备类型

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EquipmentService {

    private final EquipmentMapper equMapper;

    public final List<Equipment> SelectSbtype() {

        Example example = new Example(Equipment.class);
        Example.Criteria criteria = example.createCriteria();
        List<Equipment> Equ=equMapper.selectByExample(example);
        return Equ;
    }
}
