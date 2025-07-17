package org.gsh.genidxpage;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void respondBlogPostListInGivenGroupKey(String groupKey,
        boolean hasManyPost) {
        instance.stubFor(get(urlPathTemplate("/web/20230614220926/archives"))
            .withQueryParam("groupKey", matching(groupKey))
            .willReturn(aResponse().withStatus(200)
                .withBody(
                    buildPostListPage(groupKey, hasManyPost)
                ).withHeader("Content-Type", "text/html; charset=utf-8")
            )
        );
    }

    private String buildPostListPage(String groupKey, boolean hasManyPost) {
        String stringPath;
        if (hasManyPost) {
            stringPath = "src/test/resources/year-month-multiple-post-list-page.html";
        } else {
            stringPath = "src/test/resources/year-month-one-post-list-page.html";
        }

        try {
            Path path = Paths.get(stringPath);
            String fileContent = Files.readString(path, StandardCharsets.UTF_8);
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void respondItHasArchivedPageFor(String groupKey) {
        instance.stubFor(get(urlPathTemplate("/wayback/available"))
            .withQueryParam("url", matching(String.format("http[s]?://agile.egloos.com/archives/%s", groupKey)))
            .withQueryParam("timestamp", matching("[0-9]{8}"))
            .willReturn(aResponse().withStatus(200).withBody(
                    String.format("""
                        {
                          "url": "agile.egloos.com/archives/%s",
                          "archived_snapshots": {
                            "closest": {
                              "status": "200",
                              "available": true,
                              "url": "http://localhost:8080/web/20230614220926/archives?groupKey=%s",
                              "timestamp": "20230614220926"
                            }
                          },
                          "timestamp": "20240101"
                        }
                        """, groupKey, groupKey)
                )
            )
        );
    }

    public void respondItHasArchivedPage() {
        respondItHasArchivedPageFor("2021/03");
    }

    public void respondItHasNoArchivedPageFor(String groupKey) {
        instance.stubFor(get(urlPathTemplate("/wayback/available"))
            .withQueryParam("url", matching(
                String.format("http[s]?://agile.egloos.com/archives/%s",
                    groupKey
                )
            ))
            .withQueryParam("timestamp", matching("[0-9]{8}"))
            .willReturn(aResponse().withStatus(200).withBody(
                String.format("""
                    {
                      "url": "agile.egloos.com/archives/%s",
                      "archived_snapshots": {}
                    }
                    """, groupKey)
                )
            )
        );
    }

    public void respondItHasNoArchivedPage() {
        respondItHasNoArchivedPageFor("1999/07");
    }

    public void hasReceivedMultipleRequests(int requestCount) {
        hasReceivedMultipleAccessUrlRequests(requestCount);
        hasReceivedMultiplePostListPageRequests(requestCount);
    }

    public void hasReceivedMultipleRequests(int accessUrlReqCnt, int listPageReqCnt) {
        hasReceivedMultipleAccessUrlRequests(accessUrlReqCnt);
        hasReceivedMultiplePostListPageRequests(listPageReqCnt);
    }

    public void hasReceivedMultipleAccessUrlRequests(int requestCount) {
        instance.verify(requestCount, getRequestedFor(urlPathTemplate("/wayback/available"))
            .withQueryParam("url",
                matching("http[s]?://agile.egloos.com/archives/[12][0-9]{3}/[01][0-9]"))
            .withQueryParam("timestamp", matching("[0-9]{8}"))
        );
    }

    public void hasReceivedMultiplePostListPageRequests(int requestCount) {
        instance.verify(requestCount,
            getRequestedFor(urlPathTemplate("/web/{timestamp}/archives"))
                .withQueryParam("groupKey", matching("[12][0-9]{3}/[01][0-9]"))
                .withPathParam("timestamp", matching("[0-9]{14}"))
        );
    }
}
