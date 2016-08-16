package input;

import com.msk.graph.BaseGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.storage.AccumuloLegacyStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224A on 4/7/2016.
 */
public class CopyExcel {
    private static Logger logger = LoggerFactory.getLogger(CopyExcel.class);

    public static void main(String[] args) {
        logger.debug("Test running....");
        AccumuloLegacyStorage.Builder builder = AccumuloLegacyStorage.Builder.RajaampatBuilder();
        builder.setTablename("geoword4");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        logger.debug("Builder options: {}", builder);
        BaseGraph graph = new BaseGraph("testAccumulograph", new AccumuloLegacyStorage(builder));
        graph.indexes.indexers.add(new NeighborOutIndex(graph.indexes.storage));
        graph.indexes.indexers.add(new NeighborInIndex(graph.indexes.storage));
        graph.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new VertexTypeIndex(graph.indexes.storage));

        long start = System.currentTimeMillis();

        List<GraphFilter> filters = new ArrayList<GraphFilter>();
        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "jalan"));

        Vertices vv = graph.getVertices(filters);
        File file = new File("D:\\hasil.csv");

        try {
            // Step 3 : Buat file kosong menggunakan method createNewFile
            if (file.createNewFile()) {
                System.out.println("File Berhasil dibuat");
            } else {
                System.out.println("File sudah pernah dibuat");
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf = new BufferedWriter(fw);
            for(Vertex v: vv){
                bf.write(v.getVertexId());
                bf.newLine();
                logger.info(v.getVertexId());
            }
            bf.close();

            System.out.println("Selesai");

        } catch (IOException ex) {
            System.err.println("Terjadi kesalahan menulis file");
            ex.printStackTrace();
        }

        long end = System.currentTimeMillis();
        logger.debug((end - start) + " ms elapsed");
    }
}
