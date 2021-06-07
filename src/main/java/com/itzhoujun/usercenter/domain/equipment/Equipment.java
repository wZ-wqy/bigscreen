package com.itzhoujun.usercenter.domain.equipment;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "equipment")
@Component
public class Equipment {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 设备
     */
    @Column(name = "serialNumber")
    private String serialnumber;

    /**
     * 设备类型
     */
    private String sbtype;
}