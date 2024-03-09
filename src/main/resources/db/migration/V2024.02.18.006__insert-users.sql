INSERT INTO access.users
(user_id,
 username,
 password,
 enabled,
 nickname,
 birthday,
 gender_id,
 created_date,
 updated_date,
 role
)
VALUES ('2D1EBC5B7D2741979CF0E84451C5AAA1', 'admin@admin.com', '$2a$10$wyo01qo4mW5TMbr9nFO3tOC8CkX2XhbRjPAlKa5crQJ87AxgMbgJu', true, 'Admin','2024-01-01', 1,'2024-01-01', '2024-01-01', 'ROLE_ADMIN'),
       ('2D1EBC5B7D2741979CF0E84451C5AAA2', 'info@dar3.eu', '$2a$10$exj7O8P4WzCc4CN2TpFuOuRP4QpnTcX8z0NL0JJ8E31.UTBPKpsIO', true, 'Info','2023-01-01', 2,'2023-01-01', '2023-01-01', 'ROLE_USER'),
       ('2D1EBC5B7D2741979CF0E84451C5AAA3', 'test@test.com', '$2a$10$1eHdmWBfEBDULxVC3VmbW.QGBioyVufeShWXDuHJlzpDnZ7FmD.Le', true, 'Test','2024-01-01', 1,'2024-01-01', '2024-01-01', 'ROLE_USER');