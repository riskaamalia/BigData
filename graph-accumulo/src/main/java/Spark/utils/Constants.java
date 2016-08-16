package Spark.utils;

/**
 * Created by msk-1180 on 08-Sep-15.
 */
public class Constants {
    public static final String APP_NAME = "individu test";
    public static final boolean TEST_IN_LOCAL = true;
    public static final String WORD_TYPE = "wordtype";
    public static final String WORD_TYPE_NAME = WORD_TYPE + ".personname";
    public static final String WORD_TYPE_ADMIN = WORD_TYPE + ".administrative";
    public static final String DEFAULT_SOURCE = "default";
    public static final String DEFAULT_VISIBILITY = "public";
    public static final String LEBAH_ZOO_SERVERS = "agnes:2181,semut:2181,nyamuk:2181,kevin:2181,rayap:2181";
    public static final String DEPATI_ZOO_SERVERS = "depatihamzah:2181,iswahyudi:2181,soetomo:2181,muwardi:2181,soeharso:2181";
    public static final String DEFAULT_ZOO_SERVERS = "localhost";

    /**
     * Accumulo instance name. Please refer to cloudera manager settings.
     */
    public static String INSTANCE_NAME = "instance.name";
    /**
     * Job name will be used in accumulo & spark
     */
    public static String JOB_NAME = "job.name";
    /**
     * Jar collections to distribute in cluster
     */
    public static String JAR = "jar.name";
    /**
     * Accumulo username
     */
    public static String USERNAME = "username";
    /**
     * Accumulo password
     */
    public static String PASSWORD = "password";
    /**
     * Visibility on accumulo table
     */
    public static String AUTH_VISIBILITY = "auth.visibility";
    /**
     * Comma separated string of available zookeeper servers
     */
    public static String ZOOKEEPERS = "zookeeper.servers";
    /**
     * Accumulo table to read
     */
    public static String TABLE_INPUT = "table.input";
    /**
     * Accumulo table to write on
     */
    public static String TABLE_OUTPUT = "table.output";
    /**
     * AppSearch can/can't create table if the output table is not exists
     */
    public static String CREATE_TABLE = "table.can.create";

    public static String ROWKEY_START = "table.rowkey.start";

    public static String ROWKEY_END = "table.rowkey.end";

    public static String COLUMN_FAMILY = "table.column.family";

    public static String COLUMN_QUALIFIER = "table.column.qualifier";

    public static String ROWKEY_START_SECONDARY = "table.rowkey.start.secondary";

    public static String ROWKEY_END_SECONDARY = "table.rowkey.end.secondary";

}
