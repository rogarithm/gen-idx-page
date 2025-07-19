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
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                    buildPostListPage(hasManyPost) // TODO groupKey에 따라 다른 페이지를 반환하도록 변경해야 한다
                ).withHeader("Content-Type", "text/html; charset=utf-8")
            )
        );
    }

    private String buildPostListPage(boolean hasManyPost) {
        String stringPath;
        if (hasManyPost) {
            stringPath = "src/test/resources/year-month-multiple-post-list-page.html";
        } else {
            stringPath = "src/test/resources/year-month-one-post-list-page.html";
        }

        try {
            Path path = Paths.get(stringPath);
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void respondItHasArchivedPageFor(String groupKey) {
        MatcherInfo mi = MatcherInfo.parse(groupKey);

        instance.stubFor(get(urlPathTemplate("/wayback/available"))
            .withQueryParam("url", matching(String.format(mi.url() + "%s", groupKey)))
            .withQueryParam("timestamp", matching("[0-9]{8}"))
            .willReturn(aResponse().withStatus(200).withBody(
                    String.format("""
                        {
                          "url": "%s%s",
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
                        """, mi.urlNoScheme(), groupKey, groupKey)
                )
            )
        );
    }

    public void respondItHasArchivedPage() {
        respondItHasArchivedPageFor("2021/03");
    }

    public void respondItHasNoArchivedPageFor(String groupKey) {
        MatcherInfo mi = MatcherInfo.parse(groupKey);

        instance.stubFor(get(urlPathTemplate("/wayback/available"))
            .withQueryParam("url", matching(
                String.format(mi.url() + "%s",
                    groupKey
                )
            ))
            .withQueryParam("timestamp", matching("[0-9]{8}"))
            .willReturn(aResponse().withStatus(200).withBody(
                    String.format("""
                        {
                          "url": "%s%s",
                          "archived_snapshots": {}
                        }
                        """, mi.urlNoScheme(), groupKey)
                )
            )
        );
    }

    public void respondItHasNoArchivedPage() {
        respondItHasNoArchivedPageFor("1999/07");
    }

    public void hasReceivedMultipleRequests(List<String> requests) {
        hasReceivedMultipleAccessUrlRequests(requests);
        hasReceivedMultiplePostListPageRequests(requests);
    }

    public void hasReceivedMultipleRequests(List<String> failedRequests,
        List<String> passedRequests) {
        List<String> allRequests = Stream.concat(
            passedRequests.stream(),
            Stream.concat(failedRequests.stream(), failedRequests.stream())
        ).collect(Collectors.toList());

        // 접근 url을 가져오는 요청 중 비정상 응답받은 경우는 재시도한다
        // passRequests + failRequests.size() * 2,
        hasReceivedMultipleAccessUrlRequests(allRequests);
        // 블로그 목록 페이지를 가져오는 요청 중 재시도한 요청은 또 다시 실패한다
        // passRequests
        hasReceivedMultiplePostListPageRequests(passedRequests);
    }

    public void hasReceivedMultipleAccessUrlRequests(List<String> requests) {
        String regex = "[12][0-9]{3}/[01][0-9]";
        Map<Boolean, List<String>> groupByGroupKey =
            requests.stream()
                .collect(Collectors.groupingBy(groupKey -> Pattern.matches(regex, groupKey)));

        for (Map.Entry<Boolean, List<String>> entry : groupByGroupKey.entrySet()) {
            MatcherInfo mi = MatcherInfo.parse(entry.getValue().get(0));

            instance.verify(entry.getValue().size(),
                getRequestedFor(urlPathTemplate("/wayback/available"))
                    .withQueryParam("url",
                        matching(String.format(mi.url() + mi.getPattern())))
                    .withQueryParam("timestamp", matching("[0-9]{8}"))
            );
        }
    }

    public void hasReceivedMultiplePostListPageRequests(List<String> requests) {
        String regex = "[12][0-9]{3}/[01][0-9]";
        Map<Boolean, List<String>> groupByGroupKey =
            requests.stream()
                .collect(Collectors.groupingBy(groupKey -> Pattern.matches(regex, groupKey)));

        for (Map.Entry<Boolean, List<String>> entry : groupByGroupKey.entrySet()) {
            MatcherInfo mi = MatcherInfo.parse(entry.getValue().get(0));

            instance.verify(entry.getValue().size(),
                getRequestedFor(urlPathTemplate("/web/{timestamp}/archives"))
                    .withQueryParam("groupKey", matching(mi.getPattern()))
                    .withPathParam("timestamp", matching("[0-9]{14}"))
            );
        }
    }
}

class MatcherInfo {

    private final String scheme;
    private final String host;
    private final String pattern;

    public MatcherInfo(final String scheme, final String host, String pattern) {
        this.scheme = scheme;
        this.host = host;
        this.pattern = pattern;
    }

    static MatcherInfo parse(String groupKey) {
        if (groupKey.matches("[12][0-9]{3}/[01][0-9]")) {
            return new MatcherInfo("https", "agile.egloos.com/archives/",
                "[12][0-9]{3}/[01][0-9]");
        }
        return new MatcherInfo("https", "aeternum.egloos.com/category/", ".*");
    }

    String url() {
        return scheme + "://" + host;
    }

    String urlNoScheme() {
        return host;
    }

    public String getPattern() {
        return pattern;
    }
}
