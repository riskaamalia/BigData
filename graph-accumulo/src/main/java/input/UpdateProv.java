package input;

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
 * Created by 1224A on 4/11/2016.
 */
public class UpdateProv {
    private static final Logger logger = LoggerFactory.getLogger(UpdateWeekly.class);
    public static final Stopwatch timer = new Stopwatch();

    public static void main(String[] args) throws Exception {
        timer.start();
        UpdateProv IG=new UpdateProv();
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
        /*java.util.Date tanggal = new java.util.Date();
        long timestamp = tanggal.getTime();
*/
        String sql = "select \"name\",\"id\",\"textfromgeom\" from administrasi_prov_indonesia";
        ResultSet rs = getResultSQL(sql);
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
            if(j++%100==0)
                System.out.println("Currently processed " + j + " lines and it took " + timer.elapsedTime(TimeUnit.SECONDS) + " seconds");
//            System.out.println("+++++");
            String name = rs.getString(1);
            String id ="INA"+rs.getString(2);
            String polygon = rs.getString(3);
            Vertex vnama,vpolygon,vprov;
//            System.out.println(polygon.replace(" ", "\\c"));
            /*for (int i=0;i< 2100000;i++) {
                System.out.print("c");
            }*/


            //========================================================================================================================================================

            if((name!=null) && (id!=null) && (polygon!=null)) {
                vnama=graph.createVertex("provinsi",name.toLowerCase().replace(",","\\c")/*+"_dbdevgis"*//*+"_"+timestamp*/);
                vprov = graph.createVertex("location",id.replace(",","\\c")/*+"_dbdevgis"*//*+"_"+timestamp*/);
                /*vpolygon = graph.createVertex("geom",polygon.toLowerCase().replace(",","\\c")+"_dbdevgis"*//*+"_"+timestamp*//*);*/
//                System.out.println("========="+vnama.getId()+"++++++++");


                switch (doing) {
                    case 0: {
                        vnama.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_admbaru", name.toLowerCase().replace(",", "\\c")));
                        vnama.getProperties().addProperty(new SimpleProperty("geom.posgre_dbdevgis_admbaru", polygon.toLowerCase().replace(",", "\\c")));
                        vprov.getProperties().addProperty(new SimpleProperty("id.posgre_dbdevgis_admbaru", id.toLowerCase().replace(",","\\c")));
                        vprov.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_admbaru", name.toLowerCase().replace(",", "\\c")));
                        vprov.getProperties().addProperty(new SimpleProperty("level.posgre_dbdevgis_admbaru", "provinsi"));
                        vprov.getProperties().addProperty(new SimpleProperty("geom.posgre_dbdevgis_admbaru", polygon.toLowerCase().replace(",", "\\c")));
                        /*vpolygon.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_administrasiprovindonesia", polygon.toLowerCase().replace(",","\\c")));*/
                        /*graph.createEdge(vnama,vpolygon,"haspolygon").getProperties().
                                addProperty(new SimpleProperty("haspolygon.posgre_dbdevgis_administrasiprovindonesia", "haspolygon"));
                        graph.createEdge(vpolygon,vnama,"polygonof").getProperties().
                                addProperty(new SimpleProperty("polygonof.posgre_dbdevgis_administrasiprovindonesia", "polygonof"));*/
                        //prov
                        graph.createEdge(vnama,vprov,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_dbdevgis_administrasiprovindonesia", "haslocation"));
                        graph.createEdge(vprov,vnama,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_dbdevgis_administrasiprovindonesia", "locationof"));
                        /*graph.createEdge(vprov,vpolygon,"haspolygon").getProperties().
                                addProperty(new SimpleProperty("haspolygon.posgre_dbdevgis_administrasiprovindonesia", "haspolygon"));
                        graph.createEdge(vpolygon,vprov,"polygonof").getProperties().
                                addProperty(new SimpleProperty("polygonof.posgre_dbdevgis_administrasiprovindonesia", "polygonof"));*/
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
        builder.setTablename("master_data_administrasi");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("UpdateProv", new AccumuloLegacyStorage2(builder));
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
