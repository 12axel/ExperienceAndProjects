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

  const adjustTimeZone = () => {
    return new Promise((resolve, reject) => {
      pool.query("SET time_zone = 'America/New_York'", (error) => {
        if(error) {
          console.log(`Error encountered: ${error}`)
          reject(error)
        }
        else {
          resolve()
        }
      })
    })
  }

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

  // to check the current highest bid of the specified itemID
  const getHighestBidOnItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT IFNULL(MAX(AmountBid), 0) AS MaxBidOnItem FROM Bid " +
        "WHERE RelatedItemID = ?", [itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows.length > 0) {
          return resolve(rows[0].MaxBidOnItem);
        } else {
          return resolve(0);
        }
      })
    })
  }

  
  const getBuyerOfHighestBidOnItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT RelatedBuyer FROM Bid WHERE RelatedItemID = ? AND AmountBid = " +
        "(SELECT IFNULL(MAX(AmountBid), 0) AS MaxBidOnItem FROM Bid " +
        "WHERE RelatedItemID = ?)", [itemID, itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows.length > 0) {
          return resolve(rows[0].RelatedBuyer);
        } else {
          return resolve(null);
        }
      })
    })
  }

  const checkItemIsCompleted = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Item WHERE ItemID = ? and ActivityStatus='Completed'", [itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows.length > 0) {
          resolve(true);
        } else {
          return resolve(false);
        }
      })
    });
  }

  const updateSellerCurrentBalance = (username, amountToAdd) => {
    return new Promise((resolve, reject) => {
      pool.query("UPDATE Seller Set Funds = Funds + (? * 0.95)  WHERE Username = ?", [amountToAdd, username], (error, results) => {
        if (error) {
          return reject(error);
        } else if (results && results.changedRows > 0) {
          resolve(results.changedRows);
        } else {
          return resolve(0);
        }
      });
    });
  }

  const updateBuyerCurrentBalance = (buyerUsername, amountToCharge) => {
    return new Promise((resolve, reject) => {
      pool.query("UPDATE Buyer Set AccountFunds = AccountFunds - ? WHERE Username = ?", 
      [amountToCharge, buyerUsername], 
      (error, results) => {
        if (error) {
          return reject(error);
        } else if (results && results.changedRows > 0) {
          return resolve(results.changedRows);
        } else {
          return resolve(0);
        }
      });
    });
  }

  const updateACFunds = (amountToCharge) => {
    return new Promise((resolve, reject) => {
      pool.query("UPDATE Admin Set TotalACFunds = TotalACFunds + (? * 0.05) WHERE Username = 'Admin1'", 
      [amountToCharge], 
      (error, results) => {
        if (error) {
          return reject(error);
        } else if (results && results.changedRows > 0) {
          return resolve(results.changedRows);
        } else {
          return resolve(0);
        }
      });
    });
  }

  const changeActivityStausToFulfilled = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("UPDATE Item SET ActivityStatus = 'Archived' WHERE ItemID = ?", [itemID], (error, results) => {
        if (error) {
          return reject(error);
        } else if (results && results.changedRows > 0) {
          return resolve(results.changedRows);
        } else {
          return resolve(0);
        }
      });
    });
  }

  const changeItemSoldDateAndBuyerSoldTo = (username, itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("UPDATE Item SET SoldDate = NOW(), BuyerSoldTo = ? WHERE ItemID = ?", [username, itemID], (error, results) => {
        if (error) {
          return reject(error);
        } else if (results && results.changedRows > 0) {
          return resolve(results.changedRows);
        } else {
          return resolve(0);
        }
      });
    });
  }

  const updatedItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Item WHERE ItemID = ?", [itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows.length > 0) {
          resolve(rows[0]);
        } else {
          return resolve(null);
        }
      })
    });
  }


  // Main query to fulfill item
  try {
    const seller = await sellerExists(username, password);
    if(!seller){
      response.statusCode = 400;
      response.error = "Invalid buyer credentials";
    } else {
      const isCompleted = await checkItemIsCompleted(itemID);
      if (!isCompleted) {
        response.statusCode = 400;
        response.error = "Item does not have an ActivityStatus of 'Completed'";
      } else {
        const [highestBidOnItem, highestBidBuyer] = await Promise.all([
          getHighestBidOnItem(itemID),
          getBuyerOfHighestBidOnItem(itemID)
        ]);
        console.log("Highest bid: " + highestBidOnItem)
        console.log("Buyer with the highest bid: " + highestBidBuyer)
        if (!highestBidOnItem || !highestBidBuyer) {
          response.statusCode = 400;
          response.error = "Problem retrieving highest bid on item or the buyer with the highest bid";
        } else {
          // update the respective seller and buyer accounts
          const [changedSellerCurrentBalance, changedBuyerCurrentBalance, updatedACFunds] = await Promise.all([
            updateSellerCurrentBalance(username, highestBidOnItem),
            updateBuyerCurrentBalance(highestBidBuyer, highestBidOnItem),
            updateACFunds(highestBidOnItem),
          ]);
          console.log("Seller # of rows changed succesfully: " + changedSellerCurrentBalance);
          console.log("Buyer # of rows changed successfully: " + changedBuyerCurrentBalance);
          if (!changedSellerCurrentBalance) {
            response.statusCode = 400;
            response.error = "Seller current balance did not successfully update";
          } else if (!changedBuyerCurrentBalance) {
            response.statusCode = 400;
            response.error = "Buyer current balance did not successfully updated";
          } else {
            // update the activity status
            const activityStatusUpdated = await changeActivityStausToFulfilled(itemID);
            if (!activityStatusUpdated) {
              response.statusCode = 400;
              response.error = "Activity Status was not updated successfully.";
            } else {
              const getChangeItemSoldDateAndBuyerSoldTo = await changeItemSoldDateAndBuyerSoldTo(highestBidBuyer, itemID);
              const getUpdatedItem = await updatedItem(itemID);
              response.statusCode = 200;
              response.body = JSON.stringify(getUpdatedItem);
            }
          }
        }
      }
    }
  } catch(error) {
    response.statusCode = 400;
    response.error = "Could not successfully fullfill the item. " + error
  }
  
  return response;
};