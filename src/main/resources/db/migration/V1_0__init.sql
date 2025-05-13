CREATE TABLE page_url_report
(
    id          bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    year        varchar(255),
    month       varchar(255),
    page_exists bool,
    created_at  datetime,
    updated_at  datetime,
    deleted_at  datetime
);
