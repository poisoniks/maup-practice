ALTER TABLE users ADD COLUMN is_anonymous BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE users MODIFY COLUMN password VARCHAR(100);

INSERT INTO roles (name) VALUES ('ANONYMOUS');
