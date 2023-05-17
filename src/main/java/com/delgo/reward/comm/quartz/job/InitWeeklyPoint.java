//package com.delgo.reward.comm.quartz.job;
//
//import com.delgo.reward.service.RankingService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import java.time.LocalTime;
//
//@Slf4j
//@RequiredArgsConstructor
//public class InitWeeklyPoint extends QuartzJobBean {
//
//    private final RankingService rankingService;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        log.info(LocalTime.now() + ": InitialLastWeeklyPoint Execute");
//
////        rankingService.initLastWeeklyPoint();
////        rankingService.setLastWeeklyPoint();
////        rankingService.setLastRanking();
////        rankingService.initWeeklyPoint();
////        rankingService.rankingByPoint();
//
//        log.info(LocalTime.now() + ": InitialLastWeeklyPoint Exit");
//    }
//}
