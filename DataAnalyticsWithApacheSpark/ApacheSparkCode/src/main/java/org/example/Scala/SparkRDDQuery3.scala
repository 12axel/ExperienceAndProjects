import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkRDDQuery3 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("SparkRDDQuery3")
    val sc = new SparkContext(sparkConf)

    val peopleSomeInfected = sc.textFile("/Users/axelluca/Desktop/Trial2/Infected_Data/PEOPLE-SOME-INFECTED-large.csv")
      .filter(!_.startsWith("ID"))
      .map(_.split(","))

    val infected: RDD[(String, Double, Double)] = peopleSomeInfected
      .filter(parts => parts(5) == "yes")
      .map(parts => (parts(0), parts(1).toDouble, parts(2).toDouble))

    val notInfected: RDD[(String, Double, Double)] = peopleSomeInfected
      .filter(parts => parts(5) == "no")
      .map(parts => (parts(0), parts(1).toDouble, parts(2).toDouble))


    val closeContactsCounts: RDD[(String, Int)] = infected.cartesian(notInfected)
      .filter { case ((id1, x1, y1), (id2, x2, y2)) => id1 != id2 && math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) <= 6}
      .map { case ((infectedId, _, _), (notInfectedId, _, _)) => (infectedId, 1) }
      .reduceByKey(_ + _)

    val closeContactsCountsArray = closeContactsCounts.collect()
    closeContactsCountsArray.foreach(println)

    sc.stop()
  }
}

