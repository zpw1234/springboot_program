package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService staService;

    //每隔5秒钟运行一次
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1(){
//        System.out.println("***********task1");
//    }

    //在每天凌晨一点，把前一天的数据查询,执行方法，把数据查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2(){
        staService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
