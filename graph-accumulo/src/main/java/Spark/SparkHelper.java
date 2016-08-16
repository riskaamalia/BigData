package Spark;

/**
 * Created by 1224A on 5/24/2016.
 */

import Spark.utils.Constants;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.IOException;

public class SparkHelper {
    public static Job getMapReduceJob(Class clazz) throws IOException {
        Job job = Job.getInstance(new org.apache.hadoop.conf.Configuration());
        job.setJobName(clazz.getName());
        job.setJarByClass(clazz);

        return job;
    }

    public static SparkConf getSparkConf(Class clazz){
        Configuration config = new ConfigurationFactory.ConfigurationBuilder().getConfiguration();
        SparkConf sconf = new SparkConf();
        if (Constants.TEST_IN_LOCAL)
            sconf.setMaster("local[2]");
        sconf.setAppName(clazz.getName());

        sconf.set("spark.storage.memoryFraction", "0.5");
        sconf.set("spark.driver.maxResultSize", "3g");
        sconf.set("spark.executor.extraClassPath","/opt/cloudera/CDH/lib/hive/lib/*");
        sconf.set("spark.executor.extraJavaOptions",
                "-Dsun.io.serialization.extendedDebugInfo=true");
        sconf.set("spark.serializer",
                "org.apache.spark.serializer.KryoSerializer");
        sconf.set("spark.kryoserializer.buffer.mb", "256");
        sconf.set("spark.kryoserializer.buffer.max", "5");
        sconf.set("spark.kryo.registrator", "com.msk.graph.bulk.utils.kryo.Registrator");
        return sconf;
    }

    public static final String[] getJARs(Class clazz){
        return new String[] { new File(clazz
                .getProtectionDomain().getCodeSource().getLocation().getPath())
                .getName() };
    }

    public static JavaSparkContext getDefaultJSC (Class clazz){
        return new JavaSparkContext(getSparkConf(clazz));
    }

}