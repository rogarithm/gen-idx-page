package org.gsh.genidxpage.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.IndexPageGenerator;
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
        doNothing().when(sender).sendAll(any(), any(ArchivePageService.class));

        WebArchiveScheduler scheduler = new WebArchiveScheduler(sender, service, null);

        scheduler.doSend();

        verify(sender).sendAll(any(), any(ArchivePageService.class));
    }

    @DisplayName("요청 결과로 인덱스 파일을 만든다")
    @Test
    public void generate_index_file_with_response() {
        IndexPageGenerator generator = mock(IndexPageGenerator.class);
        List<String> pageLinksList = List.of("l1", "l2", "l3");

        doNothing().when(generator).generateIndexPage(any());

        WebArchiveScheduler scheduler = new WebArchiveScheduler(null, null, generator);

        scheduler.doGenerate(pageLinksList);

        verify(generator).generateIndexPage(any());
    }

    @DisplayName("db에서 인덱스 파일 생성에 쓸 블로그 링크 html을 가져온다")
    @Test
    public void read_index_content_from_db() {
        ArchivePageService service = mock(ArchivePageService.class);

        WebArchiveScheduler scheduler = new WebArchiveScheduler(
            mock(BulkRequestSender.class),
            service,
            null
        );

        scheduler.readIndexContent();

        verify(service).readIndexContent();
    }

    @DisplayName("실패한 요청에 대해 재시도한다")
    @Test
    public void retry_failed_requests() {
        BulkRequestSender sender = mock(BulkRequestSender.class);
        ArchivePageService service = mock(ArchivePageService.class);

        doNothing().when(sender).sendAll(any(), any(ArchivePageService.class));

        WebArchiveScheduler scheduler = new WebArchiveScheduler(
            sender, service, null
        );

        scheduler.doRetry();

        verify(service).findFailedRequests();
        verify(sender).sendAll(any(), any(ArchivePageService.class));
    }
}
