CREATE TABLE T_Group_Authority
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT,
    group_id BIGINT REFERENCES T_Group (id),
    role     VARCHAR(15)

);

