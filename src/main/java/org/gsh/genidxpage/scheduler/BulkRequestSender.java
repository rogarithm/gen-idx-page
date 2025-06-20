package org.gsh.genidxpage.scheduler;

import org.gsh.genidxpage.service.ArchivePageService;

import java.util.List;

public interface BulkRequestSender {

    List<String> prepareInput();

    void sendAll(List<String> requestInputs, ArchivePageService sender);
}
