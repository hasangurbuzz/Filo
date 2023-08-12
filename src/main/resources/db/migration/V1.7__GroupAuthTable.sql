CREATE TABLE T_Group_Vehicle_Authority
(
    ID             BIGSERIAL PRIMARY KEY,
    group_id       BIGINT,
    v_authority_id BIGINT,
    FOREIGN KEY (group_id) REFERENCES T_Group (ID),
    FOREIGN KEY (v_authority_id) REFERENCES T_Vehicle_Authority (ID)
);
