package Spark;

/**
 * Created by 1224A on 8/25/2016.
 */

import au.com.bytecode.opencsv.CSVReader;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.io.StringReader;

public class csv {

    public static class ParseLine implements Function<String, String[]> {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("riskacoba");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> csvFile1 = sc.textFile("tes");
        JavaRDD<String[]> csvData = csvFile1.map(new ParseLine());

        public String[] call(String line) throws Exception {
            CSVReader reader = new CSVReader(new StringReader(line));
            System.out.println(reader.readNext());
            return reader.readNext();
        }
    }

    public static void main (String [] args) throws Exception {
        ParseLine obj = new ParseLine();
        obj.call("D:/mediatrac/DATA/jalan indonesia.csv");
    }

}
