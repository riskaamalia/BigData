package input;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Edge;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.memory.MemoryGraph;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224-sementara on 3/17/2016.
 */
public class StreetGraph {
    static MemoryGraph superStreetGraph, subStreetGraph;
    public static MemoryGraph getSuperGraph(){
        if(superStreetGraph==null){
            superStreetGraph=new MemoryGraph("superGraph");
//            System.out.println(1);
            AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
            builder.setTablename("geoword4");
            builder.setUserAuth("riska");
            builder.setPassword("12345678");
//            System.out.println(2);
            AccumuloLegacyGraph g = new AccumuloLegacyGraph("validator", new AccumuloLegacyStorage2(builder));
           /* g.addNewIndexer(WordIndexer.class);*/
            g.addNewIndexer(new NeighborOutIndex(g.indexes.storage, g));
            g.addNewIndexer(new EdgeTypeIndexer(g.indexes.storage));
            g.addNewIndexer(new NeighborInIndex(g.indexes.storage));
            g.addNewIndexer(new DataTypeIndexer(g.indexes.storage));
            g.addNewIndexer(new VertexTypeIndex(g.indexes.storage));
//            System.out.println(3);
            List<GraphFilter> filters=new ArrayList<>();
            filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "jalan"));
            Vertices vs=g.getVertices(filters);
            int i=0;
//            System.out.println(4);
            for(Vertex v:vs){
//                System.out.println(i);
                Vertex vSuper=superStreetGraph.createVertex(v.getType(), v.getVertexId());
                vSuper.getProperties().addProperty(new SimpleProperty("name",v.getVertexId()));
                if (i++%1000==0)
                    System.out.println("sudah masuk "+i+" vertex");
            }
            i=0;
            for (Vertex v1:superStreetGraph.getVertices(null)){
                Vertices vs1=g.getVertex(v1.getType(),v1.getVertexId()).getNeighbors();
                Edge e = superStreetGraph.createEdge(v1.getId(), v1.getId(), "haveSimilarNeighbour");
                e.getProperties().addProperty(new SimpleProperty("weight", String.valueOf(1)));
                if(vs1.iterator().hasNext()) {
                    for (Vertex v2 : superStreetGraph.getVertices(null)) {
                        if (v1 != v2) {
                            Vertices vs2 = g.getVertex(v2.getType(),v2.getVertexId()).getNeighbors();
                            if(vs2.iterator().hasNext()) {
                                Vertices intersection = new Vertices(vs1, vs2, superStreetGraph);
                                intersection.setOperation = Vertices.Operation.INTERSECTION;
                                if (intersection.iterator().hasNext()) {
                                    for (Vertex v : intersection)
                                        i++;
                                    e = superStreetGraph.createEdge(v1.getId(), v2.getId(), "haveSimilarNeighbour");
                                    e.getProperties().addProperty(new SimpleProperty("weight", String.valueOf(i)));
                                }
                            }
                        }
                    }
                }
            }
//            Utils.subGraph(g,subStreetGraph,vs,null);
        }
        return superStreetGraph;
    }
    public static MemoryGraph getSubStreetGraph(){
        return null;
    }
    public static void main(String[] args){
        MemoryGraph m =getSuperGraph();
        for (Vertex v:m.getVertices(null))
            System.out.println(v);
    }
}
