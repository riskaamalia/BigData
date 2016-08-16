package Jena;

import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Graph;
import com.msk.graph.Vertex;
import com.msk.graph.indexer.*;
import com.msk.graph.memory.MemoryGraph;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by 1224A on 6/2/2016.
 */
public class RDFInput extends RuleTypeRDF {
    private static Logger logger = LoggerFactory.getLogger(RDFInput.class);

public void openfile (String path,String splitnya) throws IOException {
    RDFInput objof = new RDFInput();
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        String line = br.readLine();
        int baris=1;
        while (line != null) {
            String [] hasil = br.readLine().split(splitnya);
            Graph graph = getGraph("riskaRDF");
            Vertex source = objof.insertVertex(hasil[0],graph,"");
            Vertex target = objof.insertVertex(hasil[2],graph,"");
            graph.createEdge(source,target,"haspredikat").getProperties().
                    addProperty(new SimpleProperty("predikat.hasilRDF",hasil[1].replace("<","").replace(">","").replace(" .","").trim().toLowerCase()));
            graph.createEdge(target,source,"predikatof").getProperties().
                    addProperty(new SimpleProperty("haspredikat.hasilRDF", hasil[1].replace("<","").replace(">","").replace(" .","").trim().toLowerCase()));
            logger.info(br.readLine()+"=="+baris);
            baris++;
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        br.close();
    }

}

    public Vertex insertVertex (String id,Graph thegraph,String subjek) throws FileNotFoundException {
        String cleanid = id.replace("<","").replace(">","").replace(" .","").trim().toLowerCase();
        String [] getall = cleanid.split("/");
        String cleantipe = subjek.replace("<","").replace(">","").replace(" .","").trim().toLowerCase();
        String [] gettipe = cleantipe.split("/");
        /*Vertex source = thegraph.createVertex(getall[getall.length-2],cleanid);
        source.getProperties().addProperty(new SimpleProperty("name.hasilRDF",getall[getall.length-1] ));*/
        String tipenya = moveToDomainType(gettipe[gettipe.length-1]);
        if (tipenya.equals(""))
            tipenya = gettipe[gettipe.length-1];
        Vertex source = thegraph.createVertex(tipenya,getall[getall.length-1]);
        source.getProperties().addProperty(new SimpleProperty("name.hasilRDF","1" ));

        return source;
    }

    public void getTypeRDF (String path,String splitnya) throws IOException {
        RDFInput objof = new RDFInput();
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            String line = br.readLine();
            int baris=1;
            while (line != null) {
                String [] hasil = br.readLine().split(splitnya);
                Graph graph = getGraph("tesRDF");
                Vertex ambiltipe = objof.insertVertex(hasil[2],graph,hasil[0]);
                logger.info(ambiltipe.getType()+"=="+baris);
                baris++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }

    }

    public boolean isVertexExist (Vertex thevertex,Graph thegraph) {
        Boolean thetrue=false;
        //use multi storage for access more than one table

        //find by type and vertex id

        //find by vertex id, search in vertex.word

        //find all by scan all type

        return thetrue;
    }

    public static AccumuloLegacyGraph getGraph (String table) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(table);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("RDFInput", new AccumuloLegacyStorage2(builder));
        /*graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
*/
        return graph;
    }

    public static MemoryGraph getGraphMemory (String graphname) {
        MemoryGraph graph = new MemoryGraph(graphname);
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));

        return graph;
    }

    public static void main(String[] args) throws Exception {
        RDFInput obj = new RDFInput();
        obj.getTypeRDF("D:/mediatrac/DATA/DBPedia/instance_types_small.nt","> ");
    }
}
