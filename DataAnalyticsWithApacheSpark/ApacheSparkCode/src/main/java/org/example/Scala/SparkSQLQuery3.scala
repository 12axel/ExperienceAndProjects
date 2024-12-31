import org.apache.spark.sql.SparkSession

import java.io.{File, PrintWriter}
import scala.io.Source

object SparkSQLQuery3 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Filter Purchases")
      .master("local[*]")
      .getOrCreate()

    val T1DF = spark.read.format("csv")
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .csv("/Users/axelluca/Downloads/T1.csv")

    T1DF.createOrReplaceTempView("T1")

    val customersDF = spark.read.format("csv")
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .csv("/Users/axelluca/Downloads/Customers.csv")

    customersDF.createOrReplaceTempView("customers")

    val groupedPurchasesDF = spark.sql("SELECT ID, Age, " +
      "COUNT(*) AS TotalItems, " +
      "SUM(TransTotal) AS TotalSpent " +
      "FROM T1 " +
      "JOIN customers ON T1.CustID = customers.ID " +
      "WHERE Age BETWEEN 18 AND 25 " +
      "GROUP BY ID, Age")


    groupedPurchasesDF.repartition(1)
      .write
      .mode("overwrite")
      .option("header", "true")
      .csv("/Users/axelluca/Desktop/Trial2/T3_temp")

    val tempDir = new File("/Users/axelluca/Desktop/Trial2/T3_temp")
    val tempCsvFile = tempDir.listFiles().find(_.getName.endsWith(".csv")).get

    tempCsvFile.renameTo(new File("/Users/axelluca/Downloads/T3.csv"))

    spark.stop()

  }

}
