package org.gsh.genidxpage.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.BulkRequestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class WebArchiveSchedulerTest {

    static final List<String> IGNORE_REQUEST_INPUT = Collections.emptyList();

    @DisplayName("여러 요청을 한 번에 보낸다")
    @Test
    public void schedule_to_send_multiple_requests() {
        BulkRequestSender sender = mock(BulkRequestSender.class);
        ArchivePageService service = mock(ArchivePageService.class);

        when(sender.prepareInput()).thenReturn(IGNORE_REQUEST_INPUT);
        when(sender.sendAll(any(), any(ArchivePageService.class))).thenReturn(List.of("l1", "l2", "l3"));

        WebArchiveScheduler scheduler = new WebArchiveScheduler(sender, service);

        scheduler.doSend();

        verify(sender).sendAll(any(), any(ArchivePageService.class));
    }

}
