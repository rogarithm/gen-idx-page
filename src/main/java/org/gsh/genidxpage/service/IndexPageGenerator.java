package org.gsh.genidxpage.service;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class IndexPageGenerator {

    private final String inputPath;

    public IndexPageGenerator(@Value("${index-page.input-path}") final String inputPath) {
        this.inputPath = inputPath;
    }

    public void generateIndexPage(List<String> pageLinksList) {

    }
}
