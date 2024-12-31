import mysql from 'mysql2/promise';  


const pool = mysql.createPool({
  host: '212.192.29.218',
  user: 'dbms',
  password: 'DBMS_group123',
  database: 'CS542ProjectDB',
  waitForConnections: true,
  connectionLimit: 10,  
  queueLimit: 0,  
  connectTimeout: 10000  
});


pool.getConnection()
  .then(conn => {
    console.log('Successfully connected to the database');
    conn.release();  
  })
  .catch(err => {
    console.error('Error connecting to the database:', err.code, err.message);
  });

export default pool;
