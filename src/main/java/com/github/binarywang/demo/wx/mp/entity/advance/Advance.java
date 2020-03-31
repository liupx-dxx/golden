package com.github.binarywang.demo.wx.mp.entity.advance;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "CL_ADVANCE")
public class Advance {

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;

    /**
     * 姓名
     * */
    @Getter@Setter
    private String userName;
    /**
     * 手机号
     * */
    @Getter@Setter
    private String phone;
    /**
     * 校区
     * */
    @Getter@Setter
    private String campus;

    /**
     * 留言
     * */
    @Getter@Setter
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime createTime;

}
