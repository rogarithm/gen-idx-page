package org.gsh.genidxpage.service;

import org.gsh.genidxpage.common.exception.ErrorCode;
import org.gsh.genidxpage.exception.FailToCreateDirectoryException;
import org.gsh.genidxpage.exception.FailToWriteFileException;
import org.gsh.genidxpage.repository.IndexContentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AgileStoryIndexPageGenerator implements IndexPageGenerator {

    private final String inputPath;
    private final IndexContentReader reader;

    public AgileStoryIndexPageGenerator(@Value("${index-page.path}") final String inputPath,
        IndexContentReader reader) {
        this.inputPath = inputPath;
        this.reader = reader;
    }

    // TODO
    //  파일 관련 연산을 별도의 객체로 분리
    //  예외 발생 상황 테스트를 추가
    public void generateIndexPage(List<String> postLinksList) {
        StringBuilder builder = new StringBuilder();
        builder.append(generateHeader());
        for (String postLinks : postLinksList) {
            String postLinksGroupId = postLinks.split(":")[0].trim();
            builder.append(String.format("<div class=\"post-list-id\">%s</div>", postLinksGroupId));

            String postLinksHtml = Arrays.stream(postLinks.split(":")).skip(1)
                .collect(Collectors.joining(":"));
            for (String postLink : postLinksHtml.split("\n")) {
                builder.append(postLink);
                builder.append("<br>");
                builder.append("\n");
            }
        }
        builder.append(generateFooter());

        Path dirPath = Path.of(inputPath);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new FailToCreateDirectoryException(e, ErrorCode.SERVER_FAULT,
                "fail to create directory to store index file");
        }

        Path filePath = Path.of(inputPath + "/index.html");
        try {
            Files.write(filePath, builder.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new FailToWriteFileException(e, ErrorCode.SERVER_FAULT,
                "fail to write index file");
        }
    }

    private String generateHeader() {
        return """
             <!DOCTYPE html>
             <html>
               <head>
                 <meta charset="utf-8">
                 <title>agile story blog index</title>
                 <link rel="stylesheet" href="./index.css" type="text/css" media="screen" />
               </head>
               <body>
                 <div class="site">
            """;
    }

    private String generateFooter() {
        return """
                </div>
              </body>
            </html>
            """;
    }

    public List<String> readIndexContent() {
        return reader.readAllIndexContent();
    }
}
