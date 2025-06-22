package org.gsh.genidxpage.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.IndexPageGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class WebArchiveJobTest {

    static final List<String> IGNORE_REQUEST_INPUT = Collections.emptyList();

    @DisplayName("여러 요청을 한 번에 보낸다")
    @Test
    public void schedule_to_send_multiple_requests() {
        BulkRequestSender sender = mock(BulkRequestSender.class);
        ArchivePageService service = mock(ArchivePageService.class);

        when(sender.prepareInput()).thenReturn(IGNORE_REQUEST_INPUT);
        doNothing().when(sender).sendAll(any(), any(ArchivePageService.class));

        WebArchiveJob archiveJob = new WebArchiveJob(sender, service, null);

        archiveJob.doSend();

        verify(sender).sendAll(any(), any(ArchivePageService.class));
    }

    @DisplayName("요청 결과로 인덱스 파일을 만든다")
    @Test
    public void generate_index_file_with_response() {
        IndexPageGenerator generator = mock(IndexPageGenerator.class);
        List<String> pageLinksList = List.of("l1", "l2", "l3");

        doNothing().when(generator).generateIndexPage(any());

        WebArchiveJob scheduler = new WebArchiveJob(null, null, generator);

        scheduler.doGenerate(pageLinksList);

        verify(generator).generateIndexPage(any());
    }

    @DisplayName("db에서 인덱스 파일 생성에 쓸 블로그 링크 html을 가져온다")
    @Test
    public void read_index_content_from_db() {
        IndexPageGenerator generator = mock(IndexPageGenerator.class);

        WebArchiveJob scheduler = new WebArchiveJob(
            mock(BulkRequestSender.class),
            mock(ArchivePageService.class),
            generator
        );

        scheduler.readIndexContent();

        verify(generator).readIndexContent();
    }

    @DisplayName("실패한 요청에 대해 재시도한다")
    @Test
    public void retry_failed_requests() {
        BulkRequestSender sender = mock(BulkRequestSender.class);
        ArchivePageService service = mock(ArchivePageService.class);

        doNothing().when(sender).sendAll(any(), any(ArchivePageService.class));

        WebArchiveJob scheduler = new WebArchiveJob(
            sender, service, null
        );

        scheduler.doRetry();

        verify(service, atLeast(1)).findFailedRequests();
        verify(sender).sendAll(any(), any(ArchivePageService.class));
    }

    @DisplayName("재시도 이후 실패/성공한 요청 정보를 알아낼 수 있다")
    @Test
    public void get_success_fail_info_after_retry() {
        List<String> sendInfo = List.of("2020/01", "2020/02", "2020/03", "2020/04");
        List<String> failedAfterRetry = List.of("2020/01", "2020/02");
        WebArchiveJob scheduler = new WebArchiveJob(
            null, null, null
        );
        Assertions.assertThat(scheduler.successAfterRetry(sendInfo, failedAfterRetry))
            .isEqualTo(List.of("2020/03", "2020/04"));
    }
}
