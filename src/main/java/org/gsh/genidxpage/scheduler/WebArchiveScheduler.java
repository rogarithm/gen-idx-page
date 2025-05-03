package org.gsh.genidxpage.scheduler;

import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.BulkRequestSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebArchiveScheduler {

    private final BulkRequestSender bulkRequestSender;
    private final ArchivePageService archivePageService;

    public WebArchiveScheduler(
        BulkRequestSender bulkRequestSender,
        ArchivePageService archivePageService
    ) {
        this.bulkRequestSender = bulkRequestSender;
        this.archivePageService = archivePageService;
    }

    public void scheduleSend() {
        doSend();
    }

    public void doSend() {
        List<String> yearMonths = bulkRequestSender.prepareInput();
        bulkRequestSender.sendAll(yearMonths, archivePageService);
    }
}
