package spark_in_action2021.part2consume_data

import org.apache.spark.sql.SparkSession

/**
 * Failing multiline ingestion JSON ingestion in a dataframe.
 *
 * This example illustrates what happens when you forget the multiline
 * option and try to ingest a multiline JSON file.
 *
 * Output is:
 *
 * <pre>
 * +--------------------+
 * |     _corrupt_record|
 * +--------------------+
 * |                 [ {|
 * |       "tag" : "A1",|
 * |  "geopoliticalar...|
 * +--------------------+
 * only showing top 3 rows
 *
 * root
 * |-- _corrupt_record: string (nullable = true)
 * </pre>
 *
 * The data comes from The Bureau of Consular Affairs of the US Department
 * of State. You can access their open data portal at
 * https://cadatacatalog.state.gov/.
 *
 * @author rambabu.posa
 */
object Lab7_34MultilineJsonToDataframeWithCorruptRecordScalaApp {

  def main(args: Array[String]): Unit = {
    // Creates a session on a local master
    val spark: SparkSession = SparkSession.builder
      .appName("Multiline JSON to Dataframe, without multiline option")
      .master("local[*]")
      .getOrCreate

    // Reads a JSON, called countrytravelinfo.json, stores it in a
    // dataframe,
    // without specifying the multiline option
    val df = spark.read
      .format("json")
      .load("data/chapter7/countrytravelinfo.json")
      .cache()

    // Shows at most 3 rows from the dataframe
    df.filter(df.col("_corrupt_record").isNotNull).count()

    df.show(3)
    df.printSchema

    spark.stop
  }

}