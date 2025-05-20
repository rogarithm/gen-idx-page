package org.gsh.genidxpage.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.gsh.genidxpage.entity.ArchiveStatus;
import org.gsh.genidxpage.entity.ArchiveStatusBuilder;
import org.gsh.genidxpage.entity.Post;
import org.gsh.genidxpage.entity.PostListPage;
import org.gsh.genidxpage.entity.PostListPageBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class PersistenceTest {

    @Autowired
    private ArchiveStatusMapper statusMapper;
    @Autowired
    private PostListPageMapper postListPageMapper;
    @Autowired
    private PostMapper postMapper;

    @Test
    public void check_archive_status_mapping() {
        ArchiveStatus report = ArchiveStatusBuilder.builder()
            .withYearMonth("2021", "03")
            .thatExists()
            .buildAsNew();
        statusMapper.insert(report);

        ArchiveStatus loadedEntity = statusMapper.selectByYearMonth("2021", "03");

        assertThat(loadedEntity).isNotNull();
        assertThat(loadedEntity.getYear()).isEqualTo("2021");
        assertThat(loadedEntity.getPageExists()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void check_post_list_page_mapping() {
        PostListPage postListPage = PostListPageBuilder.builder()
            .withYearMonth("2021", "3")
            .withUrl("http://localhost:8080/web/20230614220926/archives/2021/03")
            .buildAsNew();
        postListPageMapper.insert(postListPage);

        PostListPage loadedEntity = postListPageMapper.selectByYearMonth("2021", "3");

        assertThat(loadedEntity).isNotNull();
        assertThat(loadedEntity.getId()).isNotNull();
        assertThat(loadedEntity.getYear()).isEqualTo("2021");
        assertThat(loadedEntity.getUrl()).isEqualTo(
            "http://localhost:8080/web/20230614220926/archives/2021/03");
    }

    @Test
    public void check_post_mapping() {
        // fk 정합성을 위해 postListPage를 insert한다
        PostListPage postListPage = PostListPageBuilder.builder()
            .withYearMonth("2021", "3")
            .withUrl("http://localhost:8080/web/20230614220926/archives/2021/03")
            .buildAsNew();
        postListPageMapper.insert(postListPage);

        String rawHtml = "<html>hello</html>";
        Long parentPageId = postListPageMapper.selectByYearMonth("2021", "3").getId();
        postMapper.insert(Post.createFrom(rawHtml, parentPageId));

        Post loadedEntity = postMapper.selectByParentPageId(parentPageId);
        assertThat(loadedEntity).isNotNull();
        assertThat(loadedEntity.getRawHtml()).isEqualTo(rawHtml);
        assertThat(loadedEntity.getParentPageId()).isEqualTo(parentPageId);
    }
}
