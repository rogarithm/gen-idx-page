package org.gsh.genidxpage.service;

import java.util.List;

public interface IndexPageGenerator {

    void generateIndexPage(List<String> postLinksList);

    List<String> readIndexContent();
}
