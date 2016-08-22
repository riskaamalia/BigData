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
public class UpdateRandom {

    private static final Logger logger = LoggerFactory.getLogger(UpdateWeekly.class);
    public static final Stopwatch timer = new Stopwatch();

    public static void main(String[] args) throws Exception {
        timer.start();
        UpdateRandom IG=new UpdateRandom();
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
        String sql = "select \"name\",\"id\",\"textfromgeom\",\"the_geom\",\"kabid\" from administrasi_desa_indonesia";
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
            String kec  = "INA"+rs.getString(2).substring(0, id.length() - 3);
            String polygon = rs.getString(3);
            String geom = rs.getString(4);
            String kab = "INA"+rs.getString(5);
            String prov = "INA"+rs.getString(2).substring(0, id.length() - 8);

            Vertex vnama, vkelid, vkab,vpolygon,vkec,vprov;

            if(name!=null) {
                vnama=graph.createVertex("desa",name.toLowerCase().replace(",","\\c")/*+"_dbdevgis"*//*+"_"+timestamp*/);
                vkelid = graph.createVertex ("location", id.replace(",","\\c")/*+"_dbdevgis"*//*+"_"+timestamp*/);
                vkec = new Vertex("location",kec.replace(",","\\c")/*+"_dbdevgis"*//*+"_"+timestamp*/);
                vkab = new Vertex("location",kab.replace(",","\\c")/*+"_dbdevgis"*//*+"_"+timestamp*/);
                vprov = new Vertex("location",prov.replace(",","\\c")/*+"_dbdevgis"*//*+"_"+timestamp*/);
//                vpolygon = graph.createVertex("geom",polygon.toLowerCase().replace(",","\\c")+"_dbdevgis"/*+"_"+timestamp*/);
//                System.out.println("========="+vnama.getId());

                switch (doing) {
                    case 0: {
                        vnama.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_admbaru", name.toLowerCase().replace(",", "\\c")));
                        vnama.getProperties().addProperty(new SimpleProperty("geom.posgre_dbdevgis_admbaru", polygon.toLowerCase().replace(",", "\\c")));

                        vkelid.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_admbaru", name.toLowerCase().replace(",","\\c")));
                        vkelid.getProperties().addProperty(new SimpleProperty("id.posgre_dbdevgis_admbaru",id.replace(",", "\\c")));
                        vkelid.getProperties().addProperty(new SimpleProperty("level.posgre_dbdevgis_admbaru","desa"));
                        vkelid.getProperties().addProperty(new SimpleProperty("geom.posgre_dbdevgis_admbaru", polygon.toLowerCase().replace(",", "\\c")));

                        logger.info(name+" == "+id);
                        /*vkec.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_administrasidesaindonesia2", kec.toLowerCase().replace(",","\\c")));
                        vkab.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_administrasidesaindonesia2", kab.toLowerCase().replace(",","\\c")));
                        vprov.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_administrasidesaindonesia2", prov.toLowerCase().replace(",","\\c")));*/
                       /* vpolygon.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_administrasidesaindonesia2", polygon.toLowerCase().replace(",","\\c")));
                        vpolygon.getProperties().addProperty(new SimpleProperty("thegeom.posgre_dbdevgis_administrasidesaindonesia2", geom.toLowerCase().replace(",","\\c")));*/
                        //kel
                        graph.createEdge(vnama,vkelid,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_dbdevgis_administrasidesaindonesia2", "haslocation"));
                        graph.createEdge(vkelid,vnama,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_dbdevgis_administrasidesaindonesia2", "locationof"));
                        /*graph.createEdge(vnama,vpolygon,"haspolygon").getProperties().
                                addProperty(new SimpleProperty("haspolygon.posgre_dbdevgis_administrasidesaindonesia2", "haspolygon"));
                        graph.createEdge(vpolygon,vnama,"polygonof").getProperties().
                                addProperty(new SimpleProperty("polygonof.posgre_dbdevgis_administrasidesaindonesia2", "polygonof"));
                        graph.createEdge(vkelid,vpolygon,"haspolygon").getProperties().
                                addProperty(new SimpleProperty("haspolygon.posgre_dbdevgis_administrasidesaindonesia2", "haspolygon"));
                        graph.createEdge(vpolygon,vkelid,"polygonof").getProperties().
                                addProperty(new SimpleProperty("polygonof.posgre_dbdevgis_administrasidesaindonesia2", "polygonof"));*/
                        //kec
                        graph.createEdge(vnama,vkec,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_dbdevgis_administrasidesaindonesia2", "haslocation"));
                        graph.createEdge(vkec,vnama,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_dbdevgis_administrasidesaindonesia2", "locationof"));
                        //vkab
                        graph.createEdge(vnama,vkab,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_dbdevgis_administrasidesaindonesia2", "haslocation"));
                        graph.createEdge(vkab,vnama,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_dbdevgis_administrasidesaindonesia2", "locationof"));
                        //prov
                        graph.createEdge(vnama,vprov,"haslocation").getProperties().
                                addProperty(new SimpleProperty("haslocation.posgre_dbdevgis_administrasidesaindonesia2", "haslocation"));
                        graph.createEdge(vprov,vnama,"locationof").getProperties().
                                addProperty(new SimpleProperty("locationof.posgre_dbdevgis_administrasidesaindonesia2", "locationof"));
                        //antar adm
                        graph.createEdge(vkelid,vkec,"haskecamatan").getProperties().
                                addProperty(new SimpleProperty("haskecamatan.posgre_dbdevgis_administrasidesaindonesia2", "haskecamatan"));
                        graph.createEdge(vkec,vkelid,"kecamatanof").getProperties().
                                addProperty(new SimpleProperty("kecamatanof.posgre_dbdevgis_administrasidesaindonesia2", "kecamatanof"));
                        graph.createEdge(vkelid,vkab,"haskabupaten").getProperties().
                                addProperty(new SimpleProperty("haskabupaten.posgre_dbdevgis_administrasidesaindonesia2", "haskabupaten"));
                        graph.createEdge(vkab,vkelid,"kabupatenof").getProperties().
                                addProperty(new SimpleProperty("kabupatenof.posgre_dbdevgis_administrasidesaindonesia2", "kabupatenof"));
                        graph.createEdge(vkelid,vprov,"hasprovinsi").getProperties().
                                addProperty(new SimpleProperty("hasprovinsi.posgre_dbdevgis_administrasidesaindonesia2", "hasprovinsi"));
                        graph.createEdge(vprov,vkelid,"provinsiof").getProperties().
                                addProperty(new SimpleProperty("provinsiof.posgre_dbdevgis_administrasidesaindonesia2", "provinsiof"));

                        graph.createEdge(vkec,vkab,"haskabupaten").getProperties().
                                addProperty(new SimpleProperty("haskabupaten.posgre_dbdevgis_administrasidesaindonesia2", "haskabupaten"));
                        graph.createEdge(vkab,vkec,"kabupatenof").getProperties().
                                addProperty(new SimpleProperty("kabupatenof.posgre_dbdevgis_administrasidesaindonesia2", "kabupatenof"));
                        graph.createEdge(vkec,vprov,"hasprovinsi").getProperties().
                                addProperty(new SimpleProperty("hasprovinsi.posgre_dbdevgis_administrasidesaindonesia2", "hasprovinsi"));
                        graph.createEdge(vprov,vkec,"provinsiof").getProperties().
                                addProperty(new SimpleProperty("provinsiof.posgre_dbdevgis_administrasidesaindonesia2", "provinsiof"));

                        graph.createEdge(vkab,vprov,"hasprovinsi").getProperties().
                                addProperty(new SimpleProperty("hasprovinsi.posgre_dbdevgis_administrasidesaindonesia2", "hasprovinsi"));
                        graph.createEdge(vprov,vkab,"provinsiof").getProperties().
                                addProperty(new SimpleProperty("provinsiof.posgre_dbdevgis_administrasidesaindonesia2", "provinsiof"));


                        break;
                    }
                }
            }
           /* timestamp = tanggal.getTime();*/
        }
        logger.info("Hasil Akhir  :"+j);
    }

    public static AccumuloLegacyGraph getGraph () {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("master_data_administrasi");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("UpdateDesa", new AccumuloLegacyStorage2(builder));
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
