package org.gsh.genidxpage.service;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class IndexPageGenerator {

    private final String inputPath;

    public IndexPageGenerator(@Value("${index-page.path}") final String inputPath) {
        this.inputPath = inputPath;
    }

    public void generateIndexPage(List<String> pageLinksList) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(generateHeader());
        for (String pageLink : pageLinksList) {
            String[] split = pageLink.split("\n");
            for (String link : split) {
                builder.append(link);
                builder.append("<br>");
                builder.append("\n");
            }
        }
        builder.append(generateFooter());

        Path dirPath = Path.of(inputPath);
        Files.createDirectories(dirPath);

        Path filePath = Path.of(inputPath + "/index.html");
        Files.write(filePath, builder.toString().getBytes(), StandardOpenOption.CREATE);
    }

    String generateHeader() {
        return """
  <html>
    <head>
      <meta charset="utf-8">
    </head>
    <body>
  """;
    }

    String generateFooter() {
        return """
                </body>
              </html>
              """;
    }
}
