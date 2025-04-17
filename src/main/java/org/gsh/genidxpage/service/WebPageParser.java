package org.gsh.genidxpage.service;

import org.gsh.genidxpage.web.response.PostLinkInfo;
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

    public String buildPageLinks(List<PostLinkInfo> postLinks) {
        StringBuilder result = new StringBuilder();
        for (PostLinkInfo postLink : postLinks) {
            result.append(postLink.buildPageLink());
            result.append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

}
