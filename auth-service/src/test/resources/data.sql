INSERT INTO roles (id, name) VALUES (1, 'ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'USER');

INSERT INTO `user` (id, email, password, first_name, last_name) VALUES (1, 'user@email.com', '$2a$10$s9Sf9PpU0q2Khqw.z9CdW.bpHO1lGIHMe23kba.sinjBOj9JGz0HK', 'firstName', 'lastName');

INSERT INTO `user_roles` (user_id, role_id) VALUES (1, 2);