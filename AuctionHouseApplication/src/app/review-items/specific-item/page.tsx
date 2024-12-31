'use client'
import axios from "axios";
import React from "react";
import { Carousel } from "react-responsive-carousel";
import "react-responsive-carousel/lib/styles/carousel.min.css"
import { useRouter } from "next/navigation";
import LoadingSpinner from "@/app/components/LoadingSpinner";
import Table from "./Table";


const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

export default function Home() {
    const [itemID, setItemID] = React.useState<number | null>(null);
    const [itemName, setItemName] = React.useState<string | null>(null);
    const [itemDescription, setItemDescription] = React.useState<string | null>(null);
    const [initialPrice, setInitialPrice] = React.useState<number | null>(null);
    const [bidStartDate, setBidStartDate] = React.useState<string | null>(null);
    const [isABuyNow, setIsABuyNow] = React.useState<number>(0);
    const [bidEndDate, setBidEndDate] = React.useState<string | null>(null);
    const [bids, setBids] = React.useState<[]>([]);
    const [publishedDate, setPublishedDate] = React.useState<string | null>(null);
    const [soldDate, setSoldDate] = React.useState<string | null>(null);
    const [isFrozen, setIsFrozen] = React.useState<number | null>(null);
    const [requestedUnfreeze, setRequestedUnfreeze] = React.useState<number | null>(null);
    const [creator, setCreator] = React.useState<string | null>(null);
    const [buyerSoldTo, setBuyerSoldTo] = React.useState<string | null>(null);
    const [activityStatus, setActivityStatus] = React.useState<string | null>(null);
    const [pictures, setPictures] = React.useState<[] | null>(null);
    const router = useRouter();
    const [bidEndDateTooSoon, setBidEndDateTooSoon] = React.useState<boolean>(false); // to indicate if today's publish date will be later than the currently entered end date
    const [successfullyArchivedItem, setSuccessfullyArchivedItem] = React.useState<boolean>(false); // helper variable for disappearing success message

    // loading use state variables
    const [pageLoading, setPageLoading] = React.useState<boolean>(true);
    const [removeItemLoading, setRemoveItemLoading] = React.useState<boolean>(false);
    const [publishItemLoading, setPublishItemLoading] = React.useState<boolean>(false);
    const [unpublishItemLoading, setUnpublishItemLoading] = React.useState<boolean>(false);
    const [fulfillItemLoading, setFulfillItemLoading] = React.useState<boolean>(false);
    const [requestUnfreezeLoading, setRequestUnfreezeLoading] = React.useState<boolean>(false);

    // success and error messages variables
    const [fulfillItemSuccessMessage, setFulfillItemSuccessMessage] = React.useState<string>('');
    const [fulfillItemErrorMessage, setFulfillItemErrorMessage] = React.useState<string>('');

    React.useEffect(() => {

        const updateItemsActivityStatus = async () => {
            try {
                const response = await axios.get(`${baseURL}/update-items-activity-status`);
                const data = JSON.parse(response.data.body);
                console.log('Response from getReviewSpecificItem:', JSON.parse(response.data.body));
                return 1;

            } catch (error) {
                console.error('Error updating items activity status: ', error);
                return 0;
            }
        };

        const selectedItemID = sessionStorage.getItem('selectedItemID');
        if (selectedItemID) {
            console.log('Selected item ID:', selectedItemID);
            setItemID(Number(selectedItemID));

            const payload = {
                body: {
                    username: sessionStorage.getItem('userName'),
                    password: sessionStorage.getItem('password'),
                    itemID: selectedItemID,
                }
            };

            const getReviewSpecificItem = async () => {
                const updatedItems = await updateItemsActivityStatus();
                if (updatedItems == 1) {
                    try {
                        const response = await axios.post(`${baseURL}/review-items-specific-item`, payload, {
                            headers: {
                                'Content-Type': 'application/json',
                            },
                        });
                        console.log('Response from getReviewSpecificItem:', JSON.parse(response.data.body));

                        const item = JSON.parse(response.data.body);
                        setItemName(item.Name);
                        setItemDescription(item.ItemDescription);
                        setInitialPrice(item.InitialPrice);
                        setIsABuyNow(item.IsBuyNow);
                        setBidStartDate(item.BidStartDate);
                        setBidEndDate(item.BidEndDate);
                        setBids(item.Bids);
                        setPublishedDate(item.PublishedDate);
                        setSoldDate(item.SoldDate);
                        setIsFrozen(item.IsFrozen);
                        setRequestedUnfreeze(item.requestedUnfreeze);
                        setCreator(item.Creator);
                        setBuyerSoldTo(item.BuyerSoldTo);
                        setActivityStatus(item.ActivityStatus);
                        setPictures(item.Pictures);
                    } catch (error) {
                        console.error('Error getting specified item: ', error);
                    } finally {
                        setPageLoading(false);
                    }
                }
                else {
                    console.log("Failed to update items activity status.");
                }
            };

            getReviewSpecificItem();

        };
    }, []);

    // handle remove item
    const handleRemoveItem = async () => {
        setRemoveItemLoading(true);

        const payload = {
            body: {
                username: sessionStorage.getItem('userName'),
                password: sessionStorage.getItem('password'),
                itemID: itemID,
            }
        };

        try {
            const response = await axios.post(`${baseURL}/remove-inactive-item`, payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            console.log('Response from remove-inactive-item:', response.data.body);
        } catch (error) {
            console.error('Error removing Inactive item:', error);
        } finally {
            setRemoveItemLoading(false);
            sessionStorage.removeItem('selectedItemID');
            router.push('/review-items');
        }
    };

    const canPublishItem = () => {
        const currentDate = new Date();
        //currentDate.setTime(currentDate.getTime() - (currentDate.getTimezoneOffset() * 60000));
        const currentDateAsString = currentDate.toISOString();
        console.log(currentDateAsString);
        const currentDatePlus30Minutes = new Date(currentDate.getTime() + 30 * 60 * 1000);
        const currentDatePlus30MinutesAsString = currentDatePlus30Minutes.toISOString();
        console.log("currentDatePlus30MinutesAsString: " + currentDatePlus30MinutesAsString);
        console.log("bid end date: ",bidEndDate);
        if (activityStatus?.toLowerCase() === "inactive" && bidEndDate && bidEndDate >= currentDatePlus30MinutesAsString) {
            setBidStartDate(currentDateAsString);
            console.log('Can publish item');
            return true;
        } else if (bidEndDate && new Date(bidEndDate).toISOString() < currentDatePlus30MinutesAsString) {
            const isoString = new Date(bidEndDate).toISOString();
            console.log("Bid End Date ISO String: ", isoString);
            console.log('Bid end date is too soon');
            setBidEndDateTooSoon(true);
            return false;
        } else {
            return false;
        }
    };


    // handle publish item
    const handlePublishItem = async () => {
        setPublishItemLoading(true);

        const payload = {
            body: {
                username: sessionStorage.getItem('userName'),
                password: sessionStorage.getItem('password'),
                itemID: itemID,
            }
        };

        try {
            const response = await axios.post(`${baseURL}/publish-item`, payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            console.log('Response from publish-item:', JSON.parse(response.data.body));
            const updatedItem = JSON.parse(response.data.body);
            setItemName(updatedItem.Name);
            setItemDescription(updatedItem.ItemDescription);
            setInitialPrice(updatedItem.InitialPrice);
            setIsABuyNow(updatedItem.IsBuyNow);
            setBidStartDate(updatedItem.BidStartDate);
            setBidEndDate(updatedItem.BidEndDate);
            //setBids(updatedItem.Bids);
            setPublishedDate(updatedItem.PublishedDate);
            setSoldDate(updatedItem.SoldDate);
            setIsFrozen(updatedItem.IsFrozen);
            setRequestedUnfreeze(updatedItem.requestedUnfreeze);
            setCreator(updatedItem.Creator);
            setBuyerSoldTo(updatedItem.BuyerSoldTo);
            setActivityStatus(updatedItem.ActivityStatus);
        } catch (error) {
            console.error('Error publishing item:', error);
        } finally {
            setPublishItemLoading(false);
        }
    };

    // handle unpublish item
    const handleUnpublishItem = async () => {
        setUnpublishItemLoading(true);

        const payload = {
            body: {
                username: sessionStorage.getItem('userName'),
                password: sessionStorage.getItem('password'),
                itemID: itemID,
            }
        };

        try {
            const response = await axios.post(`${baseURL}/unpublish-item`, payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            console.log('Response from unpublish-item:', JSON.parse(response.data.body));
            const updatedItem = JSON.parse(response.data.body);
            setItemName(updatedItem.Name);
            setItemDescription(updatedItem.ItemDescription);
            setInitialPrice(updatedItem.InitialPrice);
            setIsABuyNow(updatedItem.IsBuyNow);
            setBidStartDate(updatedItem.BidStartDate);
            setBidEndDate(updatedItem.BidEndDate);
            setPublishedDate(updatedItem.PublishedDate);
            setSoldDate(updatedItem.SoldDate);
            setIsFrozen(updatedItem.IsFrozen);
            setRequestedUnfreeze(updatedItem.requestedUnfreeze);
            setCreator(updatedItem.Creator);
            setBuyerSoldTo(updatedItem.BuyerSoldTo);
            setActivityStatus(updatedItem.ActivityStatus);
            // setPictures(updatedItem.Pictures);
        } catch (error) {
            console.error('Error unpublishing item:', error);
        } finally {
            setUnpublishItemLoading(false);
        }
    };

    // handle fulfill item
    const handleFulfillItem = async () => {
        setFulfillItemLoading(true);
        const payload = {
            body: {
                username: sessionStorage.getItem('userName'),
                password: sessionStorage.getItem('password'),
                itemID: itemID,
            }
        };

        try {
            const response = await axios.post(`${baseURL}/fulfill-item`, payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            const statusCode = response.data.statusCode;
            if (statusCode === 200) {
                const fulfilledItem = JSON.parse(response.data.body);
                setSoldDate(fulfilledItem.SoldDate);
                setBuyerSoldTo(fulfilledItem.BuyerSoldTo)
                setActivityStatus('Archived');
                setFulfillItemLoading(false);
                setFulfillItemSuccessMessage('Item has been successfully fulfilled.');
                setTimeout(() => {
                    setFulfillItemSuccessMessage('');
                }, 5000);
            } else {
                console.error('Error fulfilling item:', response.data.error);
                setFulfillItemErrorMessage('Error fulfilling item: ' + response.data.error);
            }
        } catch (error) {
            console.error('Error fulfilling item:', error);
        }
    };

    // handle archive item
    const handleArchiveItem = async () => {
        const payload = {
            body: {
                username: sessionStorage.getItem('userName'),
                password: sessionStorage.getItem('password'),
                itemID: itemID,
            }
        };

        try {
            const response = await axios.post(`${baseURL}/archive-item`, payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            const statusCode = response.data.statusCode;
            if (statusCode === 200) {
                const updatedItem = JSON.parse(response.data.body);
                console.log('Response from archive-item:', JSON.parse(response.data.body));
                setItemName(updatedItem.Name);
                setItemDescription(updatedItem.ItemDescription);
                setInitialPrice(updatedItem.InitialPrice);
                setIsABuyNow(updatedItem.IsBuyNow);
                setBidStartDate(updatedItem.BidStartDate);
                setBidEndDate(updatedItem.BidEndDate);
                setPublishedDate(updatedItem.PublishedDate);
                setSoldDate(updatedItem.SoldDate);
                setIsFrozen(updatedItem.IsFrozen);
                setRequestedUnfreeze(updatedItem.requestedUnfreeze);
                setCreator(updatedItem.Creator);
                setBuyerSoldTo(updatedItem.BuyerSoldTo);
                setActivityStatus(updatedItem.ActivityStatus);
                // setPictures(updatedItem.Pictures);

                // to handle archive item success message
                setSuccessfullyArchivedItem(true);
                setTimeout(() => {
                    setSuccessfullyArchivedItem(false);
                }, 5000);
            } else {
                console.error('Error archiving item:', response.data.error);
            }
        } catch (error) {
            console.error('Error archiving item:', error);
        }
    };

    const handleRequestUnfreeze = async () => {
        setRequestUnfreezeLoading(true);

        const payload = {
            body: {
                username: sessionStorage.getItem('userName'),
                password: sessionStorage.getItem('password'),
                itemID: itemID,
            }
        };

        try {
            const response = await axios.post(`${baseURL}/request-unfreeze-item`, payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            console.log('Response from request-unfreeze-item:', JSON.parse(response.data.body));
            const updatedItem = JSON.parse(response.data.body);
            setItemName(updatedItem.Name);
            setItemDescription(updatedItem.ItemDescription);
            setInitialPrice(updatedItem.InitialPrice);
            setIsABuyNow(updatedItem.IsBuyNow);
            setBidStartDate(updatedItem.BidStartDate);
            setBidEndDate(updatedItem.BidEndDate);
            setPublishedDate(updatedItem.PublishedDate);
            setSoldDate(updatedItem.SoldDate);
            setIsFrozen(updatedItem.IsFrozen);
            setRequestedUnfreeze(updatedItem.requestedUnfreeze);
            setCreator(updatedItem.Creator);
            setBuyerSoldTo(updatedItem.BuyerSoldTo);
            setActivityStatus(updatedItem.ActivityStatus);
            // setPictures(updatedItem.Pictures);
        } catch (error) {
            console.error('Error requesting unfreeze item: ', error);
        } finally {
            setRequestUnfreezeLoading(false);
        }
    }

    // if fetching data is taking too long
    if (pageLoading) {
        return (<div className="flex items-center justify-center">
            <LoadingSpinner />;
        </div>)
    }

    return (
        <main>
            <div className="container mx-auto mt-5">
                {activityStatus?.toLowerCase() === "active" && isFrozen === 1 && (
                    <div className="mt-4 mb-4 rounded bg-red-200 p-2">
                        <h2 className="text-2xl">
                            Warning:
                        </h2>
                        <p className="text-xl">
                            {`This item has been frozen by the Auction House administrator. 
                            ${requestedUnfreeze ?
                                    'You have submitted a request to unfreeze this item. Please be patient.' :
                                    'You can request to unfreeze the item below.'}`}
                        </p>
                    </div>
                )}

                <h1 className="text-4xl rounded bg-slate-200 p-2">Item ID: {itemID}</h1>
                <h2 className="text-2xl mt-4 rounded bg-slate-200 p-2">Item Name: {itemName}</h2>
                <h2 className="text-2xl mt-4 rounded bg-slate-200 p-2">Item Activity Status: {activityStatus}</h2>
                <div className="mt-4 rounded bg-slate-200 p-2">
                    {pictures &&
                        <Carousel>
                            {pictures.map((picture: any, index: number) => (
                                <div key={index}>
                                    <img src={picture.URL} />
                                </div>
                            ))}
                        </Carousel>
                    }
                    {!pictures && <div>No pictures available</div>}
                </div>
                <div className="mt-4 rounded bg-slate-200 p-2">
                    <h2 className="text-2xl">Item Description: </h2>
                    <p className="text-xl">{itemDescription}</p>
                </div>
                <div className="mt-4 rounded bg-slate-200 p-2">
                    <h2 className="text-2xl">Initial Price: </h2>
                    <p className="text-xl">${initialPrice}</p>
                </div>
                <div className="mt-4 rounded bg-slate-200 p-2">
                    <h2 className="text-2xl">{isABuyNow ? "Purchase" : "Bid"} Start Date: </h2>
                    <p className="text-xl">{bidStartDate ? new Date(bidStartDate.replace(' ', 'T'))
                        .toLocaleDateString('en-US', {
                            month: '2-digit',
                            day: '2-digit',
                            year: 'numeric',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit',
                            hour12: false
                        }) : "Item is currently not published."}</p>
                </ div>
                <div className="mt-4 rounded bg-slate-200 p-2">
                    <h2 className="text-2xl">{isABuyNow ? "Purchase" : "Bid"} End Date: </h2>
                    <p className="text-xl">{bidEndDate ? new Date(bidEndDate.replace(' ', 'T'))
                        .toLocaleDateString('en-US', {
                            month: '2-digit',
                            day: '2-digit',
                            year: 'numeric',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit',
                            hour12: false
                        }) : null}</p>
                </div>
                <div className="mt-4 rounded bg-slate-200 p-2">
                    <h2 className="text-2xl">Published Date: </h2>
                    <p className="text-xl">{publishedDate ? new Date(publishedDate.replace(' ', 'T'))
                        .toLocaleDateString('en-US', {
                            month: '2-digit',
                            day: '2-digit',
                            year: 'numeric',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit',
                            hour12: false
                        }) : "Item is currently not published."}</p>
                </div>
                {activityStatus?.toLowerCase() === "archived" && bids.length > 0 &&
                    < div className="mt-4 rounded bg-slate-200 p-2">
                        <h2 className="text-2xl">Sold Date: </h2>
                        <p className="text-xl">{soldDate ? new Date(soldDate.replace(' ', 'T'))
                            .toLocaleDateString('en-US', {
                                month: '2-digit',
                                day: '2-digit',
                                year: 'numeric',
                                hour: '2-digit',
                                minute: '2-digit',
                                second: '2-digit',
                                hour12: false
                            }) : "Item has not been sold."}</p>
                    </div>}

                {activityStatus?.toLowerCase() === "archived" && bids.length > 0 &&
                    < div className="mt-4 rounded bg-slate-200 p-2">
                        <h2 className="text-2xl">Buyer Sold To: </h2>
                        <p className="text-xl">{buyerSoldTo ? buyerSoldTo : "Item has not been sold."}</p>
                    </div>}
                {/* Bid Table for Bids When Item is published */}
                {(activityStatus?.toLowerCase() === "active" || activityStatus?.toLowerCase() === "completed" || (activityStatus?.toLowerCase() === "archived")) &&
                    <div className="flex justify-center mt-4 py-6 rounded bg-slate-200">
                        {bids.length === 0 ?
                            <div>{isABuyNow == 0 ? "No bids have been placed" : "The item has not been purchased."}</div> :
                            <div>
                                <div>
                                    <h3 className="text-center text-2xl text-black-700">
                                        Table of {isABuyNow ? "the purchase made on the item." : "the bids made on the item."}
                                    </h3>
                                    <Table bids={bids} IsBuyNow={isABuyNow} />
                                </div>
                            </div>}
                    </div>}
                {/* Buttons */}
                
                    <div className="flex justify-between mt-4 mb-8">
                        {activityStatus?.toLowerCase() === "inactive" &&
                            <button
                                onClick={handleRemoveItem}
                                className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
                                {removeItemLoading ?
                                    <LoadingSpinner /> :
                                    "Remove Item"}
                            </button>}
                        {activityStatus?.toLowerCase() === "inactive" &&
                            <button
                                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                                onClick={() => {
                                    if (canPublishItem()) {
                                        handlePublishItem();
                                    }
                                }}>
                                {publishItemLoading ?
                                    <LoadingSpinner /> :
                                    "Publish Item"}
                            </button>}
                        {activityStatus?.toLowerCase() === "active" && bids.length === 0 && !isFrozen &&
                            <button
                                className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                                onClick={handleUnpublishItem}>
                                {unpublishItemLoading ?
                                    <LoadingSpinner /> :
                                    "Unpublish Item"}
                            </button>}
                        {activityStatus?.toLowerCase() === "inactive" &&
                            <button
                                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                                onClick={() => {
                                    sessionStorage.setItem('editItemID', itemID!.toString());
                                    router.push('/review-items/specific-item/edit-item');
                                }}>
                                Edit Details
                            </button>}
                        {(activityStatus?.toLowerCase() === "inactive" || activityStatus?.toLowerCase() === "failed") && (
                            <button
                                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                                onClick={handleArchiveItem}>
                                Archive Item
                            </button>)}
                        {activityStatus?.toLowerCase() === "completed" && !isFrozen &&(
                            <button
                                onClick={handleFulfillItem}
                                disabled={fulfillItemLoading}
                                className={!fulfillItemLoading ? "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" : "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded opacity-50 cursor-not-allowed"}>
                                {fulfillItemLoading ? <LoadingSpinner /> : "Fulfill Item"}
                            </button>
                        )}
                    </div>
                {/* Request Unfreeze */}
                {activityStatus?.toLowerCase() === "active" && isFrozen === 1 &&
                    <div className="flex justify-center items-center mt-4 mb-8">
                        {!requestedUnfreeze ? (
                            <button
                                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                                onClick={handleRequestUnfreeze}>
                                {requestUnfreezeLoading ?
                                    <LoadingSpinner /> :
                                    "Request Unfreeze"
                                }
                            </button>
                        ) :
                            (<div className="flex-auto flex justify-center items-center rounded bg-yellow-200 p-2">
                                <h2 className="text-2xl">
                                    You have submitted a request to unfreeze this item. Please be patient.
                                </h2>
                            </div>)}
                    </div>}
                {/* Fulfill Item success message */}
                {fulfillItemSuccessMessage &&
                    <div className="mt-4 rounded bg-green-200 p-2">
                        <h2 className="text-2xl">Success: </h2>
                        <p className="text-xl">{fulfillItemSuccessMessage}</p>
                    </div>}
                {/* Publish Item Error Message Below */}
                {bidEndDateTooSoon &&
                    <div className="mt-4 rounded bg-red-200 p-2">
                        <h2 className="text-2xl">Error: </h2>
                        <p className="text-xl">
                            {isABuyNow ? "Purchase" : "Bid"} End Date needs to be at least 30 minutes after the current dateTime. Please click "Edit Details" button to update the {isABuyNow ? "Purchase" : "Bid"} End Date.
                        </p>
                    </div>}
                {/* Archive Item Success Message */}
                {successfullyArchivedItem &&
                    <div className="mt-4 rounded bg-green-200 p-2">
                        <h2 className="text-2xl">Success: </h2>
                        <p className="text-xl">
                            Item has been successfully archived.
                        </p>
                    </div>}
                {/* Back to Review Items Button Below */}
                <div className="flex justify-center mt-4 mb-8">
                    <button
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                        onClick={() => {
                            sessionStorage.removeItem('selectedItemID');
                            router.back();
                        }}>
                        Go Back to Review Items
                    </button>
                </div>
            </div>
        </main >
    );
}
function moment() {
    throw new Error("Function not implemented.");
}

