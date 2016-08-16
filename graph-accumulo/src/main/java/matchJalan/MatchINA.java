package matchJalan;

import com.google.common.base.Stopwatch;
import com.msk.graph.*;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.property.Property;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import com.msk.graph.storage.AccumuloStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
import com.msk.graph.Test.Neighbours;
import com.msk.graph.Test.VertexDegree;
*/

/**
 * Created by 1224A on 4/19/2016.
 */
public class

MatchINA {
    private static final Logger logger = LoggerFactory.getLogger(MatchINA.class);
    static final Stopwatch timer = new Stopwatch();

    public static void main(String[] args) throws Exception {
        timer.start();
        AccumuloLegacyGraph graph = getGraph ("master_data_jalan");
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));

        //adm

        AccumuloLegacyGraph graphadm = getGraph ("master_data_administrasi");
        graphadm.addNewIndexer(new NeighborOutIndex(graphadm.indexes.storage,graphadm));
        graphadm.addNewIndexer(new NeighborInIndex(graphadm.indexes.storage, graphadm));
        graphadm.addNewIndexer(new VertexTypeIndex(graphadm.indexes.storage));
        graphadm.indexes.indexers.add(new DataTypeIndexer(graphadm.indexes.storage));
        graphadm.indexes.indexers.add(new EdgeTypeIndexer(graphadm.indexes.storage));
        List<GraphFilter> filters=new ArrayList<>();
        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "jalan"));
        Vertices vs=graph.getVertices(filters);
        int i=0;
        for(Vertex v:vs) {
            logger.info(v.key());
            Edges tetangga = v.getEdges();
            if (i % 100 == 0)
                System.out.println("Currently processed " + i++ + " lines and it took " + timer.elapsed(TimeUnit.SECONDS) + " seconds");
            for (Edge tetang:tetangga) {
                try {
                    logger.info(tetang.getTargetId() + "=====");
                    String vtetangga = tetang.getTargetId();
                    String[] split = vtetangga.split("[.]");

                    switch (split[1]) {
                        case "desa": {
                            try {
//                            logger.info(graphadm.getVertex(split[1], split[2]).getId());
                                Vertex v1 = graphadm.getVertex(split[1], split[2].replace("desa", ""));
                                for (Vertex vv : v1.getNeighbors()) {
                                    if (vv.getType().equals("location")) {
//                                    logger.info("==>"+vv.getId());
                                        graph.createEdge(v.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(v1.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(vv.getId(), v.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                        graph.createEdge(vv.getId(), v1.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                    }
                                }

                            } catch (NullPointerException nii) {
                               logger.info("ada error");
                            }
                            break;
                        }

                        case "kecamatan": {
                            try {
//                            logger.info(graphadm.getVertex(split[1], split[2]).getId());
                                Vertex v1 = graphadm.getVertex(split[1], split[2].replace("kecamatan", "").replace("kec.", ""));
                                for (Vertex vv : v1.getNeighbors()) {
                                    if (vv.getType().equals("location")) {
//                                    logger.info("==>"+vv.getId());
                                        graph.createEdge(v.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(v1.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(vv.getId(), v.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                        graph.createEdge(vv.getId(), v1.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                    }
                                }

                            } catch (NullPointerException nii) {
                                logger.info("ada error");
                            }
                            break;
                        }

                        case "kabupaten": {
                            try {
//                            logger.info(graphadm.getVertex(split[1], split[2]).getId());
                                Vertex v1 = graphadm.getVertex(split[1], split[2].replace("kabupaten", "").replace("kota", ""));
                                for (Vertex vv : v1.getNeighbors()) {
                                    if (vv.getType().equals("location")) {
//                                    logger.info("==>"+vv.getId());
                                        graph.createEdge(v.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(v1.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(vv.getId(), v.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                        graph.createEdge(vv.getId(), v1.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                    }
                                }

                            } catch (NullPointerException nii) {
                                logger.info("ada error");
                            }
                            break;
                        }

                        case "provinsi": {
                            try {
//                            logger.info(graphadm.getVertex(split[1], split[2]).getId());
                                Vertex v1 = graphadm.getVertex(split[1], split[2].replace("provinsi", "").replace("prov", ""));
                                for (Vertex vv : v1.getNeighbors()) {
                                    if (vv.getType().equals("location")) {
//                                    logger.info("==>"+vv.getId());
                                        graph.createEdge(v.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(v1.getId(), vv.getId(), "haslocation").getProperties().addProperty(new SimpleProperty("haslocation.posgre_match_streetadm", "haslocation"));
                                        graph.createEdge(vv.getId(), v.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                        graph.createEdge(vv.getId(), v1.getId(), "locationof").getProperties().addProperty(new SimpleProperty("locationof.posgre_match_streetadm", "locationof"));
                                    }
                                }

                            } catch (NullPointerException nii) {
                                logger.info("ada error");
                            }
                            break;
                        }
                    }
                }
                catch (NullPointerException poiii) {
                    logger.info("ada error");
                }
            }
            /*Vertices tetangga = v.getNeighbors("desaof");
            for (Vertex hasil:tetangga) {
                logger.info(hasil.getId());
            }*/

            /*MatchINA objone = new MatchINA();
            Vertex dicari = new Vertex(bagiinput[0], bagiinput[1]);
            //logger.info(dicari.type + "==" + dicari.id);
            System.out.println("EXACT :");
            objone.getVertex(dicari, true);

            //
            System.out.println("LEVENSHTEIN :");
            objone.getLevenshtein(dicari, jumlahmirip);
            //
            System.out.println("LEVENSHTEIN+JACCARD :");
            objone.getLevenshteinJaccard(dicari, jumlahmirip);
            //
            System.out.println("SUBLEVENSHTEIN+JACCARD :");
            objone.getSubLevenshteinJaccard(dicari, jumlahmirip);
            //
            System.out.println("ABBREVLEVENSHTEIN :");
            objone.getAbb(dicari, jumlahmirip);*/
        }
    }

    public List<Vertex> getVertex(Vertex finding,boolean asli) {
        AccumuloStorage.Builder builder = AccumuloStorage.Builder.RajaampatBuilder();
        builder.setTablename("master_data_jalan");
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

/*
    public void getLevenshtein (Vertex dicari,int jumlahmirip) {
        MatchINA objasli = new MatchINA();
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
                builder2.setTablename("master_data_jalan");
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
        MatchINA objasli = new MatchINA();
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
        MatchINA objasli = new MatchINA();
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
        MatchINA objasli = new MatchINA();
        List<Vertex> vmirip = new ArrayList<>();
        List<Vertex> vhasil = new ArrayList<>();
        vmirip.addAll(objasli.getVertex(dicari, false));

        //
        AbbrevLevenshtein levenshtein = new AbbrevLevenshtein();
        VertexDegree objtampung = new VertexDegree();
        MatchINA objsimilar = new MatchINA();

        for (Vertex vb:vmirip) {
//            System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
            if (levenshtein.getScore(dicari.getVertexId(),vb.getVertexId()) > 0.9 ){
                //System.out.println(dicari.id+"=="+vb.id+"=="+levenshtein.getScore(dicari.id,vb.id));
                //
                objtampung.insertVertex(new VertexDegree(new Vertex(vb.getType(), vb.getVertexId()), levenshtein.getScore(dicari.getVertexId(), vb.getVertexId())));
                objsimilar.isSimilar(dicari, vb);
            }
        }
        objtampung.sorting(jumlahmirip);
    }

    public boolean isSimilar (Vertex vexact,Vertex vbanding) {
        boolean hasil=false;

        Neighbours obj = new Neighbours();
        AccumuloLegacyStorage2.Builder builder2 = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder2.setTablename("master_data_jalan");
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
*/

    public static AccumuloLegacyGraph getGraph (String table) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(table);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("matchINA", new AccumuloLegacyStorage2(builder));
        return graph;
    }
}
