package com.github.binarywang.demo.wx.mp.entity.surce;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户课程签到、请假表
 *
 * */
@Entity    // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name = "CL_USER_SIGN_IN")
public class LsUserSignIn {

    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    @Getter@Setter
    private Long id;

    /**
     * 用户ID
     * */
    @Getter@Setter
    private Long userId;

    /**
     * 用户姓名
     * */
    @Getter@Setter
    private String userName;

    /**
     * 用户手机号
     * */
    @Getter@Setter
    private String phone;

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
     * 课程类型
     * */
    @Getter@Setter
    private String classType;

    /**
     * 0 签到  1  请假
     * */
    @Getter@Setter
    @Length(max = 4)
    private String flag;

    /**
     * 是否审核   0 未审核  1  已审核    2  不用审核
     * */
    @Getter@Setter
    @Length(max = 4)
    private String examineFlag;

    /**
     * 是否同意   0 不同意  1  同意
     * */
    @Getter@Setter
    @Length(max = 4)
    private String agreeState;

    /**
     * 是否反馈   0 未反馈  1  已反馈
     * */
    @Getter@Setter
    @Length(max = 4)
    private String feedbackFlag;

    /**
     * 反馈内容
     * */
    @Getter@Setter
    private String feedbackContent;

    /**
     * 审核意见
     * */
    @Getter@Setter
    private String examineIdea;


    /**
     * 操作时间
     *
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime createTime;

    /**
     * 审核时间
     *
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime examineTime;

    /**
     * 反馈时间
     *
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter
    @Getter
    private LocalDateTime feedbackTime;

}
