package sharding.official;

/**
 * Created by 1224A on 8/30/2016.
 */
import com.beust.jcommander.Parameter;
import org.apache.accumulo.core.cli.BatchWriterOpts;
import org.apache.accumulo.core.cli.ClientOnRequiredTable;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Text;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This program indexes a set of documents given on the command line into a shard table.
 *
 * What it writes to the table is row = partition id, column family = term, column qualifier = document id.
 *
 * See docs/examples/README.shard for instructions.
 */

public class Index {

    static Text genPartition(int partition) {
        return new Text(String.format("%08x", Math.abs(partition)));
    }

    public static void index(int numPartitions, Text docId, String doc, String splitRegex, BatchWriter bw) throws Exception {

        String[] tokens = doc.split(splitRegex);

        Text partition = genPartition(doc.hashCode() % numPartitions);

        Mutation m = new Mutation(partition);

        HashSet<String> tokensSeen = new HashSet<>();

        for (String token : tokens) {
            token = token.toLowerCase();

            if (!tokensSeen.contains(token)) {
                tokensSeen.add(token);
                m.put(new Text(token), docId, new Value(new byte[0]));
            }
        }

        if (m.size() > 0)
            bw.addMutation(m);
    }

    public static void index(int numPartitions, File src, String splitRegex, BatchWriter bw) throws Exception {
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            if (files != null) {
                for (File child : files) {
                    index(numPartitions, child, splitRegex, bw);
                }
            }
        } else {
            FileReader fr = new FileReader(src);

            StringBuilder sb = new StringBuilder();

            char data[] = new char[4096];
            int len;
            while ((len = fr.read(data)) != -1) {
                sb.append(data, 0, len);
            }

            fr.close();

            index(numPartitions, new Text(src.getAbsolutePath()), sb.toString(), splitRegex, bw);
        }

    }

    static class Opts extends ClientOnRequiredTable {
        @Parameter(names = "--partitions", required = true, description = "the number of shards to create")
        int partitions;
        @Parameter(required = true, description = "<file> { <file> ... }")
        List<String> files = new ArrayList<>();
    }

    public static void main(String[] args) throws Exception {
        Opts opts = new Opts();
        BatchWriterOpts bwOpts = new BatchWriterOpts();
        opts.parseArgs(Index.class.getName(), args, bwOpts);

        String splitRegex = "\\W+";

        BatchWriter bw = opts.getConnector().createBatchWriter(opts.tableName, bwOpts.getBatchWriterConfig());
        for (String filename : opts.files) {
            index(opts.partitions, new File(filename), splitRegex, bw);
        }
        bw.close();
    }
}
