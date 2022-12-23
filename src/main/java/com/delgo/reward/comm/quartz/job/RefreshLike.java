package com.delgo.reward.comm.quartz.job;

import com.delgo.reward.service.LikeListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


@Slf4j
@RequiredArgsConstructor
public class RefreshLike extends QuartzJobBean {

    private final LikeListService likeListService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        likeListService.updateDB();
    }

}
