import express, { Request, Response } from "express";
import { RowDataPacket } from 'mysql2'
import ViteExpress from "vite-express";
import cors from 'cors';
import pool from './db';
import { fileURLToPath } from 'url';
import path from "path";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);


const app = express();
app.use(cors())
app.use(express.json());
app.use(express.static(path.join(__dirname, '../../dist')));

interface DefaultIngredientName extends RowDataPacket {
  Ingredient_Name: string;
}

interface UnitName extends RowDataPacket {
  Unit: string;
}

interface TopRated extends RowDataPacket {
  Recipe_ID: number;
  Recipe_Name: string;
  Average_Rating: number;
}

const getTopRatedrecipes = async (): Promise<TopRated[]> => {
  try {
      const [rows] = await pool.query<TopRated[]>("SELECT * FROM top_10_recipes_by_average_rating");
      // console.log(rows);
      return rows as TopRated[];
  } catch (error) {
      console.error("Error fetching top 10 highest rated recipes ", error);
      throw error;
  }
};

app.get('/top-rated-recipes', async (req: Request, res: Response) => {
  try {
      const topRatedRecipes = await getTopRatedrecipes();
      res.status(200).json(topRatedRecipes.map((row) => ({
        Recipe_ID: row.Recipe_ID,
        Recipe_Name: row.Recipe_Name,
        Average_Rating: row.Average_Rating
    })));
    
      // console.log("Names of the ingredients", ingredientNames);
  } catch (error) {
      console.error('Error fetching most saved recipes:', error);
      res.status(500).json({ error: 'Failed to fetch most saved recipes' });
  }
});


interface MostSaved extends RowDataPacket {
  Recipe_ID: number;
  Recipe_Name: string;
  Number_Of_Saves: number;
}

const getMostSavedrecipes = async (): Promise<MostSaved[]> => {
  try {
      const [rows] = await pool.query<MostSaved[]>("SELECT * FROM top_10_recipes_by_number_of_saves");
      // console.log(rows);
      return rows as MostSaved[];
  } catch (error) {
      console.error("Error fetching top 10 most saved recipes ", error);
      throw error;
  }
};

app.get('/most-saved-recipes', async (req: Request, res: Response) => {
  try {
      const mostSavedRecipes = await getMostSavedrecipes();
      res.status(200).json(mostSavedRecipes.map((row) => ({
        Recipe_ID: row.Recipe_ID,
        Recipe_Name: row.Recipe_Name,
        Number_Of_Saves: row.Number_Of_Saves
    })));
    
      // console.log("Names of the ingredients", ingredientNames);
  } catch (error) {
      console.error('Error fetching most saved recipes:', error);
      res.status(500).json({ error: 'Failed to fetch most saved recipes' });
  }
});


interface Healthiest extends RowDataPacket {
  Recipe_ID: number;
  Recipe_Name: string;
  Calories: number;
}

const getHealthiestRecipes = async (): Promise<Healthiest[]> => {
  try {
      const [rows] = await pool.query<Healthiest[]>("SELECT * FROM top_10_healthiest_recipes");
      // console.log(rows);
      return rows as Healthiest[];
  } catch (error) {
      console.error("Error fetching top 10 recipes by lowest amount of calories ", error);
      throw error;
  }
};

app.get('/healthiest-recipes', async (req: Request, res: Response) => {
  try {
      const healthiestRecipes = await getHealthiestRecipes();
      res.status(200).json(healthiestRecipes.map((row) => ({
        Recipe_ID: row.Recipe_ID,
        Recipe_Name: row.Recipe_Name,
        Calories: row.Calories
    })));
    
      // console.log("Names of the ingredients", ingredientNames);
  } catch (error) {
      console.error('Error fetching most saved recipes:', error);
      res.status(500).json({ error: 'Failed to fetch most saved recipes' });
  }
});

const getIngredientNames = async (): Promise<DefaultIngredientName[]> => {
  try {
      const [rows] = await pool.query<DefaultIngredientName[]>("SELECT DISTINCT Ingredient_Name FROM Ingredient ORDER BY Ingredient_Name ASC");
      // console.log(rows);
      return rows as DefaultIngredientName[];
  } catch (error) {
      console.error("Error fetching ingredient names:", error);
      throw error;
  }
};

const getUnitNames = async (): Promise<UnitName[]> => {
  try {
    const [rows] = await pool.query<UnitName[]>("SELECT DISTINCT Unit FROM Recipe_Content ORDER BY Unit ASC");
    // console.log(rows);
    return rows as UnitName[];
} catch (error) {
    console.error("Error fetching ingredient names:", error);
    throw error;
}
};

