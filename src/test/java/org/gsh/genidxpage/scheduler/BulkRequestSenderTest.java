package org.gsh.genidxpage.scheduler;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.exception.FailToReadRequestInputFileException;
import org.gsh.genidxpage.service.ArchivePageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

class BulkRequestSenderTest {

    private static final String IGNORE_INPUT_PATH = "ignore";

    @DisplayName("한 번에 여러 요청을 보낼 수 있다")
    @Test
    public void send_multiple_requests() {
        List<BulkRequestSender> bulkRequestSenders = List.of(
            new YearMonthBulkRequestSender(IGNORE_INPUT_PATH),
            new CategoryBulkRequestSender(IGNORE_INPUT_PATH)
        );
        List<List<String>> requestGroups = List.of(
            List.of("2021/03", "2020/05"),
            List.of("Domain-Driven Design", "Software Design")
        );

        ArchivePageService sender = mock(ArchivePageService.class);

        when(sender.findBlogPageLink(any())).thenReturn("link");

        IntStream.range(0, bulkRequestSenders.size())
            .forEach(idx -> {
                BulkRequestSender bulkRequestSender = bulkRequestSenders.get(idx);
                List<String> requestGroup = requestGroups.get(idx);
                bulkRequestSender.sendAll(requestGroup, sender);
            });

        Integer requestTotalCnt = requestGroups.stream()
            .map(List::size)
            .reduce(0, (acc, reqGroupSize) -> acc + reqGroupSize);
        verify(sender, times(requestTotalCnt)).findBlogPageLink(any());
    }

    @DisplayName("요청 입력 파일을 읽는 데 실패했을 경우를 처리할 수 있다")
    @Test
    public void handle_fail_to_read_request_input_file() {
        List<BulkRequestSender> bulkRequestSenders = List.of(
            new YearMonthBulkRequestSender(IGNORE_INPUT_PATH),
            new CategoryBulkRequestSender(IGNORE_INPUT_PATH)
        );

        for (BulkRequestSender bulkRequestSender : bulkRequestSenders) {
            assertThrows(
                FailToReadRequestInputFileException.class,
                bulkRequestSender::prepareInput
            );
        }
    }
}
