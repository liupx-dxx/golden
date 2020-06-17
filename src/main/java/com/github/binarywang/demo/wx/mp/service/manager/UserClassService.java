package com.github.binarywang.demo.wx.mp.service.manager;

import com.github.binarywang.demo.wx.mp.entity.surce.*;
import com.github.binarywang.demo.wx.mp.enums.*;
import com.github.binarywang.demo.wx.mp.repository.client.ClientUserRepository;
import com.github.binarywang.demo.wx.mp.repository.client.UserSignInRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.SignInRemindRepository;
import com.github.binarywang.demo.wx.mp.repository.manager.UserClassRepository;
import com.github.binarywang.demo.wx.mp.utils.*;
import com.github.binarywang.demo.wx.mp.vo.UserClassReq;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class UserClassService {

    UserClassRepository userClassRepository;

    ClassManagerService classManagerService;

    ClientUserRepository clientUserRepository;

    UserSignInRepository userSignInRepository;

    SignInRemindRepository signInRemindRepository;

    PathUtil pathUtil;

    /**
     * 分页获取所有信息
     * @return
     */
    public Page<LsUserClass> findPage(Map<String, String> params, Pageable pageable) {
        return userClassRepository.findPage(params,pageable);
    }


    public List<LsUserClass> findAll() {
        return userClassRepository.findAll();
    }


    /**
     * 新增用户课程
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public void save(LsUserClass userClass) {
        Long id = userClass.getId();
        //证明是新增
        if(id==null){
            userClass.setCreateTime(LocalDateTime.now());
            userClass.setUpdateTime(LocalDateTime.now());
        }else{
            LsUserClass entity = userClassRepository.findOne(id);
            UpdateToolUtil.copyNullProperties(entity,userClass);
        }
        String classId = userClass.getClassId();
        if(!StringUtils.isEmpty(classId)){
            LsClass byId = classManagerService.findById(classId);
            if(byId!=null){
                userClass.setClassName(byId.getClassName());
            }
        }
        userClassRepository.save(userClass);
        String clientUserPhone = userClass.getClientUserPhone();
        if(!StringUtils.isEmpty(clientUserPhone)){
            LsClientUser byPhone = clientUserRepository.findByPhone(clientUserPhone);
            if(byPhone==null){
                //证明该人还么有在该机构购买过课程
                //保存该用户信息
                LsClientUser clientUser = new LsClientUser();
                clientUser.setPhone(clientUserPhone);
                String password = "123456";
                //将密码加密
                String encode = Base64.getEncoder().encodeToString(password.getBytes());
                clientUser.setPassword(encode);
                clientUser.setUserName(userClass.getClientUserName());
                clientUser.setCreateTime(LocalDateTime.now());
                clientUserRepository.save(clientUser);
            }
        }
    }

    /**
     * 批量保存用户课程
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(UserClassReq userClassReq) {
        List<LsUserClass> userClassList = userClassReq.getUserClassList();
        if(!CollectionUtils.isEmpty(userClassList)){
            userClassList.stream().forEach(item ->{
                item.setCreateTime(LocalDateTime.now());
            });
        }
        userClassRepository.saveAll(userClassList);
        if(StringUtils.isEmpty(userClassReq.getPhone())){
            //证明该人还么有在该机构购买过课程
            //保存该用户信息
            LsClientUser clientUser = new LsClientUser();
            clientUser.setPhone(userClassReq.getPhone());
            String password = "123456";
            //将密码加密
            String encode = Base64.getEncoder().encodeToString(password.getBytes());
            clientUser.setPassword(encode);
            clientUser.setUserName(userClassReq.getUserName());
            clientUser.setCreateTime(LocalDateTime.now());
            clientUserRepository.save(clientUser);
        }

    }


    public void deleteById(String id) {
        LsUserClass one = userClassRepository.findOne(Long.valueOf(id));
        if(one!=null){
            userClassRepository.delete(one);
        }
    }

    public LsUserClass findById(String id) {
        Optional<LsUserClass> byId = userClassRepository.findById(Long.valueOf(id));
        if(byId==null){
            return null;
        }
        LsUserClass lsUserClass = byId.get();
        /*if(lsUserClass!=null){
            LsUserSignIn userSignIn = userSignInRepository.findByUserIdAndUserClassId(lsUserClass.getId());
            if(userSignIn!=null){
                String flag = userSignIn.getFlag();
                if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                    lsUserClass.setSignInFlag(SignInStateEnum.SIGN_IN.getCode());
                }else{
                    lsUserClass.setSignInFlag(SignInStateEnum.LEAVE.getCode());
                }
            }else{
                lsUserClass.setSignInFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
            }
        }*/
        return lsUserClass;
    }


    /**
     * 根据用户查询个人购买课程
     *
     * */
    public List<LsUserClass> findByUserPhone(String param,String phone,Long userId) {
        //获取该用户今天签到、请假的课程
        List<LsUserSignIn> userSignIns = userSignInRepository.findByUserId(userId);
        List<LsUserClass> userClassList = userClassRepository.findByUserPhone(phone,param);
        if(!CollectionUtils.isEmpty(userClassList)){
            userClassList.stream().forEach(item ->{
                if(!CollectionUtils.isEmpty(userSignIns)){
                    userSignIns.stream().forEach(userSignIn ->{
                        if(item.getId().equals(userSignIn.getUserClassId())){
                            String flag = userSignIn.getFlag();
                            if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                                item.setSignInFlag(SignInStateEnum.SIGN_IN.getCode());
                            }else{
                                item.setSignInFlag(SignInStateEnum.LEAVE.getCode());
                            }
                        }
                    });
                }else{
                    item.setSignInFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
                }
                //查看该课程今天是否签到
                /*LsUserSignIn lsUserSignIn = userSignInRepository.findByUserIdAndUserClassId(userId + "", item.getId());
                if(lsUserSignIn!=null && !StringUtils.isEmpty(lsUserSignIn.getFlag())){
                    String flag = lsUserSignIn.getFlag();
                    if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                        item.setSignInFlag(SignInStateEnum.SIGN_IN.getCode());
                    }else{
                        item.setSignInFlag(SignInStateEnum.LEAVE.getCode());
                    }
                }else{
                    item.setSignInFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
                }*/
                String classType = item.getClassType();
                if(!StringUtils.isEmpty(classType)){
                    if(ClassTypeEnum.CLASS.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.CLASS.getDesc());
                    }else if(ClassTypeEnum.GROUP_COURSE.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.GROUP_COURSE.getDesc());
                    }else if(ClassTypeEnum.ONE_ON_ONE.getCode().equals(classType)){
                        item.setClassType(ClassTypeEnum.ONE_ON_ONE.getDesc());
                    }
                }
            });
        }
        return userClassList;
    }
    /**
     * 用户签到扣除相应的课时
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity userSignIn(String id, LsClientUser clientUser) {
        //查询该购买记录
        LsUserClass userClass = userClassRepository.findOne(Long.valueOf(id));
        if(userClass==null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR);
        }
        //扣除相应的课时
        int classHourNum = userClass.getClassHourNum();
        if(classHourNum<=1){
            return ResultUtils.fail(ResultCodeEnum.OPERATE_FAIL,"剩余课时不足");
        }
        //证明他购买的该课程还有剩余课时
        //课时扣除1
        classHourNum = classHourNum-2;
        userClass.setClassHourNum(classHourNum);
        userClass.setUpdateTime(LocalDateTime.now());
        userClassRepository.save(userClass);
        //记录当前人签到记录
        LsUserSignIn lsUserSignIn = new LsUserSignIn();
        lsUserSignIn.setClassId(userClass.getClassId());
        lsUserSignIn.setUserName(clientUser.getUserName());
        lsUserSignIn.setClassType(userClass.getClassType());
        lsUserSignIn.setUserId(clientUser.getId());
        lsUserSignIn.setUserClassId(userClass.getId());
        lsUserSignIn.setClassName(userClass.getClassName());
        lsUserSignIn.setFlag(OperationTypeEnum.SIGN_IN.getCode());
        lsUserSignIn.setExamineFlag(ExamineStateEnum.NO_EXAMINE.getCode());
        lsUserSignIn.setFeedbackFlag(FeedbackStateEnum.NO_FEEDBACK.getCode());
        lsUserSignIn.setExamineIdea("自动审核");
        lsUserSignIn.setCreateTime(LocalDateTime.now());
        userSignInRepository.save(lsUserSignIn);
        return ResultUtils.success();
    }


    /**
     * 用户请假  不应扣课时
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity leave(String id, LsClientUser clientUser) {
        //查询该购买记录
        LsUserClass userClass = userClassRepository.findOne(Long.valueOf(id));
        if(userClass==null){
            return ResultUtils.fail(ResultCodeEnum.PARAMETER_ERROR);
        }
        userClass.setUpdateTime(LocalDateTime.now());
        userClassRepository.save(userClass);
        //记录当前人请假记录
        LsUserSignIn lsUserSignIn = new LsUserSignIn();
        lsUserSignIn.setClassId(userClass.getClassId());
        lsUserSignIn.setClassName(userClass.getClassName());
        lsUserSignIn.setUserName(clientUser.getUserName());
        lsUserSignIn.setUserId(clientUser.getId());
        lsUserSignIn.setUserClassId(userClass.getId());
        lsUserSignIn.setClassType(userClass.getClassType());
        lsUserSignIn.setFlag(OperationTypeEnum.LEAVE.getCode());
        lsUserSignIn.setExamineFlag(ExamineStateEnum.UN_EXAMINE.getCode());
        lsUserSignIn.setFeedbackFlag(FeedbackStateEnum.NO_FEEDBACK.getCode());
        lsUserSignIn.setCreateTime(LocalDateTime.now());
        userSignInRepository.save(lsUserSignIn);
        return ResultUtils.success();
    }

    /**
     *
     * 批量删除
     * */
    @Transactional(rollbackFor = Exception.class)
    public void delById(List<LsUserClass> userClassList) {
        userClassRepository.deleteAll(userClassList);
    }

    /**
     *
     * 批量删除
     * */
    public List<LsUserClass> findByClassId(Long id) {
        return userClassRepository.findByClassId(id+"");
    }

    /**
     *
     * 根据周单位查询
     *
     * */
    public List<LsUserClass> findByWeek(String week) {
        return userClassRepository.findByWeek(week);
    }

    /**
     *
     * 获取总课时
     *
     * */
    public String getUserSurplusByPhone(String phone) {
        return userClassRepository.getUserSurplusByPhone(phone);
    }

    /*public LsUserClass findByRemindId(String remindId) {
    }*/

    /**
     *
     * 根据提醒ID获取购买信息
     * **/
    public LsUserClass findByRemindId(String id) {
        LsSignInRemind remind = signInRemindRepository.findOne(Long.valueOf(id));
        LsUserClass lsUserClass = userClassRepository.findOne(remind.getUserClassId());
        if(lsUserClass!=null){
            LsUserSignIn userSignIn = userSignInRepository.findByUserIdAndUserClassId(lsUserClass.getId(),dateToString(remind.getCreateTime()));
            if(userSignIn!=null){
                String flag = userSignIn.getFlag();
                if(OperationTypeEnum.SIGN_IN.getCode().equals(flag)){
                    lsUserClass.setSignInFlag(SignInStateEnum.SIGN_IN.getCode());
                }else{
                    lsUserClass.setSignInFlag(SignInStateEnum.LEAVE.getCode());
                }
            }else{
                lsUserClass.setSignInFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
            }
        }
        return lsUserClass;
    }

    private String dateToString(LocalDateTime localDateTime){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String localTime = df.format(localDateTime);
        return localTime;
    }

    /**
     * 获取导出的数据
     *
     * */
    public List<LsUserClass> getUserClassByParm(UserClassReq userClassReq) {
        return userClassRepository.getUserClassByParm(userClassReq);
    }

    /**
     * 导出Excel报表
     *
     * @param username 导出人
     * @param list     导出数据
     * @param userClassReq  查询条件
     * @return
     */
    public String exportExcel(String username, List<LsUserClass> list, UserClassReq userClassReq) {
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
        //生成excel
        exportExcel(list, path, "购买课程统计", username, userClassReq.getUserName(), userClassReq.getPhone());
        if (new File(path).exists() && new File(path).isFile()) {
            //设置文件地址
            //获取系统ip地址
            String serviceIp = "121.89.197.78:8000";
            StringBuffer buffer = new StringBuffer("http://")
                .append(serviceIp)
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
    public void exportExcel(List<LsUserClass> list, String path, String name, String userName, String clientUserName, String clientUserPhone) {
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
    private void process(String userName, String clientUserName, Workbook workbook, Sheet sheet, List<LsUserClass> list) {
        sheet.setDefaultColumnWidth(23);
        sheet.setDefaultRowHeightInPoints(15);
        // 标题样式
        CellStyle style = workbook.createCellStyle();
        // 设置样式
        ExcelUtil.assembleCellStyle(style);
        // 第一行
        Row row1 = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) 6));
        row1.setHeightInPoints(25);
        // --->创建一个单元格
        Cell cell1 = row1.createCell(0);
        cell1.setCellStyle(style);
        cell1.setCellValue("学生购买课程情况统计");

        // 第二行
        sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 0, (short) 6));
        Row row2 = sheet.createRow(1);
        row2.setHeightInPoints(25);
        Cell cell2 = row2.createCell(0);

        cell2.setCellStyle(style);
        cell2.setCellValue("学生：" + (StringUtils.isEmpty(clientUserName)?"所有":clientUserName));

        // 第三行
        sheet.addMergedRegion(new CellRangeAddress(2, (short) 2, 0, (short) 6));
        Row row3 = sheet.createRow(2);
        row1.setHeightInPoints(25);
        Cell cell3 = row3.createCell(0);

        cell3.setCellStyle(style);
        cell3.setCellValue("导出人：" + userName);

        // 第四行
        sheet.addMergedRegion(new CellRangeAddress(3, (short) 3, 0, (short) 6));
        Row row4 = sheet.createRow(3);
        row1.setHeightInPoints(25);
        Cell cell4 = row4.createCell(0);

        cell4.setCellStyle(style);
        if (list != null && list.size() > 0) {
            cell4.setCellValue("导出数目：" + (list.size()));
        } else {
            cell4.setCellValue("导出数目：" + 0);
        }

        setCellBorder(1, 6, row1, style);
        setCellBorder(1, 6, row2, style);
        setCellBorder(1, 6, row3, style);
        setCellBorder(1, 6, row4, style);

        // 第五行
        Row row5 = sheet.createRow(4);
        Cell cel_0 = row5.createCell(0);
        cel_0.setCellStyle(style);

        cel_0.setCellValue("购买课程");
        Cell cel_1 = row5.createCell(1);
        cel_1.setCellStyle(style);

        cel_1.setCellValue("课程类型");
        Cell cel_2 = row5.createCell(2);
        cel_2.setCellStyle(style);
        cel_2.setCellValue("购买价格");

        Cell cel_3 = row5.createCell(3);
        cel_3.setCellStyle(style);
        cel_3.setCellValue("剩余课时");

        Cell cel_4 = row5.createCell(4);
        cel_4.setCellStyle(style);
        cel_4.setCellValue("上课时间");

        Cell cel_5 = row5.createCell(5);
        cel_5.setCellStyle(style);
        cel_5.setCellValue("购买时间");

        Cell cel_6 = row5.createCell(6);
        cel_6.setCellStyle(style);
        cel_6.setCellValue("总计");

        // 报表数据
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                int count = 5;
                LsUserClass vo = list.get(i);
                Row row = sheet.createRow(count + i);
                row.setHeightInPoints(15);
                Cell cell_0 = row.createCell(0);
                cell_0.setCellStyle(style);
                cell_0.setCellValue(vo.getClassName());
                Cell cell_1 = row.createCell(1);
                cell_1.setCellStyle(style);
                cell_1.setCellValue(vo.getClassType());
                Cell cell_2 = row.createCell(2);
                cell_2.setCellStyle(style);
                cell_2.setCellValue(vo.getPrice());
                Cell cell_3 = row.createCell(3);
                cell_3.setCellStyle(style);
                cell_3.setCellValue(vo.getClassHourNum());
                Cell cell_4 = row.createCell(4);
                cell_4.setCellStyle(style);
                cell_4.setCellValue(vo.getClassTime());
                Cell cell_5 = row.createCell(5);
                cell_5.setCellStyle(style);
                cell_5.setCellValue(vo.getCreateTime().toString());
                Cell cell_6 = row.createCell(6);
                cell_6.setCellStyle(style);
                cell_6.setCellValue("");
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
