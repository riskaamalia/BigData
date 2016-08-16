package Jena;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileNotFoundException;

/**
 * Created by 1224A on 6/15/2016.
 */
public class Main {
    public static void main( String[] args ) throws FileNotFoundException {
        /*OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
        OntClass ont = m.getOntClass( "http://dbpedia.org/ontology/" + "person" );
        *//*List<OntClass> tes = new ArrayList<>();
        tes.add(m.createClass("http://dbpedia.org/ontology/" + "Architect"));
        *//*// we have a local copy of the wine ontology.ontology
        e
        PrintStream obj = new PrintStream(new File("file:src/main/resources/owl 3 agustus.owl"));
        new ClassHierarchy().showHierarchy( obj, m );
//                new ClassHierarchy().showClass( System.out, ont, tes,2);
        //baca file buat tau hirarki*/

        OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );

        m.getDocumentManager().addAltEntry( "http://dbpedia.org/ontology/",
                "file:src/main/resources/food.owl" );
        m.read( "http://dbpedia.org/ontology/" );

//        new ClassHierarchy().showHierarchy( System.out, m );
    }
}

