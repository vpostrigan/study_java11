package spark_in_action2021.part2consume_data

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, StreamingQueryException}
import org.slf4j.LoggerFactory

/**
 * Reads a stream from a stream (network)
 *
 * @author rambabu.posa
 */
class Lab10_21ReadLinesFromNetworkStreamScalaApp {
  private val log = LoggerFactory.getLogger(classOf[Lab10_21ReadLinesFromNetworkStreamScalaApp])

  def start(): Unit = {
    log.debug("-> start()")

    val spark = SparkSession.builder
      .appName("Read lines over a network stream")
      .master("local[*]")
      .getOrCreate

    val df = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load

    val query = df.writeStream
      .outputMode(OutputMode.Update)
      .format("console")
      .start

    try {
      query.awaitTermination(60000)
    } catch {
      case e: StreamingQueryException =>
        log.error(s"Exception while waiting for query to end ${e.getMessage}.", e)
    }

    // Executed only after a nice kill
    log.debug("Query status: {}", query.status)
    log.debug("<- start()")
  }
}

object ReadLinesFromNetworkStreamScalaApplication {

  def main(args: Array[String]): Unit = {
    val app = new Lab10_21ReadLinesFromNetworkStreamScalaApp
    app.start()
  }

}
