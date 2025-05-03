package org.gsh.genidxpage.scheduler;

import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.BulkRequestSender;
import org.gsh.genidxpage.service.IndexPageGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebArchiveScheduler {

    private final BulkRequestSender bulkRequestSender;
    private final ArchivePageService archivePageService;
    private final IndexPageGenerator indexPageGenerator;

    public WebArchiveScheduler(BulkRequestSender bulkRequestSender,
        ArchivePageService archivePageService, IndexPageGenerator indexPageGenerator) {
        this.bulkRequestSender = bulkRequestSender;
        this.archivePageService = archivePageService;
        this.indexPageGenerator = indexPageGenerator;
    }

    public void scheduleSend() {
        doSend();
    }

    public List<String> doSend() {
        List<String> yearMonths = bulkRequestSender.prepareInput();
        return bulkRequestSender.sendAll(yearMonths, archivePageService);
    }

    void doGenerate(List<String> pageLinkList) {
        indexPageGenerator.generateIndexPage(pageLinkList);
    }
}
