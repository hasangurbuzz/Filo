CREATE TABLE Groups(
  ID BIGSERIAL PRIMARY KEY,
  name VARCHAR(50),
  parent_group_id BIGINT,
  FOREIGN KEY (parent_group_id) REFERENCES Groups(ID)
);

create table Vehicles(
  id BIGSERIAL PRIMARY KEY,
  number_plate VARCHAR(10) NOT NULL,
  chassis_number VARCHAR(17),
  label VARCHAR,
  brand VARCHAR not NULL,
  model VARCHAR not NULL,
  model_year SMALLINT not NULL,
  company_id BIGINT NOT NULL,
  group_id BIGINT,
  FOREIGN KEY(group_id) REFERENCES GROUPS(ID)
);

CREATE TABLE Vehicle_Authorities(
  ID BIGSERIAL PRIMARY KEY,
  user_id BIGINT,
  authorized_vehicle_id BIGINT, 
  FOREIGN KEY(authorized_vehicle_id) REFERENCES vehicles(id)
);


  