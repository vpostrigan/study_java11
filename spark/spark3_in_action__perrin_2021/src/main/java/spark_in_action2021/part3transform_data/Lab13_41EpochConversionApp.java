package spark_in_action2021.part3transform_data;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_unixtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class Lab13_41EpochConversionApp {

    public static void main(String[] args) {
        Lab13_41EpochConversionApp app = new Lab13_41EpochConversionApp();
        app.start();
    }

    private void start() {
        SparkSession spark = SparkSession.builder()
                .appName("expr()")
                .master("local")
                .getOrCreate();

        StructType schema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("event", DataTypes.IntegerType, false),
                DataTypes.createStructField("ts", DataTypes.StringType, false)});

        // Building a df with a sequence of chronological timestamps
        List<Row> rows = new ArrayList<>();
        long now = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 50; i++) {
            rows.add(RowFactory.create(i, String.valueOf(now)));
            now += new Random().nextInt(3) + 1;
        }
        Dataset<Row> df = spark.createDataFrame(rows, schema);
        df.show();

        // Turning the timestamps to dates
        df = df.withColumn("date", from_unixtime(col("ts")));
        df.show();

        // Collecting the result and printing on
        List<Row> timeRows = df.collectAsList();
        for (Row r : timeRows) {
            System.out.printf("[%d] : %s (%s)\n",
                    r.getInt(0),
                    r.getString(1),
                    r.getString(2));
        }
    }
/*
+-----+----------+
|event|        ts|
+-----+----------+
|    0|1665745987|
|    1|1665745989|
|    2|1665745990|
|    3|1665745992|
|    4|1665745993|
|    5|1665745994|
|    6|1665745996|
|    7|1665745999|
|    8|1665746001|
|    9|1665746002|
|   10|1665746004|
|   11|1665746006|
|   12|1665746008|
|   13|1665746009|
|   14|1665746012|
|   15|1665746015|
|   16|1665746016|
|   17|1665746017|
|   18|1665746020|
|   19|1665746022|
+-----+----------+
only showing top 20 rows

+-----+----------+-------------------+
|event|        ts|               date|
+-----+----------+-------------------+
|    0|1665745987|2022-10-14 14:13:07|
|    1|1665745989|2022-10-14 14:13:09|
|    2|1665745990|2022-10-14 14:13:10|
|    3|1665745992|2022-10-14 14:13:12|
|    4|1665745993|2022-10-14 14:13:13|
|    5|1665745994|2022-10-14 14:13:14|
|    6|1665745996|2022-10-14 14:13:16|
|    7|1665745999|2022-10-14 14:13:19|
|    8|1665746001|2022-10-14 14:13:21|
|    9|1665746002|2022-10-14 14:13:22|
|   10|1665746004|2022-10-14 14:13:24|
|   11|1665746006|2022-10-14 14:13:26|
|   12|1665746008|2022-10-14 14:13:28|
|   13|1665746009|2022-10-14 14:13:29|
|   14|1665746012|2022-10-14 14:13:32|
|   15|1665746015|2022-10-14 14:13:35|
|   16|1665746016|2022-10-14 14:13:36|
|   17|1665746017|2022-10-14 14:13:37|
|   18|1665746020|2022-10-14 14:13:40|
|   19|1665746022|2022-10-14 14:13:42|
+-----+----------+-------------------+
only showing top 20 rows

[0] : 1665745987 (2022-10-14 14:13:07)
[1] : 1665745989 (2022-10-14 14:13:09)
[2] : 1665745990 (2022-10-14 14:13:10)
[3] : 1665745992 (2022-10-14 14:13:12)
[4] : 1665745993 (2022-10-14 14:13:13)
[5] : 1665745994 (2022-10-14 14:13:14)
[6] : 1665745996 (2022-10-14 14:13:16)
[7] : 1665745999 (2022-10-14 14:13:19)
[8] : 1665746001 (2022-10-14 14:13:21)
[9] : 1665746002 (2022-10-14 14:13:22)
[10] : 1665746004 (2022-10-14 14:13:24)
[11] : 1665746006 (2022-10-14 14:13:26)
[12] : 1665746008 (2022-10-14 14:13:28)
[13] : 1665746009 (2022-10-14 14:13:29)
[14] : 1665746012 (2022-10-14 14:13:32)
[15] : 1665746015 (2022-10-14 14:13:35)
[16] : 1665746016 (2022-10-14 14:13:36)
[17] : 1665746017 (2022-10-14 14:13:37)
[18] : 1665746020 (2022-10-14 14:13:40)
[19] : 1665746022 (2022-10-14 14:13:42)
[20] : 1665746024 (2022-10-14 14:13:44)
[21] : 1665746025 (2022-10-14 14:13:45)
[22] : 1665746028 (2022-10-14 14:13:48)
[23] : 1665746031 (2022-10-14 14:13:51)
[24] : 1665746033 (2022-10-14 14:13:53)
[25] : 1665746034 (2022-10-14 14:13:54)
[26] : 1665746036 (2022-10-14 14:13:56)
[27] : 1665746037 (2022-10-14 14:13:57)
[28] : 1665746038 (2022-10-14 14:13:58)
[29] : 1665746040 (2022-10-14 14:14:00)
[30] : 1665746042 (2022-10-14 14:14:02)
[31] : 1665746044 (2022-10-14 14:14:04)
[32] : 1665746045 (2022-10-14 14:14:05)
[33] : 1665746046 (2022-10-14 14:14:06)
[34] : 1665746048 (2022-10-14 14:14:08)
[35] : 1665746049 (2022-10-14 14:14:09)
[36] : 1665746050 (2022-10-14 14:14:10)
[37] : 1665746051 (2022-10-14 14:14:11)
[38] : 1665746053 (2022-10-14 14:14:13)
[39] : 1665746054 (2022-10-14 14:14:14)
[40] : 1665746056 (2022-10-14 14:14:16)
[41] : 1665746058 (2022-10-14 14:14:18)
[42] : 1665746061 (2022-10-14 14:14:21)
[43] : 1665746062 (2022-10-14 14:14:22)
[44] : 1665746065 (2022-10-14 14:14:25)
[45] : 1665746067 (2022-10-14 14:14:27)
[46] : 1665746068 (2022-10-14 14:14:28)
[47] : 1665746071 (2022-10-14 14:14:31)
[48] : 1665746073 (2022-10-14 14:14:33)
[49] : 1665746076 (2022-10-14 14:14:36)
 */
}