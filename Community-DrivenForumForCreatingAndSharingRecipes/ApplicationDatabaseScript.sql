DROP TABLE Recipe_Content;
DROP TABLE Ingredient;
DROP TABLE User_Review;
DROP TABLE User_Saved_Recipe;
DROP TABLE Recipe;
DROP TABLE User;

CREATE TABLE User (
Email VARCHAR(250),
Password varchar(250),
CONSTRAINT PK_USER PRIMARY KEY (Email)
);

CREATE TABLE Recipe (
    Recipe_ID INT,
    Email VARCHAR(250),
    Recipe_Name VARCHAR(250),
    Prep_Time INT,
    Cook_Time INT,
    Additional_Time INT,
    Calories INT,
    Servings INT,
    Number_Of_Saves INT,
    Average_Rating DECIMAL(10,2),
    Directions TEXT,
    CONSTRAINT PK_Recipe PRIMARY KEY(Recipe_ID),
    CONSTRAINT FK_Recipe FOREIGN KEY(Email) REFERENCES User (Email) ON DELETE CASCADE,
    CONSTRAINT CHK_Prep_Time_Not_Negative CHECK (Prep_Time >= 0),
    CONSTRAINT CHK_Cook_Time_Not_Negative CHECK (Cook_Time >= 0),
    CONSTRAINT CHK_Additional_Time_Not_Negative CHECK (Additional_Time >= 0),
    CONSTRAINT CHK_Calories_Not_Negative CHECK (Calories >= 0),
    CONSTRAINT CHK_Servings_Not_Negative CHECK (Servings >= 0)
);


CREATE TABLE User_Saved_Recipe (
Email VARCHAR(250),
Recipe_ID INT,
CONSTRAINT PK_User_Saved_Recipes PRIMARY KEY(Email, Recipe_ID),
CONSTRAINT FK1_User_Saved_Recipes FOREIGN KEY(Email) REFERENCES User (Email) ON DELETE CASCADE,
CONSTRAINT FK2_User_Saved_Recipes FOREIGN KEY (Recipe_ID) REFERENCES Recipe (Recipe_ID) ON DELETE CASCADE
);

CREATE TABLE User_Review (
Email VARCHAR(250),
Recipe_ID INT,
Review_ID INT,
Comment VARCHAR(2000),
Rating DECIMAL(10,2),
CONSTRAINT PK_User_Reviews PRIMARY KEY(Email, Recipe_ID, Review_ID),
CONSTRAINT FK1_User_Reviews FOREIGN KEY(Email) REFERENCES User(Email) ON DELETE CASCADE,
CONSTRAINT FK2_User_Reviews FOREIGN KEY(Recipe_ID) REFERENCES Recipe(Recipe_ID) ON DELETE CASCADE,
CONSTRAINT CHK_Rating_Values CHECK (Rating BETWEEN 0 AND 5)
);

CREATE TABLE Ingredient (
Ingredient_ID INT AUTO_INCREMENT,
Ingredient_Name VARCHAR(250),
CONSTRAINT PK_Ingredient PRIMARY KEY (Ingredient_ID)
);

CREATE TABLE Recipe_Content (
Recipe_Content_ID INT AUTO_INCREMENT,
Recipe_ID INT,
Ingredient_ID INT,
Quantity DECIMAL(10,3),
Unit VARCHAR(250),
CONSTRAINT PK_Recipe_Content PRIMARY KEY (Recipe_Content_ID, Recipe_ID, Ingredient_ID),
CONSTRAINT FK1_Recipe FOREIGN KEY(Recipe_ID) REFERENCES Recipe (Recipe_ID) ON DELETE CASCADE,
CONSTRAINT FK2_Recipe FOREIGN KEY(Ingredient_ID) REFERENCES Ingredient (Ingredient_ID) ON DELETE CASCADE
);

DELIMITER //

CREATE TRIGGER generate_next_review_id
BEFORE INSERT ON User_Review
FOR EACH ROW
BEGIN
	DECLARE next_review_id INT;
    
    SET next_review_id = 0;
    
    SELECT IFNULL(MAX(Review_ID), 0) INTO next_review_id
    FROM User_Review;
    
	SET NEW.Review_ID = next_review_id + 1;
END//
  
DELIMITER ;

DELIMITER //

CREATE TRIGGER generate_next_recipe_id
BEFORE INSERT ON Recipe
FOR EACH ROW
BEGIN
	DECLARE next_recipe_id INT;
    
    SET next_recipe_id = 0;
    
    SELECT IFNULL(MAX(Review_ID), 0) INTO next_recipe_id
    FROM User_Review;
    
    IF NEW.Recipe_ID IS NULL OR NEW.Recipe_ID = 0 THEN
        SET NEW.Recipe_ID = next_recipe_id + 1;
    END IF;
END//
  
DELIMITER ;

DELIMITER //

CREATE TRIGGER prevent_user_duplicate_recipe_names
BEFORE INSERT ON Recipe
FOR EACH ROW
BEGIN
	DECLARE recipe_name_by_user_counter INT;
    
    SET recipe_name_by_user_counter = 0;
    
    SELECT COUNT(Recipe_Name) INTO recipe_name_by_user_counter
    FROM Recipe
    WHERE Email = NEW.Email AND Recipe_Name = NEW.Recipe_Name;
    
	IF recipe_name_by_user_counter > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicate recipe name by the same user is not allowed.';
	END IF;
