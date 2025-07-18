package org.gsh.genidxpage.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.gsh.genidxpage.entity.ArchiveStatus;
import org.gsh.genidxpage.entity.ArchiveStatusBuilder;
import org.gsh.genidxpage.entity.Post;
import org.gsh.genidxpage.entity.PostListPage;
import org.gsh.genidxpage.entity.PostListPageBuilder;
import org.gsh.genidxpage.vo.IndexContent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class PersistenceTest {

    @Autowired
    private ArchiveStatusMapper statusMapper;
    @Autowired
    private PostListPageMapper postListPageMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private IndexContentMapper indexContentMapper;

    @Test
    public void check_archive_status_mapping() {
        ArchiveStatus report = ArchiveStatusBuilder.builder()
            .withGroupKey("2021/03")
            .thatExists()
            .buildAsNew();
        statusMapper.insert(report);

        ArchiveStatus loadedEntity = statusMapper.selectByGroupKey("2021/03");

        assertThat(loadedEntity).isNotNull();
        assertThat(loadedEntity.getGroupKey()).isEqualTo("2021/03");
        assertThat(loadedEntity.getPageExists()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void check_post_list_page_mapping() {
        PostListPage postListPage = PostListPageBuilder.builder()
            .withGroupKey("2021/03")
            .withUrl("http://localhost:8080/web/20230614220926/archives/2021/03")
            .buildAsNew();
        postListPageMapper.insert(postListPage);

        PostListPage loadedEntity = postListPageMapper.selectByGroupKey("2021/03");

        assertThat(loadedEntity).isNotNull();
        assertThat(loadedEntity.getId()).isNotNull();
        assertThat(loadedEntity.getGroupKey()).isEqualTo("2021/03");
        assertThat(loadedEntity.getUrl()).isEqualTo(
            "http://localhost:8080/web/20230614220926/archives/2021/03");
    }

    @Test
    public void check_post_mapping() {
        // fk 정합성을 위해 postListPage를 insert한다
        PostListPage postListPage = PostListPageBuilder.builder()
            .withGroupKey("2021/03")
            .withUrl("http://localhost:8080/web/20230614220926/archives/2021/03")
            .buildAsNew();
        postListPageMapper.insert(postListPage);

        String rawHtml = "<html>hello</html>";
        Long parentPageId = postListPageMapper.selectByGroupKey("2021/03").getId();
        postMapper.insert(Post.createFrom(rawHtml, parentPageId));

        Post loadedEntity = postMapper.selectByParentPageId(parentPageId);
        assertThat(loadedEntity).isNotNull();
        assertThat(loadedEntity.getRawHtml()).isEqualTo(rawHtml);
        assertThat(loadedEntity.getParentPageId()).isEqualTo(parentPageId);
    }

    @Test
    public void check_index_content_mapping() {
        List<String> yearMonths = List.of("2021/01", "2021/02", "2021/03");

        yearMonths.stream()
            .forEach(yearMonth -> {
                String year = yearMonth.split("/")[0];
                String month = yearMonth.split("/")[1];

                PostListPage postListPage = PostListPageBuilder.builder()
                    .withGroupKey(yearMonth)
                    .withUrl(
                        String.format("http://localhost:8080/web/20230614220926/archives/%s/%s",
                            year, month))
                    .buildAsNew();
                postListPageMapper.insert(postListPage);

                String rawHtml = String.format("<html>url for %s/%s</html>", year, month);
                Long parentPageId = postListPageMapper.selectByGroupKey(yearMonth).getId();
                postMapper.insert(Post.createFrom(rawHtml, parentPageId));
            });

        List<IndexContent> loadedVOs = indexContentMapper.selectAll();
        loadedVOs.stream().forEach(
            loadedVO -> {
                assertThat(loadedVO).isNotNull();
            }
        );
    }
}
