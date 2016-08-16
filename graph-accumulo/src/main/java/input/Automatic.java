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
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Riska on 15/03/2016.
 */
public class Automatic {

    private static final Logger logger = LoggerFactory.getLogger(Automatic.class);
    public static final Stopwatch timer = Stopwatch.createStarted();

    public static void main(String[] args) throws Exception {
        //pilih input pake apa switch case
        System.out.println("Auto Input Program");
        System.out.println("Pilih Sumber Data :\n 1.PostgreSQL");
        int data=1;
        String sumberdb=null;
        ArrayList <String> headerlist = new ArrayList<>();
        switch (data) {
            //tampilkan header kolom
            case 1 : {
                String db = "db_devgis";
                String username = "gilang";
                String password = "Sementara3";
                String table = "Indonesia_street";
                System.out.print("Tabel Postgre : ");Scanner tabelpostgre = new Scanner(System.in);table = tabelpostgre.nextLine();
                String sqlheader = "select \"column_name\" from information_schema.columns where table_name =\'"+table+"\'";
                sumberdb = "postgre_"+db.toLowerCase().replace(",","\\c").trim()+"_"+table.replace(",","\\c").trim();
                ResultSet rsheader = getResultSQL("jdbc:postgresql://192.168.10.11:5432/"+db,username,password,sqlheader);
                int ihead=1,num=0;
                while (rsheader.next()) {
                    headerlist.add(rsheader.getString(ihead));
                    System.out.println(num + ".) " + rsheader.getString(ihead) + "===" + headerlist.get(num));
                    num++;
                }

                String sqlheader2 = "select * from \""+table+"\""/*"select * from "+table*/;
                ResultSet rsreal = getResultSQL("jdbc:postgresql://192.168.10.11:5432/"+db,username,password,sqlheader2);
                Vertex vertexpilihan = null; Vertex vertexgenap = null;
                System.out.print("Tabel Accu : ");Scanner tabelaccu = new Scanner(System.in);String tabelmulo = tabelaccu.nextLine();
                System.out.print("Relasi Vertex : (2-1 atau 1 gunakan , antar relasi )");
                Scanner inputheader = new Scanner(System.in);String inputawal = inputheader.nextLine();
                ArrayList <String> vertexlist = new ArrayList<>();

                //buat list

                if (( inputawal.contains(",") == false ) && ( inputawal.contains("-") == false )) {
                    //ini yang sendirian
                    vertexlist.add(headerlist.get(Integer.parseInt(inputawal)));
                } else if ((inputawal.contains(",") == false) && (inputawal.contains("-") == true)) {
                    //ini yang sendirian ada relasi
                    String [] relasi = inputawal.split("-");

                    for (String looprelasis:relasi) {
                        vertexlist.add(headerlist.get(Integer.parseInt(looprelasis.trim())));
                    }
                }
                else {
                    //ini yg lebih dari 1
                    String [] relasi = inputawal.split(",");

                    for (String looprelasi:relasi) {
                        if (looprelasi.contains("-") == false) {
                            //lebih dr 1 ga ada relasi
                            vertexlist.add(headerlist.get(Integer.parseInt(looprelasi.trim())));
                            }
                            else if (( inputawal.contains(",") == false ) && ( inputawal.contains("-") == true )) {
                            //lebih dari 1 ga ada relasi
                                String[] hasiledge2 = inputawal.split("-");
                                for (String loophasiledge : hasiledge2 ) {
                                    vertexlist.add(headerlist.get(Integer.parseInt(loophasiledge.trim())));
                                }
                             }
                                else {
                                    //ini yang buat ada - , buat bikin edge
                                    String [] hasiledge = looprelasi.split("-");
                                    for (String loophasiledge : hasiledge ) {
                                        vertexlist.add(headerlist.get(Integer.parseInt(loophasiledge.trim())));
                                    }

                        }
                    }
                }

                System.out.print("Relasi "+vertexlist+" : ");Scanner edgenya = new Scanner(System.in);String sedgenya = edgenya.nextLine();
                System.out.print("Sisa Prop taro di (nama kolomnya): ");Scanner sisapropnya = new Scanner(System.in);String svsisa = sisapropnya.nextLine();
                AccumuloLegacyGraph graphVertex = getGraph(tabelmulo);

                int j=0;
                try {
                    while (rsreal.next()) {
                        j++;
                        if(j++%100==0)
                            System.out.println("Currently processed " + j + " lines and it took " + timer.elapsed(TimeUnit.SECONDS) + " seconds");
//                        System.out.println("Header list di awal rs "+headerlist);
                        if ((inputawal.contains(",") == false) && (inputawal.contains("-") == false)) {
                            vertexpilihan = graphVertex.createVertex(headerlist.get(Integer.parseInt(inputawal.trim())).toLowerCase().replace(",", "\\c").trim(), rsreal.getString(Integer.parseInt(inputawal.trim()) + 1).toLowerCase().replace(",", "\\c").trim());
                            vertexpilihan.getProperties().addProperty(new SimpleProperty("name." + sumberdb, rsreal.getString(Integer.parseInt(inputawal) + 1).toLowerCase().replace(",", "\\c").trim()));
//                            logger.info("INi tipeee "+vertexpilihan.getType());
                        } else if ((inputawal.contains(",") == false) && (inputawal.contains("-") == true)) {
                            String[] hasiledge = inputawal.split("-");
                            int loopedge = 0;
                            for (String loophasiledge : hasiledge) {
//                                System.out.println("loop hasiledge " + loophasiledge);
                                if (loopedge % 2 == 0) {
                                    vertexpilihan = graphVertex.createVertex(headerlist.get(Integer.parseInt(loophasiledge.trim())).toLowerCase().replace(",", "\\c").trim(), rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim());
                                    vertexpilihan.getProperties().addProperty(new SimpleProperty("name." + sumberdb, rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim()));
                                } else {
                                    vertexgenap = graphVertex.createVertex(headerlist.get(Integer.parseInt(loophasiledge.trim())).toLowerCase().replace(",", "\\c").trim(), rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim());
                                    vertexgenap.getProperties().addProperty(new SimpleProperty("name." + sumberdb, rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim()));
//                                System.out.print("Relasi "+vertexpilihan.getId()+" dan "+vertexgenap.getId()+" : ");Scanner edgenya = new Scanner(System.in);String sedgenya = edgenya.nextLine();
                                    //edge relasi nya pake ganjil genap, yang awal yang genap tapi loop dari angka 1 terus -
                                    graphVertex.createEdge(vertexpilihan, vertexgenap, sedgenya).getProperties().
                                            addProperty(new SimpleProperty(sedgenya + "." + sumberdb, sedgenya));
                                    graphVertex.createEdge(vertexgenap, vertexpilihan, sedgenya + "of").getProperties().
                                            addProperty(new SimpleProperty(sedgenya + "of." + sumberdb, sedgenya + "of"));
                                }

                                loopedge++;
                            }

                        } else {
                            String[] relasi = inputawal.split(",");

                            for (String looprelasi : relasi) {
                                if (looprelasi.contains("-") == false) {
//                                    System.out.println("loop relasi " + looprelasi);
                                    if (rsreal.getString(Integer.parseInt(looprelasi)) != null) {
                                        vertexpilihan = graphVertex.createVertex(headerlist.get(Integer.parseInt(looprelasi)).toLowerCase().replace(",", "\\c").trim(), rsreal.getString(Integer.parseInt(looprelasi.trim()) + 1).toLowerCase().replace(",", "\\c").trim());
                                        vertexpilihan.getProperties().addProperty(new SimpleProperty("name." + sumberdb, rsreal.getString(Integer.parseInt(looprelasi.trim()) + 1).toLowerCase().replace(",", "\\c").trim()));
                                    }
                                } else {
                                    //ini yang buat ada - , buat bikin edge
                                    String[] hasiledge = looprelasi.split("-");
                                    int loopedge = 0;
                                    for (String loophasiledge : hasiledge) {
//                                        System.out.println("loop hasiledge " + loophasiledge);
                                        if (loopedge % 2 == 0) {
                                            vertexpilihan = graphVertex.createVertex(headerlist.get(Integer.parseInt(loophasiledge.trim())).toLowerCase().replace(",", "\\c").trim(), rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim());
                                            vertexpilihan.getProperties().addProperty(new SimpleProperty("name." + sumberdb, rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim()));
                                        } else {
                                            vertexgenap = graphVertex.createVertex(headerlist.get(Integer.parseInt(loophasiledge.trim())).toLowerCase().replace(",", "\\c").trim(), rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim());
                                            vertexgenap.getProperties().addProperty(new SimpleProperty("name." + sumberdb, rsreal.getString(Integer.parseInt(loophasiledge.trim()) + 1).toLowerCase().replace(",", "\\c").trim()));
//                                        System.out.print("Relasi "+vertexpilihan.getId()+" dan "+vertexgenap.getId()+" : ");Scanner edgenya = new Scanner(System.in);String sedgenya = edgenya.nextLine();
                                            //edge relasi nya pake ganjil genap, yang awal yang genap tapi loop dari angka 1 terus -
                                            graphVertex.createEdge(vertexpilihan, vertexgenap, sedgenya).getProperties().
                                                    addProperty(new SimpleProperty(sedgenya + "." + sumberdb, sedgenya));
                                            graphVertex.createEdge(vertexgenap, vertexpilihan, sedgenya + "of").getProperties().
                                                    addProperty(new SimpleProperty(sedgenya + "of." + sumberdb, sedgenya + "of"));
                                        }

                                        loopedge++;
                                    }

                                }
                            }
                        }

                        //ini buat yang kolom ga kepake, nanya apakah jadi properties di mana gitu, dan juga buat properti tambahan
                        //headerlist - vertexlist
//                        System.out.println("Header list di tengah" + headerlist);
                        ArrayList <String> proplist = new ArrayList<>();
                        proplist.addAll(headerlist);
                        proplist.removeAll(vertexlist);
//                        System.out.println("sisanya " + proplist);

                        vertexpilihan = graphVertex.createVertex (svsisa.toLowerCase().replace(",", "\\c").trim(), rsreal.getString(svsisa).toLowerCase().replace(",", "\\c").trim());
                        int count=0;
                            for (count=0;count<proplist.size();count++) {
//                                System.out.println("label " + proplist.get(count) + " isi " + rsreal.getString(proplist.get(count)));
//                                logger.info(rsreal.getString(svsisa));
                                try {
                                    vertexpilihan.getProperties().addProperty(new SimpleProperty(proplist.get(count).toLowerCase().replace(",", "\\c").trim() + "." + sumberdb, rsreal.getString(proplist.get(count)).toLowerCase().replace(",", "\\c").trim()));
                                }
                                catch (NullPointerException tes) {
                                    tes.getMessage();
                                }
                            }
//                        System.out.println("Header list di ujung" + headerlist);


                    }
                }
                catch (NullPointerException kosong) {
                    logger.info("ada error");
                }

                break;
            }

        }



    }

    public static ResultSet getResultSQL(String dbUrl,String username,String password,String sql) {
        Connection conn = connectSql(dbUrl, username, password);
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


    public static AccumuloLegacyGraph getGraph (String table) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(table);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("Automatic", new AccumuloLegacyStorage2(builder));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
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

