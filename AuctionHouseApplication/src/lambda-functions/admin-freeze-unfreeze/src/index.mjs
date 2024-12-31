import mysql from 'mysql';

// Set up the connection to your MySQL database
const pool = mysql.createPool({
    host: 'auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com',
    user: 'AHadmin',
    password: 'Az339667',
    database: 'CS509DB',
    multipleStatements: true,
});

function query(conx, sql, params) {
    return new Promise((resolve, reject) => {
        conx.query(sql, params, function (err, rows) {
            if (err) {
                console.error("Database query error:", err, "Query:", sql, "Params:", params);
                reject(err);
            } else {
                console.log("Query successful. Rows affected:", rows.affectedRows || 0);
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
            "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
        },
    };

    let action, ItemID;

    try {
        // Parse the body from the request
        const body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
        console.log("Received payload:", body);

        // Extract and validate fields
        action = body.action;
        ItemID = body.ItemID;

        if (!action || (action !== "freeze" && action !== "unfreeze") || !ItemID) {
            console.error("Validation failed:", { action, ItemID });
            response.statusCode = 400;
            response.body = JSON.stringify({ message: "Invalid action or ItemID" });
            return response;
        }
    } catch (error) {
        console.error("Error parsing event.body:", error);
        response.statusCode = 400;
        response.body = JSON.stringify({ message: "Invalid request body format" });
        return response;
    }

    const updateItem = async (ItemID, isFrozen) => {
        const checkQuery = "SELECT * FROM Item WHERE ItemID = ?";
        const updateQueryUnfreeze = `
            UPDATE Item 
            SET IsFrozen = 0, 
                requestedUnfreeze = 0 
            WHERE ItemID = ? AND ActivityStatus = 'Active'
        `;
        const updateQueryFreeze = `
            UPDATE Item 
            SET IsFrozen = 1
            WHERE ItemID = ? AND ActivityStatus = 'Active'
        `;
    
        try {
            // Debug the row before updating
            const rows = await query(pool, checkQuery, [ItemID]);
            console.log("Item details before update:", rows);
    
            if (rows.length === 0) {
                console.warn("No item found with the specified ItemID:", ItemID);
                return false;
            }
    
            let result;
            if (isFrozen === 0) {
                // Unfreeze logic: Set IsFrozen to 0 and requestedUnfreeze to 0
                result = await query(pool, updateQueryUnfreeze, [ItemID]);
            } else {
                // Freeze logic: Set IsFrozen to 1 (keep requestedUnfreeze unchanged)
                result = await query(pool, updateQueryFreeze, [ItemID]);
            }
    
            console.log("Update result:", result);
            return result.affectedRows > 0;
        } catch (error) {
            console.error("Database query failed:", error);
            throw new Error("Failed to execute query");
        }
    };
    

    try {
        const isFrozen = action === "freeze" ? 1 : 0; // Freeze: 1, Unfreeze: 0
        const result = await updateItem(ItemID, isFrozen);

        if (result) {
            response.statusCode = 200;
            response.body = JSON.stringify({
                message: `Item successfully ${action}d.`,
            });
        } else {
            response.statusCode = 400;
            response.body = JSON.stringify({
                message: `Item could not be ${action}d. It may not meet the conditions.`,
            });
        }
    } catch (error) {
        console.error("Error in handler:", error);
        response.statusCode = 500;
        response.body = JSON.stringify({
            message: "Internal server error.",
            error: error.message,
        });
    }

    return response;
};
