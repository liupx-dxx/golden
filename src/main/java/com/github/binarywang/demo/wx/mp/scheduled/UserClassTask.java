package com.github.binarywang.demo.wx.mp.scheduled;

import com.github.binarywang.demo.wx.mp.entity.surce.LsClass;
import com.github.binarywang.demo.wx.mp.entity.surce.LsClassTime;
import com.github.binarywang.demo.wx.mp.entity.surce.LsSignInRemind;
import com.github.binarywang.demo.wx.mp.entity.surce.LsUserClass;
import com.github.binarywang.demo.wx.mp.service.manager.ClassManagerService;
import com.github.binarywang.demo.wx.mp.service.manager.ClassTimeService;
import com.github.binarywang.demo.wx.mp.service.manager.UserClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

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
    //每天半小时查询有没有半小时之后需要签到的用户
    @Scheduled(cron = "0 0/30 * * * ?")
    public void remind(){
        //查看今天是周几
        String week = getWeek();
        //获取今天有那些课
        List<LsClassTime> lsClassTimeList = classTimeService.findByWeek(week);
        if(CollectionUtils.isEmpty(lsClassTimeList)){
            LOGGER.info("今天"+week+"没有课程可以上哦!");
            return;
        }
        //证明今天有课程可以上  查询当前有哪些课程
        List<Long> classIds = lsClassTimeList.stream().map(LsClassTime::getClassId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(classIds)){
            LOGGER.info("你的数据问题哦！课程的ID为空了！");
            return;
        }
        List<LsClass> classList = classManagerService.findByIds(classIds);
        if(CollectionUtils.isEmpty(classList)){
            LOGGER.info("你的数据问题哦！查询课程信息的为空了！");
            return;
        }
        List<LsSignInRemind> signInRemindList = new ArrayList<>();
        //查询正常 流程继续  获取购买该课程的学生  并且是课时大于0的
        classList.stream().forEach(item ->{
            List<LsUserClass> userClassList = userClassService.findByClassId(item.getId());
            if(CollectionUtils.isEmpty(classList)){
                LOGGER.info("该课程并没有学生报名哦!");
            }else{
                //证明有学生报名了该课程
                userClassList.stream().forEach(userClass ->{
                    LsSignInRemind lsSignInRemind = new LsSignInRemind();
                    lsSignInRemind.setUserPhone(userClass.getClientUserPhone());
                    lsSignInRemind.setClassTime(userClass.getClassTime());
                    lsSignInRemind.setClassName(userClass.getClassName());
                    lsSignInRemind.setClassType(userClass.getClassType());
                    lsSignInRemind.setTeacherName(item.getTeacherName());
                    signInRemindList.add(lsSignInRemind);
                });
            }

        });
        //判断该时间段有没有学生需要提醒
        if(CollectionUtils.isEmpty(signInRemindList)){
            LOGGER.info("该课程并没有学会报名哦！循环停止咯");
            return;
        }
        //保存需要提醒的信息


        //实时推送消息到客户


    }

    public static void main(String[] args) {
        String week = getWeek();
        System.out.println(week);
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




}