const getRecipeReviews = async (recipeId: number): Promise<RowDataPacket[]> => {
  try {
    const query = `
      SELECT Email, Rating, Comment
      FROM User_Review
      WHERE Recipe_ID = ?
    `;
    // console.log(query)
    const [rows] = await pool.query<RowDataPacket[]>(query, [recipeId]);
    // console.log(rows);
    return rows;
  } catch (error) {
    console.error('Error fetching recipe reviews:', error);
    throw error;
  }
};


app.get('/default-ingredients', async (req: Request, res: Response) => {
  try {
      const ingredientNames = await getIngredientNames();
      res.status(200).json(ingredientNames.map((row) => row.Ingredient_Name));
      // console.log("Names of the ingredients", ingredientNames);
  } catch (error) {
      console.error('Error fetching ingredient names:', error);
      res.status(500).json({ error: 'Failed to fetch ingredient names' });
  }
});

app.get('/units', async (req: Request, res: Response) => {
  try {
      const unitNames = await getUnitNames();
      res.status(200).json(unitNames.map((row) => row.Unit));
      // console.log("Names of the ingredients", ingredientNames);
  } catch (error) {
      console.error('Error fetching unit names:', error);
      res.status(500).json({ error: 'Failed to fetch unit names' });
  }
});

const constructRecipeUrl = (recipeName: string): string => {
  const formattedName = recipeName.trim().toLowerCase().replace(/\s+/g, '-');
  return `https://www.allrecipes.com/recipes/${formattedName}`;
};


app.get('/recipes', async (req: Request, res: Response) => {
  try {
    
    const recipeQuery = `
      SELECT r.Recipe_ID, r.Recipe_Name, r.Prep_Time, r.Cook_Time, r.Calories, r.Servings,
             i.Ingredient_Name, rc.Quantity, rc.Unit, r.Directions
      FROM Recipe r
      JOIN Recipe_Content rc ON r.Recipe_ID = rc.Recipe_ID
      JOIN Ingredient i ON rc.Ingredient_ID = i.Ingredient_ID
    `;

    const ratingsQuery = `
      SELECT r.Recipe_ID, COALESCE(AVG(rv.Rating), 0) AS Average_Rating
      FROM Recipe r
      LEFT JOIN User_Review rv ON r.Recipe_ID = rv.Recipe_ID
      GROUP BY r.Recipe_ID
    `;
    const [recipeRows] = await pool.query<RowDataPacket[]>(recipeQuery);
    const [ratingsRows] = await pool.query<RowDataPacket[]>(ratingsQuery);

    const ratingsMap = ratingsRows.reduce((map, row) => {
      map[row.Recipe_ID] = row.Average_Rating;
      return map;
    }, {} as { [key: number]: number });

    const recipes = recipeRows.reduce((acc: any, row: any) => {
      const recipeId = row.Recipe_ID;
      if (!acc[recipeId]) {
        acc[recipeId] = {
          Recipe_ID: recipeId,
          Recipe_Name: row.Recipe_Name,
          Prep_Time: row.Prep_Time,
          Cook_Time: row.Cook_Time,
          Calories: row.Calories,
          Servings: row.Servings,
          Recipe_Url: constructRecipeUrl(row.Recipe_Name), 
          Ingredients: [],
          Directions: row.Directions,
          Average_Rating: ratingsMap[recipeId] || 0, 
        };
      }
      acc[recipeId].Ingredients.push({
        Ingredient_Name: row.Ingredient_Name,
        Quantity: row.Quantity,
        Unit: row.Unit
      });
      return acc;
    }, {});
    res.status(200).json(Object.values(recipes)); 
  } catch (err) {
    if (err instanceof Error) {
      console.error('Error fetching recipes:', err.message);
      res.status(500).send(`Error fetching recipes: ${err.message}`);
    } else {
      console.error('Unknown error fetching recipes:', err);
      res.status(500).send('Unknown error fetching recipes');
    }
  }
});

app.post('/login', async (req: Request, res: Response) => {
	try {
		const { username, password } = req.body;
		const [user] = await pool.query<RowDataPacket[]>('SELECT Email, Password FROM User Where Email=?', [username]);
		if (!user) {
			return res.json({
				success: false,
				message: 'User not found'
			});
		}
		
		let valid_password = false;
		if (password == user[0].Password) valid_password = true;
		if (!valid_password) {
			return res.json({
				success: false,
				message: 'Invalid password'
			});
		}

		res.json({
			success: true,
			message: 'Login success',
			user: username
		});
	} catch (error) {
		console.log(error);
		res.json({
			success: false,
			message: 'error during login'
		});
	}	
});

