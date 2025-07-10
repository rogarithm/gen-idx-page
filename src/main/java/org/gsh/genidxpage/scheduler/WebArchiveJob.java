package org.gsh.genidxpage.scheduler;

import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.IndexPageGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WebArchiveJob {

    private final BulkRequestSender bulkRequestSender;
    private final ArchivePageService archivePageService;
    private final IndexPageGenerator indexPageGenerator;

    public WebArchiveJob(
        @Qualifier("yearMonthBulkRequestSender") BulkRequestSender bulkRequestSender,
        @Qualifier("agileStoryArchivePageService") ArchivePageService archivePageService,
        @Qualifier("agileStoryIndexPageGenerator") IndexPageGenerator indexPageGenerator
    ) {
        this.bulkRequestSender = bulkRequestSender;
        this.archivePageService = archivePageService;
        this.indexPageGenerator = indexPageGenerator;
    }

    public void execute() {
        doSend();
        doRetry();
        List<String> pageLinkList = readIndexContent();
        doGenerate(pageLinkList);
    }

    public void doSend() {
        List<String> yearMonths = bulkRequestSender.prepareInput();
        reportSendInfo(yearMonths);
        bulkRequestSender.sendAll(yearMonths, archivePageService);
    }

    public void doRetry() {
        List<String> yearMonths = archivePageService.findFailedRequests();
        reportRetryInfo(yearMonths);
        bulkRequestSender.sendAll(yearMonths, archivePageService);
        reportRetryResult(yearMonths);
    }

    void reportSendInfo(final List<String> sendInfo) {
        log.info(String.format("Sending %s requests", sendInfo.size()));
    }

    void reportRetryInfo(final List<String> retryInfo) {
        log.info(String.format("Retrying for %s failed requests: %s", retryInfo.size(), retryInfo));
    }

    void reportRetryResult(final List<String> sendInfo) {
        List<String> failedAfterRetry = archivePageService.findFailedRequests();
        List<String> successAfterRetry = successAfterRetry(sendInfo, failedAfterRetry);

        log.info(String.format("Failed %s retry requests for: %s", failedAfterRetry.size(),
            failedAfterRetry));
        log.info(
            String.format("Successed %s retry requests for: %s", successAfterRetry.size(),
                successAfterRetry));
    }

    List<String> successAfterRetry(List<String> sendInfo, List<String> failedAfterRetry) {
        List<String> successAfterRetry = new ArrayList<>();
        for (String aSendInfo : sendInfo) {
            if (!failedAfterRetry.contains(aSendInfo)) {
                successAfterRetry.add(aSendInfo);
            }
        }
        return successAfterRetry;
    }

    List<String> readIndexContent() {
        return indexPageGenerator.readIndexContent();
    }

    void doGenerate(List<String> pageLinkList) {
        indexPageGenerator.generateIndexPage(pageLinkList);
    }
}
