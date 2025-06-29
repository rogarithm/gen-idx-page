-- 처리 불가능한 요청일 경우임을 나타내는 post_group_type을 추가한다
INSERT INTO post_group_type (group_type, created_at)
VALUES ('unsupported', CURRENT_DATE())
