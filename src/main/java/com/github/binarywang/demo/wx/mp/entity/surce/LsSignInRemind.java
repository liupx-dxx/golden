package com.github.binarywang.demo.wx.mp.entity.surce;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * 签到提醒表
 * */
@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "CL_SIGN_IN_REMIND")
public class LsSignInRemind {

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;

    /**
     * 用户手机号
     *
     * */
    @Getter@Setter
    private String userPhone;

    /**
     * 课程名称
     *
     * */
    @Getter@Setter
    private String className;

    /**
     * 上课时间
     * */
    @Getter@Setter
    private String classTime;



    /**
     * 老师姓名
     * */
    @Getter@Setter
    private String teacherName;

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    @Getter@Setter
    private String classType;

}
