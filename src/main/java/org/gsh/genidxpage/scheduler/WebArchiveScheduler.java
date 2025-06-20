package org.gsh.genidxpage.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Component
public class WebArchiveScheduler {

    private final List<WebArchiveJob> webArchiveJobs;

    public WebArchiveScheduler(List<WebArchiveJob> webArchiveJobs) {
        this.webArchiveJobs = webArchiveJobs;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void scheduleSend() {
        for (WebArchiveJob webArchiveJob : webArchiveJobs) {
            webArchiveJob.execute();
        }
    }
}
