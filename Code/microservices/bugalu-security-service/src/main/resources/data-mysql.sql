INSERT INTO users (email, username, password, enabled, role) VALUES
--- admin@g00glen00b.be:password
  ('admin@g00glen00b.be', 'g00glen00b', '$2a$10$Fg.pwGKNEk8TtRq3C86DEeIo6CnUI05umcVQuvRh2DdwEKJUPtsJK', 1, 'admin');

INSERT INTO role (email, role) VALUES
  ('admin@g00glen00b.be', 'admin'),
  ('admin@g00glen00b.be', 'user');