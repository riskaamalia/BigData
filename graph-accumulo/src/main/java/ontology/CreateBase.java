package ontology;

import com.msk.graph.Utils;
import com.msk.graph.Vertex;
import com.msk.graph.indexer.*;
import com.msk.graph.memory.MemoryGraph;
import com.msk.graph.property.SimpleProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by 0.5229 on 6/28/200.56.
 */
public class CreateBase {
    public static void /*MemoryGraph*/ main(String[] args){
        try {
            MemoryGraph mG = new MemoryGraph("baseAdress");
            mG.addNewIndexer(NeighborInIndex.class);
            mG.addNewIndexer(NeighborOutIndex.class);
//        mG.indexes.indexers.add(new NeighborInIndex(mG.indexes.storage,mG));
//        mG.indexes.indexers.add(new NeighborOutIndex(mG.indexes.storage,mG));
            mG.indexes.indexers.add(new DataTypeIndexer(mG.indexes.storage));
            mG.indexes.indexers.add(new EdgeTypeIndexer(mG.indexes.storage));
            mG.indexes.indexers.add(new VertexTypeIndex(mG.indexes.storage));
//        mG.addNewIndexer(new WordIndexer(mG.indexes.storage, mG));
            Vertex fulladdress = mG.createVertex("type", "address").addProperty(new SimpleProperty("name", "address"));
            Vertex jalan = mG.createVertex("type", "jalan").addProperty(new SimpleProperty("name", "jalan"));
            Vertex km = mG.createVertex("type", "km").addProperty(new SimpleProperty("name", "km"));
            Vertex nomor = mG.createVertex("type", "nomor").addProperty(new SimpleProperty("name", "nomor"));
            Vertex desa = mG.createVertex("type", "desa").addProperty(new SimpleProperty("name", "desa"));
            Vertex kelurahan = mG.createVertex("type", "kelurahan").addProperty(new SimpleProperty("name", "kelurahan"));
            Vertex kecamatan = mG.createVertex("type", "kecamatan").addProperty(new SimpleProperty("name", "kecamatan"));
            Vertex kabupaten = mG.createVertex("type", "kabupaten").addProperty(new SimpleProperty("name", "kabupaten"));
            Vertex provinsi = mG.createVertex("type", "provinsi").addProperty(new SimpleProperty("name", "provinsi"));
            Vertex rt = mG.createVertex("type", "rt").addProperty(new SimpleProperty("name", "rt"));
            Vertex rw = mG.createVertex("type", "rw").addProperty(new SimpleProperty("name", "rw"));
            Vertex gedung = mG.createVertex("type", "gedung").addProperty(new SimpleProperty("name", "gedung"));
            Vertex blok = mG.createVertex("type", "blok").addProperty(new SimpleProperty("name", "blok"));
            Vertex lantai = mG.createVertex("type", "lantai").addProperty(new SimpleProperty("name", "lantai"));
            Vertex gang = mG.createVertex("type", "gang").addProperty(new SimpleProperty("name", "gang"));
            Vertex kavling = mG.createVertex("type", "kavling").addProperty(new SimpleProperty("name", "kavling"));
            Vertex komplek = mG.createVertex("type", "komplek").addProperty(new SimpleProperty("name", "komplek"));
            Vertex no_telepon = mG.createVertex("type", "no_telepon").addProperty(new SimpleProperty("name", "no telepon"));
            Vertex kodepos = mG.createVertex("type", "kodepos").addProperty(new SimpleProperty("name", "kodepos"));
            mG.createEdge(fulladdress, jalan, "hasjalan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasjalan"));
            mG.createEdge(fulladdress, km, "haskm").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskm"));
            mG.createEdge(fulladdress, nomor, "hasnomor").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasnomor"));
            mG.createEdge(fulladdress, kelurahan, "haskelurahan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskelurahan"));
            mG.createEdge(fulladdress, desa, "hasdesa").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasdesa"));
            mG.createEdge(fulladdress, kecamatan, "haskecamatan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskecamatan"));
            mG.createEdge(fulladdress, kabupaten, "haskabupaten").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskabupaten"));
            mG.createEdge(fulladdress, provinsi, "hasprovinsi").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasprovinsi"));
            mG.createEdge(fulladdress, rt, "hasrt").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasrt"));
            mG.createEdge(fulladdress, rw, "hasrw").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasrw"));
            mG.createEdge(fulladdress, gedung, "hasgedung").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasgedung"));
            mG.createEdge(fulladdress, blok, "hasblok").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasblok"));
            mG.createEdge(fulladdress, lantai, "haslantai").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haslantai"));
            mG.createEdge(fulladdress, gang, "hasgang").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasgang"));
            mG.createEdge(fulladdress, kavling, "haskavling").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskavling"));
            mG.createEdge(fulladdress, komplek, "haskomplek").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskomplek"));
            mG.createEdge(fulladdress, no_telepon, "hastelp").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hastelp"));
            mG.createEdge(fulladdress, kodepos, "haskodepos").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskodepos"));
            mG.createEdge(km, jalan, "hasjalan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasjalan"));
            mG.createEdge(nomor, jalan, "hasjalan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasjalan"));
            mG.createEdge(jalan, kabupaten, "haskabupaten").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskabupaten"));
            mG.createEdge(desa, kelurahan, "haskelurahan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskelurahan"));
            mG.createEdge(desa, kecamatan, "haskecamatan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskecamatan"));
            mG.createEdge(desa, kabupaten, "haskabupaten").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskabupaten"));
            mG.createEdge(desa, provinsi, "hasprovinsi").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasprovinsi"));
            mG.createEdge(kelurahan, desa, "hasdesa").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasdesa"));
            mG.createEdge(kelurahan, kecamatan, "haskecamatan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskecamatan"));
            mG.createEdge(kelurahan, kabupaten, "haskabupaten").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskabupaten"));
            mG.createEdge(kelurahan, provinsi, "hasprovinsi").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasprovinsi"));
            mG.createEdge(kecamatan, desa, "hasdesa").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasdesa"));
            mG.createEdge(kecamatan, kelurahan, "haskelurahan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskelurahan"));
            mG.createEdge(kecamatan, kabupaten, "haskabupaten").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskabupaten"));
            mG.createEdge(kecamatan, provinsi, "hasprovinsi").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasprovinsi"));
            mG.createEdge(kabupaten, desa, "hasdesa").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasdesa"));
            mG.createEdge(kabupaten, kelurahan, "haskelurahan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskelurahan"));
            mG.createEdge(kabupaten, kecamatan, "haskecamatan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskecamatan"));
            mG.createEdge(kabupaten, provinsi, "hasprovinsi").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasprovinsi"));
            mG.createEdge(provinsi, desa, "hasdesa").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasdesa"));
            mG.createEdge(provinsi, kelurahan, "haskelurahan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskelurahan"));
            mG.createEdge(provinsi, kecamatan, "haskecamatan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskecamatan"));
            mG.createEdge(provinsi, kabupaten, "haskabupaten").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskabupaten"));
            mG.createEdge(blok, gedung, "hasgedung").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasgedung"));
            mG.createEdge(lantai, gedung, "hasgedung").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasgedung"));
            mG.createEdge(nomor, gedung, "hasgedung").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasgedung"));
            mG.createEdge(gang, jalan, "hasjalan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "hasjalan"));
            mG.createEdge(kavling, komplek, "haskomplek").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskomplek"));
            mG.createEdge(blok, komplek, "haskomplek").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskomplek"));
            mG.createEdge(rt, kelurahan, "haskelurahan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskelurahan"));
            mG.createEdge(rw, kelurahan, "haskelurahan").addProperty(new SimpleProperty("weight", "0.5")).addProperty(new SimpleProperty("name", "haskelurahan"));
            /*
            * mG.createEdge(fulladdress, jalan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, km, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, nomor, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, kelurahan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, desa, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, kecamatan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, kabupaten, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, provinsi, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, rt, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, rw, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, gedung, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, blok, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, lantai, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, gang, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, kavling, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, komplek, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, no_telepon, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(fulladdress, kodepos, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(km, jalan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(nomor, jalan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(jalan, kabupaten, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(desa, kelurahan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(desa, kecamatan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(desa, kabupaten, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(desa, provinsi, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kelurahan, desa, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kelurahan, kecamatan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kelurahan, kabupaten, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kelurahan, provinsi, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kecamatan, desa, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kecamatan, kelurahan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kecamatan, kabupaten, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kecamatan, provinsi, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kabupaten, desa, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kabupaten, kelurahan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kabupaten, kecamatan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kabupaten, provinsi, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(provinsi, desa, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(provinsi, kelurahan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(provinsi, kecamatan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(provinsi, kabupaten, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(blok, gedung, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(lantai, gedung, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(nomor, gedung, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(gang, jalan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(kavling, komplek, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(blok, komplek, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(rt, kelurahan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));
            mG.createEdge(rw, kelurahan, "relatedTo").addProperty(new SimpleProperty("weight", "0.5"));*/
        /*for(Vertex v:mG.getVertices(null)){
            System.out.println(v+"\t"+v.out("relatedTo").iterator().hasNext());
            for(Vertex v1:v.out("relatedTo"))
                System.out.println(v1);
        }
        for (Edge e:mG.getEdges(null))
            System.out.println(e);*/
            Files.write(new File("C:/test/masterAddress.html").toPath(), Utils.generateViewer(mG, new String[]{"relatedTo"}, "name").getBytes());
//            return mG;
        }
        catch (IOException e){
            e.printStackTrace();
//            return null;
        }
    }
}
