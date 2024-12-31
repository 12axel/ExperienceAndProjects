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

  let pictureExists = (pictureID) => {
    return new Promise((resolve, reject) => {
        pool.query("SELECT * FROM Picture WHERE PictureID=?", [pictureID], (error, rows) => {  
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

  let removePicture = (pictureID, relatedItem) => {
    return new Promise((resolve, reject) => {
        pool.query("DELETE FROM Picture WHERE PictureID=? AND RelatedItem=?", [pictureID, relatedItem], (error, rows) => {  
            if (error) { return reject(error); }
            if ((rows)) {
              return resolve(true);
            } else {
              return resolve(true);
            }
            }
        );
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
              response.error = "Item does not exist or does not belong to current seller.";
            }
            else {
              const doesPictureExists = await pictureExists(info.pictureID);
              if(!doesPictureExists){
                response.statusCode = 400;
                response.error = "Picture does not exist."
              }
              else{
                const pictureIsRemoved = await removePicture(info.pictureID, info.relatedItem);
                if(!pictureIsRemoved){
                  response.statusCode = 400;
                  response.error = "Failed to remove picture.";
                }
                else {
                response.statusCode = 200;
                response.body = "Successfully removed picture.";
                }
              }
            }
            
        }
    } catch (error) {
        console.log("ERROR: " + error);
        response.statusCode = 400;
        response.error = error;
    }

    return response;
};