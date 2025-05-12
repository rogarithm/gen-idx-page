package org.gsh.genidxpage;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.config.CustomRestTemplateBuilder;
import org.gsh.genidxpage.dao.WebArchiveReportMapper;
import org.gsh.genidxpage.scheduler.BulkRequestSender;
import org.gsh.genidxpage.scheduler.WebArchiveScheduler;
import org.gsh.genidxpage.service.AgileStoryArchivePageService;
import org.gsh.genidxpage.service.ApiCallReporter;
import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.IndexPageGenerator;
import org.gsh.genidxpage.service.WebArchiveApiCaller;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.gsh.genidxpage.web.ArchivePageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@TestPropertySource(properties = "app.scheduling.enable=false")
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
            ArchivePageService service = new AgileStoryArchivePageService(apiCaller, reporter);

            archivePageController = new ArchivePageController(service);
        }

        @DisplayName("요청 연월에 등록된 블로그 글이 web archive에 없으면, 빈 응답을 받는다")
        @Test
        public void receive_not_found_msg_when_send_request() {
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            fakeWebArchiveServer.respondItHasNoArchivedPage();

            fakeWebArchiveServer.start();

            // 서버는 web archive server에 아카이브된 블로그 글을 요청한다
            // web archive server는 등록된 페이지가 없음을 알린다
            // 서버는 클라이언트에게 빈 문자열로 응답한다
            Assertions.assertThat(archivePageController.getBlogPostLinks("1999", "7").getBody())
                .isEqualTo("");

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

    private BulkRequestSender bulkRequestSender;
    private AgileStoryArchivePageService service;

    @Nested
    class ArchivePageSchedulingTest {
        @BeforeEach
        public void setUp() {
            bulkRequestSender = new BulkRequestSender("static/year-month-list");
            WebArchiveApiCaller apiCaller = new WebArchiveApiCaller(
                "http://localhost:8080",
                "/wayback/available?url={url}&timestamp={timestamp}",
                CustomRestTemplateBuilder.get()
            );
            service = new AgileStoryArchivePageService(apiCaller, reporter);
        }

        @DisplayName("한 번에 여러 요청을 보낸다")
        @Test
        public void schedule_sending_two_requests_to_web_archive() {
            // 요청할 모든 입력쌍을 만든다
            // 입력쌍의 갯수만큼 요청을 보낸다
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            // 요청 입력값을 파일로부터 읽어온다
            List<String> yearMonths = bulkRequestSender.prepareInput();

            yearMonths.forEach(yearMonth -> {
                String[] pair = yearMonth.split("/");
                String year = pair[0];
                String month = pair[1];
                // 주어진 연월 쌍을 요청받았을 때 FakeWebArchive 서버가 응답할 수 있도록 설정한다
                fakeWebArchiveServer.respondItHasArchivedPageFor(year, month);
                fakeWebArchiveServer.respondBlogPostListInGivenYearMonth(year, month, false);
            });

            fakeWebArchiveServer.start();

            List<String> pageLinksList = bulkRequestSender.sendAll(yearMonths, service);

            pageLinksList.stream().forEach(pageLinks -> {
                // 외부 서버로부터 가져온 모든 결과값은 링크 형식이다
                Assertions.assertThat(pageLinks).matches("<a href=\".*\">.*</a>");
            });

            fakeWebArchiveServer.stop();
        }

        @DisplayName("블로그 글 링크 목록으로 인덱스 파일을 생성한다")
        @Test
        public void generate_index_file_using_blog_post_links() throws IOException {
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            // 요청 입력값을 파일로부터 읽어온다
            List<String> yearMonths = bulkRequestSender.prepareInput();

            // 요청할 모든 입력쌍을 만든다
            yearMonths.forEach(yearMonth -> {
                String[] pair = yearMonth.split("/");
                String year = pair[0];
                String month = pair[1];
                // 주어진 연월 쌍을 요청받았을 때 FakeWebArchive 서버가 응답할 수 있도록 설정한다
                fakeWebArchiveServer.respondItHasArchivedPageFor(year, month);
                fakeWebArchiveServer.respondBlogPostListInGivenYearMonth(year, month, false);
            });

            fakeWebArchiveServer.start();

            // 입력쌍의 갯수만큼 요청을 보낸다
            List<String> pageLinksList = bulkRequestSender.sendAll(yearMonths, service);

            IndexPageGenerator generator = new IndexPageGenerator(
                "/tmp/genidxpage/test"
            );
            generator.generateIndexPage(pageLinksList);

            Assertions.assertThat(
                Files.readString(Path.of("/tmp/genidxpage/test/index.html"), StandardCharsets.UTF_8)
            ).isNotNull();

            fakeWebArchiveServer.stop();
        }

        @DisplayName("설정한 일정에 맞춰 여러 요청을 보낸다")
        @Test
        public void send_scheduled_multiple_requests() {
            // 요청할 모든 입력쌍을 만든다
            // 입력쌍의 갯수만큼 요청을 보낸다
            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            WebArchiveScheduler scheduler = new WebArchiveScheduler(bulkRequestSender, service, null);

            // 요청 입력값을 파일로부터 읽어온다
            List<String> yearMonths = bulkRequestSender.prepareInput();
            yearMonths.forEach(yearMonth -> {
                String[] pair = yearMonth.split("/");
                String year = pair[0];
                String month = pair[1];
                // 주어진 연월 쌍을 요청받았을 때 FakeWebArchive 서버가 응답할 수 있도록 설정한다
                fakeWebArchiveServer.respondItHasArchivedPageFor(year, month);
                fakeWebArchiveServer.respondBlogPostListInGivenYearMonth(year, month, false);
            });

            fakeWebArchiveServer.start();

            scheduler.doSend();
            fakeWebArchiveServer.hasReceivedMultipleRequests(yearMonths.size());

            fakeWebArchiveServer.stop();
        }

        @DisplayName("실패한 요청을 모아서 재시도한다")
        @Test
        public void retry_failed_requests() {

            FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

            WebArchiveScheduler scheduler = new WebArchiveScheduler(bulkRequestSender, service,
                null);

            // 요청할 모든 입력쌍을 만든다
            List<String> yearMonths = bulkRequestSender.prepareInput();
            List<String> passRequests = yearMonths.stream()
                .filter(ym -> ym.split("/")[0].equals("2021"))
                .toList();
            passRequests.forEach(yearMonth -> {
                String[] pair = yearMonth.split("/");
                String year = pair[0];
                String month = pair[1];
                // 주어진 연월 쌍을 요청받았을 때 FakeWebArchive 서버가 정상 응답한다
                fakeWebArchiveServer.respondItHasArchivedPageFor(year, month);
                fakeWebArchiveServer.respondBlogPostListInGivenYearMonth(year, month, false);
            });
            List<String> failRequests = yearMonths.stream()
                .filter(ym -> !ym.split("/")[0].equals("2021"))
                .toList();
            failRequests.forEach(yearMonth -> {
                String[] pair = yearMonth.split("/");
                String year = pair[0];
                String month = pair[1];
                // 주어진 연월 쌍을 요청받았을 때 FakeWebArchive 서버가 비정상 응답한다
                fakeWebArchiveServer.respondItHasNoArchivedPageFor(year, month);
            });

            fakeWebArchiveServer.start();

            scheduler.doSend();
            fakeWebArchiveServer.hasReceivedMultipleRequests(
                passRequests.size() + failRequests.size() * 2 // 비정상 응답받은 경우는 재시도한다
            );

            fakeWebArchiveServer.stop();
        }
    }
}
