package ontology;

/**
 * Created by 1224A on 6/23/2016.
 */
import com.msk.graph.*;
import com.msk.graph.property.SimpleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class createTreeOWL {
    private static final Logger logger = LoggerFactory.getLogger(createTreeOWL.class);

    public static void visualize(String pathFile, byte[] visualByte){
        try {
            Files.write(new File(pathFile).toPath(), visualByte);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void createTree(Graph g, BufferedReader br, String node, String vertexType, String relType,
                                  int levelNode, int levelNow, Vertex sibling){
        if (node != null) {
            Vertex kelas=g.createVertex(vertexType.toLowerCase(),node.toLowerCase());
            kelas.addProperty(new SimpleProperty("name",node.toLowerCase()));
            if(levelNode==levelNow){
                if(sibling!=null) {
                    Vertices ascendants = sibling.in(relType.toLowerCase());
                    if (ascendants.iterator().hasNext())
                        for (Vertex v : ascendants) {
                            Edge e2=v.connectTo(kelas, "descendant", null);
                            e2.addProperty(new SimpleProperty("name","descendant"));
                        }
                }
                logger.trace("\""+node.toLowerCase()+"\" have processed");
                logger.info("\""+node.toLowerCase()+"\" have processed");

            }
            else if(levelNode>levelNow){
                Edge e2=sibling.connectTo(kelas,relType.toLowerCase(),null);
                e2.addProperty(new SimpleProperty("name",relType.toLowerCase()));
                levelNow=levelNode;
                logger.trace("\""+node.toLowerCase()+"\" have processed");
                logger.info("\""+node.toLowerCase()+"\" have processed");
            }
            else {
                levelNow--;
                for(Vertex v:sibling.in(relType)){
                    createTree(g,br,node,vertexType.toLowerCase(),relType.toLowerCase(),levelNode,levelNow,v);
                }
            }
        }

        try {
            String line=br.readLine();
            if(line!=null) {
                line=line.trim().toLowerCase();
                String vertexID=line.substring(line.indexOf(":")+1).toLowerCase();
                int level = line.replace("==|", "\n").lastIndexOf("\n") + 1;
                if (node == null)
                    createTree(g, br, vertexID.toLowerCase(), vertexType.toLowerCase(),relType.toLowerCase(),levelNode, levelNow, null);
                else {
                    createTree(g, br, vertexID.toLowerCase(), vertexType.toLowerCase(),relType.toLowerCase(),level, levelNow, g.getVertex(vertexType.toLowerCase(), node.toLowerCase()));
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static byte[] createTree(Graph g, BufferedReader fileReader, String vertexType,String relType){
        createTree(g,fileReader,null,vertexType.toLowerCase(),relType.toLowerCase(),0,0,null);
        logger.info("creating tree is almost done");
        logger.trace("creating tree is almost done");
        return Utils.generateViewer(g, new String[]{relType.toLowerCase()}, "name").getBytes();
    }
}

