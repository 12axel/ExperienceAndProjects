import mysql from "mysql";

// Set up the connection to your MySQL database
const pool = mysql.createPool({
    host: "auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com",
    user: "AHadmin",
    password: "Az339667",
    database: "CS509DB",
    multipleStatements: true,
});

function query(sql, params) {
    return new Promise((resolve, reject) => {
        pool.query(sql, params, (err, rows) => {
            if (err) {
                console.error("Database query error:", err, "Query:", sql, "Params:", params);
                reject(err);
            } else {
                resolve(rows);
            }
        });
    });
}

export const handler = async (event, context) => {
    context.callbackWaitsForEmptyEventLoop = false;

    // Initialize response object
    let response = {
        headers: {
            "Access-Control-Allow-Headers": "Content-Type",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
        },
        statusCode: 500,
        body: JSON.stringify({ message: "An unknown error occurred" }), // Default error message
    };

    try {
        const body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
        const { action, ItemID } = body;

        if (action === "fetchRequests") {
            // Fetch all unfreeze requests
            const sql = `
                SELECT 
                    ItemID, 
                    Name, 
                    ItemDescription AS Description, 
                    Creator, 
                    InitialPrice, 
                    IsFrozen 
                FROM Item 
                WHERE IsFrozen = 1 AND requestedUnfreeze = 1 AND SYSDATE() < BidEndDate
            `;
            const unfreezeRequests = await query(sql, []);
            response = {
                ...response,
                statusCode: 200,
                body: JSON.stringify(unfreezeRequests),
            };
        } else if (action === "fetchItemDetails") {
            // Fetch details for a specific item
            const sql = `
                SELECT 
                    ItemID, 
                    Name, 
                    ItemDescription AS Description, 
                    Creator, 
                    InitialPrice, 
                    IsFrozen 
                FROM Item 
                WHERE ItemID = ?
            `;
            const itemDetails = await query(sql, [ItemID]);
            if (itemDetails.length > 0) {
                response = {
                    ...response,
                    statusCode: 200,
                    body: JSON.stringify(itemDetails[0]),
                };
            } else {
                response = {
                    ...response,
                    statusCode: 404,
                    body: JSON.stringify({ message: "Item not found." }),
                };
            }
        } else if (action === "unfreeze") {
            // Unfreeze the item
            const sql = `
                UPDATE Item 
                SET IsFrozen = 0, requestedUnfreeze = 0 
                WHERE ItemID = ?
            `;
            const result = await query(sql, [ItemID]);
            if (result.affectedRows > 0) {
                response = {
                    ...response,
                    statusCode: 200,
                    body: JSON.stringify({ message: "Item successfully unfrozen." }),
                };
            } else {
                response = {
                    ...response,
                    statusCode: 400,
                    body: JSON.stringify({ message: "Failed to unfreeze the item." }),
                };
            }
        } else if (action === "deny") {
            // Deny the unfreeze request
            const sql = `
                UPDATE Item 
                SET requestedUnfreeze = 0 
                WHERE ItemID = ?
            `;
            const result = await query(sql, [ItemID]);
            if (result.affectedRows > 0) {
                response = {
                    ...response,
                    statusCode: 200,
                    body: JSON.stringify({ message: "Unfreeze request denied." }),
                };
            } else {
                response = {
                    ...response,
                    statusCode: 400,
                    body: JSON.stringify({ message: "Failed to deny the unfreeze request." }),
                };
            }
        } else {
            response = {
                ...response,
                statusCode: 400,
                body: JSON.stringify({ message: "Invalid action specified." }),
            };
        }
    } catch (error) {
        console.error("Error handling request:", error);
        response = {
            ...response,
            statusCode: 500,
            body: JSON.stringify({
                message: "Internal server error.",
                error: error.message,
            }),
        };
    }

    return response;
};
