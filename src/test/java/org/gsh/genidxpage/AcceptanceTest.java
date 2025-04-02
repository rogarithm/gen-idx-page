package org.gsh.genidxpage;

import com.github.tomakehurst.wiremock.http.Body;
import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.service.WebArchiveApiCaller;
import org.gsh.genidxpage.web.ArchivePageController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class AcceptanceTest {

    @DisplayName("요청 연월에 등록된 블로그 글이 web archive에 없으면, 리소스가 존재하지 않음을 응답으로 받는다")
    @Test
    public void receive_not_found_msg_when_send_request() {
        ArchivePageController archivePageController = new ArchivePageController(
                new WebArchiveApiCaller("http://localhost:8080")
        );

        FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

        fakeWebArchiveServer.instance.stubFor(get(urlPathTemplate("/posts/{year}/{month}"))
                .withPathParam("year", equalTo("1999"))
                .withPathParam("month", equalTo("7"))
                .willReturn(aResponse().withStatus(500).withResponseBody(
                        Body.fromOneOf(null, "resource not found", null, null)
                )));

        fakeWebArchiveServer.start();

        // 서버는 web archive server에 아카이브된 블로그 글을 요청한다
        ResponseEntity<String> response = archivePageController.getBlogPost("1999", "7");

        // web archive server는 처리할 수 없음 메시지를 반환한다
        // 서버는 처리할 수 없는 요청임을 클라이언트에게 알린다
        Assertions.assertThat(response.getBody()).isEqualTo("resource not found");
        Assertions.assertThat(response.getStatusCode().is5xxServerError()).isTrue();

        fakeWebArchiveServer.stop();
    }
}
