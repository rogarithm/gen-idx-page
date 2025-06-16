-- 기존 post_list_page 테이블의 데이터가 새로 추가한 post_group_type에 연결되도록 업데이트한다
ALTER TABLE post_list_page
    ADD COLUMN post_group_type_id BIGINT;
ALTER TABLE post_list_page
    ADD COLUMN group_key VARCHAR(255);

UPDATE post_list_page
SET post_group_type_id = (SELECT id
                          FROM post_group_type pgt
                          WHERE pgt.group_type = 'year_month'),
    group_key    = CONCAT(year, '/', month);

ALTER TABLE post_list_page
    MODIFY post_group_type_id BIGINT NOT NULL;
ALTER TABLE post_list_page
    MODIFY group_key VARCHAR(255) NOT NULL;

ALTER TABLE post_list_page
    ADD FOREIGN KEY (post_group_type_id) REFERENCES post_group_type (id);
