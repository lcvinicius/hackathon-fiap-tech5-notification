-- Minimal schema for the notification MVP (read-only from the app)

CREATE TABLE IF NOT EXISTS users (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  phone TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_medication_subscription (
  user_id TEXT NOT NULL,
  medicine_id TEXT NOT NULL,
  ubs_id TEXT,
  PRIMARY KEY (user_id, medicine_id, ubs_id)
);

INSERT INTO users (id, name, phone) VALUES
  ('user-1', 'Maria', '+5511999999999'),
  ('user-2', 'Joao',  '+5511888888888')
ON CONFLICT DO NOTHING;

INSERT INTO user_medication_subscription (user_id, medicine_id, ubs_id) VALUES
  ('user-1', 'med-123', 'ubs-456'),
  ('user-2', 'med-123', 'ubs-456')
ON CONFLICT DO NOTHING;
