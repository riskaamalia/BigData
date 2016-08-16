package similarity;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.indexer.EdgeTypeIndexer;
import com.msk.graph.indexer.NeighborOutIndex;
import com.msk.graph.indexer.WordIndexer;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import input.Neighbours;

/**
 * Created by 1224-2 on 28-Mar-16.
 */
public class TestNeighbor {
    public static void main(String[] args) {
    Neighbours obj = new Neighbours();
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("geoword4");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph g = new AccumuloLegacyGraph("validator", new AccumuloLegacyStorage2(builder));
        g.addNewIndexer(new WordIndexer(g.indexes.storage, g));
        g.addNewIndexer(new NeighborOutIndex(g.indexes.storage, g));
        g.addNewIndexer(new EdgeTypeIndexer(g.indexes.storage));
        Vertices tes = obj.getNeighbours("vertex.jalan.senopati",g);
        for (Vertex ff:tes) {
            System.out.println("==="+ff);
        }

    }
}
