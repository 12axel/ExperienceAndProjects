import mysql from 'mysql';

// Set up the connection to your MySQL database
const pool = mysql.createPool({
  host: 'auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com',
  user: 'AHadmin',
  password: 'Az339667',
  database: 'CS509DB',
  multipleStatements: true
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

  const itemID = event.body.itemID;
  const username = event.body.username;
  const password = event.body.password;
  console.log(`even.itemID: ${itemID}`)

  // to authenticate user
  const sellerExists = (username, password) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Seller WHERE Username=? AND Password =?", [username, password], (error, rows) => {
        if (error) {
          console.log(`Error encountered: ${error}`);
          reject(error);
        } else if (rows && rows.length === 1) {
          return resolve(true)
        } else {
          return resolve(false)
        }
      })
    })
  }

  // to check if item belongs to seller
  const getSpecificSellerItem = (itemID, username) => {
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

  // to check that the item ActivityStatus is active
  const checkIfItemActive = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Item WHERE ItemID = ?", [itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows[0].ActivityStatus == "Active") {
          return resolve(true);
        } else {
          return resolve(false);
        }
      })
    })
  }

  // to check that there are no bids on the item
  const checkNoBidsExist = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Bid WHERE relatedItemID = ?", [itemID], (error, rows) => {
        if (error) {
          return reject(error)
        } else if (rows && rows.length > 0) {
          resolve(false)
        } else {
          resolve(true)
        }
      })
    })
  }

  const changeActivityStatustToInactive = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query('UPDATE Item SET ActivityStatus = ?, BidStartDate = NULL, PublishedDate = NULL WHERE ItemID = ?; SELECT * FROM Item WHERE ItemID = ?', ['Inactive', itemID, itemID], (error, results) => {
        if (error) {
          console.error('Database query error:', error);
          reject(false)
        } else {
          console.log(`Results: ${JSON.stringify(results[1][0])}`)
          resolve({
            statusCode: 200,
            body: JSON.stringify(results[1][0])
          });
        }
      });
    });
  }

  // Main query to unpublish item
  try {
    const seller = await sellerExists(username, password);
    if(!seller){
      response.statusCode = 400;
      response.error = "Invalid seller credentials";
    } else {
      const sellerItem = await getSpecificSellerItem(itemID, username);
      if(sellerItem === null){
        response.statusCode = 400;
        response.error = "Item does not exist or does not belong to the current seller.";
      } else {
        const noBids = await checkNoBidsExist(itemID);
        if (!noBids) {
          response.statusCode = 400;
          response.error = "Item has bids on it and cannot be unpublished."
        } else {
          const itemActive = await checkIfItemActive(itemID);
          if (!itemActive) {
            response.statusCode = 400;
            response.error = "Item's ActivityStatus is not Active."
          } else {
            const changedItem = await changeActivityStatustToInactive(itemID);
            if (changedItem) {
              response.statusCode = changedItem.statusCode;
              response.body = changedItem.body;
            }
          }
        }
      }
    }
  } catch(error) {
    response.statusCode = 400;
    response.error = "Could not successfully change ActivityStatus to Inactive."
  }
  
  return response;
};