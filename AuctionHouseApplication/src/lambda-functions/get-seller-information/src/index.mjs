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

  // to get the seller's info
  const getSellerInfo = (username, password) => {
    return new Promise((resolve, reject) => {
      pool.query("SELECT * FROM Seller WHERE Username=? AND Password =?", [username, password], (error, rows) => {
        if (error) {
          console.log(`Error encountered: ${error}`);
          reject(error);
        } else if (rows && rows.length === 1) {
          return resolve(rows[0])
        } else {
          return resolve(false)
        }
      })
    })
  }

  // Main query to get the seller's information
  try {
    const [NYTimeZone, sellerInfo] = await Promise.all([
      adjustTimeZone(),
      getSellerInfo(username, password)
    ]);
    if (!sellerInfo) {
      response.statusCode = 400;
      response.error = "Did not find the specified seller with those credentials";
    } else {
      response.statusCode = 200;
      response.body = JSON.stringify(sellerInfo);
    }
  } catch(error) {
    response.statusCode = 400;
    response.error = "Could not successfully retrieve the seller's information." + error;
  }
  
  return response;
};
