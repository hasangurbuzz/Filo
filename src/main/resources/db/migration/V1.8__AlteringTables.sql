DO
$$
    BEGIN
        BEGIN
            ALTER TABLE T_Vehicle
                ADD COLUMN group_id BIGINT REFERENCES T_Group (id);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column <column_name> already exists in <table_name>.';
        END;
    END;
$$;
