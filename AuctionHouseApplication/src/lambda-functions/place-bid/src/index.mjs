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
  const requestedBid = event.body.requestedBid;

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
          reject(error);
        } else if (rows && rows.length === 1) {
          return resolve(true)
        } else {
          return resolve(false)
        }
      })
    })
  }

  const getCurrentBuyerBalance = (username, password) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT IFNULL(AccountFunds, 0) AS BuyerFunds FROM Buyer WHERE Username=? AND Password =?", [username, password], (error, rows) => {
        if (error) {
          reject(error);
        } else if (rows && rows.length > 0) {
          return resolve(rows[0].BuyerFunds);
        } else {
          return resolve(0);
        }
      })
    })
  }

  const countBidsForItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT IFNULL(COUNT(*), 0) AS NumOfBids FROM Bid " +
        "WHERE RelatedItemID = ?", [itemID], (error, rows) => {
          if (error) {
            return reject(error);
          }
          else if (rows && rows.length > 0) {
            return resolve(rows[0].NumOfBids)
          }
          else {
            return resolve(0);
          }
        }
      )
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

  const getHighestBidOnItemOfBuyer = (itemID, username) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT IFNULL(MAX(AmountBid), 0) AS UserMaxBidOnItem FROM Bid " +
        "WHERE RelatedItemID = ? AND RelatedBuyer = ?", [itemID, username], (error, rows) => {
          if (error) {
            return reject(error);
          } else if (rows && rows.length > 0) {
            return resolve(rows[0].UserMaxBidOnItem);
          } else {
            return resolve(0);
          }
        })
    })
  }

  const getBuyerNameOfHighestBidOnItem = (itemID) => {
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


  let getNewBid = (insertID) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Bid WHERE BidID=?", [insertID], (error, rows) => {
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


  // Main query to publish item
  try {
    const itemFrozen = await isFrozenItem(itemID);
    if (!itemFrozen) {
      const itemExpired = await isExpiredItem(itemID);
      if (!itemExpired) {
        const buyer = await buyerExists(username, password);
        if (!buyer) {
          response.statusCode = 400;
          response.error = "Invalid buyer credentials";
        } else {
          const itemBidCount = await countBidsForItem(itemID);
          let currentHighestBidOnItem;
          let currentBuyerHighestBidOnItem;
          let absoluteDifferenceBetweenRequestedBidAndHighestBuyerBidOnItem;
          if (itemBidCount > 0) {
            [currentHighestBidOnItem, currentBuyerHighestBidOnItem] = await Promise.all([
              getHighestBidOnItem(itemID),
              getHighestBidOnItemOfBuyer(itemID, username)
            ])
            console.log(currentHighestBidOnItem);
            console.log(currentBuyerHighestBidOnItem);
            absoluteDifferenceBetweenRequestedBidAndHighestBuyerBidOnItem = requestedBid - currentBuyerHighestBidOnItem;
            console.log(absoluteDifferenceBetweenRequestedBidAndHighestBuyerBidOnItem);
          }
          else {
            currentHighestBidOnItem = await getItemPrice(itemID);
            console.log(`current highest bid:` + currentHighestBidOnItem);
            currentBuyerHighestBidOnItem = 0;
            absoluteDifferenceBetweenRequestedBidAndHighestBuyerBidOnItem = requestedBid;
          }
          console.log(currentHighestBidOnItem);
          console.log(200 <= currentHighestBidOnItem);
          if (requestedBid <= currentHighestBidOnItem) {
            response.statusCode = 400;
            response.error = "Requested bid on item must be greater than both the item's current highest bid and the item's original price.";
          } else {
            const buyerWithCurrentHighestBidOnItem = await getBuyerNameOfHighestBidOnItem(itemID);
            console.log(buyerWithCurrentHighestBidOnItem);
            if (username == buyerWithCurrentHighestBidOnItem) {
              response.statusCode = 400;
              response.error = "You currently have the highest bid on the item."
            } else {
              const [currentBalance, sumOfBuyerHighestActiveBids, sumOfBuyerHighestCompletedAndUnfrozenBids] = await Promise.all([
                getCurrentBuyerBalance(username, password),
                sumHighestActiveBids(username),
                sumHighestCompletedAndUnfrozenBids(username)
              ])

              console.log(currentBalance);
              console.log(sumOfBuyerHighestActiveBids);
              console.log(sumOfBuyerHighestCompletedAndUnfrozenBids);
              if (absoluteDifferenceBetweenRequestedBidAndHighestBuyerBidOnItem + sumOfBuyerHighestActiveBids + sumOfBuyerHighestCompletedAndUnfrozenBids > currentBalance) {
                response.statusCode = 400;
                response.error = "You do not have enough funds to place this bid on the item."
              }
              else {
                const insertedBidID = await placeBid(itemID, requestedBid, username);
                const getNewlyInsertedBid = await getNewBid(insertedBidID);
                response.statusCode = 200;
                response.body = JSON.stringify(getNewlyInsertedBid);
              }
            }
          }
        }
      }
      else {
        response.statusCode = 400;
        response.error = "The bid expiration has passed for this item."
      }
    }
    else {
      response.statusCode = 400;
      response.error = "This item is frozen and currently cannot receive new bids."
    }
  } catch (error) {
    response.statusCode = 400;
    response.error = "Could not successfully insert the new bid on the item."
  }

  return response;
};