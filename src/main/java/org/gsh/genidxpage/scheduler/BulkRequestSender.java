package org.gsh.genidxpage.scheduler;

import org.gsh.genidxpage.common.exception.ErrorCode;
import org.gsh.genidxpage.exception.FailToReadRequestInputFileException;
import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BulkRequestSender {

    private final String inputPath;

    public BulkRequestSender(@Value("${bulk-request.input-path}") final String inputPath) {
        this.inputPath = inputPath;
    }

    public List<String> prepareInput() {
        ClassPathResource classPathResource = new ClassPathResource(inputPath);
        String fileContent = "";

        try {
            Path  path = Paths.get(classPathResource.getURI());
            fileContent = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FailToReadRequestInputFileException(e, ErrorCode.SERVER_FAULT, "fail to read request input file");
        }

        List<String> yearMonths = Arrays.stream(fileContent.strip().split("\n"))
            .collect(ArrayList::new, List::add, List::addAll);
        return yearMonths;
    }

    public List<String> sendAll(List<String> yearMonths, ArchivePageService sender) {
        ArrayList<String> pageLinksList = new ArrayList<>();
        yearMonths.forEach(yearMonth -> {
            // 외부 서버에 요청할 입력 형식으로 파일 내용을 정제한다
            String[] pair = yearMonth.split("/");
            String year = pair[0];
            String month = pair[1];
            CheckPostArchivedDto dto = new CheckPostArchivedDto(year, month);

            String pageLinks = sender.findBlogPageLink(dto);
            pageLinksList.add(pageLinks);
        });

        return pageLinksList;
    }
}
