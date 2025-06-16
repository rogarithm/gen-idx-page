-- 연월 이외 다른 기준으로 블로그 글을 그룹핑하는 블로그도 처리할 수 있도록 블로그 글 그룹핑 기준 테이블을 추가한다
CREATE TABLE post_group_type
(
    id         BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    group_type VARCHAR(255)       NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
);

INSERT INTO post_group_type (group_type, created_at)
VALUES ('year_month', CURRENT_DATE()),
       ('category', CURRENT_DATE());
