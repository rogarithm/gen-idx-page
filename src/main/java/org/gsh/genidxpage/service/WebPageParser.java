package org.gsh.genidxpage.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebPageParser {

    public List<PostLinkInfo> findPostLinks(String stringDoc) {
        Document doc = Jsoup.parse(stringDoc);
        Elements postLinks = doc.select(".POST_BODY > a");

        List<PostLinkInfo> result = new ArrayList<>();
        for (Element postLink : postLinks) {
            result.add(new PostLinkInfo(
                postLink.attribute("href").getValue(),
                postLink.text()
            ));
        }

        return Collections.unmodifiableList(result);
    }

    public class PostLinkInfo {

        private final String baseUrl = "https://web.archive.org";
        private String pageUrl;
        private String pageTitle;

        public PostLinkInfo(String pageUrl, String pageTitle) {
            this.pageUrl = pageUrl;
            this.pageTitle = pageTitle;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public String buildPageLink() {
            return String.format("<a href=\"%s%s\">%s</a>", baseUrl, pageUrl, pageTitle);
        }
    }
}
