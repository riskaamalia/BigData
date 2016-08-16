package similarity;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224-2 on 03-Mar-16.
 */
public class mclTest {
    private static Logger logger = LoggerFactory.getLogger(mclTest.class);

    public static void main(String[] args) {
        logger.debug("Test running....");
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("geoword4");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        logger.debug("Builder options: {}", builder);
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("testAccumulograph", new AccumuloLegacyStorage2(builder));
        graph.indexes.indexers.add(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new NeighborInIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new VertexTypeIndex(graph.indexes.storage));
        graph.indexes.indexers.add(new WordIndexer(graph.indexes.storage,graph));
        /*JaccardIndex jaccard = new JaccardIndex(graph.indexes.storage,  new JaccardIndexTest.NullFilter(), 15);
        graph.addNewIndexer(jaccard);
*/
        long start = System.currentTimeMillis();

        List<GraphFilter> filters = new ArrayList<GraphFilter>();
//        filters.add(new GraphFilter(GraphFilter.VERTEX_NEIGHBOR_OUT, "vertex.jalan", "hasjalan"));
//        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE,"vertex.jalan"));


//        filters.addAll(JaccardIndex.getLevenshteinSubstringFilter("name", "sudirman", 1.0));
//        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "jalan"));
        filters.add(new GraphFilter(WordIndexer.WORD,"kelapa dua"));
//        filters.add(new GraphFilter(GraphFilter.EDGE_TYPE, "hasgedung"));
//        filters.addAll(JaccardIndex.getLevenshteinSubstringFilter("name", "tower", 0.75));

        Vertices vv = graph.getVertices(filters);
        for(Vertex v: vv) {
            if (v.getType().equals("word") == false && v.getType().equals("wordtype") == false && v.getType().equals("phrase") == false) {
                logger.info(v.getType() + " == " + v.getVertexId());
                /*Vertices tatanggi = v.getNeighbors();
                for (Vertex tt : tatanggi) {
                    if (tt.getType().equals("word") == false && tt.getType().equals("wordtype") == false && tt.getType().equals("phrase") == false)
                        logger.info(v.getType() + " == " + v.getVertexId() + " --> " + tt.getId());
                }*/
            }
        }
/*
        filters.add(new GraphFilter(WordIndexer.HAS_WORD, "2555122","address"));
*/
       long end = System.currentTimeMillis();
        logger.debug((end - start) + " ms elapsed");
    }
}
