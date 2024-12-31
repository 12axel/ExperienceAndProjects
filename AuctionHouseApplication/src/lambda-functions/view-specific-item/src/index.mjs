import mysql from 'mysql';

const pool = mysql.createPool({
    host: 'auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com',
    user: 'AHadmin',
    password: 'Az339667',
    database: 'CS509DB',
});

function query(sql, params) {
    return new Promise((resolve, reject) => {
        pool.query(sql, params, (err, rows) => {
            if (err) {
                console.error("Database query error:", err);
                reject(err);
            } else {
                resolve(rows);
            }
        });
    });
}

export const handler = async (event, context) => {
    context.callbackWaitsForEmptyEventLoop = false;

    console.log("Received event:", JSON.stringify(event)); // Log the event for debugging

    let response = {
        headers: {
            "Access-Control-Allow-Headers": "Content-Type",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "POST"
        }
    };

    try {
        // Safely parse the event body
        let body;
        try {
            body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
        } catch (err) {
            throw new Error("Invalid request body format");
        }

        const { username, password, itemID, userType } = body;

        if (!username || !password || !itemID || !userType) {
            throw new Error("Missing required parameters: username, password, or itemID, or userType");
        }
        

          // Validate credentials based on user type
          let userQuery;
          if (userType === "Admin") {
              userQuery = `  
                  SELECT Username  
                  FROM Admin  
                  WHERE Username = ? AND Password = ?  
              `;  
          } else if (userType === "Buyer") {  
              userQuery = `  
                  SELECT Username 
                  FROM Buyer 
                  WHERE Username = ? AND Password = ? 
              `;  
          } else {  
              throw new Error("Invalid userType. Must be 'Admin' or 'Buyer'.");  
          }  
    
          const userResult = await query(userQuery, [username, password]);  
          if (userResult.length === 0) {  
              throw new Error("Invalid credentials");  
          }

        // Query to get item details
        const itemDetailsQuery = `
            SELECT 
                *
            FROM Item
            WHERE ItemID = ?
        `;
        const itemDetails = await query(itemDetailsQuery, [itemID]);

        if (itemDetails.length === 0) {
            throw new Error("Item not found");
        }

        const item = itemDetails[0];

        // Query to get item images
        const imagesQuery = `
            SELECT URL
            FROM Picture
            WHERE RelatedItem = ?
        `;
        const images = await query(imagesQuery, [itemID]);
        const itemImages = images.map((image) => image.URL);

        // Query to get bidding history
        const biddingHistoryQuery = `
            SELECT AmountBid, PlacementDate, RelatedBuyer
            FROM Bid
            WHERE RelatedItemID = ?
            ORDER BY PlacementDate DESC
        `;
        const biddingHistory = await query(biddingHistoryQuery, [itemID]);

        // Determine current highest bid
        const highestBidQuery = `
            SELECT MAX(AmountBid) AS HighestBid
            FROM Bid
            WHERE RelatedItemID = ?
        `;
        const highestBidResult = await query(highestBidQuery, [itemID]);
        const highestBid = highestBidResult[0]?.HighestBid || item.InitialPrice;

        const auctionStatusQuery = `
        SELECT 
            CASE 
                WHEN SYSDATE() < BidEndDate AND ActivityStatus = 'Active' THEN FALSE
            ELSE TRUE
            END AS IsAuctionActive
        FROM Item
        WHERE ItemID = ?
        `;

        // Check if the auction is expired or the item is sold
        const isExpiredArray = await query(auctionStatusQuery, [itemID]);
        const isExpired = isExpiredArray[0].IsAuctionActive;
        const isSold = item.ActivityStatus === "Archived";

        response.statusCode = 200;
        response.body = JSON.stringify({
            itemDetails: {
                ItemID: item.ItemID,
                Name: item.Name,
                Description: item.ItemDescription,
                InitialPrice: item.InitialPrice,
                IsBuyNow: item.IsBuyNow,
                BidStartDate: item.BidStartDate,
                BidEndDate: item.BidEndDate,
                SoldDate: item.SoldDate,
                Images: itemImages,
                HighestBid: highestBid,
                IsExpired: isExpired,
                IsSold: isSold,
                IsFrozen: item.IsFrozen,
                BuyerSoldTo: item.BuyerSoldTo
            },
            biddingHistory: biddingHistory,
            userDetails: {
                Username: username,
                Role: userType, // Added role for clarity
                CanPlaceHigherBid: userType === "Buyer" && !isExpired && !isSold, // Only Buyers can bid
                CanPlaceCustomBid: userType === "Buyer" && !isExpired && !isSold, // Only Buyers can bid
            },
        });
    } catch (error) {
        console.error("Error in Lambda function:", error);
        response.statusCode = 400;
        response.body = JSON.stringify({
            error: "Couldn't fetch item details.",
            details: error.message,
        });
    }

    return response;
};
