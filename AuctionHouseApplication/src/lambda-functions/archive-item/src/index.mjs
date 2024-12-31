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

  const getItemActivityStatus = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT ActivityStatus FROM Item WHERE ItemID = ?", [itemID], (error, rows) => {
          if(error) {
            return reject(error);
          }
          else if(rows && rows.length > 0) {
            return resolve(rows[0].ActivityStatus);
          }
          else {
            return resolve(null);
          }
        }
      )
    })
  }

  const makeItemArchived = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "UPDATE Item SET ActivityStatus = 'Archived'" +
        "WHERE ItemID = ?", [itemID], (error, result) => {
          if(error) {
            return reject(error);
          }
          else if(result && result.affectedRows > 0) {
            return resolve(itemID);
          }
          else {
            return resolve(null);
          }
        }
      )
    })
  }

  const getUpdatedItem = (itemID) => {
    return new Promise((resolve, reject) => {
      pool.query(
        "SELECT * FROM Item WHERE ItemID = ?", [itemID], (error, rows) => {
          if(error) {
            return reject(error);
          }
          else if(rows && rows.length > 0) {
            return resolve(rows[0]);
          }
          else {
            return resolve(null);
          }
        }
      )
    })
  }


  // Main query to publish item
  try {
    const seller = await sellerExists(username, password);
    if(!seller){
      response.statusCode = 400;
      response.error = "Invalid seller credentials";
    }
    else {
      const itemActivityStatus = await getItemActivityStatus(itemID);
      if (!(itemActivityStatus == 'Inactive' || itemActivityStatus == 'Failed')) {
        response.statusCode = 400;
        response.error = "Can only archive inactive or failed items.";
      }
      else {
        const archivedItemID = await makeItemArchived(itemID);
        const newlyArchivedItem = await getUpdatedItem(archivedItemID);
        response.statusCode = 200;
        response.body = JSON.stringify(newlyArchivedItem);
      }
    }
  } catch(error) {
    response.statusCode = 400;
    response.error = "Could not successfully insert the new bid on the item."
  }
  
  return response;
};

