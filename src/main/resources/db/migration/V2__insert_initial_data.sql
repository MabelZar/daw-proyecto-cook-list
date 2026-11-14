
-- ROLES
INSERT INTO roles (name)
VALUES
    ('ADMIN'),
    ('CLIENT')
ON CONFLICT (name) DO NOTHING;

-- EXTENSION PARA CIFRAR CONTRASEÑAS CON BCRYPT
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- CATEGORIES
INSERT INTO category (name)
VALUES
    ('Sopas y cremas'),
    ('Entrantes'),
    ('Ensaladas'),
    ('Pastas'),
    ('Arroces'),
    ('Carnes'),
    ('Pescados'),
    ('Legumbres'),
    ('Verduras'),
    ('Guisos')
ON CONFLICT (name) DO NOTHING;

-- UNITS
INSERT INTO units (name, symbol)
VALUES
    ('Gramo', 'g'),
    ('Kilogramo', 'kg'),
    ('Mililitro', 'ml'),
    ('Litro', 'l'),
    ('Unidad', 'ud'),
    ('Cucharadita', 'cdta'),
    ('Cucharada', 'cda'),
    ('Taza', 'taza'),
    ('Pizca', 'pizca'),
    ('Paquete', 'paq')
ON CONFLICT (name) DO NOTHING;

-- INGREDIENTS
INSERT INTO ingredients (name, unit_id)
VALUES
    ('Arroz', (SELECT id FROM units WHERE name = 'Gramo')),
    ('Pasta', (SELECT id FROM units WHERE name = 'Gramo')),
    ('Harina', (SELECT id FROM units WHERE name = 'Gramo')),
    ('Leche', (SELECT id FROM units WHERE name = 'Mililitro')),
    ('Aceite de oliva', (SELECT id FROM units WHERE name = 'Mililitro')),
    ('Huevo', (SELECT id FROM units WHERE name = 'Unidad')),
    ('Patata', (SELECT id FROM units WHERE name = 'Kilogramo')),
    ('Tomate', (SELECT id FROM units WHERE name = 'Kilogramo')),
    ('Sal', (SELECT id FROM units WHERE name = 'Gramo')),
    ('Azúcar', (SELECT id FROM units WHERE name = 'Gramo'))
ON CONFLICT (name) DO NOTHING;