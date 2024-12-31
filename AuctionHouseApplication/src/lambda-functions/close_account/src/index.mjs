import mysql from 'mysql';

const pool = mysql.createPool({
    host: 'auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com',
    user: 'AHadmin',
    password: 'Az339667',
    database: 'CS509DB',
    connectionLimit: 10, // Limit the number of connections in the pool
});

function query(sql, params) {
    return new Promise((resolve, reject) => {
        pool.query(sql, params, (err, rows) => {
            if (err) {
                console.error("Database query error:", err); // Log detailed error message
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

    let body;
    try {
        body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    } catch (parseError) {
        console.error("JSON Parse Error:", parseError);
        response.statusCode = 400;
        response.body = JSON.stringify({ message: "Invalid JSON format" });
        return response;
    }

    if (!body || !body.username || !body.userType) {
        response.statusCode = 400;
        response.body = JSON.stringify({ message: "Username and userType are required" });
        return response;
    }

    const { username, userType } = body;

    try {
        if (userType === "Seller") {
            // Check if the seller exists
            const sellerExists = await query("SELECT * FROM Seller WHERE Username = ?", [username]);
            if (sellerExists.length === 0) {
                response.statusCode = 404;
                response.body = JSON.stringify({ message: "Seller not found" });
                return response;
            }

            // Check for active auctions by the seller in the Item table
            const activeItems = await query("SELECT * FROM Item WHERE Creator = ? AND ActivityStatus = 'Active'", [username]);
            if (activeItems.length > 0) {
                response.statusCode = 400;
                response.body = JSON.stringify({ message: "Cannot close account with active auctions" });
                return response;
            }

            // Close the seller account
            await query("UPDATE Seller SET IsClosed = 1 WHERE Username = ?", [username]);
            response.statusCode = 200;
            response.body = JSON.stringify({ message: "Seller account closed successfully" });
        } else if (userType === "Buyer") {
            // Check if the buyer exists
            const buyerAccountExists = await query("SELECT * FROM Buyer WHERE Username = ?", [username]);
            if (buyerAccountExists.length === 0) {
                response.statusCode = 404;
                response.body = JSON.stringify({ message: "Buyer not found" });
                return response;
            }

            // Check for active bids by the buyer
            const activeBids = await query(
                `SELECT b.BidID 
                 FROM Bid b 
                 INNER JOIN Item i ON b.RelatedItemID = i.ItemID 
                 WHERE b.RelatedBuyer = ? AND i.ActivityStatus = 'Active'`,
                [username]
            );

            if (activeBids.length > 0) {
                response.statusCode = 400;
                response.body = JSON.stringify({ message: "Cannot close account with active bids on active items" });
                return response;
            }

            // Close the buyer account
            await query("UPDATE Buyer SET IsClosed = 1 WHERE Username = ?", [username]);
            response.statusCode = 200;
            response.body = JSON.stringify({ message: "Buyer account closed successfully" });
        } else {
            response.statusCode = 400;
            response.body = JSON.stringify({ message: "Invalid userType. Must be 'Seller' or 'Buyer'." });
        }
    } catch (error) {
        console.error("Close Account Error:", error);
        response.statusCode = 500;
        response.body = JSON.stringify({
            message: "Failed to close account",
            error: error.message,
        });
    }

    return response;
};
