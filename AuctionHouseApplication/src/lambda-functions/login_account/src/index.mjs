import mysql from 'mysql';

const pool = mysql.createPool({
    host: 'auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com',
    user: 'AHadmin',
    password: 'Az339667',
    database: 'CS509DB',
    connectionLimit: 10 // Limit the number of connections in the pool
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

    // Handle OPTIONS method for CORS preflight
    if (event.httpMethod === 'OPTIONS') {
        return {
            statusCode: 200,
            headers: {
                "Access-Control-Allow-Headers": "Content-Type",
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Methods": "OPTIONS,POST"
            },
            body: JSON.stringify({ message: "CORS preflight check successful" })
        };
    }

    let response = {
        headers: {
            "Access-Control-Allow-Headers": "Content-Type",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "OPTIONS,POST"
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

    if (!body || !body.username || !body.password) {
        response.statusCode = 400;
        response.body = JSON.stringify({ message: "Username and password are required" });
        return response;
    }

    const { username, password } = body;

    try {
        // Check the Seller table first
        const seller = await query("SELECT * FROM Seller WHERE Username = ? AND Password = ?", [username, password]);

        if (seller.length > 0) {
            if (seller[0].IsClosed === 1) {
                response.statusCode = 403; // Forbidden
                response.body = JSON.stringify({ message: "Account is closed and cannot be logged in" });
                return response;
            }

            response.statusCode = 200;
            response.body = JSON.stringify({
                message: "Login successful",
                userType: "Seller" // Specify the userType as seller
            });
            return response;
        }

        // If not found in Seller, check the Buyer table
        const buyer = await query("SELECT * FROM Buyer WHERE Username = ? AND Password = ?", [username, password]);

        if (buyer.length > 0) {
            if (buyer[0].IsClosed === 1) {
                response.statusCode = 403; // Forbidden
                response.body = JSON.stringify({ message: "Account is closed and cannot be logged in" });
                return response;
            }

            response.statusCode = 200;
            response.body = JSON.stringify({
                message: "Login successful",
                userType: "Buyer" // Specify the userType as buyer
            });
            return response;
        }

        // Check the Admin table
        const admin = await query("SELECT * FROM Admin WHERE Username = ? AND Password = ?", [username, password]);
        if (admin.length > 0) {
            response.statusCode = 200;
            response.body = JSON.stringify({
                message: "Login successful",
                userType: "Admin" // Specify the userType as admin
            });
            return response;
        }

        // If the user is not found in any table
        response.statusCode = 401; // Unauthorized
        response.body = JSON.stringify({ message: "Invalid username or password" });
    } catch (error) {
        console.error("Login Error:", error);
        response.statusCode = 500;
        response.body = JSON.stringify({
            message: "Failed to login",
            error: error.message
        });
    }

    return response;
};
