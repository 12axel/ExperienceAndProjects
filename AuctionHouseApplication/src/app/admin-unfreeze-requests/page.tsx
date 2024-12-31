"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";

const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

const AdminUnfreezeRequests = () => {
    const [unfreezeRequests, setUnfreezeRequests] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // Fetch unfreeze requests
    const fetchUnfreezeRequests = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.post(`${baseURL}/fetch-unfreeze-request`, {
                body: { action: "fetchRequests" },
            });

            // Parse the `body` field correctly
            const parsedData = JSON.parse(response.data.body);
            setUnfreezeRequests(parsedData); // Save parsed data to state
        } catch (error) {
            console.error("Error fetching unfreeze requests:", error);
            setError("Failed to load unfreeze requests.");
        } finally {
            setLoading(false);
        }
    };

    // Handle Unfreeze action
    const handleUnfreeze = async (itemID: number) => {
        try {
            const response = await axios.post(`${baseURL}/fetch-unfreeze-request`, {
                body: { action: "unfreeze", ItemID: itemID },
            });

            if (response.status === 200) {
                alert("Item successfully unfrozen!");
                fetchUnfreezeRequests(); // Refresh the table
            } else {
                alert("Failed to unfreeze the item.");
            }
        } catch (error) {
            console.error("Error unfreezing item:", error);
            alert("An error occurred while unfreezing the item.");
        }
    };

    // Handle Deny action
    const handleDeny = async (itemID: number) => {
        try {
            const response = await axios.post(`${baseURL}/fetch-unfreeze-request`, {
                body: { action: "deny", ItemID: itemID },
            });

            if (response.status === 200) {
                alert("Unfreeze request denied!");
                fetchUnfreezeRequests(); // Refresh the table
            } else {
                alert("Failed to deny the unfreeze request.");
            }
        } catch (error) {
            console.error("Error denying request:", error);
            alert("An error occurred while denying the unfreeze request.");
        }
    };

    useEffect(() => {
        fetchUnfreezeRequests();
    }, []);

    return (
        <main className="container mx-auto mt-5">
            <h1 className="text-4xl mb-6">Unfreeze Requests</h1>

            {loading ? (
                <p>Loading...</p>
            ) : error ? (
                <p className="text-red-500">{error}</p>
            ) : (
                <table className="table-auto w-full border-collapse border border-gray-300">
                    <thead>
                        <tr className="bg-gray-200">
                            <th className="border border-gray-300 px-4 py-2">Item ID</th>
                            <th className="border border-gray-300 px-4 py-2">Name</th>
                            <th className="border border-gray-300 px-4 py-2">Description</th>
                            <th className="border border-gray-300 px-4 py-2">Creator</th>
                            <th className="border border-gray-300 px-4 py-2">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {unfreezeRequests.map((item: any) => (
                            <tr key={item.ItemID}>
                                <td className="border border-gray-300 px-4 py-2">{item.ItemID}</td>
                                <td className="border border-gray-300 px-4 py-2">{item.Name}</td>
                                <td className="border border-gray-300 px-4 py-2">{item.Description}</td>
                                <td className="border border-gray-300 px-4 py-2">{item.Creator}</td>
                                <td className="border border-gray-300 px-4 py-2 space-x-2">
                                    <button
                                        onClick={() => handleUnfreeze(item.ItemID)}
                                        className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                                    >
                                        Unfreeze
                                    </button>
                                    <button
                                        onClick={() => handleDeny(item.ItemID)}
                                        className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                                    >
                                        Deny
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </main>
    );
};

export default AdminUnfreezeRequests;
