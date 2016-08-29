package Spark;

/**
 * Created by 1224A on 8/29/2016.
 */
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class csv2 {
    public static void main(String[] args) {
        SparkConf sparkconf = new SparkConf().setAppName("riskacoba").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(sparkconf);

        List<String> sentences = new ArrayList<String>();

        String csvFile = "D:\\mediatrac\\DATA\\jalan indonesia.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] sentence = line.split(cvsSplitBy);
                for (String eachsentence : sentence){
                    sentences.add(eachsentence);
                }
            }
            JavaRDD<String>Data =sc.parallelize(sentences);
            Data.foreach(new VoidFunction<String>() {
                public void call(String f) throws Exception {
                    System.out.println(f);
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

