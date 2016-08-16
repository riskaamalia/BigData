package Spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1224A on 5/12/2016.
 */
public class SparkSQL implements Serializable {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SparkSQL.class);

    private static final String POSTGRE_DRIVER = "org.postgresql.Driver";
    private static final String POSTGRE_USERNAME = "gilang";
    private static final String POSTGRE_PWD = "Sementara3";
    private static final String POSTGRE_CONNECTION_URL =
            "jdbc:postgresql://192.168.10.11:5432/db_devgis?user=" + POSTGRE_USERNAME + "&password=" + POSTGRE_PWD;

    public static void main(String[] args) {
        JavaSparkContext sc =
                new JavaSparkContext(new SparkConf().setAppName("SparkJdbcDs").setMaster(args[0]));

        SQLContext sqlContext = new SQLContext(sc);
        //Data source options
        Map<String, String> options = new HashMap<>();
        options.put("driver", POSTGRE_DRIVER);
        options.put("url", POSTGRE_CONNECTION_URL);
        options.put("dbtable", "temp_may");
        options.put("temp_may",
                "(select * from temp_may limit 100");


        //Load POSTGRE query result as DataFrame
        DataFrame jdbcDF = sqlContext.load("jdbc", options);
        jdbcDF.show();

        /*List<Row> employeeFullNameRows = jdbcDF.collectAsList();

        for (Row employeeFullNameRow : employeeFullNameRows) {
            LOGGER.info(employeeFullNameRow);
        }*/
    }
}
