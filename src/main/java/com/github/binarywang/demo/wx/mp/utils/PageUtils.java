package com.github.binarywang.demo.wx.mp.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class PageUtils {

    /**
     * fenye
     * */
    @Getter
    @Setter
    private int page;

    /**
     * fenye
     * */
    @Getter@Setter
    private int total;

    /**
     * fenye
     * */
    @Getter@Setter
    private int size;

    /**
     * fenye
     * */
    @Getter@Setter
    private List rows;

    /**
     * fenye
     * */
    @Getter@Setter
    private String msg;

    /**
     * fenye
     * */
    @Getter@Setter
    private int code;
}
