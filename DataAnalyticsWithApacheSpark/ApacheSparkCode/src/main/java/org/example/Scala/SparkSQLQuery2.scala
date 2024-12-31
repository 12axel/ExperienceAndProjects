import org.apache.spark.sql.SparkSession

object SparkSQLQuery2 {
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

    val groupedPurchasesDF = spark.sql("SELECT TransNumItems, " +
      "percentile_approx(TransTotal, 0.5) AS median, " +
      "MIN(TransTotal) AS min," +
      "MAX(TransTotal) AS max " +
      "FROM T1 " +
      "GROUP BY TransNumItems")

    groupedPurchasesDF.show()


    spark.stop()
  }

}
