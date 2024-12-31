// RecipeBuilder.tsx
import React, { useState, useEffect, ChangeEvent } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

interface TopAverageRatingProps {
    Recipe_ID: number;
    Recipe_Name: string;
    Average_Rating: number;
};

interface MostSavesProps {
    Recipe_ID: number;
    Recipe_Name: string;
    Number_Of_Saves: number;
};

interface HealthiestProps {
    Recipe_ID: number;
    Recipe_Name: string;
    Calories: number;
};

const TopRecipes: React.FC<{ email: string }> = ({ email }) => {

    const [topRatedRecipes, setTopRatedRecipes] = useState<TopAverageRatingProps[]>([]);
    const [mostSavesRecipes, setMostSavesRecipes] = useState<MostSavesProps[]>([]);
    const [healthiestRecipes, setHealthiestRecipes] = useState<HealthiestProps[]>([]);


    const fetchTopRatedRecipes = async () => {
        try {
            const response = await fetch('/top-rated-recipes');
            if(!response.ok) {
                throw new Error('Failed to fetch top-rated recipes');
            }
            const data: TopAverageRatingProps[] = await response.json();
            setTopRatedRecipes(data);
        }
        catch (error){
            console.error('Error fetching top-rated recipes:', error);
        }
    }

    const fetchMostSavedRecipes = async () => {
        try {
            const response = await fetch('/most-saved-recipes');
            if(!response.ok) {
                throw new Error('Failed to fetch most recipes');
            }
            const data: MostSavesProps[] = await response.json();
            setMostSavesRecipes(data);
        }
        catch (error){
            console.error('Error fetching most saved recipes:', error);
        }
    }

    const fetchHealthiestRecipes = async () => {
        try {
            const response = await fetch('/healthiest-recipes');
            if(!response.ok) {
                throw new Error('Failed to fetch healthiest recipes');
            }
            const data: HealthiestProps[] = await response.json();
            setHealthiestRecipes(data);
        }
        catch (error){
            console.error('Error fetching healthiest recipes:', error);
        }
    }

    useEffect(() => {
        fetchTopRatedRecipes();
        fetchMostSavedRecipes();
        fetchHealthiestRecipes();
    }, [email]);


    return (
        <div className="mt-5 flex justify-center">
            <h2 className="text-left text-xl font-semibold mb-2">
                Top 10 Recipes by Average Rating:
            </h2>
            <table className="w-full table-auto border-collapse border border-gray-300 text-sm mb-4">
            <thead>
                <tr className="bg-gray-200 text-left">
                    <th className="border border-gray-300 px-4 py-2">Recipe ID</th>
                    <th className="border border-gray-300 px-4 py-2">Recipe Name</th>
                    <th className="border border-gray-300 px-4 py-2">Average Rating</th>
                </tr>
            </thead>
            <tbody>
                {topRatedRecipes.map((recipe, index) => (
                    <tr key={index} className="hover:bg-gray-100">
                        <td className="border border-gray-300 px-4 py-2">{recipe.Recipe_ID}</td>
                        <td className="border border-gray-300 px-4 py-2">{recipe.Recipe_Name}</td>
                        <td className="border border-gray-300 px-4 py-2">{recipe.Average_Rating}</td>
                    </tr>
                ))}
            </tbody>
        </table>
        <h2 className="text-left text-xl font-semibold mb-2">
                Top 10 Recipes by Number of Saves:
            </h2>
            <table className="w-full table-auto border-collapse border border-gray-300 text-sm mb-4">
            <thead>
                <tr className="bg-gray-200 text-left">
                    <th className="border border-gray-300 px-4 py-2">Recipe ID</th>
                    <th className="border border-gray-300 px-4 py-2">Recipe Name</th>
                    <th className="border border-gray-300 px-4 py-2">Number Of Saves</th>
                </tr>
            </thead>
            <tbody>
                {mostSavesRecipes.map((recipe, index) => (
                    <tr key={index} className="hover:bg-gray-100">
                        <td className="border border-gray-300 px-4 py-2">{recipe.Recipe_ID}</td>
                        <td className="border border-gray-300 px-4 py-2">{recipe.Recipe_Name}</td>
                        <td className="border border-gray-300 px-4 py-2">{recipe.Number_Of_Saves}</td>
                    </tr>
                ))}
            </tbody>
        </table>
        <h2 className="text-left text-xl font-semibold mb-2">
                Top 10 Recipes by Lowest Amount of Calories:
            </h2>
            <table className="w-full table-auto border-collapse border border-gray-300 text-sm mb-4">
            <thead>
                <tr className="bg-gray-200 text-left">
                    <th className="border border-gray-300 px-4 py-2">Recipe ID</th>
                    <th className="border border-gray-300 px-4 py-2">Recipe Name</th>
                    <th className="border border-gray-300 px-4 py-2">Amount Of Calories</th>
                </tr>
            </thead>
            <tbody>
                {healthiestRecipes.map((recipe, index) => (
                    <tr key={index} className="hover:bg-gray-100">
                        <td className="border border-gray-300 px-4 py-2">{recipe.Recipe_ID}</td>
                        <td className="border border-gray-300 px-4 py-2">{recipe.Recipe_Name}</td>
                        <td className="border border-gray-300 px-4 py-2">{recipe.Calories}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    </div>
    );
};

export default TopRecipes;