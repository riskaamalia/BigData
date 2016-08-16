package input; /**
 * Created by 1224-2 on 31-Mar-16.
 */

import com.google.common.base.Stopwatch;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.indexer.NeighborInIndex;
import com.msk.graph.indexer.NeighborOutIndex;
import com.msk.graph.indexer.VertexTypeIndex;
import com.msk.graph.indexer.WordIndexer;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Riska on 15/03/2016.
 */
public class UpdateStreet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateWeekly.class);
    public static final Stopwatch timer = Stopwatch.createStarted();

    public static void main(String[] args) throws Exception {
        UpdateStreet IG=new UpdateStreet();
        int input ;
        input = 0;
        IG.runPOI(input);
    }

    public ResultSet getResultSQL(String sql) {
        String dbUrl = "jdbc:postgresql://192.168.10.11:5432/db_devgis";
        String username = "gilang";
        String password = "Sementara3";
        Connection conn = connectSql(dbUrl,username,password);
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException ae) {
            // TODO Auto-generated catch block

            return null;
        }
    }

    public void runPOI(int doing) throws Exception {

        long start = System.currentTimeMillis();
        /*Date tanggal = new Date();
        long timestamp = tanggal.getTime();*/

        String sql = "select \"TYPE\",\"LABEL\",\"LABEL2\",ST_AsText(\"the_geom\") from \"Indonesia_street\"";
        ResultSet rs = getResultSQL(sql);
//        System.out.println(rs.toString());

        AccumuloLegacyGraph graph = getGraph ();
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage));
//        graph.addNewIndexer(new CoordinateIndexer(graph.indexes.storage));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
//        VertexTypeIndex vertextype = new VertexTypeIndex(graph.indexes.storage);
//        graph.addNewIndexer(vertextype);
//        JaccardIndex jaccard = new JaccardIndex(graph.indexes.storage,  new JaccardIndexTest.NullFilter(), 20);
//        graph.addNewIndexer(jaccard);
        int j=0;
        while (rs.next()) {
            if(j++%1000==0)
                System.out.println("Currently processed " + j + " lines and it took " + timer.elapsed(TimeUnit.SECONDS) + " seconds");
            String type = rs.getString(1);
            String label =rs.getString(2);
            String label2 =rs.getString(3);
            String geom =rs.getString(4);
//            System.out.println("+++++");
//            System.out.println("||||"+label);

            Vertex vlabel,vgeom;

            if(label!=null) {
                vlabel = graph.createVertex ("jalan",label.toLowerCase().replace(",","\\c")/*+"_"+timestamp*/);
//                vgeom = graph.createVertex("geom",geom.toLowerCase().replace(",","\\c")+"_dbdevgis"/*+"_"+timestamp*/);
//                System.out.println("========="+vlabel.getId());

//              Update lebih lama karena harus cek dulu ke setiap vertex, apakah ada perubahan atau tidak,
//                kalau ada baru insert vertex baru
                /*if () {
                    doing=1;
                }*/

                switch (doing) {
                    case 0: {
                        vlabel.getProperties().addProperty(new SimpleProperty("type.posgre_dbdevgis_indonesiastreet", type.toLowerCase().replace(",", "\\c")));
                        vlabel.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_indonesiastreet", label.toLowerCase().replace(",", "\\c")));
                        vlabel.getProperties().addProperty(new SimpleProperty("label.posgre_dbdevgis_indonesiastreet", label.toLowerCase().replace(",", "\\c")));
                        vlabel.getProperties().addProperty(new SimpleProperty("geom.posgre_dbdevgis_administrasikabindonesia", geom.toLowerCase().replace(",", "\\c")));
                        if (label2 != null) {
                            vlabel.getProperties().addProperty(new SimpleProperty("label2.posgre_dbdevgis_indonesiastreet", label2.toLowerCase().replace(",", "\\c")));
                        }
                        /*vgeom.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_indonesiastreet",geom.toLowerCase().replace(",","\\c")));
                        graph.createEdge(vgeom,vlabel,"geomof").getProperties().
                                addProperty(new SimpleProperty("geomof.posgre_dbdevgis_indonesiastreet", "geomof"));
                        graph.createEdge(vlabel,vgeom,"hasgeom").getProperties().
                                addProperty(new SimpleProperty("hasgeom.posgre_dbdevgis_indonesiastreet", "hasgeom"));*/
                        break;
                    }
                    case 1 : {
                        logger.info("Same.....");
                        break;
                    }
                }
            }
            /*timestamp = tanggal.getTime();*/
        }
        logger.info("Hasil Akhir  :"+j);

    }

    public static AccumuloLegacyGraph getGraph () {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("master_data_jalan");
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
