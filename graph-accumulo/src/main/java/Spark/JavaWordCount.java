package Spark;

/**
 * Created by 1224A on 5/2/2016.
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;

public final class JavaWordCount {
    public static void main(String[] args) throws Exception {
        /*String master = args[0];
        JavaSparkContext sc = new JavaSparkContext(
                master, "wordcount", System.getenv("SPARK_HOME"), System.getenv("JARS"));
        JavaRDD<String> rdd = sc.textFile(args[1]);
        JavaPairRDD<String, Integer> counts = rdd.flatMap(
                new FlatMapFunction<String, String>() {
                    public Iterable<String> call(String x) {
                        return Arrays.asList(x.split(" "));
                    }}).mapToPair(new PairFunction<String, String, Integer>(){
            public Tuple2<String, Integer> call(String x){
                return new Tuple2(x, 1);
            }}).reduceByKey(new Function2<Integer, Integer, Integer>(){
            public Integer call(Integer x, Integer y){ return x+y;}});
        counts.saveAsTextFile(args[2]);*/
        SparkConf conf = new SparkConf().setMaster("local").setAppName("riskacoba");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4));
        JavaRDD<Integer> result = rdd.map(new Function<Integer, Integer>() {
            public Integer call(Integer x) { return x*x; }
        });
        System.out.println(StringUtils.join(result.collect(), ","));
    }
}
