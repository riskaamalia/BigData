package input;

import com.msk.graph.*;
import com.msk.graph.clustering.MarkovCluster;
import com.msk.graph.filters.GraphFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224-2 on 01-Mar-16.
 */
public class StreetClustering {
//    private static Logger logger = LoggerFactory.getLogger(com.msk.graph.Test.StreetClustering.class);

    public static List<Graph> clusterStreet (BaseGraph graph, int iter, int inflate, List<GraphFilter> filter) {
        List<Graph> listGraph = new ArrayList<>();
        BaseGraph graphtarget = new BaseGraph();
        int in=0;
        /*Vertices vv = graph.getVertices(filter);
        for(Vertex v: vv){
            System.out.println(v.key());
        }*/

        for(Edge e: graph.getEdges(filter)) {
//            System.out.println(e.getSourceId()+"=="+e.getType()+"=="+e.getTargetId());
           MarkovCluster.cluster(graph, iter, inflate, e.getType()) ;
            Vertices vv = graph.getVertices(filter);
            listGraph.add(Utils.subGraph(graph, graphtarget, vv, new String[]{e.getType() + ".mcl"}));
            visualize("ninggar" + in, listGraph.get(in), new String[]{".mcl"});
            in++;
        }

        return listGraph;
    }

    private static void visualize(String name, Graph g, String[] relTypes){
        try {
            Files.write(new File("c:\\test\\"+name+".html").toPath(), Utils.generateViewer(g, relTypes).getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
