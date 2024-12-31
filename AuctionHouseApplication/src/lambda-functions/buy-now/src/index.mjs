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
        if (error) {
          console.log(`Error encountered: ${error}`)
          reject(error)
        }
        else {
          resolve()
        }
      })
    })
  }

  const isFrozenItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Item WHERE isFrozen = 1 AND ItemID = ?", [itemID], (error, rows) => {
        if (error) {
          reject(error);
        } else if (rows && rows.length > 0) {
          return resolve(true)
        } else {
          return resolve(false)
        }
      })
    })
  }

  const isExpiredItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Item WHERE SYSDATE() > BidEndDate AND ItemID = ?", [itemID], (error, rows) => {
        if (error) {
          reject(error);
        } else if (rows && rows.length > 0) {
          return resolve(true)
        } else {
          return resolve(false)
        }
      })
    })
  }

  // to authenticate user
  const buyerExists = (username, password) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Buyer WHERE Username=? AND Password =?", [username, password], (error, rows) => {
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

  // to check if an item is a buy now item
  const isBuyNowItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Item WHERE ItemID=? AND IsBuyNow=TRUE", [itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows.length > 0) {
          return resolve(true);
        } else {
          reject(false);
        }
      })
    })
  }

  const checkIfItemHasBeenBought = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Bid WHERE RelatedItemID = ?", [itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows.length > 0) {
          return resolve(true);
        } else {
          return resolve(false);
        }
      })
    })
  }

  const getItemPrice = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT InitialPrice FROM Item " +
        "WHERE ItemID = ?", [itemID], (error, rows) => {
          if (error) {
            return reject(error);
          }
          else if (rows && rows.length > 0) {
            return resolve(rows[0].InitialPrice)
          }
          else {
            return resolve(0);
          }
        }
      )
    })
  }

  // to add the buyer's highest bids on active items
  const sumHighestActiveBids = (username) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT IFNULL(SUM(MaxBidOnItem), 0) AS TotalActiveBids " +
        "FROM (SELECT Item.ItemID, MAX(AmountBid) AS MaxBidOnItem FROM Item " +
        "JOIN Bid ON Bid.RelatedItemID = Item.ItemID " +
        "WHERE BidStartDate IS NOT NULL AND ActivityStatus = 'Active' AND SYSDATE() < BidEndDate AND RelatedBuyer = ? " +
        "GROUP BY Item.ItemID) AS GetItemsMaxBids", [username], (error, rows) => {
          if (error) {
            return reject(error);
          } else if (rows && rows.length > 0) {
            return resolve(rows[0].TotalActiveBids);
          } else {
            return resolve(0);
          }
        })
    })
  }

  // to add the buyer's highest bids on completed items that are not frozen
  const sumHighestCompletedAndUnfrozenBids = (username) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT IFNULL(SUM(MaxBidOnItem), 0) AS TotalCompleteUnfrozenBids " +
        "FROM (SELECT Item.ItemID, MAX(AmountBid) AS MaxBidOnItem " +
        "FROM Item JOIN Bid ON Bid.RelatedItemID = Item.ItemID " +
        "WHERE BidStartDate IS NOT NULL AND ActivityStatus = 'Completed' AND IsFrozen = FALSE AND RelatedBuyer = ? " +
        "GROUP BY Item.ItemID) AS GetItemsMaxBids",
        [username],
        (error, rows) => {
          if (error) {
            return reject(error);
          }
          if (rows && rows.length > 0) {
            return resolve(rows[0].TotalCompleteUnfrozenBids);
          } else {
            return resolve(0);
          }
        }
      );
    });
  };

  const getCurrentBalance = (username) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT AccountFunds FROM Buyer WHERE Username=?", [username], (error, rows) => {
        if (error) {
          return reject(error);
        } else if (rows && rows.length === 1) {
          return resolve(rows[0].AccountFunds);
        } else {
          return resolve(0);
        }
      });
    });
  }

  const updateActivityStatusToCompleted = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("UPDATE Item SET ActivityStatus = 'Completed' WHERE ItemID = ?",
        [itemID],
        (error, rows) => {
          if (error) {
            return reject(error);
          } else {
            resolve(rows.affectedRows);
          }
        })
    });
  }

  const placeBid = (itemID, requestedBid, username) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (?, ?, ?, SYSDATE())", [itemID, requestedBid, username], (error, result) => {
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

  const getRecentlyBoughtItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Item WHERE ItemID = ?", [itemID], (error, rows) => {
        if (error) {
          return reject(error);
        } else {
          return resolve(rows[0])
        }
      });
    });
  }

  const getBoughtBid = (insertID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * From Bid WHERE BidID = ?", [insertID], (error, rows) => {
        if (error) {
          return reject(error);
        } else {
          return resolve(rows[0]);
        }
      })
    })
  }

  // Main query to buy the item
  try {
    const itemFrozen = await isFrozenItem(itemID);
    if (!itemFrozen) {
      const itemExpired = await isExpiredItem(itemID);
      if (!itemExpired) {
        const buyer = await buyerExists(username, password);
        console.log("buyer: ", buyer);
        if (!buyer) {
          response.statusCode = 400;
          response.error = "Invalid buyer credentials";
        } else {
          const [buyNowItem, itemAlreadyBought] = await Promise.all([
            isBuyNowItem(itemID),
            checkIfItemHasBeenBought(itemID)
          ])
          if (!(buyNowItem && !itemAlreadyBought)) {
            console.log("buyNowItem: ", buyNowItem);
            console.log("itemAlreadyBought: ", itemAlreadyBought);
            response.statusCode = 400;
            response.error = "Either the item is not a buy now item or item has already been bought";
          } else {
            const [userSumHighestActiveBids, userSumHighestCompletedAndUnfrozenBids, itemPrice, currentBalance] = await Promise.all([
              sumHighestActiveBids(username),
              sumHighestCompletedAndUnfrozenBids(username),
              getItemPrice(itemID),
              getCurrentBalance(username)
            ])
            console.log("sumHighestActiveBids: $" + userSumHighestActiveBids);
            console.log("sumHighestCompletedAndUnfrozenBids: $" + userSumHighestCompletedAndUnfrozenBids);
            console.log("itemPrice: $" + itemPrice);
            console.log("currentBalance: $" + currentBalance);
            if (userSumHighestActiveBids + userSumHighestCompletedAndUnfrozenBids +
              itemPrice > currentBalance) {
              response.statusCode = 400;
              response.error = "Not enough funds available to buy this item"
            } else {
              const [updatedActivityStatus, placedBid] = await Promise.all([
                updateActivityStatusToCompleted(itemID),
                placeBid(itemID, itemPrice, username)
              ]);
              if (!updatedActivityStatus || !placedBid) {
                if (!updatedActivityStatus) {
                  console.log("Item ActivityStatus could not be successfully updated to 'Completed'");
                }
                if (!placedBid) {
                  console.log("Bid for buy item could not be placed successfully")
                }
                response.statusCode = 400;
                response.error = "Item could not be bought successfully"
              } else {
                const [updatedItem, updatedBid] = await Promise.all([
                  getRecentlyBoughtItem(itemID),
                  getBoughtBid(placedBid)
                ]);
                updatedItem.Bids = [];
                updatedItem.Bids.push(updatedBid);
                console.log("Joined Updated Item: " + JSON.stringify(updatedItem));
                response.statusCode = 200;
                response.body = JSON.stringify(updatedItem);
              }
            }
          }
        }
      }
      else {
        response.statusCode = 400;
        response.error = "The purchase expiration has passed for this item."
      }
    }
    else {
      response.statusCode = 400;
      response.error = "This item is frozen and currently cannot be bought."
    }
  } catch (error) {
    response.statusCode = 400;
    response.error = "Could not successfully buy the item. Error: " + error
  }

  return response;
};
