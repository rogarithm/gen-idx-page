package org.gsh.genidxpage.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.exception.FailToReadRequestInputFileException;
import org.gsh.genidxpage.scheduler.BulkRequestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class BulkRequestSenderTest {

    private static final String IGNORE_INPUT_PATH = "ignore";

    @DisplayName("한 번에 여러 요청을 보낼 수 있다")
    @Test
    public void send_multiple_requests() {
        BulkRequestSender bulkRequestSender = new BulkRequestSender(IGNORE_INPUT_PATH);
        ArchivePageService sender = mock(ArchivePageService.class);

        when(sender.findBlogPageLink(any())).thenReturn("link");

        bulkRequestSender.sendAll(List.of("2021/03", "2020/05"), sender);

        verify(sender, times(2)).findBlogPageLink(any());
    }

    @DisplayName("요청 입력 파일을 읽는 데 실패했을 경우를 처리할 수 있다")
    @Test
    public void handle_fail_to_read_request_input_file() {
        BulkRequestSender bulkRequestSender = new BulkRequestSender(IGNORE_INPUT_PATH);

        assertThrows(
            FailToReadRequestInputFileException.class,
            () -> bulkRequestSender.prepareInput()
        );
    }
}
