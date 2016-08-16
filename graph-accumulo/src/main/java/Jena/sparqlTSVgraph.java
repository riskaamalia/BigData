package Jena;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.indexer.*;
import com.msk.graph.memory.MemoryGraph;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by 1224A on 6/27/2016.
 */
public class sparqlTSVgraph {
    public static void main(String[] args) throws FileNotFoundException {
            /*AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
            builder.setTablename("master_data_dbpedia2");
            builder.setUserAuth("riska");
            builder.setPassword("12345678");
            AccumuloLegacyGraph graph = new AccumuloLegacyGraph("sparqlTSVgraph", new AccumuloLegacyStorage2(builder));
            graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
            graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
            graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
            graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
            graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph));
            graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));*/
        MemoryGraph graph = new MemoryGraph();
            BufferedReader br = new  BufferedReader(new FileReader("D:/dbpedia/sparql(1)"));
            try {
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] splitnya = line.split("\t");
                    if (splitnya.length == 3) {
                        sparqlTSVgraph datanya = new sparqlTSVgraph();
                        String s= datanya.cekdata(splitnya[0]);
                        String p=datanya.cekdata(splitnya[1]);
                        String o=datanya.cekdata(splitnya[2]);
                        Vertex source=null;Vertex target = null;
                        if (p.contains("type")) {
                            source = graph.createVertex(o, s);
                            source.getProperties().addProperty(new SimpleProperty("name", s));
                            } else {
                                    source = graph.createVertex(o, s);
                                    source.getProperties().addProperty(new SimpleProperty("name", s));
                                    target = graph.createVertex("id_place", p);
                                    target.getProperties().addProperty(new SimpleProperty("name", p));
                                    graph.createEdge(source, target, "hasplace").getProperties().addProperty(new SimpleProperty("dbpedia", "hasplace"));
                        }
                        System.out.println(source.getId()+"  == " + target.getId());
                    } else {
                        System.exit(0);
                    }
                }

                br.close();
            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("=============================================== ada error =============== file ke -"+i);
            }
        }

    public String cekdata (String datanya) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("master_data_dbpedia_asli");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("sparqlTSVgraph", new AccumuloLegacyStorage2(builder));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        //dia jadi s p o , return graph , yg owl biasa otomoatis masuk ke yg asli
//        if type berarti cuma 1 vertex
        String hasilnya="";
        hasilnya = datanya.trim().substring(datanya.trim().lastIndexOf("/") + 1, datanya.trim().length() - 1)
                .replace("\"", "").replace(",", "\\c").replace("_", " ").replace("[.]","\\a").toLowerCase();
        return hasilnya;
    }

}
