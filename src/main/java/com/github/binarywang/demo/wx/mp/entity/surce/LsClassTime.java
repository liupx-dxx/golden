package com.github.binarywang.demo.wx.mp.entity.surce;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 *
 * 课程表
 * */
@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "CL_CLASS_TIME")
public class LsClassTime {

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;

    /**
     * 课程ID
     *
     * */
    @Getter@Setter
    private Long classId;
    /**
     * 周
     * */
    @Getter@Setter
    private String week;
    /**
     * 开始时间
     * */
    @Getter@Setter
    private String startTime;

    /**
     * 结束时间
     * */
    @Getter@Setter
    private String endTime;

}
