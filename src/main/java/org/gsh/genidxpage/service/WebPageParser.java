package org.gsh.genidxpage.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebPageParser {

	public Elements findBy(Document doc, String cssQuery) {
		return doc.select(cssQuery);
	}
}
