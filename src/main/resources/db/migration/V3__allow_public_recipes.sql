-- Permitir recetas públicas sin propietario
ALTER TABLE recipes
ALTER COLUMN user_id DROP NOT NULL;

CREATE EXTENSION IF NOT EXISTS unaccent;