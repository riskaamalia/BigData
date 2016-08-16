package similarity; /**
 * Created by 1224-2 on 18-Feb-16.
 */

import com.google.common.base.Stopwatch;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.EdgeTypeIndexer;
import com.msk.graph.indexer.JaccardIndex;
import com.msk.graph.indexer.NeighborOutIndex;
import com.msk.graph.indexer.WordIndexer;
import com.msk.graph.property.Property;
import com.msk.graph.storage.AccumuloLegacyStorage;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import com.msk.graph.storage.AccumuloStorage;
import com.msk.stringutils.Shingles;
import com.msk.stringutils.similarity.AbbrevLevenshtein;
import com.msk.stringutils.similarity.Jaccard;
import com.msk.stringutils.similarity.JaccardSubstring;
import com.msk.stringutils.similarity.Levenshtein;
import input.Neighbours;
import input.VertexDegree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Riska on 17-Feb-16.
 */

public class SimilarityTest {
    private static final Logger logger = LoggerFactory.getLogger(SimilarityTest.class);
    static final Stopwatch timer = new Stopwatch();

    public static void main(String[] args) throws Exception {
        timer.start();
        String yes = "";
        int jumlahmirip=10;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("Vertex yang dicari :");
            String input = scan.nextLine();
            String[] bagiinput = input.split(",");
            SimilarityTest objone = new SimilarityTest();
            Vertex dicari = new Vertex(bagiinput[0], bagiinput[1]);
            //logger.info(dicari.type + "==" + dicari.id);
            System.out.println("EXACT :");
            objone.getVertex(dicari, true);

            //
            System.out.println("LEVENSHTEIN :");
            objone.getLevenshtein(dicari,jumlahmirip);
            //
            System.out.println("LEVENSHTEIN+JACCARD :");
            objone.getLevenshteinJaccard(dicari,jumlahmirip);
            //
            System.out.println("SUBLEVENSHTEIN+JACCARD :");
            objone.getSubLevenshteinJaccard(dicari,jumlahmirip);
            //
            System.out.println("ABBREVLEVENSHTEIN :");
            objone.getAbb(dicari,jumlahmirip);
            System.out.print("Ambil Vertex ?");
            yes = scan.nextLine();
        }
        while (yes.equals("y"));
    }

    public List<Vertex> getVertex(Vertex finding,boolean asli) {
        AccumuloStorage.Builder builder = AccumuloStorage.Builder.RajaampatBuilder();
        builder.setTablename("geoword4");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        //logger.debug("Builder options: {}", builder);
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("similarity", new AccumuloLegacyStorage(builder));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        //
        List<GraphFilter> filters = new ArrayList<GraphFilter>();
        List<Vertex> alamat = new ArrayList<>();
        //filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, finding.type));
        if (asli == true) {
            Vertex exact = graph.getVertex(finding.getId());
            alamat.add(exact);
            logger.info(alamat.toString());
        }
        else if (asli == false) {
            filters.addAll(JaccardIndex.getLevenshteinSubstringFilter("name", finding.getVertexId(), 0.8));
            //filters.add(new GraphFilter(JaccardIndex.FILTER_KEY, finding.id,0.8 ));
            //logger.info("fff"+filters.toString());
            Vertices vv = graph.getVertices(filters);
            Property p = null;
            int count = 0;
            for (Vertex v : vv) {
                //logger.info("====="+v.toString());
                Vertex hasil = new Vertex(v.getType(), v.getVertexId().replace("\\c", ","));
                alamat.add(hasil);
                //logger.info(alamat.get(count));
                count++;
            }
        }
        return alamat;
    }

    public void getLevenshtein (Vertex dicari,int jumlahmirip) {
        SimilarityTest objasli = new SimilarityTest();
        List<Vertex> vmirip = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari, false));

        //
        Levenshtein levenshtein = new Levenshtein();
        VertexDegree objtampung = new VertexDegree();
        Neighbours objsimilar = new Neighbours();

        for (Vertex vb:vmirip) {
//            System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
            if (levenshtein.getScore(dicari.getVertexId(),vb.getVertexId()) > 0.8 ){
                //System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
                //
                objtampung.insertVertex(new VertexDegree(new Vertex(vb.getType(), vb.getVertexId()), levenshtein.getScore(dicari.getVertexId(), vb.getVertexId())));

                Neighbours obj = new Neighbours();
                AccumuloLegacyStorage2.Builder builder2 = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
                builder2.setTablename("geoword4");
                builder2.setUserAuth("riska");
                builder2.setPassword("12345678");
                AccumuloLegacyGraph g = new AccumuloLegacyGraph("validator", new AccumuloLegacyStorage2(builder2));
                g.addNewIndexer(new WordIndexer(g.indexes.storage, g));
                g.addNewIndexer(new NeighborOutIndex(g.indexes.storage, g));
                g.addNewIndexer(new EdgeTypeIndexer(g.indexes.storage));
                Vertices tes = obj.getNeighbours(vb.getId(),g);
                for (Vertex ff:tes) {
                    System.out.println("===" + ff);
                }
            }
        }
        objtampung.sorting(jumlahmirip);
    }

    public void getLevenshteinJaccard (Vertex dicari,int jumlahmirip) {
        SimilarityTest objasli = new SimilarityTest();
        List<Vertex> vmirip = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari, false));

        //
        Levenshtein levenshtein = new Levenshtein();
        Jaccard j = new Jaccard(Shingles.Encoding.ALPHANUMERIC_LOWERCASE, 2);
        VertexDegree objtampung = new VertexDegree();

        for (Vertex vb:vmirip) {
//            System.out.println(dicari.id+"=="+vb.id+"=="+(levenshtein.getScore(dicari.id,vb.id)+j.getScore(dicari.id,vb.id))/2);
            if ((levenshtein.getScore(dicari.getVertexId(),vb.getVertexId())+j.getScore(dicari.getVertexId(),vb.getVertexId()))/2 > 0.8 ){
                //System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
                //
                objtampung.insertVertex(new VertexDegree(new Vertex(vb.getType(), vb.getVertexId()), (levenshtein.getScore(dicari.getVertexId(),vb.getVertexId())+j.getScore(dicari.getVertexId(),vb.getVertexId()))/2));
                //bikin library, setiap vertex yang terkait dan dikaitkan dengan exact
            }
        }
        objtampung.sorting(jumlahmirip);

    }

    public void getSubLevenshteinJaccard (Vertex dicari,int jumlahmirip) {
        SimilarityTest objasli = new SimilarityTest();
        List<Vertex> vmirip = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari, false));

        //
        Levenshtein levenshtein = new Levenshtein();
        JaccardSubstring j = new JaccardSubstring(Shingles.Encoding.ALPHANUMERIC_LOWERCASE, 2);
        VertexDegree objtampung = new VertexDegree();

        for (Vertex vb:vmirip) {
//            System.out.println(dicari.id+"=="+vb.id+"=="+(levenshtein.getScore(dicari.id,vb.id)+j.getScore(dicari.id,vb.id))/2);
            if ((levenshtein.getScore(dicari.getVertexId(),vb.getVertexId())+j.getScore(dicari.getVertexId(),vb.getVertexId()))/5 > 0.9 ){
                //System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
                //
                objtampung.insertVertex(new VertexDegree(new Vertex(vb.getType(), vb.getVertexId()), (levenshtein.getScore(dicari.getVertexId(), vb.getVertexId()) + j.getScore(dicari.getVertexId(), vb.getVertexId())) / 2));
            }
        }
        objtampung.sorting(jumlahmirip);

    }

    public void getAbb (Vertex dicari,int jumlahmirip) {
        SimilarityTest objasli = new SimilarityTest();
        List<Vertex> vmirip = new ArrayList<>();
        List<Vertex> vhasil = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari, false));

        //
        AbbrevLevenshtein levenshtein = new AbbrevLevenshtein();
        VertexDegree objtampung = new VertexDegree();
        SimilarityTest objsimilar = new SimilarityTest();

        for (Vertex vb:vmirip) {
//            System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
            if (levenshtein.getScore(dicari.getVertexId(),vb.getVertexId()) > 0.9 ){
                //System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
                //
                objtampung.insertVertex(new VertexDegree(new Vertex(vb.getType(), vb.getVertexId()), levenshtein.getScore(dicari.getVertexId(), vb.getVertexId())));
                objsimilar.isSimilar(dicari,vb);
            }
        }
        objtampung.sorting(jumlahmirip);
    }

    public boolean isSimilar (Vertex vexact,Vertex vbanding) {
        boolean hasil=false;

        Neighbours obj = new Neighbours();
        AccumuloLegacyStorage2.Builder builder2 = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder2.setTablename("geoword4");
        builder2.setUserAuth("riska");
        builder2.setPassword("12345678");
        AccumuloLegacyGraph g = new AccumuloLegacyGraph("validator", new AccumuloLegacyStorage2(builder2));
        g.addNewIndexer(new WordIndexer(g.indexes.storage, g));
        g.addNewIndexer(new NeighborOutIndex(g.indexes.storage, g));
        g.addNewIndexer(new EdgeTypeIndexer(g.indexes.storage));
        Vertices tes = obj.getNeighbours(vexact.getId(),g);
        Vertices tes2 = obj.getNeighbours(vbanding.getId(), g);
        for (Vertex ff:tes) {
            System.out.println("===111111" + ff);
            for (Vertex ff2:tes2) {
                System.out.println("===" + ff2);
            }
        }

        return hasil;
    }

}
