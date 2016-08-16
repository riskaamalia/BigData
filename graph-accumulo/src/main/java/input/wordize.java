package input;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.property.MultivaluedProperty;
import com.msk.graph.property.Property;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage;
import com.msk.graph.storage.AccumuloLegacyStorage2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224A on 7/15/2016.
 */
public class wordize {
    public static void main(String[] args) throws FileNotFoundException {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("master_data_administrasi");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("wordizeadm", new AccumuloLegacyStorage(builder));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        List<GraphFilter> filters = new ArrayList<GraphFilter>();
//        filters.add(new GraphFilter(GraphFilter.DATA_TYPE, "vertex"));
        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "location"));
        int i =0;
        Vertices vv = graph.getVertices(filters);
        Boolean masuk = true;
        System.out.println("tes");
        for (Vertex vt : vv) {
            /*if (vt.getVertexId().substring(0).equals("p") == true)
                masuk = true;
*/
//            System.out.println(vt.getId());
            for (Property tes:((MultivaluedProperty)vt.getProperty("name")).props) {
                try {
                    if ((tes.get().substring(0, 3).equals("INA") == false) && (tes.get().substring(0, 3).equals("ina") == false)) {
                        System.out.println(vt.getId() + " === " + tes.get() + " = " + tes.name());
                        Vertex rewrrite = graph.createVertex(vt.getType(), vt.getVertexId());
                        rewrrite.getProperties().addProperty(new SimpleProperty("name.hasil_wordize", tes.get()));
//                        if (i % 100 == 0)
                            System.out.println("jumlah : " + i + " isinya " + vt.getType() + " === " + vt.getVertexId() + " === " + tes.get());
                    }
                } catch (StringIndexOutOfBoundsException op) {
                    System.out.println("ada error yang if = " + vt.getId());
                }
            }
                    i++;
//                }
        }
    }
}
