package com.knowswift.myspringboot.task;

import com.knowswift.myspringboot.api.customer.service.CustomerCustomerService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableScheduling
@EnableAsync
public class DailyTask {

    @Resource
    CustomerCustomerService customerCustomerService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void attendanceTask() {
    }

}
