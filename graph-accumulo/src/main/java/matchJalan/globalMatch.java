package matchJalan;

import com.google.common.base.Stopwatch;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1224A on 8/15/2016.
 */
public class globalMatch {

    private static final Logger logger = LoggerFactory.getLogger(cleansing.class);
    public static final Stopwatch timer = Stopwatch.createStarted();
    static Map<Integer,Vertices> m1 = new HashMap<>();
    static Map<Integer,Vertex> m2 = new HashMap<>();


    public static void main(String... argv) {
        globalMatch objmain = new globalMatch();
        objmain.byTetangga();
    }
    public void similarexact () {
        globalMatch objread1 = new globalMatch();
        AccumuloLegacyGraph graph = objread1.getGraph("master_data_dbpedia2");
        //write
        AccumuloLegacyGraph graphWrite = objread1.getGraph("master_data_dbpedia_v2");

        List<GraphFilter> filters = new ArrayList<>();
        filters.add(new GraphFilter(GraphFilter.DATA_TYPE, "vertex"));

        //filter banding
        List<GraphFilter> filters2 = new ArrayList<>();

        Vertices vs = graph.getVertices(filters);
        int count = 0;
        for (Vertex oo : vs) {
            logger.info(oo.getType() + " === " + oo.getVertexId());
            filters2.add(new GraphFilter(WordIndexer.WORD, oo.getVertexId()));
            Vertices hasil = graph.getVertices(filters2);
            List<String> tipelain = new ArrayList<>();
            String tipenya = null,maintipe=null;
            //cari yang aslinya
            for (Vertex oop : hasil) {
                tipenya = objread1.mainType(oop);
                if (tipenya == null) {
                    if (oop.getType().equals("wordtype") == false) {
                     tipelain.add(oop.getType());
                    } else {
                        tipelain.add(oop.getVertexId());
                        }
                } else {
                    maintipe = tipenya;
                }
            }
            logger.info("HASIL == "+maintipe);
            logger.info("TIPE LAIN == "+tipelain);

            count++;
            /*if (count == 2)
                System.exit(1);*/
        }
    }

    public void byTetangga () {
        globalMatch objread1 = new globalMatch();
        AccumuloLegacyGraph graph = objread1.getGraph("master_data_dbpedia");
        //write
        AccumuloLegacyGraph graphWrite = objread1.getGraph("master_data_dbpedia_v2");

        List<GraphFilter> filters = new ArrayList<>();
        filters.add(new GraphFilter(GraphFilter.DATA_TYPE, "vertex"));

        //filter banding
        List<GraphFilter> filters2 = new ArrayList<>();

        Vertices vall = graph.getVertices(filters);
        Vertices v1=null,v2 = null;
        int count = 0;
        double bobot = 0;
        for (Vertex vv1:vall) {
/*
            logger.info(vv1.getId());
            System.out.println("======================||||||||||||||=========================");
*/
            filters.add(new GraphFilter(GraphFilter.VERTEX_NEIGHBOR_IN, vv1));
            v1 = graph.getVertices(filters);
            objread1.ambilbobot(count,vv1,v1);
            count++;
            if (count % 4 == 0)
                System.exit(1);
        }
    }

    public void ambilbobot (int count,Vertex vnya,Vertices v1) {
        double bobot =0;
        m1.put(count, v1);
        m2.put(count, vnya);
        if (count % 2 == 1 ) {
            System.out.println("==============================================================");
            for (Vertex aa : m1.get(count)) {
                logger.info("Tetangganya "+m2.get(count)+" : " + aa);
            }
            for (Vertex aa : m1.get(count-1)) {
                logger.info("Tetangganya "+m2.get(count-1)+" : " + aa);
            }
            try {
                bobot = m1.get(count-1).intersect(m1.get(count)).toList().size()/m1.get(count-1).toList().size();
            }
            catch (ArithmeticException op) {
                op.getMessage();
            }
            logger.info("Bobot : "+bobot);
        m1.clear();m2.clear();
        }
    }


    public String mainType(Vertex tipenya) {
        String excat = null;
        if (tipenya.getType().equals("wordtype") == false) {
            switch (tipenya.getType()) {
                case "person":
                    excat = "person";
                    break;
                case "organisation":
                    excat = "organisation";
                    break;
                case "place":
                    excat = "place";
                    break;
                case "work":
                    excat = "work";
                    break;
            }
        } else {
            switch (tipenya.getVertexId()) {
                case "person":
                    excat = "person";
                    break;
                case "organisation":
                    excat = "organisation";
                    break;
                case "place":
                    excat = "place";
                    break;
                case "work":
                    excat = "work";
                    break;
            }

        }
        return excat;
    }


    public AccumuloLegacyGraph getGraph (String tablename) {
        //read
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(tablename);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("globalMatch", new AccumuloLegacyStorage2(builder));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));

        return graph;
    }
}