app.post('/newuser', async (req: Request, res: Response) => {
	try {
		const { username, password } = req.body;
    const [result] = await pool.execute('INSERT INTO User (Email, Password) VALUES (?, ?)', [username, password]);
    if (!result) {
      return res.json({
        success: false,
        message: 'Failed to create.'
      });
    }

    if ((result as any).affectedRows != 1) {
      return res.json({
        success: false,
        message: 'Error; modified too many rows.'
      });
    }
		
		res.json({
			success: true,
			message: 'successfully created user in db',
			user: username
		});

	} catch (error) {
		console.log(error);
		res.json({
			success: false,
			message: 'error during login'
		});
	} 	
});


app.get('*', (req: Request, res: Response) => {
  res.sendFile(path.join(__dirname, '../../dist/index.html'));
});


app.post('/insert-recipe', async (req, res) => {
  const { data } = req.body;

  if (!data) {
    return res.status(400).send('Data is required');
  }


  // Construct dynamic SQL query
  const columns = Object.keys(data).join(', ');
  const placeholders = Object.values(data).map(() => '?').join(', ');
  const values = Object.values(data);

  const sql = `INSERT INTO Recipe (${columns}) VALUES (${placeholders})`;

  //console.log(sql)

  try {
    const result = await pool.execute(sql, values); 
    res.status(200).json({ message: 'Data inserted successfully', result });
  } catch (err) {
    console.error('Error inserting data into Recipe:', err); // Log the error to the console
    res.status(500).send('Error inserting data');
  }
});

app.post('/insert-recipe-content', async (req, res) => {
  const { data } = req.body;

  if (!data) {
    return res.status(400).send('Data is required');
  }

  // Construct dynamic SQL query

  
  const query1 = `SELECT Recipe_ID FROM Recipe r
                  ORDER BY Recipe_ID DESC
                  LIMIT 1;`;
  
  const query2 = `SELECT Ingredient_ID FROM Ingredient i
                  WHERE ? = i.Ingredient_Name`;


  const [rows1] = await pool.query<RowDataPacket[]>(query1);
  const [rows2] = await pool.execute<RowDataPacket[]>(query2, [data.Ingredient_Name]);

  const Recipe_ID = rows1[0].Recipe_ID;
  const Ingredient_ID = rows2[0].Ingredient_ID;

  delete data.Ingredient_Name;
  
  var columns = Object.keys(data).join(', ');
  var placeholders = Object.values(data).map(() => '?').join(', ');
  const values = Object.values(data);


  columns = 'Recipe_ID, Ingredient_ID, ' + columns;
  placeholders = '?, ?, ' + placeholders;
  var extraValues = [Recipe_ID, Ingredient_ID];
  extraValues.push(...values)

  // console.log(extraValues)

  const sql = `INSERT INTO Recipe_Content (${columns}) VALUES (${[placeholders]})`;

  // console.log(sql)

  // res.status(200).json({message: 'Test Queries ran.'})

  try {
    const result = await pool.execute(sql, extraValues); 
    res.status(200).json({ message: 'Data inserted successfully', result });
  } catch (err) {
    console.error('Error inserting data into Recipe_Content:', err); // Log the error to the console
    res.status(500).send('Error inserting data');
  }
});
// Save recipe
app.post('/save-recipe', async (req: Request, res: Response) => {
  const { email, recipeId } = req.body;

  console.log('Received request to save recipe:', { email, recipeId });

  if (!email || !recipeId) {
    console.error('Email and Recipe ID are required');
    return res.status(400).send('Email and Recipe ID are required');
  }

  try {
    await pool.query(`
      INSERT INTO User_Saved_Recipe (Email, Recipe_ID)
      VALUES (?, ?)
    `, [email, recipeId]);

    res.status(200).send('Recipe saved successfully');
  } catch (err) {
    console.error('Error saving recipe:', err);
    res.status(500).send('Failed to save recipe');
  }
});

// Unsave recipe
app.post('/unsave-recipe', async (req: Request, res: Response) => {
  const { email, recipeId } = req.body;

  console.log('Received request to unsave recipe:', { email, recipeId });

  if (!email || !recipeId) {
    console.error('Email and Recipe ID are required');
    return res.status(400).send('Email and Recipe ID are required');
  }

  try {
    await pool.query(`
      DELETE FROM User_Saved_Recipe
      WHERE Email = ? AND Recipe_ID = ?
    `, [email, recipeId]);

    res.status(200).send('Recipe unsaved successfully');
  } catch (err) {
    console.error('Error unsaving recipe:', err);
    res.status(500).send('Failed to unsave recipe');
  }
});

