package com.itzhoujun.usercenter.domain.department;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bumenfenzu")
@Component
public class Department {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 分组
     */
    @Id
    @Column(name = "fenzuName")
    private String fenzuname;

    /**
     * 分区
     */
    private Integer area;
}