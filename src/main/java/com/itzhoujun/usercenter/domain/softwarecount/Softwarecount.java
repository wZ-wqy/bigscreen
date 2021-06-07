package com.itzhoujun.usercenter.domain.softwarecount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "softwarecount")
@Component
public class Softwarecount {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 月份
     */
    private String month;

    /**
     * 维护次数
     */
    private Integer frequency;

    /**
     * 满意数量
     */
    private Integer satisfaction;

    /**
     * 一般数量
     */
    private Integer ordinary;

    /**
     * 不满意数量
     */
    @Column(name = "noSatisfaction")
    private Integer nosatisfaction;

    /**
     * 意见数量
     */
    private Integer opinion;
}