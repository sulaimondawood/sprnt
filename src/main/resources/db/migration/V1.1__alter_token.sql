-- alter table tokens
--  add column expires_at timestamp(6) not null;

ALTER TABLE tokens
ADD COLUMN expires_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL;