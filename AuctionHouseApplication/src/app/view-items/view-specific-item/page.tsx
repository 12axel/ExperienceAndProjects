"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import { Carousel } from "react-responsive-carousel";
import "react-responsive-carousel/lib/styles/carousel.min.css";
import LoadingSpinner from "@/app/components/LoadingSpinner";

const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

interface ItemDetailsProps {
    BuyerSoldTo: string;
    SoldDate: string;
    BidEndDate: string;
    BidStartDate: string;
    Description: string;
    HighestBid: number;
    Images: string[];
    InitialPrice: number;
    IsBuyNow: number;
    IsExpired: number;
    IsSold: number;
    IsFrozen: number;
    ItemID: number;
    Name: string;
};

interface BiddingHistoryProps {
    AmountBid: number;
    PlacementDate: string;
    RelatedBuyer: string;
};

interface BuyerDetailsProps {
    CanPlaceCustomBid: boolean;
    CanPlaceHigherBid: false;
    Username: string;
};

interface BuyNowResponseDataBodyProps {
    ActivityStatus: string;
    BidEndDate: string;
    BidStartDate: string;
    Bids: {
        AmountBid: number;
        BidID: number;
        PlacementDate: string;
        RelatedBuyer: string;
        RelatedItemID: number;
    }[];
    BuyerSoldTo: string | null;
    Creator: string;
    InitialPrice: number;
    IsBuyNow: number;
    IsFrozen: number;
    ItemDescription: string;
    ItemID: number;
    Name: string;
    PublishedDate: string;
    SoldDate: string | null;
    requestedUnfreeze: number;
};

export default function ViewSpecificItem() {
    const [itemDetails, setItemDetails] = useState<ItemDetailsProps | null>(null);
    const [biddingHistory, setBiddingHistory] = useState<BiddingHistoryProps[]>([]);
    const [placeBidLoading, setPlaceBidLoading] = useState<boolean>(false);
    const [buyerDetails, setBuyerDetails] = useState<BuyerDetailsProps | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [placeBidError, setPlaceBidError] = useState<string>('');
    const [placeBidSuccess, setPlaceBidSuccess] = useState<string>('');
    const [Bid, setBid] = React.useState<string>(''); // Get user type from session storage
    const [userType, setUserType] = useState<string>('');
    const [userName, setUserName] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [currentItem, setCurrentItem] = useState<string>('');
    const [buyNowSuccess, setBuyNowSuccess] = useState<string>('');
    const [buyNowError, setBuyNowError] = useState<string>('');
    const [buyNowLoading, setBuyNowLoading] = useState<boolean>(false);

    useEffect(() => {
        console.log("itemDetails: ", itemDetails);
        console.log("biddingHistory: ", biddingHistory);
        console.log("buyerDetails: ", buyerDetails);
    }, [itemDetails, biddingHistory, buyerDetails]);

    const updateItemsActivityStatus = async () => {
        try {
            const response = await axios.get(`${baseURL}/update-items-activity-status`);
            const data = JSON.parse(response.data.body);
            console.log("Response from getReviewSpecificItem:", JSON.parse(response.data.body));
            return 1;
        } catch (error) {
            console.error("Error updating items activity status: ", error);
            return 0;
        }
    };



    const fetchItemDetails = async () => {
        const itemID = currentItem;
        if (!itemID) {
            setError("No item selected.");
            setLoading(false);
            return;
        }

        const updatedItems = await updateItemsActivityStatus();

        if (updatedItems === 1) {
            try {
                const payload = {
                    body: {
                        username: userName,
                        password: password,
                        itemID: parseInt(itemID),
                        userType, // Include userType in the request
                    },
                };

                const response = await axios.post(`${baseURL}/view-specific-item`, payload, {
                    headers: { "Content-Type": "application/json" },
                });

                const data = JSON.parse(response.data.body);
                setItemDetails(data.itemDetails);
                setBiddingHistory(data.biddingHistory);
                setBuyerDetails(data.buyerDetails);
            } catch (err) {
                console.error("Error fetching item details:", err);
                setError("Failed to fetch item details.");
            } finally {
                setLoading(false);
            }
        } else {
            console.error("Error updating items activity status.");
        }
    };


    useEffect(() => {
        const currentItemIDVal = sessionStorage.getItem("viewItemID");
        const userNameVal = sessionStorage.getItem("userName");
        const passwordVal = sessionStorage.getItem("password");
        const userTypeVal = sessionStorage.getItem("userType");
        if(currentItemIDVal && userNameVal && passwordVal && userTypeVal){
            setCurrentItem(currentItemIDVal);
            setUserName(userNameVal);
            setPassword(passwordVal);
            setUserType(userTypeVal);
        }

    }, []);

    useEffect(() => {
        if (currentItem && userName && password && userType) {
            fetchItemDetails();
          }
    }, [currentItem, userName, password, userType]);

    const placeBid = async () => {
        setPlaceBidError('');
        setPlaceBidLoading(true);
        console.log("placing Bid");
        const itemID = currentItem;
        if (!itemID) {
            setError("No item selected.");
            return;
        }
        const payload = {
            body: {
                username: userName,
                password: password,
                itemID: parseInt(itemID),
                requestedBid: parseInt(Bid)
            },
        };
        const response = await axios.post(`${baseURL}/place-bid`, payload, {
            headers: { "Content-Type": "application/json" },
        });
        console.log(`response data: `, response.data)
        if (response.data.body) {
            const data = JSON.parse(response.data.body);
            console.log(`response data body: `, response.data.body)
            await fetchItemDetails();
            setPlaceBidLoading(false);
            setPlaceBidSuccess('true');
            setTimeout(() => {
                setPlaceBidSuccess('');
            }, 5000);
        }
        else {
            setPlaceBidLoading(false);
            const error = response.data.error;
            setPlaceBidError(error);
            console.log(`response data error: `, response.data.error)
        }

    }

    const placeNextHigherBid = async (theNextHighestBid: number) => {
        await fetchItemDetails();
        setPlaceBidError('');
        setPlaceBidLoading(true);
        console.log("placing Bid");
        const itemID = currentItem;
        if (!itemID) {
            setError("No item selected.");
            return;
        }
        const payload = {
            body: {
                username: userName,
                password: password,
                itemID: parseInt(itemID),
                requestedBid: theNextHighestBid
            },
        };
        const response = await axios.post(`${baseURL}/place-bid`, payload, {
            headers: { "Content-Type": "application/json" },
        });
        console.log(`response data: `, response.data)
        if (response.data.body) {
            const data = JSON.parse(response.data.body);
            console.log(`response data body: `, response.data.body)
            await fetchItemDetails();
            setPlaceBidLoading(false);
            setPlaceBidSuccess('true');
            setTimeout(() => {
                setPlaceBidSuccess('');
            }, 5000);
        }
        else {
            setPlaceBidLoading(false);
            const error = response.data.error;
            setPlaceBidError(error);
            console.log(`response data error: `, response.data.error)
        }
    }

    const handleBuyNow = async () => {
        setBuyNowError('');
        setBuyNowLoading(true);
        console.log("Buy Now button pressed");
        const itemID = currentItem;
        if (!itemID) {
            setError("No item selected.");
            return;
        }
        const payload = {
            body: {
                username: userName,
                password: password,
                itemID: parseInt(itemID),
            },
        };
        const response = await axios.post(`${baseURL}/buy-now`, payload, {
            headers: { "Content-Type": "application/json" },
        });
        console.log(`buy now response data: `, response.data)
        if (response.data.body) {
            const data = JSON.parse(response.data.body);
            console.log(`buy now response data body: `, data)
            console.log("Fetching the new item details after buy now");
            setBuyNowSuccess('You have successfully purchased the item!');
            setTimeout(() => {
                setBuyNowSuccess('');
            }, 5000);
            await fetchItemDetails();
            setBuyNowLoading(false);
        } else {
            setBuyNowLoading(false);
            const error = response.data.error;
            setBuyNowError(error);
            console.log(`buy now response data error: `, response.data.error)
        }
    }


    if (loading) {
        return <p className="text-center text-gray-600">Loading...</p>;
    }

    if (error) {
        return <p className="text-center text-red-500">{error}</p>;
    }

    if (!itemDetails) {
        return <p className="text-center text-gray-600">No details available for this item.</p>;
    }

    // Determine item type
    const itemType = itemDetails.IsBuyNow === 1 ? "Buy Now" : "Bidding";

    return (
        <div className="container mx-auto p-4">
            {/* Item Details */}
            <div className="bg-white shadow-sm rounded-lg p-6 mb-6">
                <h1 className="text-3xl font-bold text-gray-800 mb-4">{itemDetails.Name}</h1>
                <p className="text-gray-700 text-lg mb-4">{itemDetails.Description}</p>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <p className="text-gray-600 mb-2">
                            <strong className="text-gray-800">Type:</strong> {itemType}
                        </p>
                        <p className="text-gray-600 mb-2">
                            <strong className="text-gray-800">Price:</strong> ${itemDetails.InitialPrice}
                        </p>
                        {itemType === "Bidding" && (
                            <p className="text-gray-600 mb-2">
                                <strong className="text-gray-800">Highest Bid:</strong>{" "}
                                {itemDetails.HighestBid ? `$${itemDetails.HighestBid}` : "No bids yet"}
                            </p>
                        )}
                        {/* <p className="text-gray-600 mb-2">
                            <strong className="text-gray-800">Published Date:</strong>{" "}
                            {itemDetails.PublishedDate
                                ? new Date(itemDetails.PublishedDate.replace(" ", "T")).toLocaleDateString("en-US", {
                                      timeZone: "UTC",
                                      year: "numeric",
                                      month: "long",
                                      day: "numeric",
                                  })
                                : "N/A"}
                        </p> */}
                        <p className="text-gray-600 mb-2">
                            <strong className="text-gray-800">Expiration DateTime:</strong>{" "}
                            {itemDetails.BidEndDate
                                ? new Date(itemDetails.BidEndDate.replace(" ", "T")).toLocaleDateString("en-US", {
                                    year: "numeric",
                                    month: "long",
                                    day: "numeric",
                                    hour: "2-digit",
                                    minute: "2-digit",
                                    second: "2-digit",
                                    hour12: false,
                                })
                                : "N/A"}
                        </p>
                        <p className="text-gray-600 mb-2">
                            <strong className="text-gray-800">Status:</strong>{" "}
                            {itemDetails.IsSold
                                ? "Sold"
                                : itemDetails.IsExpired
                                    ? "Expired"
                                    : "Active"}
                        </p>
                        {itemDetails.IsSold == 1 && <p className="text-gray-600 mb-2">
                            <strong className="text-gray-800">Sold DateTime:</strong>{" "}
                            {itemDetails.BidEndDate
                                ? new Date(itemDetails.SoldDate.replace(" ", "T")).toLocaleDateString("en-US", {
                                    year: "numeric",
                                    month: "long",
                                    day: "numeric",
                                    hour: "2-digit",
                                    minute: "2-digit",
                                    second: "2-digit",
                                    hour12: false,
                                })
                                : "N/A"}
                        </p>}
                        {itemDetails.BuyerSoldTo && <p className="text-gray-600 mb-2">
                            <strong className="text-gray-800">Buyer Sold To:</strong>{" "}
                            {itemDetails.BuyerSoldTo}
                        </p>}
                        {/* Place bid and buy now functionality below */}
                        <div>
                            {/* Place bid field, buttons, and alerts */}
                            {userType === "Buyer" && itemType === "Bidding" && itemDetails.IsSold == 0 && itemDetails.IsExpired == 0 && itemDetails.IsFrozen == 0 && (
                                <div>
                                    <div>
                                        <label htmlFor="custom_bid" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Enter Custom Bid Amount:</label>
                                        <input type="text" id="custom_bid" className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 rounded my-3"
                                            placeholder="0" value={Bid} onChange={(event) => {
                                                const value = event.target.value.replace(/^0+|[^0-9]/g, "");
                                                setBid(value);
                                            }} required />
                                    </div>
                                    <button disabled={(Bid == '' || placeBidLoading) ? true : false} onClick={placeBid} className={
                                        !(Bid == '' || placeBidLoading) ? "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded my-3" : "bg-blue-500 text-white font-bold py-2 px-4 rounded opacity-50 cursor-not-allowed my-3"}>{placeBidLoading ? <LoadingSpinner></LoadingSpinner> : "Place Custom Entered Bid"}
                                    </button>
                                    <div className="text-black font-bold py-2 px-4">Or:</div>
                                    <button onClick={(event) => {
                                        console.log("button pressed");
                                        placeNextHigherBid(itemDetails.HighestBid + 1);
                                    }} className={
                                        !(placeBidLoading) ? "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded my-3" : "bg-blue-500 text-white font-bold py-2 px-4 rounded opacity-50 cursor-not-allowed my-3"}>{placeBidLoading ? <LoadingSpinner></LoadingSpinner> : "Place Next Highest Bid (The Item's Current Highest Bid + 1)"}
                                    </button>
                                </div>)}

                            {placeBidError != '' &&
                                <div className="mt-4 rounded bg-red-200 p-2">
                                    <h2 className="text-2xl">Error: </h2>
                                    <p className="text-xl">
                                        {placeBidError}
                                    </p>
                                </div>}
                            {placeBidSuccess != '' &&
                                <div className="mt-4 rounded bg-green-200 p-2">
                                    <h2 className="text-2xl">Success: </h2>
                                    <p className="text-xl">
                                        The bid has been successfully placed.
                                    </p>
                                </div>}

                            {/* Buy now button and alerts*/}
                            {(userType === "Buyer" && itemType === "Buy Now" && biddingHistory.length == 0 && itemDetails.IsSold == 0 && itemDetails.IsExpired == 0 && itemDetails.IsFrozen == 0) &&
                                <button
                                    className={(buyNowLoading) ?
                                        "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded opacity-50 cursor-not-allowed" :
                                        "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"}
                                    onClick={handleBuyNow}
                                    disabled={buyNowLoading}>
                                    {buyNowLoading ? <LoadingSpinner /> : "Buy Now"}
                                </button>}
                            {buyNowError != '' &&
                                <div className="mt-4 rounded bg-red-200 p-2">
                                    <h2 className="text-2xl">Error: </h2>
                                    <p className="text-xl">
                                        {buyNowError}
                                    </p>
                                </div>}
                            {buyNowSuccess != '' &&
                                <div className="mt-4 rounded bg-green-200 p-2">
                                    <h2 className="text-2xl">Success: </h2>
                                    <p className="text-xl">
                                        {buyNowSuccess}
                                    </p>
                                </div>
                            }
                            {itemType === "Bidding" && itemDetails.IsFrozen == 1 && (
                                <div>
                                    <div>
                                        <label htmlFor="frozen_item_bid" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">This item is frozen and cannot receive any new bids at the moment.</label>
                                    </div>
                                </div>)}
                            {itemType === "Buy Now" && itemDetails.IsFrozen == 1 && biddingHistory.length == 0 && (
                                <div>
                                    <div>
                                        <label htmlFor="frozen_item_bid" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">This item is frozen and cannot be bought at the moment.</label>
                                    </div>
                                </div>)}
                            {itemType === "Buy Now" && biddingHistory.length > 0 && (
                                <div>
                                    <div>
                                        <label htmlFor="frozen_item_bid" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">This item has been bought.</label>
                                    </div>
                                </div>)}
                        </div>
                    </div>
                    {itemDetails.Images && itemDetails.Images.length > 0 && (
                        <div>
                            <h2 className="text-xl font-semibold text-gray-800 mb-2">Images</h2>
                            <Carousel
                                showThumbs={true}
                                showStatus={false}
                                infiniteLoop
                                className="rounded-lg shadow-md"
                            >
                                {itemDetails.Images.map((imageUrl: string, index: number) => (
                                    <div key={index}>
                                        <img
                                            src={imageUrl}
                                            alt={`Image ${index + 1}`}
                                            className="w-full h-64 object-cover rounded-lg"
                                        />
                                    </div>
                                ))}
                            </Carousel>
                        </div>
                    )}
                </div>
            </div>


            {/* Bidding History */}
            {
                itemType === "Bidding" && (
                    <div className="bg-white shadow-sm rounded-lg p-6 mb-6">
                        <h2 className="text-2xl font-bold text-gray-800 mb-4">Bidding History</h2>
                        {biddingHistory.length > 0 ? (
                            <table className="w-full table-auto border-collapse border border-gray-300 text-sm">
                                <thead>
                                    <tr className="bg-gray-200 text-left">
                                        <th className="border border-gray-300 px-4 py-2">Buyer ID</th>
                                        <th className="border border-gray-300 px-4 py-2">Bid Value</th>
                                        <th className="border border-gray-300 px-4 py-2">DateTime Made</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {biddingHistory.map((bid, index) => (
                                        <tr key={index} className="hover:bg-gray-100">
                                            <td className="border border-gray-300 px-4 py-2">{bid.RelatedBuyer}</td>
                                            <td className="border border-gray-300 px-4 py-2">${bid.AmountBid}</td>
                                            <td className="border border-gray-300 px-4 py-2">
                                                {new Date(bid.PlacementDate.replace(" ", "T")).toLocaleDateString("en-US", {
                                                    year: "numeric",
                                                    month: "long",
                                                    day: "numeric",
                                                    hour: "2-digit",
                                                    minute: "2-digit",
                                                    second: "2-digit",
                                                    hour12: false,
                                                })}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        ) : (
                            <p className="text-gray-600">No bidding history available.</p>
                        )}
                    </div>
                )
            }
            {/* Buy Now History */}
            {
                itemType === "Buy Now" && (
                    <div className="bg-white shadow-sm rounded-lg p-6 mb-6">
                        <h2 className="text-2xl font-bold text-gray-800 mb-4">Purchase History</h2>
                        {biddingHistory.length > 0 ? (
                            <table className="w-full table-auto border-collapse border border-gray-300 text-sm">
                                <thead>
                                    <tr className="bg-gray-200 text-left">
                                        <th className="border border-gray-300 px-4 py-2">Buyer ID</th>
                                        <th className="border border-gray-300 px-4 py-2">Purchase Price</th>
                                        <th className="border border-gray-300 px-4 py-2">DateTime Purchased</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {biddingHistory.map((bid, index) => (
                                        <tr key={index} className="hover:bg-gray-100">
                                            <td className="border border-gray-300 px-4 py-2">{bid.RelatedBuyer}</td>
                                            <td className="border border-gray-300 px-4 py-2">${bid.AmountBid}</td>
                                            <td className="border border-gray-300 px-4 py-2">
                                                {new Date(bid.PlacementDate.replace(" ", "T")).toLocaleDateString("en-US", {
                                                    year: "numeric",
                                                    month: "long",
                                                    day: "numeric",
                                                    hour: "2-digit",
                                                    minute: "2-digit",
                                                    second: "2-digit",
                                                    hour12: false,
                                                })}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        ) : (
                            <p className="text-gray-600">This item has not been purchased.</p>
                        )}
                    </div>
                )
            }
        </div >
    );
}
