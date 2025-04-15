package org.gsh.genidxpage;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;

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

    public void respondBlogPostListInGivenYearMonth(String year, String month, boolean hasManyPost) {
        instance.stubFor(get(urlPathTemplate("/post-links/{year}/{month}"))
            .withPathParam("year", equalTo(year))
            .withPathParam("month", equalTo(month))
            .willReturn(aResponse().withStatus(200)
                .withBody(
                    buildPostListPage(hasManyPost)
                )
            )
        );
    }

    private String buildPostListPage(boolean hasManyPost) {
        String postListPageHead = """
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
            """;
        String postListPageTail = """
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
            """;

        String firstPost = """
                <span style="font-size: 90%; color: #9b9b9b;" class="archivedate">2021/03/22</span> &nbsp; <a href="/web/20230614220926/http://agile.egloos.com/5946833">올해 첫 AC2 과정 40기가 곧 열립니다</a> <span style="font-size: 8pt; color: #9b9b9b;" class="archivedate">[3]</span><br/>
            """;
        String otherPosts = """
            <span style="font-size: 90%; color: #9b9b9b;" class="archivedate">2021/03/25</span> &nbsp; <a href="/web/20230614124528/http://agile.egloos.com/5932600">AC2 온라인 과정 : 마인크래프트로 함께 자라기를 배운다</a> <span style="font-size: 8pt; color: #9b9b9b;" class="archivedate"></span><br>
            <span style="font-size: 90%; color: #9b9b9b;" class="archivedate">2021/03/27</span> &nbsp; <a href="/web/20230614124528/http://agile.egloos.com/5931859">혹독한 조언이 나를 살릴까?</a> <span style="font-size: 8pt; color: #9b9b9b;" class="archivedate">[13]</span><br>
            """;
        if (hasManyPost) {
            return postListPageHead + firstPost + otherPosts + postListPageTail;
        }
        return postListPageHead + firstPost + postListPageTail;
    }


    public void respondItHasArchivedPage() {
        instance.stubFor(get(urlPathTemplate("/wayback/available"))
            .withQueryParam("url", equalTo("agile.egloos.com/archives/2021/03"))
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
            .withQueryParam("url", equalTo("agile.egloos.com/archives/1999/07"))
            .withQueryParam("timestamp", equalTo("20240101"))
            .willReturn(aResponse().withStatus(200).withBody(
                    """
                        {
                          "url": "agile.egloos.com/archives/1999/07",
                          "archived_snapshots": {}
                        }
                        """
                )
            )
        );
    }
}
