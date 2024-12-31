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

    let response = {
        headers: {
            "Access-Control-Allow-Headers": "Content-Type",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "POST"
        }
    };

    const username = event.body.username;
    const password = event.body.password;

    const AdminExists = async (username, password) => {
      return new Promise((resolve, reject) => {
        pool.query("SELECT * FROM Admin WHERE Username=? AND Password =?", [username, password], (error, rows) => {
            if (error) { return reject(error); }
            if ((rows) && (rows.length == 1)) {
                return resolve(true);
            } 
            else {
                return resolve(false);
            }
        });
      });
    }

    try {
        const authentcateBuyer = await AdminExists(username, password);
        if (!authentcateBuyer) {
            response.statusCode = 400;
            response.error = "Invalid Admin credentials";
        } else {
            // Query to retrieve all bids on all items with item and bid details
            const forensicQuery = `
                SELECT
                    i.ItemID,
                    i.Name AS ItemName,
                    i.ItemDescription,
                    i.Creator AS Seller,
                    i.InitialPrice,
                    i.ActivityStatus, -- 'Archived' = Sold, 'Active' = Not Sold
                    i.BuyerSoldTo,
                    i.SoldDate,
                    b.BidID,
                    b.AmountBid AS BidAmount,
                    b.RelatedBuyer AS Buyer,
                    b.PlacementDate AS BidPlacementDate,
                    -- Aggregate Data
                    (SELECT MAX(AmountBid) FROM Bid WHERE RelatedItemID = i.ItemID) AS HighestBid,
                    (SELECT MIN(AmountBid) FROM Bid WHERE RelatedItemID = i.ItemID) AS LowestBid,
                    (SELECT COUNT(BidID) FROM Bid WHERE RelatedItemID = i.ItemID) AS TotalBids
                FROM
                    Item i
                LEFT JOIN
                    Bid b ON i.ItemID = b.RelatedItemID
                ORDER BY
                    i.ItemID, b.PlacementDate;
            `;

            const forensicData = await query(forensicQuery);

            response.statusCode = 200;
            response.body = JSON.stringify(forensicData);
        }
    } catch (error) {
        console.error("Error in Lambda function:", error);
        response.statusCode = 400;
        response.body = JSON.stringify({
            error: "Couldn't fetch forensic report",
            details: error.message
        });
    }

    return response;
};
