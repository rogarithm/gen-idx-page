package org.gsh.genidxpage;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.config.CustomRestTemplateBuilder;
import org.gsh.genidxpage.dao.WebArchiveReportMapper;
import org.gsh.genidxpage.exception.ArchivedPageNotFoundExceptioin;
import org.gsh.genidxpage.service.ApiCallReporter;
import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.WebArchiveApiCaller;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.gsh.genidxpage.web.ArchivePageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public class AcceptanceTest {

    private ArchivePageController archivePageController;
    @Autowired
    private ApiCallReporter reporter;
    @Autowired
    private WebArchiveReportMapper mapper;

    @Nested
    class ArchivePageControllerTest {
        @BeforeEach
        public void setUp() {
            WebArchiveApiCaller apiCaller = new WebArchiveApiCaller(
                "http://localhost:8080",
                "/wayback/available?url={url}&timestamp={timestamp}",
                CustomRestTemplateBuilder.get()
            );
            ArchivePageService service = new ArchivePageService(apiCaller, reporter);

            archivePageController = new ArchivePageController(service);
        }

        @DisplayName("요청 연월에 등록된 블로그 글이 web archive에 없으면, 리소스가 존재하지 않음을 응답으로 받는다")
        @Test
        public void receive_not_found_msg_when_send_request() {
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            fakeWebArchiveServer.respondItHasNoArchivedPage();

            fakeWebArchiveServer.start();

            // 서버는 web archive server에 아카이브된 블로그 글을 요청한다
            // web archive server는 처리할 수 없음 메시지를 반환한다
            // 서버는 처리할 수 없는 요청임을 예외 처리기를 통해 클라이언트에게 알린다
            assertThrows(
                ArchivedPageNotFoundExceptioin.class, () -> {
                    archivePageController.getBlogPostLinks("1999", "7");
                }
            );

            fakeWebArchiveServer.stop();
        }

        @DisplayName("요청 연월에 등록된 블로그 글이 web archive에 있으면, 해당 월에 올라온 블로그 글에 접근할 수 있는 링크 목록을 응답으로 받는다")
        @Test
        public void receive_post_link_list_when_send_valid_request() {
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            fakeWebArchiveServer.respondItHasArchivedPage();
            fakeWebArchiveServer.respondBlogPostListInGivenYearMonth("2021", "3", false);

            fakeWebArchiveServer.start();

            // 서버는 web archive server에 아카이브된 주어진 연월의 블로그 글 목록 페이지를 요청한다
            ResponseEntity<String> response = archivePageController.getBlogPostLinks("2021", "3");

            // web archive server는 주어진 연월의 블로그 글 목록 페이지를 반환한다
            Assertions.assertThat(response.getBody()).isEqualTo(
                "<a href=\"https://web.archive.org/web/20230614220926/http://agile.egloos.com/5946833\">올해 첫 AC2 과정 40기가 곧 열립니다</a>"
            );
            Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

            fakeWebArchiveServer.stop();
        }

        @DisplayName("요청 연월에 등록된 블로그 글이 여러 개면, 해당 블로그 글에 접근할 수 있는 링크 목록을 응답으로 받는다")
        @Test
        public void receive_post_link_list_when_multiple_posts_exists() {
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            fakeWebArchiveServer.respondItHasArchivedPage();
            fakeWebArchiveServer.respondBlogPostListInGivenYearMonth("2021", "3", true);

            fakeWebArchiveServer.start();

            // 서버는 web archive server에 아카이브된 주어진 연월의 블로그 글 목록 페이지를 요청한다
            ResponseEntity<String> response = archivePageController.getBlogPostLinks("2021", "3");

            // web archive server는 주어진 연월의 블로그 글 목록 페이지를 반환한다
            Assertions.assertThat(response.getBody()).isEqualTo(
                "<a href=\"https://web.archive.org/web/20230614220926/http://agile.egloos.com/5946833\">올해 첫 AC2 과정 40기가 곧 열립니다</a>\n"
                    + "<a href=\"https://web.archive.org/web/20230614124528/http://agile.egloos.com/5932600\">AC2 온라인 과정 : 마인크래프트로 함께 자라기를 배운다</a>\n"
                    + "<a href=\"https://web.archive.org/web/20230614124528/http://agile.egloos.com/5931859\">혹독한 조언이 나를 살릴까?</a>"
            );
            Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

            fakeWebArchiveServer.stop();
        }

        @DisplayName("요청 처리 성공 여부를 DB에 기록한다")
        @Test
        public void write_request_result_to_db_when_send_request() {
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            fakeWebArchiveServer.respondItHasArchivedPage();
            fakeWebArchiveServer.respondBlogPostListInGivenYearMonth("2021", "3", false);

            fakeWebArchiveServer.start();

            // 서버는 web archive server에 아카이브된 블로그 글을 요청한다
            archivePageController.getBlogPostLinks("2021", "3");

            // 서버는 db에 요청 성공을 기록한다
            Assertions.assertThat(reporter.hasArchivedPage(new CheckPostArchivedDto("2021", "3")))
                .isTrue();

            fakeWebArchiveServer.stop();
        }
    }

    }
}
