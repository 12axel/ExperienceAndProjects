import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{LinearRegression, GBTRegressor}

object SparkMLlibTask {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Filter Purchases")
      .master("local[*]")
      .getOrCreate()

    val purchasesDF = spark.read.format("csv")
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .csv("/Users/axelluca/Downloads/Purchases.csv")

    purchasesDF.createOrReplaceTempView("purchases")

    val customersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("/Users/axelluca/Downloads/Customers.csv")

    customersDF.createOrReplaceTempView("customers")

    val dataset = spark.sql("SELECT Age, " +
      "Salary, TransNumItems, " +
      "TransTotal FROM purchases " +
      "JOIN customers ON purchases.CustID = customers.ID")

    val Array(trainingData, testData) = dataset.randomSplit(Array(0.8, 0.2))

    val assembler = new VectorAssembler()
      .setInputCols(Array("Age", "Salary", "TransNumItems"))
      .setOutputCol("features")

    val trainingDataAssembled = assembler.transform(trainingData)
    val testDataAssembled = assembler.transform(testData)

    val lr = new LinearRegression()
      .setLabelCol("TransTotal")
      .setFeaturesCol("features")
      .setMaxIter(25)

    val lrModel = lr.fit(trainingDataAssembled)

    val lrPredictions = lrModel.transform(testDataAssembled)

    val gbt = new GBTRegressor()
      .setLabelCol("TransTotal")
      .setFeaturesCol("features")
      .setMaxIter(25)

    val gbtModel = gbt.fit(trainingDataAssembled)

    val gbtPredictions = gbtModel.transform(testDataAssembled)

    val evaluator = new RegressionEvaluator()
      .setLabelCol("TransTotal")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val lrRMSE = evaluator.evaluate(lrPredictions)
    println(s"Linear Regression RMSE: $lrRMSE")

    val gbtRMSE = evaluator.evaluate(gbtPredictions)
    println(s"Gradient-Boosted Tree Regression RMSE: $gbtRMSE")

    spark.stop()

  }

}
