package org.gsh.genidxpage.service;

import org.gsh.genidxpage.common.exception.ErrorCode;
import org.gsh.genidxpage.exception.FailToCreateDirectoryException;
import org.gsh.genidxpage.exception.FailToWriteFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Slf4j
@Component
public class IndexPageGenerator {

    private final String inputPath;

    public IndexPageGenerator(@Value("${index-page.path}") final String inputPath) {
        this.inputPath = inputPath;
    }

    // TODO
    //  파일 관련 연산을 별도의 객체로 분리
    //  예외 발생 상황 테스트를 추가
    public void generateIndexPage(List<String> pageLinksList) {
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
            <html>
              <head>
                <meta charset="utf-8">
              </head>
              <body>
            """;
    }

    private String generateFooter() {
        return """
              </body>
            </html>
            """;
    }
}
