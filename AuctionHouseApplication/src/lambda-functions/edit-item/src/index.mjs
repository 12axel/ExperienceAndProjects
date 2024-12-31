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
        pool.query("SELECT * FROM Item WHERE ItemID=? AND Creator=?", [itemID, username], (error, rows) => {  
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

  let itemInactive = (itemID) => {
    return new Promise((resolve, reject) => {
        pool.query("SELECT * FROM Item WHERE ItemID=?", [itemID], (error, rows) => {
            if (error) { return reject(error); }
            if ((rows) && rows[0].ActivityStatus=="Inactive") {
                return resolve(true);
            } else {
                return resolve(false);
            }
        });
    });
  }


  let editItem = (itemID, itemName, creator, initialPrice, isBuyNow, itemDescription, bidEndDate) => {
    return new Promise((resolve, reject) => {
        pool.query("UPDATE Item SET Name=?, Creator=?, InitialPrice=?, IsBuyNow=?, ItemDescription=?, BidEndDate=? WHERE ItemID = ?", [itemName, creator, initialPrice, isBuyNow, itemDescription, bidEndDate, itemID], (error, result) => {
          if (error) {
            return reject(error);
          }
          if (result.affectedRows > 0) {
            return resolve(itemID);
          } else {
            return resolve(null);
          }
        });
    });
     
  }

  let getEditedItem = (itemID) => {
    return new Promise((resolve, reject) => {
        pool.query("SELECT * FROM Item WHERE ItemID=?", [itemID], (error, rows) => {
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
      const getItemBelongsToSeller = await itemBelongsToSeller(info.itemID, info.username);
      if(!getItemBelongsToSeller){
        response.statusCode = 400;
        response.error = "Item does not exist or does not belong to current seller.";
      }
      else {
        const itemIsInactive = await itemInactive(info.itemID);
        if(!itemIsInactive){
          response.statusCode = 400;
          response.error = "Cannot edit item that is not inactive.";
        }
        else {
          const editedItemID = await editItem(info.itemID, info.itemName, info.username, info.initialPrice, info.isBuyNow, info.itemDescription, info.bidEndDate);
          if(editedItemID === null){
            response.statusCode = 400;
            response.error = "The item was not edited.";
          }
          else {
            const getTheEditedItem = await getEditedItem(info.itemID)
            response.statusCode = 200;
            response.body = JSON.stringify(getTheEditedItem);
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
