package org.gsh.genidxpage;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Body;

public class FakeWebArchiveServer {

    WireMockServer instance;

    public FakeWebArchiveServer() {
        this.instance = new WireMockServer(wireMockConfig().port(8080));
    }

    public void start() {
        this.instance.start();
    }

    public void stop() {
        this.instance.stop();
    }

    public void respondNotFoundForRequestWithNoResource() {
        instance.stubFor(get(urlPathTemplate("/posts/{year}/{month}"))
            .withPathParam("year", equalTo("1999"))
            .withPathParam("month", equalTo("7"))
            .willReturn(aResponse().withStatus(500).withResponseBody(
                Body.fromOneOf(null, "resource not found", null, null)
            )));
    }

    public void respondBlogPostListInGivenYearMonth(String year, String month) {
        instance.stubFor(get(urlPathTemplate("/post-lists/{year}/{month}"))
            .withPathParam("year", equalTo(year))
            .withPathParam("month", equalTo(month))
            .willReturn(aResponse().withStatus(200)
                .withBody(
                    """
                        <html>
                        <table border="0" cellpadding="0" cellspacing="0" align="CENTER" width="100%">
                          <tr><td valign="TOP" width="90%">
                            <div id="LEFT">
                              <!-- egloos content start -->
                              <div class="POST">
                                <div class="POST_HEAD">
                                  <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                    <tr><td width="80%"><div class="POST_TTL">2021년 03월 전체 글 목록</div></td>
                                      <td width="20%" align="RIGHT"></td></tr>
                                  </table>
                                </div>
                                <div class="POST_BODY">
                                  <span style="font-size: 90%; color: #9b9b9b;" class="archivedate">2021/03/22</span> &nbsp; <a href="/web/20230614220926/http://agile.egloos.com/5946833">올해 첫 AC2 과정 40기가 곧 열립니다</a> <span style="font-size: 8pt; color: #9b9b9b;" class="archivedate">[3]</span><br/>
                                  <div style="margin-top:10px;"><a href="/web/20230614220926/http://agile.egloos.com/archives/2021/03/page/1" title="전체보기">"2021년03월" 의 글 내용 전체 보기</a></div>
                                </div>
                                <div class="POST_TAIL"></div>

                              </div><!-- egloos content end -->
                              <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr><td align="RIGHT" width="48%">&lt; 이전페이지</td><td width="4%"></td>
                                  <td align="LEFT" width="48%">다음페이지 &gt;</td></tr>
                              </table>
                              <br><br>
                            </div>
                          </td>
                        </table>
                        </html>
                        """
                )
            )
        );
    }

    public void respondItHasArchivedPage() {
        instance.stubFor(get(urlPathTemplate("/wayback/available"))
            .withQueryParam("url", equalTo("agile.egloos.com/archives/2021/3"))
            .withQueryParam("timestamp", equalTo("20240101"))
            .willReturn(aResponse().withStatus(200).withBody(
                    """
                        {
                          "url": "agile.egloos.com/archives/2021/03",
                          "archived_snapshots": {
                            "closest": {
                              "status": "200",
                              "available": true,
                              "url": "http://web.archive.org/web/20230614220926/http://agile.egloos.com/archives/2021/03",
                              "timestamp": "20230614220926"
                            }
                          },
                          "timestamp": "20240101"
                        }
                        """
                )
            )
        );
    }

    public void respondItHasNoArchivedPage() {
        instance.stubFor(get(urlPathTemplate("/wayback/available"))
            .withQueryParam("url", equalTo("agile.egloos.com/archives/2021/3"))
            .withQueryParam("timestamp", equalTo("20240101"))
            .willReturn(aResponse().withStatus(200).withBody(
                """
                    {
                      "url": "agile.egloos.com/archives/2021/03",
                      "archived_snapshots": {}
                    }
                    """
                )
            )
        );
    }
}
