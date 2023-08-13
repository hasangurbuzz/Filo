DO
$$
    BEGIN
        BEGIN
            ALTER TABLE T_Group
                ADD COLUMN company_id BIGINT;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column <column_name> already exists in <table_name>.';
        END;
    END;
$$;
