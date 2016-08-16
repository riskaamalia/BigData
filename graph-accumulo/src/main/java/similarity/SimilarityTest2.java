package similarity; /**
 * Created by 1224-2 on 18-Feb-16.
 */

import com.google.common.base.Stopwatch;
import com.msk.graph.*;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.memory.JaccardIndexTest;
import com.msk.graph.property.Property;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage;
import com.msk.graph.storage.AccumuloLegacyStorage2;
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

/**
 * Created by Riska on 17-Feb-16.
 */

public class SimilarityTest2 {
    private static final Logger logger = LoggerFactory.getLogger(SimilarityTest2.class);
    static final Stopwatch timer = new Stopwatch();

    public static void main(String[] args) throws Exception {
        String satu="SimilarityTest",dua="master_data_jalan";
        AccumuloLegacyGraph graph = getGraph(satu);
        AccumuloLegacyGraph graphinput = getGraph(dua);
        int jumlahmirip=100000;
        List <Vertex> hasil = new ArrayList<>();
        List<GraphFilter> filters = new ArrayList<GraphFilter>();
        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, null));
        Vertices vvinput = graphinput.getVertices(filters);
        Vertex tes;
        for (Vertex v : vvinput) {
            SimilarityTest2 objone = new SimilarityTest2();
            // ini vertex inputannya
//            logger.info(v.getId());
            Vertex dicari = new Vertex(v.getType(), v.getVertexId());
            logger.info(dicari.getType() + "==" + dicari.getVertexId());
            System.out.println("EXACT :");
            String tipe = objone.getVertex(dicari, true,dua).get(0).getVertexId().trim().replace("[.]", "\\c");
            tipe = tipe.replace("jalan","").replace("jl", "").replace("jln", "");
            SimpleProperty proper = new SimpleProperty("name",tipe);
            tes = graph.createVertex(objone.getVertex(dicari,true,dua).get(0).getType(),"exact_"+tipe);
            tes.getProperties().addProperty(proper);
            /*if (tes != null)
                dicari = tes;*/

            //
            System.out.println("LEVENSHTEIN :");
            hasil.addAll(objone.getLevenshtein(dicari, jumlahmirip,dua));

            for (Vertex aa:hasil) {
                try {
                    SimpleProperty proper2 = new SimpleProperty("name",aa.getId().trim().replace("[.]", "\\c"));
                    Vertex tes2 = graph.createVertex(aa.getType(),"levenshtein_"+aa.getVertexId().trim().replace("[.]", "\\c"));
                    tes2.getProperties().addProperty(proper2);
                    SimpleProperty edgeprop = new SimpleProperty("name","edge_levenshtein");
                    Edge edgenya = graph.createEdge(tes,tes2,"edge_levenshtein");
                    edgenya.getProperties().addProperty(edgeprop);
                    logger.info("leven" + aa.getType() + "==" + aa.getVertexId());
                }
                catch (ArrayIndexOutOfBoundsException tr) {
                    tr.printStackTrace();
                }

            }
            hasil.clear();
            //
            System.out.println("LEVENSHTEIN+JACCARD :");
            hasil.addAll(objone.getLevenshteinJaccard(dicari, jumlahmirip,dua));
            for (Vertex aa:hasil) {
                try {
                    SimpleProperty proper2 = new SimpleProperty("name",aa.getId().trim().replace("[.]", "\\c"));
                    Vertex tes2 = graph.createVertex(aa.getType(),"levenjaccard_"+aa.getVertexId().trim().replace("[.]", "\\c"));
                    tes2.getProperties().addProperty(proper2);
                    SimpleProperty edgeprop = new SimpleProperty("name","edge_levenjaccard");
                    Edge edgenya = graph.createEdge(tes,tes2,"edge_levenjaccard");
                    edgenya.getProperties().addProperty(edgeprop);
                    logger.info("levenjaccard" + aa.getType() + "==" + aa.getVertexId());
                }
                catch (ArrayIndexOutOfBoundsException tr) {
                    tr.printStackTrace();
                }

            }
            hasil.clear();
            //
            System.out.println("SUBLEVENSHTEIN+JACCARD :");
            hasil.addAll(objone.getSubLevenshteinJaccard(dicari, jumlahmirip,dua));
            for (Vertex aa:hasil) {
                try {
                    SimpleProperty proper2 = new SimpleProperty("name",aa.getId().trim().replace("[.]", "\\c"));
                    Vertex tes2 = graph.createVertex(aa.getType(),"sublevenjaccard_"+aa.getVertexId().trim().replace("[.]", "\\c"));
                    tes2.getProperties().addProperty(proper2);
                    SimpleProperty edgeprop = new SimpleProperty("name","edge_sublevenjaccard");
                    Edge edgenya = graph.createEdge(tes,tes2,"edge_sublevenjaccard");
                    edgenya.getProperties().addProperty(edgeprop);
                    logger.info("subleven" + aa.getType() + "==" + aa.getVertexId());
                }
                catch (ArrayIndexOutOfBoundsException tr) {
                    tr.printStackTrace();
                }

            }
            hasil.clear();
            //
            /*System.out.println("ABBREVLEVENSHTEIN :");
            hasil.addAll(objone.getAbb(dicari,jumlahmirip));

            for (Vertex aa:hasil) {
                try {
                    SimpleProperty proper2 = new SimpleProperty("name",aa.getId().trim().replace("[.]", "\\c"));
                    Vertex tes2 = graph.createVertex(aa.getType(),"abbrevlevenshtein_"+aa.getVertexId().trim().replace("[.]", "\\c"));
                    tes2.getProperties().addProperty(proper2);
                    SimpleProperty edgeprop = new SimpleProperty("name","edge_abbrevlevenshtein");
                    Edge edgenya = graph.createEdge(tes,tes2,"edge_abbrevlevenshtein");
                    edgenya.getProperties().addProperty(edgeprop);
                    logger.info("abbrev"+aa.getType() + "==" + aa.getVertexId());
                }
                catch (ArrayIndexOutOfBoundsException tr) {
                    tr.printStackTrace();
                }

            }*/
        }
    }

    public List<Vertex> getVertex(Vertex finding,boolean asli,String table) {
        AccumuloLegacyGraph graph = getGraph(table);
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

    public List<Vertex> getLevenshtein (Vertex dicari,int jumlahmirip,String table) {
        SimilarityTest2 objasli = new SimilarityTest2();
        List<Vertex> vmirip = new ArrayList<>();
        List<Vertex> vhasil = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari,false,table));
        SimilarityTest2 objbanding = new SimilarityTest2();

        //
        Levenshtein levenshtein = new Levenshtein();
        VertexDegree objtampung = new VertexDegree();
        for (Vertex vb:vmirip) {
//            System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
            if (levenshtein.getScore(dicari.getVertexId(),vb.getVertexId()) > 0.8 ){
                //System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
                //
                objtampung.insertVertex(new VertexDegree(new Vertex(vb.getType(), vb.getVertexId()), levenshtein.getScore(dicari.getVertexId(), vb.getVertexId())));
                vhasil.add(new Vertex(vb.getType(), vb.getVertexId()));

//                objbanding.isSimilar(dicari,vb);
                if (objbanding.isSimilar(dicari,vb) == true) {
                    BaseGraph graph = getGraph("similartest");
                    //konek edge yg vertex exact ke vertex yang sama dengan edge isduplicate+jumlahsama
                    String tipe = dicari.getId();
                    tipe = tipe.replace("jalan","").replace("jl", "").replace("jln", "");
                    String tipe2 = vb.getId();
                    tipe2 = tipe2.replace("jalan","").replace("jl", "").replace("jln", "");
                    SimpleProperty proper = new SimpleProperty("name",tipe);
                    SimpleProperty proper2 = new SimpleProperty("name",tipe2);
                    SimpleProperty properdup = new SimpleProperty("duplicate",tipe2);
                    SimpleProperty properdup2 = new SimpleProperty("duplicate",tipe);
                    Vertex tes = graph.createVertex(dicari.getType(),"exact_"+tipe);
                    tes.getProperties().addProperty(proper);
                    tes.getProperties().addProperty(properdup);
                    Vertex tes2 = graph.createVertex(dicari.getType(),"duplicate_"+tipe2);
                    tes2.getProperties().addProperty(proper2);
                    tes2.getProperties().addProperty(properdup2);
                }

            }
        }

        //disini pindahin yg vertex exact ke tabel baru yg cuma 1 nama jalan
        objtampung.sorting(jumlahmirip);

        return vhasil;
    }

    public List<Vertex> getLevenshteinJaccard (Vertex dicari,int jumlahmirip,String table) {
        SimilarityTest2 objasli = new SimilarityTest2();
        List<Vertex> vmirip = new ArrayList<>();
        List<Vertex> vhasil = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari,false,table));
        SimilarityTest2 objbanding = new SimilarityTest2();

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
                vhasil.add(new Vertex(vb.getType(), vb.getVertexId()));
                //                objbanding.isSimilar(dicari,vb);
                if (objbanding.isSimilar(dicari,vb) == true) {
                    BaseGraph graph = getGraph("similartest");
                    //konek edge yg vertex exact ke vertex yang sama dengan edge isduplicate+jumlahsama
                    String tipe = dicari.getId();
                    tipe = tipe.replace("jalan","").replace("jl", "").replace("jln", "");
                    String tipe2 = vb.getId();
                    tipe2 = tipe2.replace("jalan","").replace("jl", "").replace("jln", "");
                    SimpleProperty proper = new SimpleProperty("name",tipe);
                    SimpleProperty proper2 = new SimpleProperty("name",tipe2);
                    SimpleProperty properdup = new SimpleProperty("duplicate",tipe2);
                    SimpleProperty properdup2 = new SimpleProperty("duplicate",tipe);
                    Vertex tes = graph.createVertex(dicari.getType(),"exact_"+tipe);
                    tes.getProperties().addProperty(proper);
                    tes.getProperties().addProperty(properdup);
                    Vertex tes2 = graph.createVertex(dicari.getType(),"duplicate_"+tipe2);
                    tes2.getProperties().addProperty(proper2);
                    tes2.getProperties().addProperty(properdup2);
                }
            }
        }
        objtampung.sorting(jumlahmirip);

        return vhasil;

    }

    public List<Vertex> getSubLevenshteinJaccard (Vertex dicari,int jumlahmirip,String table) {
        SimilarityTest2 objasli = new SimilarityTest2();
        List<Vertex> vmirip = new ArrayList<>();
        List<Vertex> vhasil = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari,false,table));
        SimilarityTest2 objbanding = new SimilarityTest2();
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
                vhasil.add(new Vertex(vb.getType(), vb.getVertexId()));

                //                objbanding.isSimilar(dicari,vb);
                if (objbanding.isSimilar(dicari,vb) == true) {
                    BaseGraph graph = getGraph("similartest");
                    //konek edge yg vertex exact ke vertex yang sama dengan edge isduplicate+jumlahsama
                    String tipe = dicari.getId();
                    tipe = tipe.replace("jalan","").replace("jl", "").replace("jln", "");
                    String tipe2 = vb.getId();
                    tipe2 = tipe2.replace("jalan","").replace("jl", "").replace("jln", "");
                    SimpleProperty proper = new SimpleProperty("name",tipe);
                    SimpleProperty proper2 = new SimpleProperty("name",tipe2);
                    SimpleProperty properdup = new SimpleProperty("duplicate",tipe2);
                    SimpleProperty properdup2 = new SimpleProperty("duplicate",tipe);
                    Vertex tes = graph.createVertex(dicari.getType(),"exact_"+tipe);
                    tes.getProperties().addProperty(proper);
                    tes.getProperties().addProperty(properdup);
                    Vertex tes2 = graph.createVertex(dicari.getType(),"duplicate_"+tipe2);
                    tes2.getProperties().addProperty(proper2);
                    tes2.getProperties().addProperty(properdup2);
                }
            }
        }
        objtampung.sorting(jumlahmirip);

        return vhasil;

    }

    public List<Vertex> getAbb (Vertex dicari,int jumlahmirip,String table) {
        SimilarityTest2 objasli = new SimilarityTest2();
        List<Vertex> vmirip = new ArrayList<>();
        List<Vertex> vhasil = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari,false,table));
        SimilarityTest2 objbanding = new SimilarityTest2();
        //
        AbbrevLevenshtein levenshtein = new AbbrevLevenshtein();
        VertexDegree objtampung = new VertexDegree();

        for (Vertex vb:vmirip) {
//            System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
            if (levenshtein.getScore(dicari.getVertexId(),vb.getVertexId()) > 0.9 ){
                //System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
                //
                objtampung.insertVertex(new VertexDegree(new Vertex(vb.getType(), vb.getVertexId()), levenshtein.getScore(dicari.getVertexId(), vb.getVertexId())));
                vhasil.add(new Vertex(vb.getType(), vb.getVertexId()));

                //                objbanding.isSimilar(dicari,vb);
                if (objbanding.isSimilar(dicari,vb) == true) {
                    BaseGraph graph = getGraph("similartest");
                    //konek edge yg vertex exact ke vertex yang sama dengan edge isduplicate+jumlahsama
                    String tipe = dicari.getId();
                    tipe = tipe.replace("jalan","").replace("jl", "").replace("jln", "");
                    String tipe2 = vb.getId();
                    tipe2 = tipe2.replace("jalan","").replace("jl", "").replace("jln", "");
                    SimpleProperty proper = new SimpleProperty("name",tipe);
                    SimpleProperty proper2 = new SimpleProperty("name",tipe2);
                    SimpleProperty properdup = new SimpleProperty("duplicate",tipe2);
                    SimpleProperty properdup2 = new SimpleProperty("duplicate",tipe);
                    Vertex tes = graph.createVertex(dicari.getType(),"exact_"+tipe);
                    tes.getProperties().addProperty(proper);
                    tes.getProperties().addProperty(properdup);
                    Vertex tes2 = graph.createVertex(dicari.getType(),"duplicate_"+tipe2);
                    tes2.getProperties().addProperty(proper2);
                    tes2.getProperties().addProperty(properdup2);
                }
            }
        }
        objtampung.sorting(jumlahmirip);
        return vhasil;
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
                Vertices tes2 = obj.getNeighbours(vbanding.getId(),g);
                for (Vertex ff:tes) {
                    System.out.println("===111111" + ff);
                    for (Vertex ff2:tes2) {
                        System.out.println("===" + ff2);
                        if (ff.getType().equals(ff2.getType()) && ff.getVertexId().equals(ff2.getVertexId())) {
                            hasil= true;
                        }
                    }
                }

        return hasil;
    }
    public static AccumuloLegacyGraph getGraph (String table) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(table);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        //logger.debug("Builder options: {}", builder);
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("similarity", new AccumuloLegacyStorage(builder));
        graph.indexes.indexers.add(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new NeighborInIndex(graph.indexes.storage,graph));
        graph.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new VertexTypeIndex(graph.indexes.storage));
        JaccardIndex jaccard = new JaccardIndex(graph.indexes.storage,  new JaccardIndexTest.NullFilter(), 10);
        graph.addNewIndexer(jaccard);

        return graph;
    }
}
