package org.gsh.genidxpage.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebPageParser {

    public Elements findPostLinks(String stringDoc) {
        Document doc = Jsoup.parse(stringDoc);
        return doc.select(".POST_BODY > a");
    }
}
