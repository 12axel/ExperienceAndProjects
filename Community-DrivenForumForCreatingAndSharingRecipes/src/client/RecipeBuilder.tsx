// RecipeBuilder.tsx
import React, { useState, useEffect, ChangeEvent } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

interface RecipeIngredient {
  Ingredient_Name: string;
  Quantity: number;
  Unit: string;
}

interface RecipeSubmission {
  Email: String;
  Recipe_Name: String;
  Prep_Time: number;
  Cook_Time: number;
  Additional_Time: number;
  Servings: number;
  Calories: number;
  Number_Of_Saves: number;
  Average_Rating: number;
  Directions: string;
}

const RecipeBuilder: React.FC<{ email: string }> = ({ email }) => {
  const [recipeIngredients, setRecipeIngredients] = useState<
    RecipeIngredient[]
  >([]);
  const [defaultIngredients, setDefaultIngredients] = useState<string[]>([]);
  const [units, setUnits] = useState<string[]>([]);
  const [recipeSubmit, setRecipeSubmit] = useState<RecipeSubmission>({
    Email: email,
    Recipe_Name: "",
    Prep_Time: 0,
    Cook_Time: 0,
    Additional_Time: 0,
    Servings: 0,
    Calories: 0,
    Number_Of_Saves: 0,
    Average_Rating: 0,
    Directions: "",
  });

  const navigate = useNavigate();

  useEffect(() => {
    const fetchIngredientNames = async () => {
      try {
        const response = await fetch("/default-ingredients");

        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }

        const ingredientNames: string[] = await response.json();

        setDefaultIngredients(ingredientNames);
      } catch (error) {
        console.error(error);
      }
    };

    fetchIngredientNames();

    const fetchUnitNames = async () => {
      try {
        const response = await fetch("/units");

        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }

        const getUnitNames: (string | null)[] = await response.json();

        const cleanedUnitNames = getUnitNames.map((unit) =>
          unit === null ? "None" : unit
        );

        setUnits(cleanedUnitNames);
      } catch (error) {
        console.error(error);
      }
    };

    fetchUnitNames();
  }, []);

  useEffect(() => {
    // console.log("Updated defaultIngredients:", defaultIngredients);
  }, [defaultIngredients]);

  useEffect(() => {
    // console.log("Updated units:", units);
  }, [units]);

  const addIngredient = () => {
    setRecipeIngredients([
      ...recipeIngredients,
      { Ingredient_Name: "", Quantity: 0, Unit: "" },
    ]);
  };

  const deleteIngredient = (index: number) => {
    setRecipeIngredients(recipeIngredients.filter((_, i) => i !== index));
  };

  const handleIngredientChange = (
    index: number,
    field: keyof RecipeIngredient,
    value: string | number
  ) => {
    const updatedIngredients = [...recipeIngredients];
    updatedIngredients[index] = {
      ...updatedIngredients[index],
      [field]: value,
    };
    setRecipeIngredients(updatedIngredients);
  };

  const handleChange = (
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    var { name, value } = event.target;
    let newValue: string | number = value;

    // console.log(name);
    if (
      name === "Prep_Time" ||
      name === "Cook_Time" ||
      name === "Servings" ||
      name === "Calories"
    ) {
      newValue = isNaN(Number(value)) ? value : Number(value);
    }

    // Update the corresponding field in the recipeSubmit state
    setRecipeSubmit((prevState) => ({
      ...prevState,
      [name]: newValue,
    }));

    // console.log(name + " " + value)
  };

  const handleSubmit = async () => {
    console.log(recipeSubmit);

    try {
      const response = await fetch("/insert-recipe", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          data: recipeSubmit,
        }),
      });

      console.log(response);

      if (!response.ok) {
        throw new Error(`Failed to fetch data: ${response.statusText}`);
      } else {
        for (let i = 0; i < recipeIngredients.length; i++) {
          console.log(recipeIngredients[i]);
          try {
            const response = await fetch("/insert-recipe-content", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                data: recipeIngredients[i],
              }),
            });
            console.log(response);

            if (!response.ok) {
              throw new Error(`Failed to fetch data: ${response.statusText}`);
            }
          } catch (error: any) {
            console.error(error);
          }
        }
      }
      console.log("All inserts completed, redirecting... ");
      //redirect to the /Recipe page...
      navigate("/recipe");
    } catch (error) {
      console.error(error);
      Swal.fire({
        title: "Try Again",
        text: "Cannot create a recipe with an already existing name.",
        icon: "error",
        confirmButtonText: "OK",
      });
    }
  };

  return (
    <div className="row">
      <h1 className="text-center mb-4" style={{ color: "orange" }}>
        Recipe Builder
      </h1>
      <div className="col max-width">
        <div className="list-group">
          <h4 className="mt-4">Recipe Name</h4>
          <input
            type="text"
            id="Recipe_Name"
            name="Recipe_Name"
            onChange={handleChange}
          ></input>
          <br></br>
          <h4 className="mt-4">Prep Time</h4>
          <input
            type="number"
            id="Prep_Time"
            name="Prep_Time"
            onChange={handleChange}
          ></input>
          <br></br>
          <h4 className="mt-4">Cook Time</h4>
          <input
            type="number"
            id="Cook_Time"
            name="Cook_Time"
            onChange={handleChange}
          ></input>
          <br></br>
          <h4 className="mt-4">Servings</h4>
          <input
            type="number"
            id="Servings"
            name="Servings"
            onChange={handleChange}
          ></input>
          <br></br>
          <h4 className="mt-4">Calories</h4>
          <input
            type="number"
            id="Calories"
            name="Calories"
            onChange={handleChange}
          ></input>
          <br></br>
          <div className="col-md-6 offset-md-3">
            <h4>Ingredients</h4>
            {recipeIngredients.map((ingredient, index) => (
              <div key={index} className="mb-3 border p-3 rounded">
                <h5>Ingredient {index + 1}</h5>
                <select
                  value={ingredient.Ingredient_Name}
                  onChange={(e) =>
                    handleIngredientChange(
                      index,
                      "Ingredient_Name",
                      e.target.value
                    )
                  }
                  className="form-control mb-2"
                >
                  <option value="" disabled>
                    {" "}
                    --Select an Ingredient
                  </option>
                  {defaultIngredients.map((defaultIngredient, index) => (
                    <option key={index} value={defaultIngredient}>
                      {defaultIngredient}
                    </option>
                  ))}
                </select>
                <input
                  type="number"
                  placeholder="Quantity"
                  value={ingredient.Quantity}
                  onChange={(e) =>
                    handleIngredientChange(
                      index,
                      "Quantity",
                      parseFloat(e.target.value)
                    )
                  }
                  className="form-control mb-2"
                />
                <select
                  value={ingredient.Unit}
                  onChange={(e) =>
                    handleIngredientChange(index, "Unit", e.target.value)
                  }
                  className="form-control mb-2"
                >
                  <option value="" disabled>
                    {" "}
                    --Select a unit of measurement
                  </option>
                  {units.map((unit, index) => (
                    <option key={index} value={unit}>
                      {unit}
                    </option>
                  ))}
                </select>
                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={() => deleteIngredient(index)}
                >
                  Remove Ingredient
                </button>
              </div>
            ))}

            <button
              type="button"
              className="btn btn-primary mt-3"
              onClick={addIngredient}
            >
              Add Ingredient
            </button>
          </div>
          <h4 className="mt-4">Directions</h4>
          <textarea
            id="rdirections"
            name="Directions"
            style={{ height: "250px" }}
            onChange={handleChange}
          ></textarea>
          <br></br>

          <button
            type="button"
            className="btn btn-success mt-3"
            onClick={handleSubmit}
          >
            Submit Recipe
          </button>
        </div>
      </div>
    </div>
  );
};

export default RecipeBuilder;
