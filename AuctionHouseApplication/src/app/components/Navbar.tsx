'use client';
import Link from 'next/link';
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

const Navbar = () => {
    const router = useRouter();
    const [userType, setUserType] = useState<string | null>(null);
    const [userName, setUserName] = useState<string | null>(null);
    const [funds, setFunds] = useState<number | null>(null);

    const syncSessionState = () => {
        const storedUserType = sessionStorage.getItem('userType');
        const storedUserName = sessionStorage.getItem('userName');
        setUserType(storedUserType || null);
        setUserName(storedUserName || null);
    };

    const fetchFunds = async () => {
        const username = sessionStorage.getItem("userName");
        const password = sessionStorage.getItem("password");
        if (!username || !password) return;

        const payload = {
            body: {
                username,
                password,
            },
        };

        try {
            let fundsResponse = null;

            if (userType === "Seller") {
                const response = await axios.post(
                    `${baseURL}/get-seller-information`,
                    payload,
                    { headers: { "Content-Type": "application/json" } }
                );
                if (response.data?.body) {
                    const parsedBody = JSON.parse(response.data.body);
                    fundsResponse = parsedBody?.Funds;
                }
            } else if (userType === "Buyer") {
                const response = await axios.post(
                    `${baseURL}/get-buyer-information`,
                    payload,
                    { headers: { "Content-Type": "application/json" } }
                );
                if (response.data?.body) {
                    const parsedBody = JSON.parse(response.data.body);
                    fundsResponse = parsedBody?.AccountFunds;
                }
            } else if (userType === "Admin") {
                const response = await axios.post(
                    `${baseURL}/get-admin-information`,
                    payload,
                    { headers: { "Content-Type": "application/json" } }
                );
                if (response.data?.body) {
                    const parsedBody = JSON.parse(response.data.body);
                    fundsResponse = parsedBody?.TotalACFunds;
                }
            }

            if (fundsResponse) {
                sessionStorage.setItem('funds', fundsResponse.toString());
            }
            setFunds(fundsResponse || null);

            if (fundsResponse === null || fundsResponse === undefined) {
                throw new Error("Funds field is missing in the response.");
            }

            setFunds(fundsResponse);
        } catch (error) {
            console.error("Error retrieving funds:", error);
            setFunds(null);
        }
    };

    const handleCloseAccount = async () => {
        if (!userName) {
            alert("Username is not available. Please log in.");
            return;
        }

        if (!confirm("Are you sure you want to close your account? This action cannot be undone.")) {
            return;
        }

        try {
            const response = await axios.post(`${baseURL}/close-account`, {
                body: {
                    username: userName,
                    userType: userType,
                },
            }, {
                headers: { 'Content-Type': 'application/json' },
            });

            if (response.data.statusCode === 200) {
                alert("Account closed successfully!");
                sessionStorage.clear();
                setUserName(null);
                setUserType(null);
                setFunds(null);
                router.push('/');
            } else if (response.data.statusCode === 404) {
                alert("User not found.");
            } else if (response.data.statusCode === 400) {
                alert("Cannot close account with active auctions.");
            } else {
                alert("An unexpected error occurred.");
            }
        } catch (err) {
            console.error(err);
            alert("Error closing account. Please try again.");
        }
    };

    useEffect(() => {
        const handleSessionUpdate = () => {
            syncSessionState();
        };

        syncSessionState();

        window.addEventListener('sessionUpdated', handleSessionUpdate);

        return () => {
            window.removeEventListener('sessionUpdated', handleSessionUpdate);
        };
    }, []);

    useEffect(() => {
        if (userType) {
            fetchFunds();
        }
    }, [userType]);

    const handleLoginLogout = () => {
        if (userName) {
            sessionStorage.clear();
            setUserName(null);
            setUserType(null);
            setFunds(null);
            router.push('/');
        } else {
            router.push('/login-account');
        }
    };

    return (
        <div>
            <nav className="bg-gray-800">
                <div className="container mx-auto">
                    <div className="relative flex items-center justify-between h-16">
                        <div className="flex-1 flex items-center justify-center sm:items-stretch sm:justify-start">
                            <div className="flex-shrink-0">
                                <Link href="/" className="text-white font-bold text-2xl">
                                    Eiffel Team
                                </Link>
                            </div>
                            <div className="hidden sm:block sm:ml-6">
                                <div className="flex space-x-4">
                                    {userType !== 'Seller' && (
                                        <Link
                                            href="/view-items"
                                            className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                        >
                                            View Active Items
                                        </Link>
                                    )}
                                    {userType === 'Seller' ? (
                                        <>
                                            <Link
                                                href="/add-item"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                Add Item
                                            </Link>
                                            <Link
                                                href="/review-items"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                Review Items
                                            </Link>
                                        </>
                                    ) : userType === 'Admin' ? (
                                        <>
                                            <Link
                                                href="/admin-unfreeze-requests"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                Unfreeze Requests
                                            </Link>
                                            <Link
                                                href="/admin-report"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                Reports
                                            </Link>
                                            
                                        </>
                                    ) : userType === 'Buyer' ? (
                                        <>
                                            <Link
                                                href="/view-recently-sold"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                View Recently Sold Items
                                            </Link>
                                            <Link
                                                href="/review-active-bids"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                Review Active Bids
                                            </Link>
                                            <Link
                                                href="/review-purchases"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                Review Purchases
                                            </Link>
                                            <Link
                                                href="/add-funds"
                                                className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                            >
                                                Add Funds
                                            </Link>
                                        </>
                                    ) : null}
                                </div>
                            </div>
                        </div>
                        <div className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                            {userName && userType !== 'Admin' && funds !== null && funds >= 0 && (
                                <button
                                    onClick={async () => {
                                        await fetchFunds();
                                        let funds = sessionStorage.getItem('funds');
                                        if (!funds) {
                                            funds = (0).toString();
                                        }
                                        alert(`Your funds: $${funds}`);
                                    }}
                                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                >
                                    See Your Funds
                                </button>
                            )}
                            <button
                                onClick={handleLoginLogout}
                                className="bg-gray-900 text-white px-3 py-2 rounded-md text-sm font-medium"
                            >
                                {userName ? 'Logout' : 'Login'}
                            </button>
                            {!userName && (
                                <Link
                                    href="/create-account"
                                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                >
                                    Create Account
                                </Link>
                            )}
                            {userName && userType !== 'Admin' && (
                                <button
                                    onClick={handleCloseAccount}
                                    className="text-gray-300 hover:bg-red-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                                >
                                    Close Account
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            </nav>
        </div>
    );
};

export default Navbar;
