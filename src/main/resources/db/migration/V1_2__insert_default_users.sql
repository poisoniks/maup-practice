-- Insert default roles
INSERT INTO roles (name) VALUES ('USER'), ('MANAGER'), ('ADMIN');

-- Insert default users for testing
-- Passwords are encoded using BCryptPasswordEncoder
-- Passwords are "password" for all users
INSERT INTO users (first_name, last_name, email, password, enabled) VALUES ('User1', 'Test1', 'user1@test.com', '$2a$10$QKygfHX.v.AqguulnGdEyuoeKEWOkThpCXg6PpkIIp9zBtSG5Ut0O', '1');
INSERT INTO users (first_name, last_name, email, password, enabled) VALUES ('User2', 'Test2', 'user2@test.com', '$2a$10$NBFH.L6a9gCxpQk1QfaBvu184Um34axVK7IfzQSKqLWqHdoDTkfby', '1');
INSERT INTO users (first_name, last_name, email, password, enabled) VALUES ('User3', 'Test3', 'user3@test.com', '$2a$10$Y.8VSVgstr7z.ZHs2RWwUOCHzilb0NFsGzjT/iSfoP6ssB.kTHsGa', '1');
INSERT INTO users (first_name, last_name, email, password, enabled) VALUES ('Manager1', 'Test1', 'manager1@test.com', '$2a$10$ebYZhlCOuEmBpMeL8J3FueKSOEUNxbvcjK4w8DlIcqGT5zg5l8U.6', '1');
INSERT INTO users (first_name, last_name, email, password, enabled) VALUES ('Manager2', 'Test2', 'manager2@test.com', '$2a$10$ko43B8A9yYYmvo9cJTSffOleM5NFHJBOmfGbSQMBXo4xJgU6UhWgq', '1');
INSERT INTO users (first_name, last_name, email, password, enabled) VALUES ('Manager3', 'Test3', 'manager3@test.com', '$2a$10$95wD1xdRMeZHBxAhfdScjeM8oPnd/tu9tMenyugNwd6iLPt/N06M2', '1');
INSERT INTO users (first_name, last_name, email, password, enabled) VALUES ('admin', 'admin', 'admin@test.com', '$2a$10$S9YVeXQy7hW61TcPa1iqj..piht4vYqfdBXPcbp.oD38aUUK6QXhO', '1');

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (4, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (5, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (6, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (7, 3);
