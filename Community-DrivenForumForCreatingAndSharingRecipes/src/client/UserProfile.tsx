import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import Swal from "sweetalert2";

interface Review {
  Rating: number;
  Comment: string;
  Recipe_Name?: string; // Add Recipe_Name for user reviews
}

interface Recipe {
  Recipe_ID: number;
  Recipe_Name: string;
  Prep_Time: number;
  Cook_Time: number;
  Calories: number;
  Servings: number;
  Directions: string;
  Saves: number;
  Average_Rating: number;
  Reviews: Review[];
}

interface UserProfileProps {
  email: string;
}

const UserProfile: React.FC<UserProfileProps> = ({ email }) => {
  const [createdRecipes, setCreatedRecipes] = useState<Recipe[]>([]);
  const [savedRecipes, setSavedRecipes] = useState<Recipe[]>([]);
  const [userReviews, setUserReviews] = useState<Review[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCreatedRecipes = async () => {
      try {
        const response = await fetch("/user-created-recipes", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email }),
        });
        if (!response.ok) {
          throw new Error("Failed to fetch created recipes");
        }
        const data = await response.json();
        setCreatedRecipes(data || []);
      } catch (error) {
        console.error("Error fetching created recipes:", error);
        if (error instanceof Error) {
          setError(error.message);
        } else {
          setError("An unknown error occurred");
        }
      }
    };

    const fetchSavedRecipes = async () => {
      try {
        const response = await fetch("/saved-recipes", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email }),
        });
        if (!response.ok) {
          throw new Error("Failed to fetch saved recipes");
        }
        const data = await response.json();
        setSavedRecipes(data || []);
      } catch (error) {
        console.error("Error fetching saved recipes:", error);
        if (error instanceof Error) {
          setError(error.message);
        } else {
          setError("An unknown error occurred");
        }
      }
    };

    const fetchUserReviews = async () => {
      try {
        const response = await fetch("/user-reviews", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email }),
        });
        if (!response.ok) {
          throw new Error("Failed to fetch user reviews");
        }
        const data = await response.json();
        setUserReviews(data || []);
      } catch (error) {
        console.error("Error fetching user reviews:", error);
        if (error instanceof Error) {
          setError(error.message);
        } else {
          setError("An unknown error occurred");
        }
      }
    };

    fetchCreatedRecipes();
    fetchSavedRecipes();
    fetchUserReviews();
  }, [email]);

  const handleDeleteRecipe = async (recipeId: number) => {
    try {
      const response = await fetch("/delete-recipe", {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, recipeId }),
      });

      if (!response.ok) {
        throw new Error(
          "Cannot delete a high-quality recipe with an average rating over 4.0."
        );
      }

      setCreatedRecipes((prevRecipes) =>
        prevRecipes.filter((recipe) => recipe.Recipe_ID !== recipeId)
      );
      Swal.fire({
        title: "Recipe Deleted",
        text: "The recipe has been deleted successfully!",
        icon: "success",
        confirmButtonText: "OK",
      });
    } catch (err) {
      Swal.fire({
        title: "Error",
        text: "Cannot delete a high-quality recipe with an average rating over 4.0.",
        icon: "error",
        confirmButtonText: "OK",
      });
    }
  };

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div className="container mt-4">
      <h1 className="text-center mb-4">User Profile</h1>

      <h2>Created Recipes</h2>
      {createdRecipes.length === 0 ? (
        <div>No recipes created by this user.</div>
      ) : (
        <div className="list-group">
          {createdRecipes.map((recipe) => (
            <div
              key={recipe.Recipe_ID}
              className="list-group-item d-flex justify-content-between align-items-center"
            >
              <div>
                <h5 className="mb-1">{recipe.Recipe_Name}</h5>
                <small>Calories: {recipe.Calories}</small>
              </div>
              <IconButton
                onClick={() => handleDeleteRecipe(recipe.Recipe_ID)}
                aria-label="delete"
              >
                <DeleteIcon />
              </IconButton>
            </div>
          ))}
        </div>
      )}
      <h2>Saved Recipes</h2>
      {savedRecipes.length === 0 ? (
        <div>No recipes saved by this user.</div>
      ) : (
        <div className="list-group">
          {savedRecipes.map((recipe) => (
            <div key={recipe.Recipe_ID} className="list-group-item">
              <h5 className="mb-1">{recipe.Recipe_Name}</h5>
              <small>Calories: {recipe.Calories}</small>
            </div>
          ))}
        </div>
      )}

      <h2>User Reviews</h2>
      {userReviews.length === 0 ? (
        <div>No reviews left by this user.</div>
      ) : (
        <div className="list-group">
          {userReviews.map((review, index) => (
            <div key={index} className="list-group-item">
              <h5 className="mb-1">{review.Recipe_Name}</h5>
              <small>Rating: {review.Rating}</small>
              <p>{review.Comment}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UserProfile;
