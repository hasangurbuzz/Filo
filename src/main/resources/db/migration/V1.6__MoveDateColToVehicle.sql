ALTER TABLE T_VEHICLE_AUTHORITY
    DROP COLUMN CREATION_DATE;

ALTER TABLE T_VEHICLE
    ADD COLUMN CREATION_DATE DATE;