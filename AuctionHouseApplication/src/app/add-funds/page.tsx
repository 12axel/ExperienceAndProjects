'use client';

import React, { useState, useEffect } from 'react';
import axios from 'axios';

const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

export default function AddFundsPage() {
  const [username, setUsername] = useState<string | null>(null);
  const [currentBalance, setCurrentBalance] = useState<number | null>(null);
  const [amount, setAmount] = useState<string>(''); // Use string to handle empty state
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const storedUsername = sessionStorage.getItem("userName");
    if (storedUsername) {
      setUsername(storedUsername);
      fetchCurrentBalance(storedUsername);
    } else {
      setError("Username not found. Please log in.");
    }
  }, []);

  const fetchCurrentBalance = async (username: string) => {
    setError(null);
    setMessage(null);

    const payload = {
      body: {
        username,
        password: sessionStorage.getItem("password"), // Include password like in Navbar
      },
    };

    try {
      const response = await axios.post(`${baseURL}/get-buyer-information`, payload, {
        headers: { "Content-Type": "application/json" },
      });

      console.log("Full backend response for current balance:", response);

      if (response.data?.body) {
        const parsedBody =
          typeof response.data.body === "string"
            ? JSON.parse(response.data.body)
            : response.data.body;

        const accountFunds = parsedBody?.AccountFunds;
        if (accountFunds !== undefined) {
          setCurrentBalance(accountFunds);

          // Update session storage for consistency with the navbar
          sessionStorage.setItem("currentFunds", accountFunds.toString());
        } else {
          throw new Error("AccountFunds field is missing in the response.");
        }
      } else {
        throw new Error("Unexpected response format.");
      }
    } catch (error) {
      console.error("Error fetching current balance:", error);
      setError("Failed to fetch current balance. Please try again.");
    }
  };

  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (/^\d*$/.test(value)) {
      setAmount(value);
    }
  };

  const handleAddFunds = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    setError(null);

    try {
        const username = sessionStorage.getItem("userName");

        if (!username) {
            setError("User is not logged in.");
            return;
        }

        const payload = {
            body: {
                username,
                amount: parseInt(amount, 10),
            },
        };

        console.log("Sending request to add funds:", payload);

        const response = await axios.post(`${baseURL}/add-funds`, payload, {
            headers: { "Content-Type": "application/json" },
        });

        console.log("Received response after adding funds:", response);

        if (response.status === 200 && response.data) {
            const parsedBody =
                typeof response.data.body === "string"
                    ? JSON.parse(response.data.body)
                    : response.data.body;

            const updatedFunds = parsedBody?.updatedFunds;

            if (updatedFunds !== undefined) {
                setMessage("Funds added successfully!");
                setCurrentBalance(updatedFunds); // Update the balance in state

                // Update session storage for consistency with the navbar
                sessionStorage.setItem("currentFunds", updatedFunds.toString());

                // Emit custom event for Navbar to update funds
                window.dispatchEvent(new CustomEvent("fundsUpdated", { detail: { newFunds: updatedFunds } }));

                setAmount(''); // Reset the amount input field
            } else {
                console.error("Updated funds missing in response:", response.data);
                setError("Unexpected response format from server. Please try again.");
            }
        } else {
            setError("Failed to update funds. Please try again.");
        }
    } catch (err: any) {
        console.error("Error updating funds:", err);

        if (err.response && err.response.data && err.response.data.message) {
            setError(err.response.data.message);
        } else {
            setError("Failed to update funds. Please try again.");
        }
    } 
  };


  return (
    <main className="container mx-auto mt-5">
      <h1 className="text-4xl mb-6">Add Funds</h1>

      {/* Display current balance */}
      {currentBalance !== null ? (
        <div className="text-gray-700 mb-4">Current Balance: ${currentBalance}</div>
      ) : (
        <div className="text-gray-500 mb-4">Loading balance...</div>
      )}

      {/* Display success or error messages */}
      {message && <div className="text-green-500 mb-4">{message}</div>}
      {error && <div className="text-red-500 mb-4">{error}</div>}

      <form onSubmit={handleAddFunds} className="space-y-4">
        <div>
          <label className="block text-lg mb-2">Enter Amount</label>
          <input
            type="text"
            value={amount}
            onChange={handleAmountChange}
            required
            className="w-full p-2 border border-gray-300 rounded"
            placeholder="Enter amount to add"
          />
        </div>

        <button
          type="submit"
          className="w-full p-2 mt-4 bg-blue-500 text-white rounded hover:bg-blue-600"
        >
          Add Funds
        </button>
      </form>
    </main>
  );
}
