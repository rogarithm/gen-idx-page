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
public class YearMonthBulkRequestSender implements BulkRequestSender {

    private final String inputPath;

    public YearMonthBulkRequestSender(@Value("${bulk-request.input-path}") final String inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    public List<String> prepareInput() {
        ClassPathResource classPathResource = new ClassPathResource(inputPath);
        String fileContent = "";

        try {
            Path  path = Paths.get(classPathResource.getURI());
            fileContent = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FailToReadRequestInputFileException(e, ErrorCode.SERVER_FAULT, "fail to read request input file");
        }

        return Arrays.stream(fileContent.strip().split("\n"))
            .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public void sendAll(List<String> yearMonths, ArchivePageService sender) {
        yearMonths.forEach(yearMonth -> {
            CheckPostArchivedDto dto = new CheckPostArchivedDto(yearMonth);

            sender.findBlogPageLink(dto);
        });
    }
}
