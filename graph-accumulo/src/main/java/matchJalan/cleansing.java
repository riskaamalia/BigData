package matchJalan;

import com.google.common.base.Stopwatch;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224A on 8/5/2016.
 */
public class cleansing {
    private static final Logger logger = LoggerFactory.getLogger(cleansing.class);
    public static final Stopwatch timer = Stopwatch.createStarted();
    public static void main(String...argv) {

        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("master_data_jalan");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("similarTest", new AccumuloLegacyStorage2(builder));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));

        AccumuloLegacyStorage2.Builder builder2 = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder2.setTablename("coba");
        builder2.setUserAuth("riska");
        builder2.setPassword("12345678");
        AccumuloLegacyGraph graph2 = new AccumuloLegacyGraph("similarTest", new AccumuloLegacyStorage2(builder2));
        graph2.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph2.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph2.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        graph2.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph2));
        graph2.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph2));
        graph2.addNewIndexer(new WordIndexer(graph.indexes.storage, graph2));

        List<GraphFilter> filters = new ArrayList<>();
        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "kecamatan"));
//        filters.add(new GraphFilter(GraphFilter.VERTEX_NEIGHBOR_OUT, " aceh barat"));
        /*Vertex huhu =graph.createVertex("aneh", "masa");
        huhu.getProperties().addProperty(new SimpleProperty("name", "iya sih"));*/

//        filters.add(new GraphFilter(WordIndexer.WORD,"kabupaten aceh barat"));
        Vertices vs = graph.getVertices(filters);
        for (Vertex oo : vs) {
//            logger.info(oo.getType() + " = " + oo.getVertexId());
            if ( oo.getVertexId().contains("kecamatan") ) {
                logger.info("Adaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+oo.getType() + " = " + oo.getVertexId());
                Vertex huhu =graph.createVertex(oo.getType(), oo.getVertexId().replace("kecamatan", "").trim());
                        huhu.getProperties().addProperty(new SimpleProperty("name", oo.getVertexId().replace("kecamatan", "").trim()));
                Vertices yangDiEdit = oo.getNeighbors();
                for (Vertex dihapus : yangDiEdit) {
                    if ((dihapus.getType().equals("word") == false) && (dihapus.getType().equals("phrase") == false) && (dihapus.getType().equals("wordtype") == false)) {
//                        logger.info("Tetangga : " + dihapus.getId());
                        graph.createEdge(dihapus.getId(), huhu.getId(), "haskecamatan")
                                .addProperty(new SimpleProperty("posgre_dbdevgis_indonesiastreet", "haskecamatan"));
                        graph.createEdge(huhu.getId(), dihapus.getId(), "kecamatanof")
                                .addProperty(new SimpleProperty("posgre_dbdevgis_indonesiastreet", "kecamatanof"));
                    }
                }
                graph.removeVertex("kecamatan",oo.getVertexId());
//                System.exit(0);
            }
            if (oo.getVertexId().substring(0).equals("l"))
                            System.exit(0);
         }
    }

}

