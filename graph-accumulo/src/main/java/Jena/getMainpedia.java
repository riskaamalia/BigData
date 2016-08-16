package Jena;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.storage.AccumuloLegacyStorage;
import com.msk.graph.storage.AccumuloStorage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224A on 7/13/2016.
 */
public class getMainpedia {
    public static void main(String[] args) throws FileNotFoundException {
        AccumuloStorage.Builder builder = AccumuloStorage.Builder.RajaampatBuilder();
        builder.setTablename("master_data_dbpedia");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("sparqlTSVgraph", new AccumuloLegacyStorage(builder));
//        MemoryGraph graph = new MemoryGraph(table);
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        List<GraphFilter> filters = new ArrayList<>();
        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "phrase"));
        Vertices vv = graph.getVertices(filters);
        for (Vertex vt : vv) {
            System.out.println(vt.getId() + " === " + vt.getProperties().getProperty("phrase.indexer"));
        }
    }
}
