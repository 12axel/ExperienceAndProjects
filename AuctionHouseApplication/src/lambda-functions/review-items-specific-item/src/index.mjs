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

    let getSpecificSellerItem = (itemID, username) => {
        return new Promise((resolve, reject) => {
            pool.query("SELECT * FROM Item WHERE ItemID=? AND Creator = ?", [itemID, username], (error, rows) => {
              if (error) {
                return reject(error);
            }
            if ((rows) && (rows.length > 0)) {
              return resolve(rows[0]);
            } 
            else {
              return resolve(null);
            }
            });
        });
    };

    let getSpecificSellerItemBids = (relatedItemID) => {
      return new Promise((resolve, reject) => {
          pool.query("SELECT * FROM Bid WHERE RelatedItemID=?", [relatedItemID], (error, rows) => {
            if (error) {
              return reject(error);
            }
            if ((rows)) {
              return resolve(rows);
            } 
            else {
              return reject(error);
            }
          });
      });
    };

    let getSpecificSellerItemPictures = (relatedItemID) => {
      return new Promise((resolve, reject) => {
          pool.query("SELECT * FROM Picture WHERE RelatedItem=?", [relatedItemID], (error, rows) => {
            if (error) {
              return reject(error);
            }
            if ((rows)) {
              return resolve(rows);
            } 
            else {
              return reject(error);
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
        const getTheSpecificSellerItem = await getSpecificSellerItem(info.itemID, info.username);
        console.log(getTheSpecificSellerItem);
        if(getTheSpecificSellerItem === null){
          response.statusCode = 400;
          response.error = "Item does not exist or does not belong to the current seller.";
        }
        else{
          const getTheSpecificSellerItemBids = await getSpecificSellerItemBids(info.itemID);
          const getTheSpecificSellerItemPictures = await getSpecificSellerItemPictures(info.itemID);

          getTheSpecificSellerItem["Bids"] = getTheSpecificSellerItemBids;
          getTheSpecificSellerItem["Pictures"] = getTheSpecificSellerItemPictures;

          response.statusCode = 200;
          response.body = JSON.stringify(getTheSpecificSellerItem);
        }
      }
    } catch (error) {
        response.statusCode = 400;
        response.error = "Couldn't fetch the seller item.";
    }

    return response;
};