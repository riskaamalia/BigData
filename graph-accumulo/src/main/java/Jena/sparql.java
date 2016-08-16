package Jena;

import com.google.common.base.Stopwatch;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.indexer.*;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 1224A on 6/21/2016.
 */
public class sparql {
    private static final Logger logger = LoggerFactory.getLogger(sparql.class);
    public static final Stopwatch timer = Stopwatch.createStarted();
    static public void main(String...argv)
    {
        int i=0;
        String queryStr = "SELECT ?g ?s ?p ?o\n" +
                "WHERE { \n" +
                "graph ?g {?s ?p ?o} . \n" +
//                "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Organisation> .\n" +
//                "FILTER regex(str(?o), \"Indonesia\")\n" +
                "} ORDER BY ASC(?s) LIMIT 200"
                /*"SELECT ?g ?s ?p ?o\n" +
                        "WHERE { \n" +
                        "graph ?g {?s ?p ?o} . \n" +
                        "FILTER regex(str(?o), \"Malaysia\")\n" +
                        "} LIMIT 2000"*/
                /*"SELECT ?g ?s ?p ?o\n" +
                        "WHERE { \n" +
                        "graph ?g {?s ?p ?o} . \n" +
                        "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/College> .\n" +
//                        "FILTER regex(str(?o), \"Indonesia\")\n" +
                        "} LIMIT 2000"*/;
        QueryExecution qe= QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryStr);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution s = rs.nextSolution();
            System.out.println(s.toString());
            sparql objnya = new sparql();
            try {
                objnya.GraphAsli(s.getResource("?s").toString(),s.getResource("?p").toString(),s.getResource("?o").toString());
                objnya.GraphConvert(s.getResource("?s").toString(),s.getResource("?p").toString(),s.getResource("?o").toString());
            } catch (Exception op) {
                logger.info("ada error");
            }
            i++;
            logger.info("Jumlah : "+i);
        }
    }

    public void GraphAsli (String s,String p,String o) {
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
//        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        Vertex source = graph.createVertex("subject",s);
        source.getProperties().addProperty(new SimpleProperty("name", s));
        Vertex target = graph.createVertex("object",o);
        target.getProperties().addProperty(new SimpleProperty("name", o));
        graph.createEdge(source, target, p).getProperties().addProperty(new SimpleProperty("dbpedia", p));
    }

    public void GraphConvert (String s,String p,String o) {
        //yang tipenya resource harus ditelisik lagi apaan
        String s_type="";String o_type="";
        sparql objnya = new sparql();
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename("master_data_dbpedia");
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("sparqlTSVgraph", new AccumuloLegacyStorage2(builder));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new DataTypeIndexer(graph.indexes.storage));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage, graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage, graph));
        graph .addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        String s_asli = s; String p_asli = p; String o_asli = o;
        if (p.contains("yago") == false && p.contains("type") == false ) {
            s = s.substring(s.lastIndexOf("/") + 1, s.length()).trim().replace(",", "\\c").replace("_", " ").replace("[.]", "\\a").replace("\"","");
            p = p.substring(p.lastIndexOf("/") + 1, p.length()).trim().replace(",", "\\c").replace("_", " ").replace("[.]", "\\a").replace("\"","");
            o = o.substring(o.lastIndexOf("/") + 1, o.length()).trim().replace(",", "\\c").replace("_", " ").replace("[.]", "\\a").replace("\"","");
            try {
                s_type = s_asli.split("/")[s_asli.split("/").length - 2];
                o_type = o_asli.split("/")[o_asli.split("/").length - 2];
            } catch (IndexOutOfBoundsException op) {
                logger.info("ada error ");
                op.printStackTrace();
            }
            Vertex source = graph.createVertex(s_type.toLowerCase(),s.toLowerCase());
            source.getProperties().addProperty(new SimpleProperty("name", s.toLowerCase()));
            source.getProperties().addProperty(new SimpleProperty("asli", s_asli));
            Vertex target = graph.createVertex(o_type.toLowerCase(),o.toLowerCase());
            target.getProperties().addProperty(new SimpleProperty("name", o.toLowerCase()));
            target.getProperties().addProperty(new SimpleProperty("asli", o_asli));
            graph.createEdge(source, target, p.toLowerCase()).getProperties().addProperty(new SimpleProperty("dbpedia", p.toLowerCase()));
            graph.createEdge(source, target, p.toLowerCase()).getProperties().addProperty(new SimpleProperty("dbpedia.asli", p_asli));
        } else {
            s = s.substring(s.lastIndexOf("/") + 1, s.length()).trim().replace(",", "\\c").replace("_", " ").replace("[.]", "\\a").replace("\"","");
            o = objnya.cekUpperCase(o.substring(o.lastIndexOf("/") + 1, o.length()).trim().replace(",", "\\c").replace("_", " ").replace("[.]", "\\a").replace("\"",""));
            logger.info(o + "== ");
            Vertex source = graph.createVertex(o.toLowerCase(),s.toLowerCase());
            source.getProperties().addProperty(new SimpleProperty("name", s.toLowerCase()));
            source.getProperties().addProperty(new SimpleProperty("asli", s_asli));
            source.getProperties().addProperty(new SimpleProperty("tipe", o_asli));
        }
    }

    public String cekUpperCase (String katanya) {
        String hasilnya= String.valueOf(katanya.charAt(0));
        int tampungawal=0,i=1;
        do {
            if ( Character.isUpperCase(katanya.charAt(i)) ) {
                hasilnya = hasilnya + " " ;
            }
            hasilnya = hasilnya + katanya.charAt(i);
            i++;
        } while (i < katanya.length());

        return hasilnya.trim();
    }

}
