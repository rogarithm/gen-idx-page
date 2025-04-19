DROP TABLE IF EXISTS page_url;

CREATE TABLE `page_url`
(
    `id`                bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `year_month`        varchar(255),
    `url_fetch_success` varchar(255),
    `created_at`        datetime,
    `updated_at`        datetime,
    `deleted_at`        datetime
);
