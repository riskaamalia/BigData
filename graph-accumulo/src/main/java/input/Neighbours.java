package input;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.indexer.EdgeTypeIndexer;
import com.msk.graph.indexer.NeighborInIndex;
import com.msk.graph.indexer.NeighborOutIndex;
import com.msk.graph.indexer.WordIndexer;
import com.msk.graph.memory.MemoryGraph;

/**
 * Created by 1224-sementara on 3/28/2016.
 */
public class Neighbours {
    public static Vertices getNeighbours(String vertexId,AccumuloLegacyGraph g){
        g.addNewIndexer(new WordIndexer(g.indexes.storage, g));
        g.addNewIndexer(new NeighborOutIndex(g.indexes.storage, g));
        g.addNewIndexer(new NeighborInIndex(g.indexes.storage, g));
        g.addNewIndexer(new EdgeTypeIndexer(g.indexes.storage));
        Vertex v=g.getVertex(vertexId);
        Vertices vs=v.getNeighbors();
        return vs;
    }
    public static Vertices getNeighbours(String vertexId,MemoryGraph g){
        g.addNewIndexer(new WordIndexer(g.indexes.storage, g));
        g.addNewIndexer(new NeighborOutIndex(g.indexes.storage, g));
        g.addNewIndexer(new EdgeTypeIndexer(g.indexes.storage));
        Vertex v=g.getVertex(vertexId);
        Vertices vs=v.getNeighbors();
        return vs;
    }
}
