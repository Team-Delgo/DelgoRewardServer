package com.delgo.reward.comm.quartz;

import com.delgo.reward.comm.quartz.job.ClassificationCategory;
import com.delgo.reward.comm.quartz.job.InitWeeklyPoint;
import com.delgo.reward.comm.quartz.job.RefreshDailyRanking;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@Configuration
public class JobSetting {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void start_refreshDailyRankingJob(){
        JobDetail jobDetail = buildJobDetail(RefreshDailyRanking.class, new HashMap());
        try{
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0 5 0 * * ?"));
//            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/10 * * * * ?"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void start_initWeeklyPointJob(){
        JobDetail jobDetail = buildJobDetail(InitWeeklyPoint.class, new HashMap());
        try{
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0 0 0 ? * MON *"));
//            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/10 * * * * ?"));

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void start_classificationCategoryJob(){
        JobDetail jobDetail = buildJobDetail(ClassificationCategory.class, new HashMap());
        try {
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0 0 0 * * ?"));
        } catch (SchedulerException e){
            e.printStackTrace();
        }

    }

    public Trigger buildJobTrigger(String scheduleExp){
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public JobDetail buildJobDetail(Class job, Map params){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return newJob(job).usingJobData(jobDataMap).build();
    }
}
