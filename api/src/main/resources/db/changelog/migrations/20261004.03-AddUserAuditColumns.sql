--liquibase formatted sql

--changeset jzhang:AddUserAuditColumns

ALTER TABLE "user"
    ADD COLUMN create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN update_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by_id BIGINT REFERENCES "user" (user_id),
    ADD COLUMN updated_by_id BIGINT REFERENCES "user" (user_id);