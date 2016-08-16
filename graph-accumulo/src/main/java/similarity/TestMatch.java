package similarity;

import com.msk.graph.*;
import com.msk.graph.clustering.MarkovCluster;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.DataTypeIndexer;
import com.msk.graph.indexer.EdgeTypeIndexer;
import com.msk.graph.indexer.NeighborInIndex;
import com.msk.graph.indexer.NeighborOutIndex;
import com.msk.graph.memory.MemoryGraph;
import com.msk.graph.property.SimpleProperty;
import com.msk.stringutils.similarity.Levenshtein;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riska on 10/2/2015.
 */
public class TestMatch {

    public static void main (String[]args) {

        MemoryGraph graph = new MemoryGraph("testGraph");
        graph.indexes.indexers.add(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new NeighborInIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));
        long start = System.currentTimeMillis();
        long last = start;

//        MemoryStorage ms2 = (MemoryStorage) graph.indexes.storage;
//        ms2.syncmode = true;

        String [] mirip = {"desya maulana","gessa maulana","deisya khairunnisa","keysya khoirunnisa",
                "khoirunnisa","khoirunnissa","chairunnisa","hairunnisa","chairun setiawan","khairil anwar",
                "dodi setiawan","sobi setiawan","dea septiana","reza septiana","septian dwi cahyo","febrian dwi putra",
                "muhammad afriansyah","muhammad erdiansyah","muhammad arif wicaksono","muhammad ali","arif m","arif s",
                "afif dzulfikar","aip dzulkifli","dzulfikar syukur","syukur ahmad syarif","ahmad said","saidah lestari",
                "novi lestari","sofi lestari","lestari harsono","karsono hadi","dedi wijaya","winaya putri",
                "adeliya putri","delia putri","riska putri","frisca putri","frisca","frisco","rizkia n","siska m",
                "m fauzan","m faizan","firjan","irjan","Ojan Siniya","Ujang Sayana","anaya	sanaya"
        };

        TestMatch objtes = new TestMatch();
        System.out.println("creating vertices...");
        for(int i=0; i<mirip.length; i++){
            for (int j=0;j<mirip.length;j++) {
                graph.createVertex("name", mirip[i]);
                Vertex v1 = new Vertex("name", mirip[i]);
                Vertex v2 = new Vertex("name", mirip[j]);
                if ( objtes.getLevenshtein(v1, v2)!= 0.0 ){
                    Edge e = graph.createEdge(v1, v2, "miriplo");
                    e.getProperties().addProperty(new SimpleProperty("weight", String.valueOf(objtes.getLevenshtein(v1, v2))));
                    e = graph.createEdge(v2, v1, "miriplo");
                    e.getProperties().addProperty(new SimpleProperty("weight", String.valueOf(objtes.getLevenshtein(v2, v1))));
                }
            }
        }


        List<Vertex> v2 = new ArrayList<Vertex>();
        MemoryGraph graphtarget = new MemoryGraph("testSubGraph");
        for(int i=0; i<mirip.length; i++) {
            v2.add(graph.getVertex(String.valueOf(new Vertex("name",mirip[i]))));
        }
        System.out.println(graph.getIndexManager().storage);
//        Utils.subGraph(graph, graphtarget, vv, new String[]{"miriplo"});
        MarkovCluster.cluster(graph, 10, 10, "miriplo");
        List<GraphFilter> filternya = GraphFilter.filters();
        List<GraphFilter> filter = GraphFilter.filters();
        filternya.add(new GraphFilter(GraphFilter.DATA_TYPE, "edge"));
        filter.add(new GraphFilter(GraphFilter.DATA_TYPE, "vertex"));
        Vertices vv = graph.getVertices(filter);
//        Utils.subGraph(graph, graphtarget, vv, new String[]{".mcl"});
        for(Edge e : graphtarget.getEdges(filternya)) {
            System.out.println(e.getSourceId()+"=="+e.getType()+"=="+e.getTargetId());
        }

//        System.out.println(graphtarget);
        visualize("mcl2", graph, new String[]{"miriplo"});


    }

    private static void visualize(String name, Graph g, String[] relTypes){
        try {
            Files.write(new File("c:\\test\\"+name+".html").toPath(), Utils.generateViewer(g, relTypes).getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public double getLevenshtein (Vertex dicari,Vertex banding) {
        double hasillevel= 0.0;
        Levenshtein levenshtein = new Levenshtein();
            if (levenshtein.getScore(dicari.getVertexId(),banding.getVertexId()) > 0.4 ){
                hasillevel= levenshtein.getScore(dicari.getVertexId(),banding.getVertexId());
            }
        return hasillevel;
    }


}
