package com.delgo.reward.comm.quartz.job;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
public class RefreshDailyRanking extends QuartzJobBean {

    private final RankingService rankingService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(LocalTime.now() + ": RefreshDailyRankingJob Execute");

        try{
            rankingService.rankingByPoint();
        } catch (Exception e){
            log.info("RankingByPoint: no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.TOTAL.getCode());
        } catch (Exception e){
            log.info(CategoryCode.TOTAL.getCode() + ": no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.CA0001.getCode());
        } catch (Exception e){
            log.info(CategoryCode.CA0001.getCode() + ": no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.CA0002.getCode());
        } catch (Exception e){
            log.info(CategoryCode.CA0002.getCode() + ": no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.CA0003.getCode());
        } catch (Exception e){
            log.info(CategoryCode.CA0003.getCode() + ": no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.CA0004.getCode());
        } catch (Exception e){
            log.info(CategoryCode.CA0004.getCode() + ": no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.CA0005.getCode());
        } catch (Exception e){
            log.info(CategoryCode.CA0005.getCode() + ": no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.CA0006.getCode());
        } catch (Exception e){
            log.info(CategoryCode.CA0006.getCode() + ": no data");
        }
        try{
            rankingService.rankingByCategoryCode(CategoryCode.CA9999.getCode());
        } catch (Exception e){
            log.info(CategoryCode.CA9999.getCode() + ": no data");
        }

        log.info(LocalTime.now() + ": RefreshDailyRankingJob Exit");
    }

}
