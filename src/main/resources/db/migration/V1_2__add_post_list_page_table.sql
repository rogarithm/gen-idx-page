CREATE TABLE post_list_page
(
    id         BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    year       VARCHAR(255),
    month      VARCHAR(255),
    url        VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
);
