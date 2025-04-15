package org.gsh.genidxpage.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebPageParser {

	public Elements findPostLinks(Document doc) {
		return doc.select(".POST_BODY > a");
	}
}