END//

DELIMITER ;

DELIMITER //

CREATE TRIGGER update_average_rating
AFTER INSERT ON User_Review
FOR EACH ROW
BEGIN
	UPDATE Recipe
	SET Average_Rating = (SELECT AVG(Rating)
    FROM User_Review 
	WHERE Recipe_ID = NEW.Recipe_ID)
    WHERE Recipe_ID = NEW.Recipe_ID;
END//

DELIMITER ;

DELIMITER //

CREATE TRIGGER update_num_saves_when_user_saves_recipe
AFTER INSERT ON User_Saved_Recipe
FOR EACH ROW
BEGIN
	UPDATE Recipe
    SET Number_Of_Saves = Number_Of_Saves + 1
    WHERE Recipe_ID = NEW.Recipe_ID;
END//

DELIMITER ;

DELIMITER //

CREATE TRIGGER update_num_saves_when_user_unsaves_recipe
AFTER DELETE ON User_Saved_Recipe
FOR EACH ROW
BEGIN
	UPDATE Recipe
    SET Number_Of_Saves = Number_Of_Saves - 1
    WHERE Recipe_ID = OLD.Recipe_ID;
END//

DELIMITER ;

DELIMITER //

CREATE TRIGGER cannot_delete_high_quality_recipes
BEFORE DELETE ON Recipe
FOR EACH ROW
BEGIN
	IF OLD.Average_Rating >= 4.0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot delete high quality recipe';
	END IF;
END//
  
DELIMITER ;

CREATE OR REPLACE VIEW top_10_recipes_by_average_rating AS
SELECT Recipe_ID, Recipe_Name, Average_Rating
FROM Recipe
WHERE Average_Rating IS NOT NULL
ORDER BY Average_Rating DESC
LIMIT 10;

CREATE OR REPLACE VIEW top_10_recipes_by_number_of_saves AS
SELECT Recipe_ID, Recipe_Name, Number_Of_Saves
FROM Recipe
WHERE Number_Of_Saves > 0
ORDER BY Number_Of_Saves DESC
LIMIT 10;

CREATE OR REPLACE VIEW top_10_healthiest_recipes AS
SELECT Recipe_ID, Recipe_Name, Calories
FROM Recipe
WHERE Calories IS NOT NULL
ORDER BY Calories ASC
LIMIT 10;

SELECT * FROM Ingredient;

SELECT * FROM Recipe
JOIN Recipe_Content ON Recipe.Recipe_ID = Recipe_Content.Recipe_ID
JOIN Ingredient ON Ingredient.Ingredient_ID = Recipe_Content.Ingredient_ID
WHERE Recipe_Name = 'New Colorado Omelet';

SELECT * FROM Recipe_Content;

SELECT * FROM User;

SET SQL_SAFE_UPDATES = 0;

DELETE FROM Recipe_Content;

SELECT * FROM Recipe 
JOIN Recipe_Content ON Recipe.Recipe_ID = Recipe_Content.Recipe_ID
WHERE Recipe.Recipe_ID = 1;

Select COUNT(*) FROM Recipe;

Select Ingredient_Name FROM Ingredient ORDER BY Ingredient_Name;

INSERT INTO User VALUES ("test1@example.com", "password1");

delete from User_Saved_Recipe WHERE Email = 'user20@example.com' and Recipe_ID = 10;

DELETE FROM Recipe;

DELETE FROM Recipe_Content;

DELETE FROM Ingredient;

SELECT * FROM Ingredient;

INSERT INTO Recipe_Content VALUES (NULL, NULL, NULL, NULL, 'None');

Select * From Recipe_Content WHERE Unit = 'degrees f/40 degree c';

UPDATE Recipe_Content SET Unit = 'degrees f' WHERE Unit = 'degrees f/40 degree c';



EXPLAIN FORMAT = TREE
SELECT r.Recipe_ID, r.Recipe_Name, r.Prep_Time, r.Cook_Time, r.Calories, r.Servings,
i.Ingredient_Name, rc.Quantity, rc.Unit, r.Directions
FROM Recipe r
JOIN Recipe_Content rc ON r.Recipe_ID = rc.Recipe_ID
JOIN Ingredient i ON rc.Ingredient_ID = i.Ingredient_ID;


SELECT * FROM Recipe;

CREATE INDEX idx_recipe_ingredient ON Recipe_Content(Recipe_ID);
ALTER TABLE Recipe_Content ALTER INDEX idx_recipe_ingredient INVISIBLE;
DROP INDEX idx_recipe_ingredient ON Recipe_Content;



CREATE INDEX idx_recipe_ingredient2 ON Recipe_Content(Ingredient_ID);
ALTER TABLE Recipe_Content ALTER INDEX idx_recipe_ingredient2 INVISIBLE;


CREATE INDEX idx_recipe_ingredient3 ON Ingredient(Ingredient_ID);