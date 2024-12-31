import org.apache.spark.sql.SparkSession
import java.io.{File, PrintWriter}
import scala.io.Source

import org.apache.spark.sql.SparkSession

  object SparkSQLQuery1 {
    def main(args: Array[String]): Unit = {
      val spark = SparkSession.builder()
        .appName("Filter Purchases")
        .master("local[*]")
        .getOrCreate()

      val purchasesDF = spark.read.format("csv")
        .option("sep", ",")
        .option("inferSchema", "true")
        .option("header", "true")
        .load("/Users/axelluca/Downloads/Purchases.csv")

      purchasesDF.createOrReplaceTempView("purchases")

      val filteredPurchasesDF = spark.sql("SELECT * FROM purchases WHERE TransTotal <= 600")

      filteredPurchasesDF.repartition(1)
        .write
        .mode("overwrite")
        .option("header", "true")
        .csv("/Users/axelluca/Desktop/Trial2/T1_temp")

      val tempDir = new File("/Users/axelluca/Desktop/Trial2/T1_temp")
      val tempCsvFile = tempDir.listFiles().find(_.getName.endsWith(".csv")).get

      tempCsvFile.renameTo(new File("/Users/axelluca/Downloads/T1.csv"))

      spark.stop()
    }
  }




