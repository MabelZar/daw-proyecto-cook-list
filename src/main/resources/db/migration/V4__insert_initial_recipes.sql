-- RECETAS DE PRUEBA
INSERT INTO recipes (
    user_id,
    category_id,
    name,
    description,
    preparation_minutes
)
VALUES
(NULL, (SELECT id FROM category WHERE name = 'Arroces'),
 'Arroz con tomate', 'Arroz sencillo con tomate natural.', 25),

(NULL, (SELECT id FROM category WHERE name = 'Pastas'),
 'Pasta con aceite de oliva', 'Pasta aliñada con aceite de oliva.', 20),

(NULL, (SELECT id FROM category WHERE name = 'Sopas y cremas'),
 'Crema de patata', 'Crema suave de patata y leche.', 35),

(NULL, (SELECT id FROM category WHERE name = 'Entrantes'),
 'Tortilla básica', 'Tortilla elaborada con huevo y sal.', 15),

(NULL, (SELECT id FROM category WHERE name = 'Verduras'),
 'Patatas cocidas', 'Patatas cocidas con una pizca de sal.', 30),

(NULL, (SELECT id FROM category WHERE name = 'Entrantes'),
 'Bizcocho sencillo', 'Bizcocho casero con harina, huevo y azúcar.', 45),

(NULL, (SELECT id FROM category WHERE name = 'Ensaladas'),
 'Ensalada de tomate', 'Tomate aliñado con aceite de oliva y sal.', 10),

(NULL, (SELECT id FROM category WHERE name = 'Legumbres'),
 'Arroz con verduras', 'Arroz acompañado de verduras.', 30),

(NULL, (SELECT id FROM category WHERE name = 'Guisos'),
 'Patatas guisadas', 'Guiso tradicional de patatas.', 40),

(NULL, (SELECT id FROM category WHERE name = 'Carnes'),
 'Pasta cremosa', 'Pasta con leche y un toque de aceite.', 25);


-- INGREDIENTES DE LAS RECETAS
INSERT INTO recipe_ingredients (
    recipe_id,
    ingredient_id,
    quantity
)
VALUES
((SELECT id FROM recipes WHERE name = 'Arroz con tomate'),
 (SELECT id FROM ingredients WHERE name = 'Arroz'), 200),

((SELECT id FROM recipes WHERE name = 'Arroz con tomate'),
 (SELECT id FROM ingredients WHERE name = 'Tomate'), 150),

((SELECT id FROM recipes WHERE name = 'Pasta con aceite de oliva'),
 (SELECT id FROM ingredients WHERE name = 'Pasta'), 200),

((SELECT id FROM recipes WHERE name = 'Pasta con aceite de oliva'),
 (SELECT id FROM ingredients WHERE name = 'Aceite de oliva'), 20),

((SELECT id FROM recipes WHERE name = 'Crema de patata'),
 (SELECT id FROM ingredients WHERE name = 'Patata'), 300),

((SELECT id FROM recipes WHERE name = 'Crema de patata'),
 (SELECT id FROM ingredients WHERE name = 'Leche'), 250),

((SELECT id FROM recipes WHERE name = 'Tortilla básica'),
 (SELECT id FROM ingredients WHERE name = 'Huevo'), 3),

((SELECT id FROM recipes WHERE name = 'Tortilla básica'),
 (SELECT id FROM ingredients WHERE name = 'Sal'), 2),

((SELECT id FROM recipes WHERE name = 'Bizcocho sencillo'),
 (SELECT id FROM ingredients WHERE name = 'Harina'), 200),

((SELECT id FROM recipes WHERE name = 'Bizcocho sencillo'),
 (SELECT id FROM ingredients WHERE name = 'Azúcar'), 100);