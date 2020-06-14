package com.github.binarywang.demo.wx.mp.scheduled;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsSignInRemind;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.enums.ReadStateEnum;
import com.github.binarywang.demo.wx.mp.enums.SignInStateEnum;
import com.github.binarywang.demo.wx.mp.service.manager.ClassManagerService;
import com.github.binarywang.demo.wx.mp.service.manager.ClassTimeService;
import com.github.binarywang.demo.wx.mp.service.manager.SignInRemindService;
import com.github.binarywang.demo.wx.mp.service.manager.UserClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class UserClassTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserClassTask.class);

    @Autowired
    UserClassService userClassService;
    @Autowired
    ClassManagerService classManagerService;
    @Autowired
    ClassTimeService classTimeService;
    @Autowired
    SignInRemindService signInRemindService;
    //每天半小时查询有没有半小时之后需要签到的用户  半小时: 0 */30 * * * ?  十秒: 0/10 * *  * * ?
    @Scheduled(cron = "0 */30 * * * ?")
    //@Scheduled(cron = "0/10 * *  * * ?")
    public void remind(){

        //查看今天是周几
        String week = getWeek();
        LOGGER.info("今天"+week+"开始提醒用户签到咯!");
        //获取当前时间
        String time = getDate();
        List<LsSignInRemind> signInRemindList = new ArrayList<>();
        //查询所有用户
        List<LsUserClass> classList = userClassService.findByWeek(week);
        if(CollectionUtils.isEmpty(classList)){
            LOGGER.info("今天没有需要提醒签到的学生");
            return;
        }
        classList.stream().forEach(userClass ->{
            //再去判断该学生是不是选择的该时间段
            String classTime = userClass.getClassTime();
            if(StringUtils.isEmpty(classTime)){
                LOGGER.info("你的学生购买信息有误了 classTime 为 {}",classTime);
            }else{
                String[] split = classTime.split("-");
                //String weekValue = split[0];
                String startTime = split[1];
                //String minuteValue = split[2];
                long calTime = calTime(startTime, time);
                if((calTime > 0 && calTime <=30)){
                    LsClass lsClass = classManagerService.findById(userClass.getClassId() + "");
                    //证明今天该时间段有要上课的同学
                    LsSignInRemind lsSignInRemind = new LsSignInRemind();
                    lsSignInRemind.setUserPhone(userClass.getClientUserPhone());
                    lsSignInRemind.setClassTime(userClass.getClassTime());
                    lsSignInRemind.setClassName(userClass.getClassName());
                    lsSignInRemind.setClassType(userClass.getClassType());
                    lsSignInRemind.setUserClassId(userClass.getId());
                    lsSignInRemind.setTeacherName(lsClass.getTeacherName());
                    lsSignInRemind.setReadState(ReadStateEnum.NO_READ.getCode());
                    lsSignInRemind.setFlag(SignInStateEnum.NO_SIGN_IN_LEAVE.getCode());
                    lsSignInRemind.setCreateTime(LocalDateTime.now());
                    signInRemindList.add(lsSignInRemind);
                }else{
                    LOGGER.info("该时间段还没到提醒时间，或者已经过了上课时间了!");
                }
            }
        });
        //判断该时间段有没有学生需要提醒
        if(CollectionUtils.isEmpty(signInRemindList)){
            LOGGER.info("该课程并没有学会报名哦！循环停止咯");
            return;
        }
        //保存需要提醒的信息
        signInRemindService.saveAll(signInRemindList);
        List<String> phones = signInRemindList.stream().map(LsSignInRemind::getUserPhone).collect(Collectors.toList());
        LOGGER.info("已经给以下用户发送了提醒消息,{}",phones.toString());
    }


    /**
     *
     * 获取当前周几
     * */
    private static String getWeek() {
        String week = "";
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            week = "周日";
        } else if (weekday == 2) {
            week = "周一";
        } else if (weekday == 3) {
            week = "周二";
        } else if (weekday == 4) {
            week = "周三";
        } else if (weekday == 5) {
            week = "周四";
        } else if (weekday == 6) {
            week = "周五";
        } else if (weekday == 7) {
            week = "周六";
        }
        return week;
    }
    public static void main(String[] args) {
        // 计算时间差
        System.out.println(LocalDateTime.now());

    }

    // 计算两个时间差，返回为分钟。
    private static long calTime(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        long minutes = 0L;
        try {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            minutes = diff / (1000 * 60);
        } catch (ParseException e) {
            LOGGER.info("*******   抱歉，时间日期解析出错   *******");
        }
        return minutes;
    }

    private static String getDate(){
        Date dd=new Date();
        //格式化
        SimpleDateFormat sim=new SimpleDateFormat("HH:mm");
        String time=sim.format(dd);
        return time;
    }



}
