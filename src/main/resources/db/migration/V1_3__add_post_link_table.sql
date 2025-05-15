CREATE TABLE post
(
    id             BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    parent_page_id BIGINT             NOT NULL,
    raw_html       TEXT               NOT NULL,
    created_at     DATETIME,
    updated_at     DATETIME,
    deleted_at     DATETIME
);

ALTER TABLE post
    ADD FOREIGN KEY (parent_page_id) REFERENCES post_list_page (id);
