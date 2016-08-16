package Jena;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Run {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub


        InputStream in = new FileInputStream(new
                File("src/main/resources/blogger.rdf"));
        Model model = ModelFactory.createMemModelMaker().createModel(null);
        model.read(in,null); // null base URI, since model URIs are absolute
        in.close();

        // Create a new query
        String queryString =
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                        +"PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
                        "SELECT ?url ?name ?title" +
                        " WHERE {" +
                        "      ?contributor foaf:name ?name ." +
                        "      ?contributor foaf:weblog ?url . "
                        + "    ?channel dc:title ?title . " +

                        "      " +
                        "      }";

//        String str = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> SELECT ?url ?name FROM 	<bloggers.rdf> WHERE  { ?contributor foaf:name ?name . ?contributor foaf:weblog ?url . }" ;
        String tes = "select distinct ?Concept where {[] a ?Concept} LIMIT 10";

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results
        ResultSetFormatter.out(System.out, results, query);

        // Important - free up resources used running the query
        qe.close();



    }

}