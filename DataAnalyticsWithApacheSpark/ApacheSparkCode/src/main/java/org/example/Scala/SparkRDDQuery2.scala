import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object SparkRDDQuery2 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local").setAppName("SparkRDDQuery2")
    val sc = new SparkContext(sparkConf)

    val people: RDD[(String, Double, Double)] = sc.textFile("/Users/axelluca/Desktop/Trial2/Infected_Data/PEOPLE-large.csv")
      .filter(!_.startsWith("ID"))
      .map { line =>
        val parts = line.split(',')
        (parts(0), parts(1).toDouble, parts(2).toDouble)
      }

    val infected: RDD[(String, Double, Double)] = sc.textFile("/Users/axelluca/Desktop/Trial2/Infected_Data/INFECTED-small.csv")
      .filter(!_.startsWith("ID"))
      .map { line =>
        val parts = line.split(',')
        (parts(0), parts(1).toDouble, parts(2).toDouble)
      }

    val infectedIDs = infected.map { case (id, x, y) => id }
      .distinct()
      .collect()

    val broadcastInfectedIDs = sc.broadcast(infectedIDs)

    val nonInfectedPeople = people.filter { case (personID, _, _) => !broadcastInfectedIDs.value.contains(personID) }

    val closeContacts: RDD[String] = nonInfectedPeople.cartesian(infected)
      .filter { case ((id1, x1, y1), (id2, x2, y2)) => id1 != id2 && math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) <= 6}
      .map { case ((nonInfectedID, _, _), (infectedID, _, _)) => nonInfectedID }
      .distinct()

    val closeContactsArray = closeContacts.collect()
    closeContactsArray.foreach(println)

    sc.stop()
  }
}
