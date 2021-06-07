package com.itzhoujun.usercenter.domain.new_user;

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
@Table(name = "new_user")
@Component
public class NewUser {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;


    private String executorname;

    private String zhudian;

    private Integer ecnid;

    private String state;
}