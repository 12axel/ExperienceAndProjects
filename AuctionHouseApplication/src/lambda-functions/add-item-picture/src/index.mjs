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

    
let itemBelongsToSeller = (itemID, username) => {
    return new Promise((resolve, reject) => {
        pool.query("Select * FROM Item WHERE ItemID=? AND Creator=?", [itemID, username], (error, rows) => {  
            if (error) { return reject(error); }
            if ((rows) && (rows.length > 0)) {
              return resolve(true);
            } 
            else {
              return resolve(false);
            }
          });
      });
  };



  let addPicture = (relatedItem, URL) => {
    return new Promise((resolve, reject) => {
        pool.query("INSERT INTO Picture (RelatedItem, URL) VALUES (?, ?)", [relatedItem, URL], (error, result) => {  
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

    let getNewPicture = (insertID) => {
        return new Promise((resolve, reject) => {
            pool.query("SELECT * FROM Picture WHERE PictureID=?", [insertID], (error, rows) => {
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
            const getItemBelongsToSeller = await itemBelongsToSeller(info.relatedItem, info.username);
            if(!getItemBelongsToSeller){
              response.statusCode = 400;
              response.error = "Item does not belong to current seller.";
            }
            else {
              const pictureAddedID = await addPicture(info.relatedItem, info.URL);
              const getTheNewPicture = await getNewPicture(pictureAddedID)
              response.statusCode = 200;
              response.body = JSON.stringify(getTheNewPicture);
            }
            
        }
    } catch (error) {
        console.log("ERROR: " + error);
        response.statusCode = 400;
        response.error = error;
    }

    return response;
};

