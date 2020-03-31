package com.github.binarywang.demo.wx.mp.entity.surce;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * 校区表
 *
 * */
@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "CL_CAMPUS")
public class LsCampus {

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;

    /**
     * 校区名称
     * */
    @Getter@Setter
    private String campusName;

    /**
     * 校区地址
     * */
    @Getter@Setter
    private String campusAddress;

    /**
     * 校区电话
     * */
    @Getter@Setter
    private String campusPhone;

    /**
     * 校区官网
     * */
    @Getter@Setter
    private String campusSite;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime createTime;

}
