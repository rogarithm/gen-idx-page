package org.gsh.genidxpage.service;

import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BulkRequestSender {

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
