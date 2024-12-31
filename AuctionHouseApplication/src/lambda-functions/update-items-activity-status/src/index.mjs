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

  const getActiveItemsPastExpirationDate = () => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT * FROM Item " +
        "WHERE ActivityStatus = 'Active' AND SYSDATE() > BidEndDate", (error, rows) => {
          if(error) {
            return reject(error);
          }
          else if(rows && rows.length > 0) {
            return resolve(rows)
          }
          else {
            return resolve(null);;
          }
        }
      )
    })
  }

  const countBidsForItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT IFNULL(COUNT(*), 0) AS NumOfBids FROM Bid " +
        "WHERE RelatedItemID = ?", [itemID], (error, rows) => {
          if(error) {
            return reject(error);
          }
          else if(rows && rows.length > 0) {
            return resolve(rows[0].NumOfBids)
          }
          else {
            return resolve(0);
          }
        }
      )
    })
  }

  const checkIfItemExpiredItemIsFrozen = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT IsFrozen FROM Item " +
        "WHERE ItemID = ?", [itemID], (error, rows) => {
          if(error) {
            return reject(error);
          }
          else if(rows && rows[0].IsFrozen == 1) {
            return resolve(true)
          }
          else {
            return resolve(false);
          }
        }
      )
    })
  }

  const updateItemActivityStatusToCompleted = (itemID) => {
    return new Promise((resolve, reject) => {
        pool.query(
            "UPDATE Item SET ActivityStatus = 'Completed' WHERE ItemID = ?", [itemID], (error, result) => {  
                if (error) {
                    return reject(error);
                }
                if (result && result.affectedRows > 0) {
                    return resolve(true);
                } else {
                    return resolve(true);
                }
            }
        );
    });
  };

  const updateItemActivityStatusToFailed = (itemID) => { 
    return new Promise((resolve, reject) => {
        pool.query(
            "UPDATE Item SET ActivityStatus = 'Failed' WHERE ItemID = ?", [itemID], (error, result) => {  
                if (error) {
                    return reject(error);
                }
                if (result && result.affectedRows > 0) {
                    return resolve(true);
                } else {
                    return resolve(true);
                }
            }
        );
    });
  };

  const getAllItems = () => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT * FROM Item ", (error, rows) => {
          if(error) {
            return reject(error);
          }
          else if(rows) {
            return resolve(rows)
          }
          else {
            return reject(error);
          }
        }
      )
    })
  }

  try {
    const activeExpiredItems = await getActiveItemsPastExpirationDate();
    if(activeExpiredItems == null || activeExpiredItems.length === 0){
      const getAllDatabaseItems = await getAllItems();
      response.statusCode = 200;
      response.body = JSON.stringify(getAllDatabaseItems);
    } else {

      const updatePromises = activeExpiredItems.map(async (item) => {
        try {
          const [numOfBidsForItem, isItemFrozen] = await Promise.all([
            countBidsForItem(item.ItemID),
            checkIfItemExpiredItemIsFrozen(item.ItemID)
          ]);
          
          if (numOfBidsForItem > 0 && isItemFrozen == false) {
            await updateItemActivityStatusToCompleted(item.ItemID);
          } else {
            await updateItemActivityStatusToFailed(item.ItemID);
          }
        } catch (error) {
          console.log(`Error updating item ${item.ItemID}:`, error);
          return null;
        }
      });

      await Promise.all(updatePromises);

      const getAllDatabaseItems = await getAllItems();
      response.statusCode = 200;
      response.body = JSON.stringify(getAllDatabaseItems);
    }
  } catch(error) {
    response.statusCode = 400;
    response.error = "Could not successfully insert update the items' activity status."
  }
  
  return response;
};