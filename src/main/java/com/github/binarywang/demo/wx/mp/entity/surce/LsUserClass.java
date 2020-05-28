package com.github.binarywang.demo.wx.mp.entity.surce;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户课程表
 *
 * */
@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "CL_USER_CLASS")
public class LsUserClass {

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;

    /**
     * 课程ID
     * */
    @Getter@Setter
    private String classId;

    /**
     * 课程名称
     * */
    @Getter@Setter
    private String className;

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    @Getter@Setter
    @Length(max = 2)
    private String classType;

    /**
     * 客户端用户手机号
     * */
    @Getter@Setter
    @Length(max = 11)
    private String clientUserPhone;

    /**
     * 客户端用户姓名
     * */
    @Getter@Setter
    private String clientUserName;

    /**
     * 客户端用户选择的上课时间
     * */
    @Getter@Setter
    private String classTime;

    /**
     * 实付价格
     * */
    @Getter@Setter
    private double price;

    /**
     * 剩余课时
     * */
    @Getter@Setter
    private int classHourNum;

    /**
     * 今天该课程是否签到/请假
     * */
    @Getter@Setter
    private String signInFlag;

    /**
     * 修改时间
     *
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime updateTime;

    /**
     * 创建时间
     *
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime createTime;

}
