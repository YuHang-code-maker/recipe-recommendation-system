INSERT INTO recipes (id, title, instructions, image) VALUES
(1, 'Tomato Pasta', 'Boil pasta and mix with tomato sauce.', '1.jpg'),
(2, 'Fried Egg', 'Heat oil and fry the egg.', '2.jpg'),
(3, 'Garlic Bread', 'Bake bread with garlic butter.', '3.jpg'),
(4, 'Chicken Salad', 'Mix chicken with vegetables.', '4.jpg'),
(5, 'Cheese Omelette', 'Beat eggs and cook with cheese.', '5.jpg');


INSERT INTO ingredients (id, name) VALUES
(1, 'Egg'),
(2, 'Pasta'),
(3, 'Tomato'),
(4, 'Garlic'),
(5, 'Bread'),
(6, 'Chicken'),
(7, 'Cheese'),
(8, 'Olive Oil'),
(9, 'Salt'),
(10, 'Butter'),
(11, 'Lettuce');

INSERT INTO recipe_ingredients (recipe_id, ingredient_id) VALUES
(1, 2),
(1, 3),
(1, 8),
(1, 9),
(2, 1),
(2, 8),
(2, 9),
(3, 4),
(3, 5),
(3, 10),
(4, 6),
(4, 11),
(4, 8),
(4, 9),
(5, 1),
(5, 7),
(5, 8),
(5, 9);