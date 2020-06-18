package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.advance.Advance;
import com.github.binarywang.demo.wx.mp.repository.advance.AdvanceRepository;
import com.github.binarywang.demo.wx.mp.utils.ExcelUtil;
import com.github.binarywang.demo.wx.mp.utils.PathUtil;
import com.github.binarywang.demo.wx.mp.utils.WeChatUtils;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdvanceService {
    @Autowired
    AdvanceRepository advanceRepository;
    @Autowired
    WeChatUtils weChatUtils;
    @Autowired
    PathUtil pathUtil;

    @Value("${nginx.server}")
    private String nginxServer;

    /**
     * 获取所有信息
     * @return
     */
    public List<Advance> findAll() {
        return advanceRepository.findAll();
    }

    /**
     * 获取单个信息
     * @return
     */
    public Advance findById(String id) {

        return advanceRepository.findById(Long.valueOf(id)).get();
    }

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<Advance> findPage(Map<String, String> params, Pageable pageable) {
        return advanceRepository.findPage(params,pageable);
    }


    /**
     * 新增预约信息
     * @return
     */
    public void save(Advance entity) {
        entity.setCreateTime(LocalDateTime.now());
        advanceRepository.save(entity);
        //保存成功后告诉公众号有人预约报名
        //weChatUtils.getAccess_token();
    }

    public int findByPhone(String phone) {
        return advanceRepository.findByPhone(phone);
    }

    /**
     * 查询要导出的数据
     *
     * @param map 参数
     * @return
     */
    public List<Advance> getAdvanceByParm(Map<String, String> map) {
        return advanceRepository.getAdvanceByParm(map);
    }

    /**
     * 导出Excel报表
     *
     * @param username 导出人
     * @param list     导出数据
     * @param params  查询条件
     * @return
     */
    public String exportExcel(String username, List<Advance> list, Map<String,String> params) {
        String excelFileName = null;
        //设置文件名称
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(new Date()) + ".xlsx";
        //设置文件存储地址
        String sysFilePath = "/opt/upload";
        String path = pathUtil.getFileUpLoadLocalStoragePath(sysFilePath) + "/" + fileName;
        File newFile = new File(path);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String userName = "";
        if (params.containsKey("userName")) {
            userName = params.get("userName");
        }
        String phone = "";
        if (params.containsKey("phone")) {
            phone = params.get("phone");
        }
        //生成excel
        exportExcel(list, path, "购买课程统计", username, userName, phone);
        if (new File(path).exists() && new File(path).isFile()) {
            //设置文件地址
            //获取系统ip地址
            //String serviceIp = "121.89.197.78:8000";
            StringBuffer buffer = new StringBuffer("http://")
                .append(nginxServer)
                .append(path.replaceFirst(sysFilePath, ""));
            excelFileName = buffer.toString();

        }

        return excelFileName;
    }

    /**
     * 创建excel文件
     *
     * @param list
     * @param path
     * @param name
     * @param userName
     * @param clientUserName
     * @param clientUserPhone
     * @throws IndexOutOfBoundsException
     * @throws IOException
     */
    public void exportExcel(List<Advance> list, String path, String name, String userName, String clientUserName, String clientUserPhone) {
        try {
            File newFile = new File(path);
            OutputStream out = new FileOutputStream(newFile);
            SXSSFWorkbook wb = new SXSSFWorkbook();

            wb.setCompressTempFiles(true);

            Workbook workbook = new SXSSFWorkbook(100);

            Sheet sheet = workbook.createSheet(name);

            //String curDate = startTime + "  至  " + endTime;

            process(userName, clientUserName, workbook, sheet, list);

            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据写入excel文件中
     *
     * @param userName 用户名
     * @param clientUserName  参数
     * @param workbook 文本对象
     * @param sheet    脚本对象
     * @param list     导出的数据集合
     */
    private void process(String userName, String clientUserName, Workbook workbook, Sheet sheet, List<Advance> list) {
        sheet.setDefaultColumnWidth(23);
        sheet.setDefaultRowHeightInPoints(15);
        // 标题样式
        CellStyle style = workbook.createCellStyle();
        // 设置样式
        ExcelUtil.assembleCellStyle(style);
        // 第一行
        Row row1 = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) 3));
        row1.setHeightInPoints(25);
        // --->创建一个单元格
        Cell cell1 = row1.createCell(0);
        cell1.setCellStyle(style);
        cell1.setCellValue("公众号预约情况统计");

        // 第二行
        sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 0, (short) 3));
        Row row2 = sheet.createRow(1);
        row2.setHeightInPoints(25);
        Cell cell2 = row2.createCell(0);

        cell2.setCellStyle(style);
        cell2.setCellValue("预约人：" + (StringUtils.isEmpty(clientUserName)?"所有":clientUserName));

        // 第三行
        sheet.addMergedRegion(new CellRangeAddress(2, (short) 2, 0, (short) 3));
        Row row3 = sheet.createRow(2);
        row1.setHeightInPoints(25);
        Cell cell3 = row3.createCell(0);

        cell3.setCellStyle(style);
        cell3.setCellValue("导出人：" + userName);

        // 第四行
        sheet.addMergedRegion(new CellRangeAddress(3, (short) 3, 0, (short) 3));
        Row row4 = sheet.createRow(3);
        row1.setHeightInPoints(25);
        Cell cell4 = row4.createCell(0);

        cell4.setCellStyle(style);
        if (list != null && list.size() > 0) {
            cell4.setCellValue("导出数目：" + (list.size()));
        } else {
            cell4.setCellValue("导出数目：" + 0);
        }

        setCellBorder(1, 3, row1, style);
        setCellBorder(1, 3, row2, style);
        setCellBorder(1, 3, row3, style);
        setCellBorder(1, 3, row4, style);

        // 第五行
        Row row5 = sheet.createRow(4);
        Cell cel_0 = row5.createCell(0);
        cel_0.setCellStyle(style);

        cel_0.setCellValue("预约人姓名");
        Cell cel_1 = row5.createCell(1);
        cel_1.setCellStyle(style);

        cel_1.setCellValue("预约人手机号");
        Cell cel_2 = row5.createCell(2);
        cel_2.setCellStyle(style);
        cel_2.setCellValue("留言");

        Cell cel_3 = row5.createCell(3);
        cel_3.setCellStyle(style);
        cel_3.setCellValue("预约时间");


        // 报表数据
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                int count = 5;
                Advance vo = list.get(i);
                Row row = sheet.createRow(count + i);
                row.setHeightInPoints(15);
                Cell cell_0 = row.createCell(0);
                cell_0.setCellStyle(style);
                cell_0.setCellValue(vo.getUserName());
                Cell cell_1 = row.createCell(1);
                cell_1.setCellStyle(style);
                cell_1.setCellValue(vo.getPhone());
                Cell cell_2 = row.createCell(2);
                cell_2.setCellStyle(style);
                cell_2.setCellValue(vo.getMessage());
                Cell cell_3 = row.createCell(3);
                cell_3.setCellStyle(style);
                cell_3.setCellValue(vo.getCreateTime()+"");
            }
        }

    }

    /**
     * 合并单元格加边框 水平
     *
     * @param start 为和并的第二列
     * @param end   为合并的最后一列
     * @param row   为当前行
     * @param style 样式
     *              (里面有设置边框)
     */
    private void setCellBorder(int start, int end, Row row,
                               CellStyle style) {
        for (int i = start; i <= end; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(style);
        }
    }


}
