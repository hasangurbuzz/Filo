CREATE TABLE T_Group
(
    ID              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(50),
    parent_group_id BIGINT,
    FOREIGN KEY (parent_group_id) REFERENCES T_Group (ID)
);

CREATE TABLE T_Vehicle
(
    id             BIGSERIAL PRIMARY KEY,
    number_plate   VARCHAR(10) NOT NULL,
    chassis_number VARCHAR(17),
    tag            VARCHAR,
    brand          VARCHAR     NOT NULL,
    model          VARCHAR     NOT NULL,
    model_year     SMALLINT    NOT NULL,
    company_id     BIGINT      NOT NULL,
    is_deleted     BOOLEAN
);

CREATE TABLE T_Vehicle_Authority
(
    ID                    BIGSERIAL PRIMARY KEY,
    user_id               BIGINT,
    authorized_vehicle_id BIGINT,
    FOREIGN KEY (authorized_vehicle_id) REFERENCES T_Vehicle (id)
);


CREATE TYPE USER_ROLE AS ENUM
    (
        'ADMIN', 'COMPANY ADMIN', 'STANDARD'
        );