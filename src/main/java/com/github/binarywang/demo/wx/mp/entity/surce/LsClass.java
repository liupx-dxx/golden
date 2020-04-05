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
 * 课程表
 * */
@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "CL_CLASS")
public class LsClass {

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;

    /**
     * 课程名称
     *
     * */
    @Getter@Setter
    private String className;
    /**
     * 课程介绍
     * */
    @Getter@Setter
    private String classDetail;
    /**
     * 上课地址
     * */
    @Getter@Setter
    private String classAddress;

    /**
     * 老师ID
     * */
    @Getter@Setter
    private Long teacherId;

    /**
     * 老师姓名
     * */
    @Getter@Setter
    private String teacherName;

    /**
     * 价格
     * */
    @Getter@Setter
    private String price;

    /**
     * 校区ID
     * */
    @Getter@Setter
    private String campusId;

    /**
     * 校区名称
     * */
    @Getter@Setter
    private String campusName;

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    @Getter@Setter
    private String classType;

    /**
     * 开始时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime startTime;

    /**
     * 结束时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime endTime;

    /**
     * 创建时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime createTime;

    /**
     * 课程类型  1、班课  2、小组课 3、一对一
     * */
    @Getter@Setter
    @Transient
    private List<LsClassTime> classTimes;

}