// Leave a review
app.post('/leave-review', async (req: Request, res: Response) => {
  const { email, recipeId, Rating, Comment } = req.body;

  console.log('Received request to leave review:', { email, recipeId, Rating, Comment });

  if (!email || !recipeId || !Rating || !Comment) {
    console.error('Email, Recipe ID, Rating, and Comment are required');
    return res.status(400).send('Email, Recipe ID, Rating, and Comment are required');
  }

  try {
    await pool.query(`
      INSERT INTO User_Review (Email, Recipe_ID, Rating, Comment)
      VALUES (?, ?, ?, ?)
    `, [email, recipeId, Rating, Comment]);

    res.status(200).send('Review submitted successfully');
  } catch (err) {
    console.error('Error submitting review:', err);
    res.status(500).send('Failed to submit review');
  }
});

app.post('/user-created-recipes', async (req: Request, res: Response) => {
  const { email } = req.body;

  console.log('Received email:', email);

  if (!email) {
    return res.status(400).json({ error: 'Email is required' });
  }

  try {
    const query = `
      SELECT * FROM Recipe
      WHERE Email = ?
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query, [email]);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching created recipes:', error);
    res.status(500).json({ error: 'Failed to fetch created recipes' });
  }
});

app.post('/saved-recipes', async (req: Request, res: Response) => {
  const { email } = req.body;

  console.log('Received email:', email);

  if (!email) {
    return res.status(400).json({ error: 'Email is required' });
  }

  try {
    const query = `
      SELECT r.* FROM Recipe r
      JOIN User_Saved_Recipe usr ON r.Recipe_ID = usr.Recipe_ID
      WHERE usr.Email = ?
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query, [email]);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching saved recipes:', error);
    res.status(500).json({ error: 'Failed to fetch saved recipes' });
  }
});

app.post('/user-reviews', async (req: Request, res: Response) => {
  const { email } = req.body;

  console.log('Received email:', email);

  if (!email) {
    return res.status(400).json({ error: 'Email is required' });
  }

  try {
    const query = `
      SELECT r.Recipe_Name, ur.Rating, ur.Comment FROM User_Review ur
      JOIN Recipe r ON ur.Recipe_ID = r.Recipe_ID
      WHERE ur.Email = ?
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query, [email]);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching user reviews:', error);
    res.status(500).json({ error: 'Failed to fetch user reviews' });
  }
});

// Fetch total number of saves for each recipe
app.get('/recipe-saves', async (req: Request, res: Response) => {
  try {
    const query = `
      SELECT Recipe_ID, Number_Of_Saves AS Saves
      FROM Recipe
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching recipe saves:', error);
    res.status(500).json({ error: 'Failed to fetch recipe saves' });
  }
});


app.post('/recipe-saves-for-specific-recipe', async (req: Request, res: Response) => {
  const { recipeId } = req.body; 
  
  console.log('Received recipeId:', recipeId);

  if (!recipeId) {
    return res.status(400).json({ error: 'Recipe ID is required' });
  }

  try {
    const query = `
      SELECT Recipe_ID, Number_Of_Saves AS Saves
      FROM Recipe
      WHERE Recipe_ID = ?
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query, [recipeId]);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching recipe ratings:', error);
    res.status(500).json({ error: 'Failed to fetch recipe ratings' });
  }
});

app.post('/recipe-saved-ids-for-specific-user', async (req: Request, res: Response) => {
  const { email } = req.body; 
  
  console.log('Received email:', email);

  if (!email) {
    return res.status(400).json({ error: 'Email is required' });
  }

  try {
    const query = `
      SELECT Recipe_ID
      FROM User_Saved_Recipe
      WHERE Email = ?
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query, [email]);
    const savedRecipeIDs = rows.map((row) => row.Recipe_ID)
    res.status(200).json(savedRecipeIDs);
  } catch (error) {
    console.error('Error fetching recipe ratings:', error);
    res.status(500).json({ error: 'Failed to fetch saved recipe IDs' });
  }
});


