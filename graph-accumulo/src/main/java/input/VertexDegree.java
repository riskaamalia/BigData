package input;

import com.msk.graph.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 1224-2 on 19-Feb-16.
 */
public class VertexDegree {
    private static final Logger logger = LoggerFactory.getLogger(VertexDegree.class);
    public Vertex vertexnya;
    public Double degreenya;
    public List<VertexDegree> allvertex = new ArrayList<>();

    public VertexDegree(){}
    // constructor
    public VertexDegree(Vertex vertexnya, Double degreenya) {
        this.vertexnya = vertexnya;
        this.degreenya = degreenya;
    }

    // getter
    public Vertex getVertex() { return vertexnya; }
    public Double getDegree() { return degreenya; }

    // setter
    public void setVertex(Vertex vertexnya) { this.vertexnya = vertexnya; }
    public void setDegree(Double degreenya) { this.degreenya = degreenya; }

    //tampung all vertex
    public void insertVertex (VertexDegree vnya) {
        allvertex.add(vnya);
    }

    //ambil degree suatu vertex tertentu
    public Double getDegreeOfVertex (Vertex vid) {
        Double degreenya=null;
        for (int i=0;i<allvertex.size();i++) {
            if (allvertex.get(i).getVertex().getVertexId().equals(vid.getVertexId())) {
                degreenya = allvertex.get(i).getDegree();
            }
        }
        return degreenya;
    }

    //sorting degree, melibatkan list allvertex
    public void sorting (int jumlahmirip) {
        List <Double> hasildegree = new ArrayList<>();
        List <Integer> tampungindex = new ArrayList<>();
        for (int i=0;i<allvertex.size();i++) {
            hasildegree.add(allvertex.get(i).getDegree());
            Collections.sort(hasildegree);
            for (int j=0;j<hasildegree.size();j++) {
                if (allvertex.get(i).getDegree() == hasildegree.get(j)) {
                    tampungindex.add(j,i);
                }
            }
        }
        /*logger.info(hasildegree.toString());
        logger.info(tampungindex.toString());*/
        int tampil=tampungindex.size()-jumlahmirip;
        if (tampungindex.size() < jumlahmirip) {
            tampil=tampungindex.size();
        }

        for (int a=tampungindex.size()-1;a>=tampil;a--) {
            logger.info(allvertex.get(tampungindex.get(a)).getVertex().getId()+"=="+allvertex.get(tampungindex.get(a)).getDegree());
        }

    }


    public static void main(String[] args) throws Exception {
        VertexDegree objtampung = new VertexDegree();
        VertexDegree obj1 = new VertexDegree(new Vertex("tes","tesida"),32.201);
        VertexDegree obj2 = new VertexDegree(new Vertex("tesaa","aaatesid"),32.201);
        VertexDegree obj3 = new VertexDegree(new Vertex("tea","aaasid"),67.101);
        Vertex tes = new Vertex("tes","tesid");
        objtampung.insertVertex(new VertexDegree(new Vertex(tes.getType(),tes.getId()),32.2001));
        objtampung.insertVertex(obj2);
        objtampung.insertVertex(obj3);
        /*System.out.println(objtampung.allvertex.get(0).getVertex().getVertexId());
        System.out.println(objtampung.allvertex.get(0).getDegree());
        System.out.println(objtampung.getDegreeOfVertex(new Vertex("tesaa", "aaatesid")));*/
        objtampung.sorting(1);
    }
}
