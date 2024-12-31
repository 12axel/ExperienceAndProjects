import React, { useEffect } from "react";
import { Carousel } from "react-responsive-carousel";
import "react-responsive-carousel/lib/styles/carousel.min.css";

export interface ReviewItemCardComponentProps {
    ActivityStatus: string | null;
    BidEndDate: string | null;
    BidStartDate: string | null;
    BuyerSoldTo: string | null;
    Creator: string | null;
    InitialPrice: Number | null;
    IsBuyNow: number | null;
    IsFrozen: number | null;
    ItemDescription: string | null;
    ItemID: number;
    Name: string | null;
    Pictures: PicturesProps[] | null;
    PublishedDate: string | null;
    SoldDate: string | null;
    requestedUnfreeze: number | null;
}

interface PicturesProps {
    URL: string;
}

export default function Home(item: ReviewItemCardComponentProps) {

    // TODO: remove this after testing
    useEffect(() => {
        console.log(`Item ${item.ItemID}:`, item);
    }, [item]);

    return (
        <div style={{ cursor: "pointer" }}>
            <div className="block max-w-sm p-6 bg-slate-100 border border-gray-400 rounded-lg shadow hover:bg-gray-100 dark:bg-gray-800 dark:border-gray-700 dark:hover:bg-gray-700">
                <h1 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
                    Item ID: {item.ItemID}
                </h1>
                <p className="font-normal text-gray-700 dark:text-gray-400">
                    <strong>ItemName: </strong>{item.Name}
                </p>
                <p className="font-normal text-gray-700 dark:text-gray-400">
                    <strong>Item Description: </strong>{item.ItemDescription}
                </p>
                <div style={{ height: "1rem" }}>
                    {/* Spacer div */}
                </div>
                <Carousel
                    autoPlay={true}
                    infiniteLoop={true}
                    showArrows={false}
                    showThumbs={false}>
                    {item.Pictures?.map((picture, index) => (
                        <div key={index}>
                            <img src={picture.URL} style={{ height: "12rem", objectFit: "cover" }} />
                        </div>
                    ))}
                </Carousel>
            </div>
        </div>
    );
}

