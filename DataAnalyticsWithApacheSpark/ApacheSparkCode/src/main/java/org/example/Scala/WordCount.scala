import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {

    val sparConf = new SparkConf().setMaster("local").setAppName("WordCount")

    val sc = new SparkContext(sparConf)

    val lines: RDD[String] = sc.textFile("Data")

    val words: RDD[String] = lines.flatMap(_.split(" "))

    val wordGroup: RDD[(String, Iterable[String])] = words.groupBy(word => word)

    val wordGroupToCount = wordGroup.map {
      case (word, list) => {
        (word, list.size)
      }
    }

    val resultArray: Array[(String, Int)] = wordGroupToCount.collect()

    resultArray.foreach(println)

    sc.stop()
  }
}
