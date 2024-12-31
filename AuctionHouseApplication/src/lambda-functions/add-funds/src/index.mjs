import mysql from 'mysql';

// Set up the connection to your MySQL database
const pool = mysql.createPool({
  host: 'auctionhousedb.c1ya0ysewwd6.us-east-2.rds.amazonaws.com',
  user: 'AHadmin',
  password: 'Az339667',
  database: 'CS509DB',
  multipleStatements: true
});

function query(sql, params) {
  return new Promise((resolve, reject) => {
    pool.query(sql, params, (err, rows) => {
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
      "Access-Control-Allow-Methods": "OPTIONS,POST",
    },
  };

  let username, amount;
  try {
    // Parse event.body safely
    const body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
    username = body.username;
    amount = body.amount;

    if (!username || !amount || amount <= 0) {
      response.statusCode = 400;
      response.body = JSON.stringify({ message: "Invalid username or amount" });
      return response;
    }
  } catch (error) {
    console.error("Error parsing event.body:", error);
    response.statusCode = 400;
    response.body = JSON.stringify({ message: "Invalid request body format" });
    return response;
  }

  const updateBuyerFunds = async (username, amount) => {
    const sql = "UPDATE Buyer SET AccountFunds = AccountFunds + ? WHERE Username = ?";
    return query(sql, [amount, username]);
  };

  const getUpdatedFunds = async (username) => {
    const sql = "SELECT AccountFunds FROM Buyer WHERE Username = ?";
    const rows = await query(sql, [username]);
    if (rows.length === 1) {
      return rows[0].AccountFunds;
    } else {
      throw new Error("Buyer not found");
    }
  };

  try {
    await updateBuyerFunds(username, amount);
    const updatedFunds = await getUpdatedFunds(username);

    response.statusCode = 200;
    response.body = JSON.stringify({
      message: "Funds updated successfully",
      updatedFunds,
    });
  } catch (error) {
    console.error("Error updating funds:", error);
    response.statusCode = 500;
    response.body = JSON.stringify({
      message: "Failed to update funds",
      error: error.message,
    });
  }

  return response;
};
