package com.github.binarywang.demo.wx.mp.entity.sys;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "SYS_USER")
public class SysUser {

    public SysUser(){}

    public SysUser(String name,String password,String roles){
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;
    @Getter@Setter
    @Column(name = "NAME", nullable = false) // @Column： 对应数据库列名,可选, nullable 是否可以为空, 默认true
    private String name;
    @Getter@Setter
    private String password;
    @Getter@Setter
    private String phone;
    @Getter@Setter
    private String roles;

}
