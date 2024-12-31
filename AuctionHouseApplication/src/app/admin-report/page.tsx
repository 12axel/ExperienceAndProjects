'use client';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

const AdminFunds = () => {
    const [funds, setFunds] = useState<number | null>(null);
    const [auctionData, setAuctionData] = useState<any[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [unsoldCount, setUnsoldCount] = useState<number>(0);
    const [totalItems, setTotalItems] = useState<number>(0);
    const [soldCount, setSoldCount] = useState<number>(0);

    useEffect(() => {
        const fetchAdminFunds = async () => {
            const username = sessionStorage.getItem("userName");
            const password = sessionStorage.getItem("password");

            if (!username || !password) {
                setError("Please log in as an admin to view funds.");
                return;
            }

            try {
                const response = await axios.post(`${baseURL}/get-admin-information`, {
                    body: {
                        username,
                        password,
                    }
                }, {
                    headers: { 'Content-Type': 'application/json' },
                });
                const res = JSON.parse(response.data.body);

                if (res && res.TotalACFunds !== undefined) {
                    setFunds(res.TotalACFunds);
                } else {
                    setError("Failed to fetch funds. Please try again later.");
                }
            } catch (err) {
                console.error("Error fetching funds:", err);
                setError("An error occurred while fetching funds.");
            }
        };

        const fetchAuctionData = async () => {
            const username = sessionStorage.getItem("userName");
            const password = sessionStorage.getItem("password");

            if (!username || !password) {
                setError("Please log in as an admin to view auction data.");
                return;
            }
            try {
                const response = await axios.post(`${baseURL}/getAuctionForensics`, {
                    body: {
                        username,
                        password,
                    }
                }, {
                    headers: { 'Content-Type': 'application/json' },
                });
                const res = JSON.parse(response.data.body);

                if (Array.isArray(res)) {
                    const groupedByItem = res.reduce((acc, item) => {
                        if (!acc[item.ItemName] || item.BidAmount > acc[item.ItemName].BidAmount) {
                            acc[item.ItemName] = item;
                        }
                        return acc;
                    }, {});

                    const uniqueItems = Object.values(groupedByItem);

                    const total = uniqueItems.length;
                    const unsoldItems = uniqueItems.filter((item: any) => item.SoldDate === null).length;
                    const soldItems = total - unsoldItems;

                    const processedData = uniqueItems.map((item: any) => ({
                        ...item,
                        Buyer: item.SoldDate ? item.Buyer : "Not Sold",
                        AverageBid: ((item.HighestBid + item.LowestBid) / 2).toFixed(2),
                    }));

                    setAuctionData(processedData);
                    setUnsoldCount(unsoldItems);
                    setSoldCount(soldItems);
                    setTotalItems(total);
                } else {
                    setError("Failed to fetch auction data. Please try again later.");
                }
            } catch (err) {
                console.error("Error fetching auction data:", err);
                setError("An error occurred while fetching auction data.");
            }
        };

        fetchAdminFunds();
        fetchAuctionData();
    }, []);

    return (
        <div className="container mx-auto mt-5">
            <div className="px-8 pt-6 pb-8 mb-4 border">
                <h1 className="text-3xl font-bold mb-6">Admin Dashboard</h1>

                {error && (
                    <p className="text-red-500 mb-4">{error}</p>
                )}

                {funds !== null && (
                    <div className="mb-8">
                        <h2 className="text-2xl font-bold">Total Funds</h2>
                        <p className="text-gray-700 text-lg">
                            <span className="font-bold">${funds.toFixed(2)}</span>
                        </p>
                    </div>
                )}

                <h2 className="text-2xl font-bold mb-4">Auction Forensics</h2>

                {auctionData.length > 0 ? (
                    <>
                        <div className="mb-4">
                            <p className="text-lg font-bold">Total Items: <span className="text-blue-500">{totalItems}</span></p>
                            <p className="text-lg font-bold">Total Sold Items: <span className="text-green-500">{soldCount}</span></p>
                            <p className="text-lg font-bold">Total Unsold Items: <span className="text-red-500">{unsoldCount}</span></p>
                        </div>

                        <div className="overflow-x-auto overflow-y-auto max-h-96 mt-6 border border-gray-300 rounded-md">
                            <table className="table-auto w-full text-left border-collapse">
                                <thead>
                                    <tr className="bg-gray-100">
                                        <th className="px-4 py-2 border border-gray-300">Item Name</th>
                                        <th className="px-4 py-2 border border-gray-300">Initial Price</th>
                                        <th className="px-4 py-2 border border-gray-300">Highest Bid</th>
                                        <th className="px-4 py-2 border border-gray-300">Lowest Bid</th>
                                        <th className="px-4 py-2 border border-gray-300">Bid Amount</th>
                                        <th className="px-4 py-2 border border-gray-300">Average Bid</th>
                                        <th className="px-4 py-2 border border-gray-300">Sold Date</th>
                                        <th className="px-4 py-2 border border-gray-300">Total Bids</th>
                                        <th className="px-4 py-2 border border-gray-300">Seller</th>
                                        <th className="px-4 py-2 border border-gray-300">Buyer</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {auctionData.map((auction, index) => (
                                        <tr key={index} className={index % 2 === 0 ? "bg-gray-50" : ""}>
                                            <td className="px-4 py-2 border border-gray-300">{auction.ItemName}</td>
                                            <td className="px-4 py-2 border border-gray-300">${auction.InitialPrice}</td>
                                            <td className="px-4 py-2 border border-gray-300">${auction.HighestBid || "N/A"}</td>
                                            <td className="px-4 py-2 border border-gray-300">${auction.LowestBid || "N/A"}</td>
                                            <td className="px-4 py-2 border border-gray-300">${auction.BidAmount || "N/A"}</td>
                                            <td className="px-4 py-2 border border-gray-300">${auction.AverageBid}</td>
                                            <td className="px-4 py-2 border border-gray-300">{auction.SoldDate ? new Date(auction.SoldDate.replace(" ", "T")).toLocaleDateString("en-US", {
                                                year: "numeric",
                                                month: "long",
                                                day: "numeric",
                                                hour: "2-digit",
                                                minute: "2-digit",
                                                second: "2-digit",
                                                hour12: false,
                                            }) : "Not Sold"}</td>
                                            <td className="px-4 py-2 border border-gray-300">{auction.TotalBids}</td>
                                            <td className="px-4 py-2 border border-gray-300">{auction.Seller}</td>
                                            <td className="px-4 py-2 border border-gray-300">{auction.Buyer}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </>
                ) : (
                    <p className="text-gray-500">No auction data available.</p>
                )}
            </div>
        </div>
    );
};

export default AdminFunds;
