package ontology;

//import Jena.ClassHierarchy;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Graph;
import com.msk.graph.Vertex;
import com.msk.graph.indexer.*;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.*;

/**
 * Created by 1224A on 6/20/2016.
 */
public class ontology {

    public Graph mapoftype () {
        Graph baseontology = getGraph("baseontology");

        //ibunya
        baseontology.createVertex("basetype","address").addProperty(new SimpleProperty("name","address"));
        //anaknya
        baseontology.createVertex("basetype","jalan").addProperty(new SimpleProperty("name","jalan"));
        baseontology.createVertex("basetype","gang").addProperty(new SimpleProperty("name","gang"));
        baseontology.createVertex("basetype","kelurahan").addProperty(new SimpleProperty("name","kelurahan"));
        baseontology.createVertex("basetype","desa").addProperty(new SimpleProperty("name","desa"));
        baseontology.createVertex("basetype","kecamatan").addProperty(new SimpleProperty("name","kecamatan"));
        baseontology.createVertex("basetype","kabupaten").addProperty(new SimpleProperty("name","kabupaten"));
        baseontology.createVertex("basetype","provinsi").addProperty(new SimpleProperty("name","provinsi"));
        baseontology.createVertex("basetype","location").addProperty(new SimpleProperty("name","location"));
        baseontology.createVertex("basetype","kodepos").addProperty(new SimpleProperty("name","kodepos"));
        baseontology.createVertex("basetype","country").addProperty(new SimpleProperty("name","country"));
        baseontology.createVertex("basetype","longlat").addProperty(new SimpleProperty("name","longlat"));
        baseontology.createVertex("basetype","rt").addProperty(new SimpleProperty("name","rt"));
        baseontology.createVertex("basetype","rw").addProperty(new SimpleProperty("name","rw"));
        baseontology.createVertex("basetype","km").addProperty(new SimpleProperty("name","km"));
        baseontology.createVertex("basetype","nomor").addProperty(new SimpleProperty("name","nomor"));

        //ibunya
        baseontology.createVertex("basetype","person").addProperty(new SimpleProperty("name","person"));
        //anaknya
        baseontology.createVertex("basetype","ktp").addProperty(new SimpleProperty("name","ktp"));
        baseontology.createVertex("basetype","kk").addProperty(new SimpleProperty("name","kk"));
        baseontology.createVertex("basetype","sim").addProperty(new SimpleProperty("name","sim"));
        baseontology.createVertex("basetype","passpor").addProperty(new SimpleProperty("name","passpor"));
        baseontology.createVertex("basetype","nopol").addProperty(new SimpleProperty("name","nopol"));

        //ibunya
        baseontology.createVertex("basetype","poi").addProperty(new SimpleProperty("name","poi"));
        baseontology.createVertex("basetype","gedung").addProperty(new SimpleProperty("name","gedung"));
        baseontology.createVertex("basetype","kavling").addProperty(new SimpleProperty("name","kavling"));
        baseontology.createVertex("basetype","komplek").addProperty(new SimpleProperty("name","komplek"));
        baseontology.createVertex("basetype","natural location").addProperty(new SimpleProperty("name","natural location"));
        baseontology.createVertex("basetype","lantai").addProperty(new SimpleProperty("name","lantai"));
        baseontology.createVertex("basetype","blok").addProperty(new SimpleProperty("name","blok"));

        //joinan duanya nya
        baseontology.createVertex("basetype","brand").addProperty(new SimpleProperty("name","brand"));
        baseontology.createVertex("basetype","organization").addProperty(new SimpleProperty("name","organization"));

        //column name sendirian
        baseontology.createVertex("basetype","kolom").addProperty(new SimpleProperty("name","kolom"));

        //jomblo
        baseontology.createVertex("basetype","gender").addProperty(new SimpleProperty("name","gender"));
        baseontology.createVertex("basetype","family status").addProperty(new SimpleProperty("name","family status"));
        baseontology.createVertex("basetype","religion").addProperty(new SimpleProperty("name","religion"));
        baseontology.createVertex("basetype","occupation").addProperty(new SimpleProperty("name","occupation"));
        baseontology.createVertex("basetype","title").addProperty(new SimpleProperty("name","title"));
        baseontology.createVertex("basetype","bloodtype").addProperty(new SimpleProperty("name","bloodtype"));
        baseontology.createVertex("basetype","color").addProperty(new SimpleProperty("name","color"));
        baseontology.createVertex("basetype","marital status").addProperty(new SimpleProperty("name","marital status"));
        baseontology.createVertex("basetype","metric system").addProperty(new SimpleProperty("name","metric system"));
        baseontology.createVertex("basetype","telepon").addProperty(new SimpleProperty("name","telepon"));

        //ini untuk edge
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","jalan"),"hasjalan")
                .addProperty(new SimpleProperty("hasjalan.tabel_address","hasjalan"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","gang"),"hasgang")
                .addProperty(new SimpleProperty("hasgang.tabel_address","hasgang"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","kelurahan"),"haskelurahan")
                .addProperty(new SimpleProperty("haskelurahan.tabel_address","haskelurahan"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","desa"),"hasdesa")
                .addProperty(new SimpleProperty("hasdesa.tabel_address","hasdesa"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","kecamatan"),"haskecamatan")
                .addProperty(new SimpleProperty("haskecamatan.tabel_address","haskecamatan"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","kabupaten"),"haskabupaten")
                .addProperty(new SimpleProperty("haskabupaten.tabel_address","haskabupaten"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","provinsi"),"hasprovinsi")
                .addProperty(new SimpleProperty("hasprovinsi.tabel_address","hasprovinsi"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","location"),"haslocation")
                .addProperty(new SimpleProperty("haslocation.tabel_address","haslocation"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","kodepos"),"haskodepos")
                .addProperty(new SimpleProperty("haskodepos.tabel_address","haskodepos"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","country"),"hascountry")
                .addProperty(new SimpleProperty("hascountry.tabel_address","hascountry"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","longlat"),"haslonglat")
                .addProperty(new SimpleProperty("haslonglat.tabel_address","haslonglat"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","rt"),"hasrt")
                .addProperty(new SimpleProperty("hasrt.tabel_address","hasrt"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","rw"),"hasrw")
                .addProperty(new SimpleProperty("hasrw.tabel_address","hasrw"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","km"),"haskm")
                .addProperty(new SimpleProperty("haskm.tabel_address","haskm"));
        baseontology.createEdge(new Vertex("basetype","address"),new Vertex("basetype","nomor"),"hasnomor")
                .addProperty(new SimpleProperty("hasnomor.tabel_address","hasnomor"));

        //males ah belakangan sisa nya hahahahah

        return baseontology;
    }

    //        String pathclassnya = objnew.newMapOnto("http://dbpedia.org/ontology/","file:src/main/resources/dbpedia_2015-10.owl","D:/coba.txt");
    public String newMapOnto (String prefix,String pathfile,String pathhasil) throws FileNotFoundException {
        OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
        m.getDocumentManager().addAltEntry( prefix,
                pathfile );

        m.read( pathfile );
        PrintStream obj = new PrintStream(new File(pathhasil));
//        new ClassHierarchy().showHierarchy( obj, m );
       return pathhasil;
    }

    public Graph IntegrationToBase (Graph basemap,Graph newMap) throws FileNotFoundException {
        Graph resultintegration = getGraph("baseontology");
        ontology objnew = new ontology();
        //open file
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("D:/coba.txt"));
            String text="";
            while ((text = br.readLine()) != null) {
                System.out.println(text);
                if (text.split("[|]").length != 0) {
                    String [] level = text.split("[|]");
                    System.out.println(level.length);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Gagal membaca file " + "D:/coba.txt");
            e.printStackTrace();
        }

        return resultintegration;
    }

    public static void main (String[] args) throws FileNotFoundException {
        ontology obj = new ontology() ;
//        obj.mapoftype();
//       obj.IntegrationToBase(tes,tes);
//        String pathclassnya = obj.newMapOnto("http://www.w3.org/2001/sw/WebOnt/","file:src/main/resources/food.owl","D:/coba.txt");
//         String pathclassnya = objnew.newMapOnto("http://dbpedia.org/ontology/","file:src/main/resources/dbpedia_2015-10.owl","D:/coba.txt");
        createTreeOWL objowl = new createTreeOWL();
        try{
            BufferedReader br=new BufferedReader(new FileReader("D:/mediatrac/DATA/hasilowl.txt"));
            Graph g = getGraph("owl");
            byte[] visualTree=objowl.createTree(g,br,"class","descendant");
            objowl.visualize("c:/test/owlGraph.html",visualTree);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public static AccumuloLegacyGraph getGraph (String table) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(table);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("RDFInput", new AccumuloLegacyStorage2(builder));
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage,graph));
        graph.addNewIndexer(new WordIndexer(graph.indexes.storage, graph));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.addNewIndexer(new EdgeTypeIndexer(graph.indexes.storage));
        return graph;
    }
}
