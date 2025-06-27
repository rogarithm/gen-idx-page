package org.gsh.genidxpage.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.dao.PostListPageMapper;
import org.gsh.genidxpage.entity.PostGroupType;
import org.gsh.genidxpage.entity.PostGroupTypeBuilder;
import org.gsh.genidxpage.entity.PostListPage;
import org.gsh.genidxpage.entity.PostListPageBuilder;
import org.gsh.genidxpage.service.PostGroupTypeResolver;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfoBuilder;
import org.gsh.genidxpage.service.dto.CheckPostArchived;
import org.gsh.genidxpage.service.dto.CheckYearMonthPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostListPageRecorderTest {

    @DisplayName("블로그 목록 페이지의 url을 db에 기록한다")
    @Test
    public void record_post_list_page_url_to_db() {
        PostListPageMapper mapper = mock(PostListPageMapper.class);
        PostGroupTypeResolver resolver = mock(PostGroupTypeResolver.class);
        PostListPageRecorder recorder = new PostListPageRecorder(mapper, resolver);

        CheckPostArchived dto = new CheckYearMonthPostArchivedDto("2021/03");
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();
        PostGroupType postGroupType = PostGroupTypeBuilder.builder()
            .withYearMonthGroupType()
            .buildAsNew();

        when(resolver.resolve(any())).thenReturn(postGroupType);

        recorder.record(dto, archivedPageInfo);

        verify(mapper).insert(any(PostListPage.class));
    }

    @DisplayName("이미 등록된 연월의 url이면 업데이트만 한다")
    @Test
    public void only_update_when_already_inserted_url_of_year_month() {
        PostListPageMapper mapper = mock(PostListPageMapper.class);
        PostGroupTypeResolver resolver = mock(PostGroupTypeResolver.class);
        PostListPageRecorder recorder = new PostListPageRecorder(mapper, resolver);

        PostListPage postListPage = PostListPageBuilder.builder()
            .withGroupKey("2021/03")
            .withUrl("http://localhost:8080/web/20230614220926/archives/2021/03")
            .buildAsNew();
        PostGroupType postGroupType = PostGroupTypeBuilder.builder()
            .withYearMonthGroupType()
            .buildAsNew();

        when(mapper.selectByGroupKey(any())).thenReturn(postListPage);
        when(resolver.resolve(any())).thenReturn(postGroupType);

        CheckPostArchived dto = new CheckYearMonthPostArchivedDto("2021/03");
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();
        recorder.record(dto, archivedPageInfo);

        verify(mapper).update(any(PostListPage.class));
    }
}
