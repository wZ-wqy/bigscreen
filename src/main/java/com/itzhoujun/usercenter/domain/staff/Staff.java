package com.itzhoujun.usercenter.domain.staff;

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
@Table(name = "staff")
@Component
public class Staff {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 负责人
     */
    @Column(name = "executorName")
    private String executorname;

    /**
     * 分组
     */
    private Integer fenzuid;

    /**
     * 员工编号
     */
    private String ecnid;

    /**
     * 状态
     */
    private String state;
}