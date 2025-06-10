package org.gsh.genidxpage.service;

import org.gsh.genidxpage.web.response.PostLinkInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// LooseCoupling 룰을 맞추려고 Elements 대신 List<Element>를 쓰면
// Elements에서 구현한 메서드를 쓸 수 없다
@SuppressWarnings("PMD.LooseCoupling")
@Component
public class WebPageParser {

    String parse(String stringDoc) {
        List<PostLinkInfo> postLinks = findPostLinks(stringDoc);
        return buildPageLinks(postLinks);
    }

    List<PostLinkInfo> findPostLinks(String stringDoc) {
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

    String buildPageLinks(List<PostLinkInfo> postLinks) {
        StringBuilder result = new StringBuilder();
        for (PostLinkInfo postLink : postLinks) {
            result.append(postLink.buildPageLink());
            result.append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

}
