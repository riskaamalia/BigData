package BackEndSearch;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Graph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import input.Automatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224A on 5/25/2016.
 */
public class mainApp {
    private static final Logger logger = LoggerFactory.getLogger(Automatic.class);

    public static void main(String[] args) throws Exception {
//        System.out.println(theGraph("mampang prapatan","master_data_jalan"));
        String tes="apaaa";
        String.format("apa %s coba",tes);
     /*   AccumuloLegacyGraph thegraph=null;
        Vertex vs = null;

        thegraph = getGraph("geoword4");
        List<GraphFilter> filters=new ArrayList<>();
        System.out.println(thegraph.getVertex("phrase","mampang prapatan"));*/
    }

    //pisahkan vertex type dengan ->
    public static Vertex theGraph (String query,String table) {
        AccumuloLegacyGraph thegraph=null;
        Vertex vs = null;

        thegraph = getGraph(table);
        List<GraphFilter> filters=new ArrayList<>();
        if ( query.split("->").length < 2 ) {
            if ( query.trim().contains(" ") == true) {
                vs = thegraph.getVertex("phrase",query);
                if ( vs == null )
                    vs = thegraph.getVertex("word",query);
            } else
                vs = thegraph.getVertex("word",query);
        } else
            vs =thegraph.getVertex(query.split("->")[0]+"."+query.split("->")[1]);

        return vs;
    }

    public static void wordize (String table) {
        AccumuloLegacyGraph graphnya = getGraph(table);
        List<GraphFilter> filters = new ArrayList<GraphFilter>();
        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "jalan"));
//        Vertex coba = graphnya.getVertex("kecamatan","jatinangor");
//        logger.info(coba.getVertexId()+" ====");
        Vertices semua = graphnya.getVertices(filters);
        for (Vertex vnya:semua) {
            logger.info(vnya.getVertexId()+" === ");
        }
    }

    public static List<Vertex> findType (Vertex theword) {
        List<Vertex> vtipenya= new ArrayList<>();
        Graph inig = getGraph("geoword4");
        String [] reltype = {"address","blok","gedung","jalan","kabupaten","kecamatan",
                "kodepos","located_in","nomor","poi","provinsi", "rtrw","location","kota",
                "has_address","has_idkelurahan","has_kabupaten","has_kecamatan","has_latitude",
                "has_longitude","has_latitude","has_poi","hasaddress","hasblok","hasidkelurahan",
                "haskabupaten","haskecamatan","haslatitude","haslongitude","haspoi","hasnomor",
                "hascouple","hasgedung","hasjalan","haskodepos","haskomplek","haskota","hasprovinsi","hasrtrw"};
        Vertices tetangga = null;
        Vertices tetangga2 = null;
          Vertex isian = inig.getVertex(theword.getId());
        for (String tipenya:reltype) {
            tetangga = isian.getNeighbors(tipenya);
            for (Vertex tip:tetangga) {
                if (tip.getVertexId().equals(theword.getVertexId()))
                    vtipenya.add(tip);
            }
        }

        //bikin rules untuk setiap tipe , indikasikan itu tipe yg benar atau salah
        return vtipenya;
    }

    public static AccumuloLegacyGraph getGraph (String table) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(table);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("mainApp", new AccumuloLegacyStorage2(builder));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        return graph;
    }

    public static void hasil1() throws IOException {
        BufferedReader TSVFile = new BufferedReader(new FileReader("D:/RuleBaseResultWithUnknown2.tsv"));
        String dataRow;
        Vertex hasil= null;
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("D:/coba.tsv", true)));
        List<Vertex> finalnya = new ArrayList<>();
        int count=0;
        do {
            dataRow = TSVFile.readLine();
            String [] dataArray = dataRow.split("\t");
            if ( dataArray[3].replace("[","").replace("]","").split(";").length == 1 ) {
                hasil = theGraph(dataArray[3].replace("[","").replace("]","").replace(";",""),"geoword4");
                try {
//                    System.out.println("Hasil = "+hasil.getId()+ " dari -> "+ dataArray[3].replace("[","").replace("]","").replace(";",""));
                    finalnya = findType(hasil);
                    if (finalnya != null) {
                        out.print(dataArray[3]+"\t"+hasil.getId());
                        out.flush();
                        for (Vertex a : finalnya) {
                            out.print("\t" + a.getId());
                            out.flush();
                        }
                        out.println();
                    } else {
                        out.print(dataArray[3] + "\t" + hasil.getId() + "\n");
                        out.flush();
                    }
                } catch (NullPointerException pp) {
//                    System.out.println("ga ada dari -> "+ dataArray[3].replace("[","").replace("]","").replace(";",""));
                    out.print(dataArray[3]+"\t\n");
                    out.flush();
                }
            } else {
                String [] unkown2 = dataArray[3].replace("[","").replace("]","").trim().toLowerCase().split(";");
                out.print(dataArray[3]);
                out.flush();
                for (String hasill: unkown2) {
//                    System.out.println("ini hasil"+hasill);
                    hasil = theGraph(hasill.trim().toLowerCase(),"geoword4");
                    try {
//                        System.out.println("Hasil = "+hasil.getId()+ " dari -> "+ hasill);
                        finalnya = findType(hasil);
                        if (finalnya != null) {
                            out.print("\t"+hasil.getId());
                            out.flush();
                            for (Vertex a : finalnya) {
                                out.print("\t" + a.getId());
                                out.flush();
                            }
                        } else {
                            out.print("\t"+hasil.getId());
                            out.flush();
                        }
                    } catch (NullPointerException poo) {
//                        System.out.println("2. ga ada dari ->"+ hasill);
                    }
                }
                out.println();
                out.flush();
            }
            count++;
            logger.info("==Loop "+count);
        }
        while (dataRow != null);

        TSVFile.close();
        out.close();
        hasil = theGraph("sudirman","geoword4");
        System.out.println(hasil.getId());
    }

}
