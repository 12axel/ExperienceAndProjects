'use client'
import React, { useEffect } from "react";

export interface BidsProps {
    IsBuyNow: number;
    bids: {
        AmountBid: number;
        BidID: number;
        PlacementDate: string;
        RelatedBuyer: string;
        RelatedItemID: number;
    }[];
}

const styles = {
    wrapperDivStyles: {
        height: "100%",
        width: "100%",
        display: "flex",
        justifyContent: "center",
    },
}

export default function Table({ bids, IsBuyNow }: BidsProps) {

    useEffect(() => {
        console.log("Bid print from Table component: ", bids);
    }, [bids]);

    return (
        <div style={styles.wrapperDivStyles} className="relative overflow-x-auto">
            <table className="table-auto w-full text-sm text-left rtl:text-right text-black-500 dark:text-black-400">
                <thead className="text-xs text-black-700 uppercase bg-black-50 dark:bg-black-700 dark:text-black-400">
                    <tr>
                        <th scope="col" className="border px-6 py-3 text-center border-black">
                            {IsBuyNow ? "Purchase ID" : "Bid ID"}
                        </th>
                        <th scope="col" className="border px-6 py-3 text-center border-black">
                            Item ID
                        </th>
                        <th scope="col" className="border px-6 py-3 text-center border-black">
                            {IsBuyNow ? "Item Price" : "Amount Bid"}
                        </th>
                        <th scope="col" className="border px-6 py-3 text-center border-black">
                            Buyer
                        </th>
                        <th scope="col" className="border px-6 py-3 text-center border-black">
                            Placement Date
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {bids.map((bid, index) => (
                        <tr key={index} className="bg-white border-b dark:bg-black-800 dark:border-black-900">
                            <td className="border px-6 py-4 text-center border-black">{bid.BidID}</td>
                            <td className="border px-6 py-4 text-center border-black">{bid.RelatedItemID}</td>
                            <td className="border px-6 py-4 text-center border-black">{bid.AmountBid}</td>
                            <td className="border px-6 py-4 text-center border-black">{bid.RelatedBuyer}</td>
                            <td className="border px-6 py-4 text-center border-black">{new Date(bid.PlacementDate.replace(' ', 'T'))
                                .toLocaleDateString('en-US', {
                                    month: '2-digit',
                                    day: '2-digit',
                                    year: 'numeric',
                                    hour: '2-digit',
                                    minute: '2-digit',
                                    second: '2-digit',
                                    hour12: false
                                })}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
