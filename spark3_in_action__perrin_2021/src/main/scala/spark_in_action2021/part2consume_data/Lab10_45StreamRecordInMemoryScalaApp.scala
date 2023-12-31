package spark_in_action2021.part2consume_data

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory
import spark_in_action2021.streaming.StreamingScalaUtils

/**
 * Analyzes the records on the stream and send each record to a debugger class.
 *
 * @author rambabu.posa
 */
class Lab10_45StreamRecordInMemoryScalaApp {

  private val log = LoggerFactory.getLogger(classOf[Lab10_45StreamRecordInMemoryScalaApp])

  def start():Unit = {

    log.debug("-> start()")

    val spark = SparkSession.builder
      .appName("Read lines over a file stream")
      .master("local")
      .getOrCreate

    val recordSchema = new StructType()
      .add("fname", "string")
      .add("mname", "string")
      .add("lname", "string")
      .add("age", "integer")
      .add("ssn", "string")

    val df = spark.readStream
      .format("csv")
      .schema(recordSchema)
      .csv(StreamingScalaUtils.getInputDirectory)

    val query = df.writeStream
      .outputMode(OutputMode.Append)
      .format("memory")
      .option("queryName", "people")
      .start

    // Wait and process the incoming stream for the next minute
    var queryInMemoryDf: DataFrame = null
    var iterationCount = 0
    val start = System.currentTimeMillis

    while (query.isActive) {
      queryInMemoryDf = spark.sql("SELECT * FROM people")
      iterationCount += 1
      log.debug(s"Pass #$iterationCount, dataframe contains ${queryInMemoryDf.count} records")

      queryInMemoryDf.show()

      if (start + 60000 < System.currentTimeMillis)
        query.stop()

      try {
        Thread.sleep(2000)
      } catch {
        case e: InterruptedException =>
        // Simply ignored
      }
    }

    log.debug("<- start()")
  }

}

object StreamRecordInMemoryScalaApplication {

  def main(args: Array[String]): Unit = {
    val app = new Lab10_45StreamRecordInMemoryScalaApp
    app.start
  }

}
