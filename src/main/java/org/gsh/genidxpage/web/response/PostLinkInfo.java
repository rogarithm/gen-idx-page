package org.gsh.genidxpage.web.response;

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
