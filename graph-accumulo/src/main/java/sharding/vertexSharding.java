package sharding;

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
 * Created by 1224A on 9/2/2016.
 */
public class vertexSharding {

    private static Logger logger = LoggerFactory.getLogger(vertexSharding.class);

    public static void main(String[] args) {
        logger.debug("Test running....");
        long start = System.currentTimeMillis();
        vertexSharding objgraph = new vertexSharding();
        AccumuloLegacyGraph graph = objgraph.getGraph("geoword4");
        List<GraphFilter> filters = new ArrayList<GraphFilter>();
        Vertices vv = graph.getVertices(filters);

        for(Vertex v: vv) {
                logger.info(v.getType() + " == " + v.getVertexId());
        }

        long end = System.currentTimeMillis();
        logger.debug((end - start) + " ms elapsed");
    }

    public AccumuloLegacyGraph getGraph (String tablename) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(tablename);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        logger.debug("Builder options: {}", builder);
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("graph_"+tablename, new AccumuloLegacyStorage2(builder));
        graph.indexes.indexers.add(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new NeighborInIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new VertexTypeIndex(graph.indexes.storage));
        graph.indexes.indexers.add(new WordIndexer(graph.indexes.storage,graph));

        return graph;
    }
}
