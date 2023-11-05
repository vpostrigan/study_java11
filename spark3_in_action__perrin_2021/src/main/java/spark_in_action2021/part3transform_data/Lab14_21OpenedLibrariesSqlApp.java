package spark_in_action2021.part3transform_data;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import spark_in_action2021.Logs;

import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.to_timestamp;

/**
 * Custom UDF to check if in range.
 *
 * @author jgp
 */
public class Lab14_21OpenedLibrariesSqlApp {

    public static void main(String[] args) {
        Logs allLogs = new Logs();

        Lab14_21OpenedLibrariesSqlApp app = new Lab14_21OpenedLibrariesSqlApp();
        app.start(allLogs);
    }

    private void start(Logs allLogs) {
        SparkSession spark = SparkSession.builder()
                .appName("Custom UDF to check if in range")
                .master("local[*]")
                .getOrCreate();
        spark
                .udf()
                .register("isOpen", new Lab14_12IsOpenUdf(), DataTypes.BooleanType);

        Dataset<Row> librariesDf = spark.read().format("csv")
                .option("header", true)
                .option("inferSchema", true)
                .option("encoding", "cp1252")
                .load("data/chapter14/south_dublin_libraries/sdlibraries.csv")
                .drop("Administrative_Authority")
                .drop("Address1")
                .drop("Address2")
                .drop("Town")
                .drop("Postcode")
                .drop("County")
                .drop("Phone")
                .drop("Email")
                .drop("Website")
                .drop("Image")
                .drop("WGS84_Latitude")
                .drop("WGS84_Longitude");
        allLogs.showAndSaveToCsv("[1] data csv", librariesDf, 10, false, true);

        StructType schema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("date_str", DataTypes.StringType, false)});

        List<Row> rows = new ArrayList<>();
        rows.add(RowFactory.create("2019-03-11 14:30:00"));
        rows.add(RowFactory.create("2019-04-27 16:00:00"));
        rows.add(RowFactory.create("2020-01-26 05:00:00"));

        Dataset<Row> dateTimeDf = spark
                .createDataFrame(rows, schema)
                .withColumn("date", to_timestamp(col("date_str")))
                .drop("date_str");
        allLogs.showAndSaveToCsv("[2] data csv", dateTimeDf, 10, false, true);

        Dataset<Row> df = librariesDf.crossJoin(dateTimeDf);
        df.createOrReplaceTempView("libraries");
        allLogs.showAndSaveToCsv("[3] crossJoin", df, 10, false, true);

        // Using SQL
        Dataset<Row> finalDf = spark.sql(
                "SELECT Council_ID, Name, date, "
                        + "isOpen("
                        + "Opening_Hours_Monday, Opening_Hours_Tuesday, "
                        + "Opening_Hours_Wednesday, Opening_Hours_Thursday, "
                        + "Opening_Hours_Friday, Opening_Hours_Saturday, "
                        + "'closed', date) AS open FROM libraries ");
        allLogs.showAndSaveToCsv("[4] result", finalDf, 10, false, true);
    }

}
/*
15:28:38.061: [1] data csv
+----------+------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------+----------------------+
|Council_ID|Name                                |Opening_Hours_Monday                                              |Opening_Hours_Tuesday                                             |Opening_Hours_Wednesday                                           |Opening_Hours_Thursday                                            |Opening_Hours_Friday                      |Opening_Hours_Saturday|
+----------+------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------+----------------------+
|SD1       |County Library                      |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-16:30                               |09:45-16:30           |
|SD2       |Ballyroan Library                   |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-16:30                               |09:45-16:30           |
|SD3       |Castletymon Library                 |09:45-17:00                                                       |09:45-17:00                                                       |09:45-17:00                                                       |09:45-17:00                                                       |09:45-16:30                               |09:45-16:30           |
|SD4       |Clondalkin Library                  |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-16:30                               |09:45-16:30           |
|SD5       |Lucan Library                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-20:00                                                       |09:45-16:30                               |09:45-16:30           |
|SD6       |Whitechurch Library                 |14:00-17:00 and 18:00-20:00                                       |14:00-17:00 and 18:00-20:00                                       |09:45-13:00 and 14:00-17:00                                       |14:00-17:00 and 18:00-20:00                                       |Closed                                    |Closed                |
|SD7       |The John Jennings Library (Stewarts)|10:00-17:00 (16:00 July and August) - closed for lunch 12:30-13:00|10:00-17:00 (16:00 July and August) - closed for lunch 12:30-13:00|10:00-17:00 (16:00 July and August) - closed for lunch 12:30-13:00|10:00-17:00 (16:00 July and August) - closed for lunch 12:30-13:00|10:00-16:00 - closed for lunch 12:30-13:00|Closed                |
+----------+------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------------------------------+------------------------------------------+----------------------+

root
 |-- Council_ID: string (nullable = true)
 |-- Name: string (nullable = true)
 |-- Opening_Hours_Monday: string (nullable = true)
 |-- Opening_Hours_Tuesday: string (nullable = true)
 |-- Opening_Hours_Wednesday: string (nullable = true)
 |-- Opening_Hours_Thursday: string (nullable = true)
 |-- Opening_Hours_Friday: string (nullable = true)
 |-- Opening_Hours_Saturday: string (nullable = true)

15:28:38.800: [1] data csv has 7. Saved to C:\Users\admin\AppData\Local\Temp\spark_in_action2021/Lab14_21OpenedLibrariesSqlApp/1_csv
// //
15:28:39.272: [2] data csv
+-------------------+
|date               |
+-------------------+
|2019-03-11 14:30:00|
|2019-04-27 16:00:00|
|2020-01-26 05:00:00|
+-------------------+

root
 |-- date: timestamp (nullable = true)

15:28:39.539: [2] data csv has 3. Saved to C:\Users\admin\AppData\Local\Temp\spark_in_action2021/Lab14_21OpenedLibrariesSqlApp/2_csv
// //
15:28:39.864: [3] crossJoin
+----------+-------------------+--------------------+---------------------+-----------------------+----------------------+--------------------+----------------------+-------------------+
|Council_ID|Name               |Opening_Hours_Monday|Opening_Hours_Tuesday|Opening_Hours_Wednesday|Opening_Hours_Thursday|Opening_Hours_Friday|Opening_Hours_Saturday|date               |
+----------+-------------------+--------------------+---------------------+-----------------------+----------------------+--------------------+----------------------+-------------------+
|SD1       |County Library     |09:45-20:00         |09:45-20:00          |09:45-20:00            |09:45-20:00           |09:45-16:30         |09:45-16:30           |2019-03-11 14:30:00|
|SD1       |County Library     |09:45-20:00         |09:45-20:00          |09:45-20:00            |09:45-20:00           |09:45-16:30         |09:45-16:30           |2019-04-27 16:00:00|
|SD1       |County Library     |09:45-20:00         |09:45-20:00          |09:45-20:00            |09:45-20:00           |09:45-16:30         |09:45-16:30           |2020-01-26 05:00:00|
|SD2       |Ballyroan Library  |09:45-20:00         |09:45-20:00          |09:45-20:00            |09:45-20:00           |09:45-16:30         |09:45-16:30           |2019-03-11 14:30:00|
|SD2       |Ballyroan Library  |09:45-20:00         |09:45-20:00          |09:45-20:00            |09:45-20:00           |09:45-16:30         |09:45-16:30           |2019-04-27 16:00:00|
|SD2       |Ballyroan Library  |09:45-20:00         |09:45-20:00          |09:45-20:00            |09:45-20:00           |09:45-16:30         |09:45-16:30           |2020-01-26 05:00:00|
|SD3       |Castletymon Library|09:45-17:00         |09:45-17:00          |09:45-17:00            |09:45-17:00           |09:45-16:30         |09:45-16:30           |2019-03-11 14:30:00|
|SD3       |Castletymon Library|09:45-17:00         |09:45-17:00          |09:45-17:00            |09:45-17:00           |09:45-16:30         |09:45-16:30           |2019-04-27 16:00:00|
|SD3       |Castletymon Library|09:45-17:00         |09:45-17:00          |09:45-17:00            |09:45-17:00           |09:45-16:30         |09:45-16:30           |2020-01-26 05:00:00|
|SD4       |Clondalkin Library |09:45-20:00         |09:45-20:00          |09:45-20:00            |09:45-20:00           |09:45-16:30         |09:45-16:30           |2019-03-11 14:30:00|
+----------+-------------------+--------------------+---------------------+-----------------------+----------------------+--------------------+----------------------+-------------------+
only showing top 10 rows

root
 |-- Council_ID: string (nullable = true)
 |-- Name: string (nullable = true)
 |-- Opening_Hours_Monday: string (nullable = true)
 |-- Opening_Hours_Tuesday: string (nullable = true)
 |-- Opening_Hours_Wednesday: string (nullable = true)
 |-- Opening_Hours_Thursday: string (nullable = true)
 |-- Opening_Hours_Friday: string (nullable = true)
 |-- Opening_Hours_Saturday: string (nullable = true)
 |-- date: timestamp (nullable = true)

15:28:41.389: [3] crossJoin has 21. Saved to C:\Users\admin\AppData\Local\Temp\spark_in_action2021/Lab14_21OpenedLibrariesSqlApp/3_csv
// //
15:28:42.284: [4] result
+----------+-------------------+-------------------+-----+
|Council_ID|Name               |date               |open |
+----------+-------------------+-------------------+-----+
|SD1       |County Library     |2019-03-11 14:30:00|true |
|SD1       |County Library     |2019-04-27 16:00:00|true |
|SD1       |County Library     |2020-01-26 05:00:00|false|
|SD2       |Ballyroan Library  |2019-03-11 14:30:00|true |
|SD2       |Ballyroan Library  |2019-04-27 16:00:00|true |
|SD2       |Ballyroan Library  |2020-01-26 05:00:00|false|
|SD3       |Castletymon Library|2019-03-11 14:30:00|true |
|SD3       |Castletymon Library|2019-04-27 16:00:00|true |
|SD3       |Castletymon Library|2020-01-26 05:00:00|false|
|SD4       |Clondalkin Library |2019-03-11 14:30:00|true |
+----------+-------------------+-------------------+-----+
only showing top 10 rows

root
 |-- Council_ID: string (nullable = true)
 |-- Name: string (nullable = true)
 |-- date: timestamp (nullable = true)
 |-- open: boolean (nullable = true)

15:28:43.400: [4] result has 21. Saved to C:\Users\admin\AppData\Local\Temp\spark_in_action2021/Lab14_21OpenedLibrariesSqlApp/4_csv
 */