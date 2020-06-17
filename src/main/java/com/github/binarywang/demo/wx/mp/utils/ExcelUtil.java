package com.github.binarywang.demo.wx.mp.utils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class ExcelUtil {

    /**
     * 设置excel样式
     *
     * @param style
     * @return
     */
    public static CellStyle assembleCellStyle(CellStyle style) {
        // 加边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        // 居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 垂直
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }


}
