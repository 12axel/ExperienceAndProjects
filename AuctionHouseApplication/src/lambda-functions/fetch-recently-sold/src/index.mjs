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
            "Access-Control-Allow-Methods": "GET"
        }
    };

    try {
        // Query to retrieve all recently sold items
        const itemSql = `
            SELECT *
            FROM Item 
            WHERE ActivityStatus = 'Archived'
            AND SoldDate IS NOT NULL
            AND SYSDATE() <= SoldDate + INTERVAL 24 HOUR
        `;
        const recentlySoldItems = await query(itemSql, []);

        // Extract item IDs
        const itemIds = recentlySoldItems.map(item => item.ItemID);

        if (itemIds.length === 0) {
            response.statusCode = 200;
            response.body = JSON.stringify([]);
            return response;
        }

        // Query to retrieve the highest bid for each item
        const highestBidSql = `
            SELECT RelatedItemID, MAX(AmountBid) AS HighestBid
            FROM Bid
            WHERE RelatedItemID IN (?)
            GROUP BY RelatedItemID
        `;
        const highestBids = await query(highestBidSql, [itemIds]);

        // Map highest bids to corresponding items
        const highestBidsByItem = highestBids.reduce((acc, bid) => {
            acc[bid.RelatedItemID] = bid.HighestBid;
            return acc;
        }, {});

        // Query to get all images related to recently sold items
        const pictureSql = `
            SELECT RelatedItem, URL
            FROM Picture
            WHERE RelatedItem IN (?)
        `;
        const pictures = await query(pictureSql, [itemIds]);

        // Organize images by item
        const imagesByItem = pictures.reduce((acc, picture) => {
            const { RelatedItem, URL } = picture;
            if (!acc[RelatedItem]) {
                acc[RelatedItem] = [];
            }
            acc[RelatedItem].push(URL);
            return acc;
        }, {});

        // Add images and highest bid to each item
        const itemsWithDetails = recentlySoldItems.map(item => ({
            ...item,
            Images: imagesByItem[item.ItemID] || [], 
            HighestBid: highestBidsByItem[item.ItemID] || null 
        }));

        response.statusCode = 200;
        response.body = JSON.stringify(itemsWithDetails);
    } catch (error) {
        console.error("Error in Lambda function:", error);
        response.statusCode = 400;
        response.body = JSON.stringify({
            error: "Couldn't fetch recently sold items.",
            details: error.message
        });
    }

    return response;
};