// Fetch total number of saves for each recipe
app.get('/recipe-ratings', async (req: Request, res: Response) => {
  try {
    const query = `
      SELECT Recipe_ID, Number_Of_Saves AS Saves
      FROM Recipe
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching recipe saves:', error);
    res.status(500).json({ error: 'Failed to fetch recipe saves' });
  }
});



// Fetch average rating for each recipe
app.post('/recipe-ratings-for-specific-recipe', async (req: Request, res: Response) => {
  const { recipeId } = req.body; 
  
  console.log('Received recipeId:', recipeId);

  if (!recipeId) {
    return res.status(400).json({ error: 'Recipe ID is required' });
  }

  try {
    const query = `
      SELECT Recipe_ID, Average_Rating
      FROM Recipe
      WHERE Recipe_ID = ?
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query, [recipeId]);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching recipe ratings:', error);
    res.status(500).json({ error: 'Failed to fetch recipe ratings' });
  }
});

app.get('/recipe-reviews', async (req: Request, res: Response) => {
  try {
    const query = `
      SELECT Email, Rating, Comment
      FROM User_Review
    `;
    const [rows] = await pool.query<RowDataPacket[]>(query);
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching recipe saves:', error);
    res.status(500).json({ error: 'Failed to fetch recipe saves' });
  }
});

// Fetch reviews for each recipe
app.post('/recipe-reviews-for-specific-recipe', async (req: Request, res: Response) => {
  const { recipeId } = req.body; // Assuming you're sending recipeId in the body as part of the request
  
  console.log('Received recipeId:', recipeId);

  if (!recipeId) {
    return res.status(400).json({ error: 'Recipe ID is required' });
  }

  try {
    // Adjust the query to use the recipeId dynamically
    const query = `
      SELECT Email, Rating, Comment
      FROM User_Review
      WHERE Recipe_ID = ?
    `;
    
    // Execute the query with the recipeId
    const [rows] = await pool.execute<RowDataPacket[]>(query, [recipeId]);
    
    // Send the reviews back as the response
    res.status(200).json(rows);
  } catch (error) {
    console.error('Error fetching recipe reviews:', error);
    res.status(500).json({ error: 'Failed to fetch recipe reviews' });
  }
});



app.get('/recipes', async (req: Request, res: Response) => {
  try {
    const query = `
      SELECT r.Recipe_ID, r.Recipe_Name, r.Prep_Time, r.Cook_Time, r.Calories, r.Servings,
             i.Ingredient_Name, rc.Quantity, rc.Unit, r.Directions
      FROM Recipe r
      JOIN Recipe_Content rc ON r.Recipe_ID = rc.Recipe_ID
      JOIN Ingredient i ON rc.Ingredient_ID = i.Ingredient_ID
    `;
    
    const [rows] = await pool.query<RowDataPacket[]>(query);
    const recipes = rows.reduce((acc: any, row: any) => {
      const recipeId = row.Recipe_ID;
      if (!acc[recipeId]) {
        acc[recipeId] = {
          Recipe_ID: recipeId,
          Recipe_Name: row.Recipe_Name,
          Prep_Time: row.Prep_Time,
          Cook_Time: row.Cook_Time,
          Calories: row.Calories,
          Servings: row.Servings,
          Recipe_Url: constructRecipeUrl(row.Recipe_Name), 
          Ingredients: [],
          Directions: row.Directions,
          Reviews: []
        };
      }
      acc[recipeId].Ingredients.push({
        Ingredient_Name: row.Ingredient_Name,
        Quantity: row.Quantity,
        Unit: row.Unit
      });
      return acc;
    }, {});

    // Fetch reviews for each recipe
    for (const recipeId in recipes) {
      const reviews = await getRecipeReviews(Number(recipeId));
      recipes[recipeId].Reviews = reviews;
    }

    res.status(200).json(Object.values(recipes));
  } catch (error) {
    console.error('Error fetching recipes:', error);
    res.status(500).json({ error: 'Failed to fetch recipes' });
  }
});

app.delete('/delete-recipe', async (req: Request, res: Response) => {
  const { email, recipeId } = req.body;

  console.log('Received request to delete recipe:', { email, recipeId });

  if (!email || !recipeId) {
    console.error('Email and Recipe ID are required');
    return res.status(400).send('Email and Recipe ID are required');
  }

  try {
    const query = `
      DELETE FROM Recipe
      WHERE Recipe_ID = ? AND Email = ?
    `;
    const [result] = await pool.query(query, [recipeId, email]);

    if ((result as any).affectedRows === 0) {
      return res.status(404).send('Recipe not found or you do not have permission to delete this recipe');
    }

    res.status(200).send('Recipe deleted successfully');
  } catch (err) {
    console.error('Error deleting recipe:', err);
    res.status(500).send('Failed to delete recipe');
  }
});

ViteExpress.listen(app, 3000, () =>
  console.log("Server is listening on port 3000..."),
);