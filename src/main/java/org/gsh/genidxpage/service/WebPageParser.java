package org.gsh.genidxpage.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebPageParser {

    public PostLinkInfo findPostLinks(String stringDoc) {
        Document doc = Jsoup.parse(stringDoc);
        Elements postLinks = doc.select(".POST_BODY > a");
        return new PostLinkInfo(postLinks.get(0));
    }

    public class PostLinkInfo {

        private final String baseUrl = "https://web.archive.org";
        private String pageUrl;
        private String pageTitle;

        public PostLinkInfo(Element postLink) {
            this.pageUrl = postLink.attribute("href").getValue();
            this.pageTitle = postLink.text();
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
