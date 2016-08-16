package input;

import com.google.common.base.Stopwatch;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.indexer.NeighborInIndex;
import com.msk.graph.indexer.NeighborOutIndex;
import com.msk.graph.indexer.VertexTypeIndex;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Riska on 15/03/2016.
 */
public class UpdateWeekly2 {

    private static final Logger logger = LoggerFactory.getLogger(UpdateWeekly2.class);
    public static final Stopwatch timer = Stopwatch.createStarted();

    public static void main(String[] args) throws Exception {
        UpdateWeekly2 IG=new UpdateWeekly2();
        int input ;
        input = 0;
        IG.runPOI(input);
    }

    public void runPOI(int doing) throws Exception {

        long start = System.currentTimeMillis();

        AccumuloLegacyGraph graph = getGraph ();
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage));
//        graph.addNewIndexer(new CoordinateIndexer(graph.indexes.storage));
//        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
//        VertexTypeIndex vertextype = new VertexTypeIndex(graph.indexes.storage);
//        graph.addNewIndexer(vertextype);
//        JaccardIndex jaccard = new JaccardIndex(graph.indexes.storage,  new JaccardIndexTest.NullFilter(), 20);
//        graph.addNewIndexer(jaccard);
        int j=0;
        List<String> names = Files.readAllLines(new File("d:\\mediatrac\\DATA\\tb_all_poi_201604221357_update.txt").toPath(), Charset.defaultCharset());
        for(String name: names){
            String[] ss= name.split("\t");
            if(j++%10==0)
                System.out.println("Currently processed " + j + " lines and it took " + timer.elapsed(TimeUnit.SECONDS) + " seconds");

//            logger.info(ss[0]);
            try {
            String poi = ss[0].toLowerCase().replace(",","").replace("\"","");
            String Long =ss[1].replace(",","").replace("\"","");
            String Lat = ss[2].replace(",","").replace("\"","");
            String Alamatnya = ss[4].replace(",","").replace("\"","");
            String Kelid = "INA"+ss[3].replace(",","").replace("\"","");

            Vertex valamat, vkelid, vkecid, vkotid, vprovid,vpoi;

            if(Alamatnya!=null) {
                valamat=graph.createVertex("address",Alamatnya.toLowerCase().replace(",","\\c"));
                logger.info(Alamatnya);
                vkelid = new Vertex("location", Kelid);
                vkecid = new Vertex("location", Kelid.substring(0, Kelid.length() - 3));
                vkotid = new Vertex("location", Kelid.substring(0, Kelid.length() - 6));
                vprovid = new Vertex("location", Kelid.substring(0, Kelid.length() - 8));
                vpoi = graph.createVertex("poi",poi.toLowerCase().replace(",", "\\c"));

                switch (doing) {
                    case 0: {
                        valamat.getProperties().addProperty(new SimpleProperty("name.posgre_tballpoi", Alamatnya.toLowerCase().replace(",", "\\c")));
                        valamat.getProperties().addProperty(new SimpleProperty("coordinate.posgre_tballpoi", Lat + ", " + Long));
                        vpoi.getProperties().addProperty(new SimpleProperty("name.posgre_tballpoi", poi.toLowerCase().replace(",", "\\c")));
                        graph.createEdge(valamat,vkelid,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_tballpoi", "haslocation"));
                        graph.createEdge(vkelid,valamat,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_tballpoi", "locationof"));
                        //poi
                        graph.createEdge(vpoi,valamat,"hasaddress").getProperties().
                                addProperty(new SimpleProperty("hasaddress.posgre_tballpoi", "hasaddress"));
                        graph.createEdge(valamat,vpoi,"haspoi").getProperties().
                                addProperty(new SimpleProperty("haspoi.posgre_tballpoi", "haspoi"));
                        graph.createEdge(valamat,vkecid,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_tballpoi", "haslocation"));
                        graph.createEdge(vkecid,valamat,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_tballpoi", "locationof"));
                        graph.createEdge(valamat,vkotid,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_tballpoi", "haslocation"));
                        graph.createEdge(vkotid,valamat,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_tballpoi", "locationof"));
                        graph.createEdge(valamat,vprovid,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_tballpoi", "haslocation"));
                        graph.createEdge(vprovid,valamat,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_tballpoi", "locationof"));
                        break;
                    }
                }
            }
            } catch (ArrayIndexOutOfBoundsException poia) {
                poia.printStackTrace();
            }
        }

    }

    public static AccumuloLegacyGraph getGraph () {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("geoword4");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("insertLatLong", new AccumuloLegacyStorage2(builder));
        return graph;
    }

    public static Connection connectSql(String dbUrl, String username, String password){
        Connection connection = null;
        try {
            Properties props = new Properties();
            props.setProperty("user",username);
            props.setProperty("password", password);
            connection = DriverManager.getConnection(dbUrl, props);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
