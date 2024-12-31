import mysql from 'mysql';
            
const pool = mysql.createPool({
    host: 'auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com',
    user: 'AHadmin',
    password: 'Az339667',
    database: 'CS509DB',
});
        
function query(conx, sql, params) {
    return new Promise((resolve, reject) => {
        conx.query(sql, params, function (err, rows) {
            if (err) {
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

    let actual_event = event.body;
    let info = actual_event;
    console.log("info:", JSON.stringify(info));

    let SellerExists = (username, password) => {
      return new Promise((resolve, reject) => {
          pool.query("SELECT * FROM Seller WHERE Username=? AND Password =?", [username, password], (error, rows) => {
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

    
let addItem = (itemName, creator, initialPrice, isBuyNow, itemDescription, bidEndDate) => {
    return new Promise((resolve, reject) => {
        pool.query(
            "INSERT INTO Item (Name, Creator, InitialPrice, IsBuyNow, ItemDescription, BidEndDate) VALUES (?, ?, ?, ?, ?, ?)", [itemName, creator, initialPrice, isBuyNow, itemDescription, bidEndDate], (error, result) => {  
                if (error) {
                    return reject(error);
                }
                if (result) {
                    return resolve(result.insertId);
                } else {
                    return resolve(null);
                }
            }
        );
    });
};

    let getNewItem = (insertID) => {
        return new Promise((resolve, reject) => {
            pool.query("SELECT * FROM Item WHERE ItemID=?", [insertID], (error, rows) => {
              if (error) { return reject(error); }
              if ((rows) && (rows.length > 0)) {
                return resolve(rows[0]);
            } 
            else {
                return resolve(null);
            }
            });
        });
    };



    try {
        const getSellerExists = await SellerExists(info.username, info.password);
        if(!getSellerExists){
          response.statusCode = 400;
          response.error = "Invalid seller credentials";
        }
        else {
            const itemAddedID = await addItem(info.itemName, info.username, info.initialPrice, info.isBuyNow, info.itemDescription, info.bidEndDate);
            console.log(itemAddedID)
            const getTheNewItem = await getNewItem(itemAddedID)
            response.statusCode = 200;
            response.body = JSON.stringify(getTheNewItem);
            
        }
    } catch (error) {
        console.log("ERROR: " + error);
        response.statusCode = 400;
        response.error = error;
    }

    return response;
};
