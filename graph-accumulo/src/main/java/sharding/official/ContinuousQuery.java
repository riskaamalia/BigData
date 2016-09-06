package sharding.official;

/**
 * Created by 1224A on 8/30/2016.
 */
import com.beust.jcommander.Parameter;
import com.google.common.collect.Iterators;
import org.apache.accumulo.core.cli.BatchScannerOpts;
import org.apache.accumulo.core.cli.ClientOpts;
import org.apache.accumulo.core.client.BatchScanner;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.iterators.user.IntersectingIterator;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Using the doc2word table created by Reverse.java, this program randomly selects N words per document. Then it continually queries a random set of words in
 * the shard table (created by {@link Index}) using the {@link IntersectingIterator}.
 *
 * See docs/examples/README.shard for instructions.
 */

public class ContinuousQuery {

    static class Opts extends ClientOpts {
        @Parameter(names = "--shardTable", required = true, description = "name of the shard table")
        String tableName = null;
        @Parameter(names = "--doc2Term", required = true, description = "name of the doc2Term table")
        String doc2Term;
        @Parameter(names = "--terms", required = true, description = "the number of terms in the query")
        int numTerms;
        @Parameter(names = "--count", description = "the number of queries to run")
        long iterations = Long.MAX_VALUE;
    }

    public static void main(String[] args) throws Exception {
        Opts opts = new Opts();
        BatchScannerOpts bsOpts = new BatchScannerOpts();
        opts.parseArgs(ContinuousQuery.class.getName(), args, bsOpts);

        Connector conn = opts.getConnector();

        ArrayList<Text[]> randTerms = findRandomTerms(conn.createScanner(opts.doc2Term, opts.auths), opts.numTerms);

        Random rand = new Random();

        BatchScanner bs = conn.createBatchScanner(opts.tableName, opts.auths, bsOpts.scanThreads);
        bs.setTimeout(bsOpts.scanTimeout, TimeUnit.MILLISECONDS);

        for (long i = 0; i < opts.iterations; i += 1) {
            Text[] columns = randTerms.get(rand.nextInt(randTerms.size()));

            bs.clearScanIterators();
            bs.clearColumns();

            IteratorSetting ii = new IteratorSetting(20, "ii", IntersectingIterator.class);
            IntersectingIterator.setColumnFamilies(ii, columns);
            bs.addScanIterator(ii);
            bs.setRanges(Collections.singleton(new Range()));

            long t1 = System.currentTimeMillis();
            int count = Iterators.size(bs.iterator());
            long t2 = System.currentTimeMillis();

            System.out.printf("  %s %,d %6.3f%n", Arrays.asList(columns), count, (t2 - t1) / 1000.0);
        }

        bs.close();

    }

    private static ArrayList<Text[]> findRandomTerms(Scanner scanner, int numTerms) {

        Text currentRow = null;

        ArrayList<Text> words = new ArrayList<>();
        ArrayList<Text[]> ret = new ArrayList<>();

        Random rand = new Random();

        for (Entry<Key,Value> entry : scanner) {
            Key key = entry.getKey();

            if (currentRow == null)
                currentRow = key.getRow();

            if (!currentRow.equals(key.getRow())) {
                selectRandomWords(words, ret, rand, numTerms);
                words.clear();
                currentRow = key.getRow();
            }

            words.add(key.getColumnFamily());

        }

        selectRandomWords(words, ret, rand, numTerms);

        return ret;
    }

    private static void selectRandomWords(ArrayList<Text> words, ArrayList<Text[]> ret, Random rand, int numTerms) {
        if (words.size() >= numTerms) {
            Collections.shuffle(words, rand);
            Text docWords[] = new Text[numTerms];
            for (int i = 0; i < docWords.length; i++) {
                docWords[i] = words.get(i);
            }

            ret.add(docWords);
        }
    }
}