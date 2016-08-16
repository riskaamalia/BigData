package matchJalan;

import com.google.common.base.Stopwatch;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.internal.UrlSigner;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.msk.graph.AccumuloLegacyGraph;
import com.msk.graph.Vertex;
import com.msk.graph.Vertices;
import com.msk.graph.filters.GraphFilter;
import com.msk.graph.indexer.*;
import com.msk.graph.property.SimpleProperty;
import com.msk.graph.storage.AccumuloLegacyStorage2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by 1224A on 4/12/2016.
 */

public class match {

    private static final Logger logger = LoggerFactory.getLogger(match.class);
    public static final Stopwatch timer = new Stopwatch();

    public static void main(String[] args) throws Exception {

        timer.start();
        AccumuloLegacyGraph graph = getGraph ("master_data_jalan");
        graph.addNewIndexer(new NeighborOutIndex(graph.indexes.storage));
        graph.addNewIndexer(new NeighborInIndex(graph.indexes.storage));
        graph.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graph.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graph.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));
        List<GraphFilter> filters=new ArrayList<>();
        Vertices vs=graph.getVertices(filters);

        filters.add(new GraphFilter(GraphFilter.VERTEX_TYPE, "jalan"));
//        Vertex v = graph.getVertex("jalan", "senopati_dbdevgis");
        String clientID="gme-mediatracsistemkomunikasi";
        String key = "78kE37r_JfuGutwtupaHGcH5kdw=";
        GeoApiContext context =  new GeoApiContext().setEnterpriseCredentials(clientID, key);

        AccumuloLegacyGraph graphgoogle = getGraph ("master_data_google");
        graphgoogle.addNewIndexer(new NeighborOutIndex(graph.indexes.storage));
        graphgoogle.addNewIndexer(new NeighborInIndex(graph.indexes.storage));
        graphgoogle.addNewIndexer(new VertexTypeIndex(graph.indexes.storage));
        graphgoogle.indexes.indexers.add(new DataTypeIndexer(graph.indexes.storage));
        graphgoogle.indexes.indexers.add(new EdgeTypeIndexer(graph.indexes.storage));
        Vertex vfulladdress,vdesa,vkec,vkab,vprov,vtampung;
        int j=0;
        Boolean benar = false;
           for(Vertex v:vs) {
                try
                {
                    //vertex.jalan.bulevard kelapa gading
//                    v.getVertexId().substring(0, 22).equals("bulevard kelapa gading")
                    if (v.getVertexId().equals(args[0]) == true)
                        benar = true;
                }
                catch (StringIndexOutOfBoundsException po){
                    logger.info("ada error");
                }

               if (benar == true) {
                   logger.info("yang di ambil : "+v.getId());
                   if (j++ % 100 == 0)
                       System.out.println("Currently processed " + j + " lines and it took " + timer.elapsedTime(TimeUnit.SECONDS) + " seconds");
               /*GeocodingResult[] results = GeocodingApi.geocode(context, v.getProperties().getProperty("name").get()).await();
               try {
//                   System.out.println(results[0].formattedAddress);

               }
               catch (ArrayIndexOutOfBoundsException ea) {
                   logger.info("ga ada=======================");
               }*/
                   vfulladdress = graph.createVertex("jalan", v.getProperties().getProperty("name").get());
                   vtampung = vfulladdress;
                   vfulladdress.getProperties().addProperty(new SimpleProperty("name.hasil_gmapapi_dbdevgis", v.getProperties().getProperty("name").get()));
                   try {
                       if (v.getProperties().getProperty("geom").get().equals(null) == false) {
//               ini untuk yg lokasi geomnya
//                   logger.info(v.getProperties().getProperty("geom").get());
                           String geom1 = v.getProperties().getProperty("geom").get().replace("linestring", "").replace("(", "").replace(")", "").replace("\\c", ",");
//                   logger.info(geom1+"----------------------");
                           String[] satusatu = geom1.split(",");
                           for (int i = 0; i < satusatu.length; i++) {
                               String[] pasangan = satusatu[i].split(" ");
                               LatLng obj = new LatLng(Double.parseDouble(pasangan[1]), Double.parseDouble(pasangan[0]));
                               GeocodingResult[] reverse = GeocodingApi.reverseGeocode(context, obj).await();
                               try {
//                       System.out.println(reverse[0].formattedAddress+"koordinaaaaaaaaaaaaat");
                                   vfulladdress.getProperties().addProperty(new SimpleProperty("koordinat" + i + ".hasil_gmapapi_dbdevgis", reverse[0].formattedAddress.replace(",", "\\c").toLowerCase().trim()));

                                   // di sini konek buat edge berdasarkan hasil JSON================================================================================================================
                                   InputStream inputStream = null;
                                   String json = "";

                                   try {
                                       HttpClient client = new DefaultHttpClient();
                                       UrlSigner urlnya = new UrlSigner(key);
                                       String hostname = "https://maps.googleapis.com";
                                       URL host = new URL(hostname);
                                       String path = "/maps/api/geocode/json";
                                       StringBuilder url = new StringBuilder(path);
                                       url.append("?client=").append(clientID);
                                       url.append("&latlng=" + pasangan[1] + "%2C" + pasangan[0]);
                                       String signature = urlnya.getSignature(url.toString());
                                       url.append("&signature=").append(signature);

                                       HttpPost post = new HttpPost(host.toString() + url.toString());
                                       HttpResponse response = client.execute(post);
                                       HttpEntity entity = response.getEntity();
                                       inputStream = entity.getContent();
//                                       logger.info(post.getURI().toString());
                                       Vertex vurl = graphgoogle.createVertex("url", post.getURI().toString());
                                       Vertex vtampung2 = graphgoogle.createVertex(vtampung.getType(), vtampung.getVertexId());
                                       vurl.getProperties().addProperty(new SimpleProperty("name.hasil_gmapapi_dbdevgis", post.getURI().toString()));
                                       vtampung2.getProperties().addProperty(new SimpleProperty("name.hasil_gmapapi_dbdevgis", vtampung.getVertexId()));
                                       graph.createEdge(vurl, vtampung2, "hasurl").getProperties().
                                               addProperty(new SimpleProperty("hasurl.posgre_dbdevgis_indonesiastreet", "hasurl"));
                                   } catch (Exception e) {
                                   }

                                   try {
                                       BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                                       StringBuilder sbuild = new StringBuilder();
                                       String line = null;
                                       while ((line = reader.readLine()) != null) {
                                           sbuild.append(line);
                                       }
                                       inputStream.close();
                                       json = sbuild.toString();
                                   } catch (Exception e) {
                                   }

                                   JSONArray jsonObject1 = null;
                                   JSONObject jsonObject2 = null;
                                   JSONArray jsonObject3 = null;
                                        try {
                                            //now parse
                                            JSONParser parser = new JSONParser();
                                            Object obj2 = parser.parse(json);
                                            JSONObject jb = (JSONObject) obj2;

                                            //now read
                                            jsonObject1 = (JSONArray) jb.get("results");
                                            jsonObject2 = (JSONObject) jsonObject1.get(0);
                                            jsonObject3 = (JSONArray) jsonObject2.get("address_components");
                                        }
                                        catch (IndexOutOfBoundsException oioi) {
                                            logger.info("error");
                                        }

                                   try {
                                       for (int ia = 0; ia < jsonObject3.size(); ia++) {
                                           JSONObject longname = (JSONObject) jsonObject3.get(ia);
                                           JSONArray type = (JSONArray) longname.get("types");
                                           switch (type.get(0).toString()) {
                                               case "administrative_area_level_4":
                                                   vdesa = graph.createVertex("desa", longname.get("long_name").toString().toLowerCase().trim());
                                                   vdesa.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_indonesiastreet", longname.get("long_name").toString().toLowerCase().trim().trim()));
                                                   graph.createEdge(vfulladdress, vdesa, "hasdesa").getProperties().
                                                           addProperty(new SimpleProperty("hasdesa.posgre_dbdevgis_indonesiastreet", "hasdesa"));
                                                   graph.createEdge(vdesa, vfulladdress, "desaof").getProperties().
                                                           addProperty(new SimpleProperty("desaof.posgre_dbdevgis_indonesiastreet", "desaof"));
                                                   //cari vertex.location.INA
                                                   break;
                                               case "administrative_area_level_3":
//                               logger.info("kec"+longname.get("long_name"));
                                                   vkec = graph.createVertex("kecamatan", longname.get("long_name").toString().toLowerCase().trim());
                                                   vkec.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_indonesiastreet", longname.get("long_name").toString().toLowerCase().trim().trim()));
                                                   graph.createEdge(vfulladdress, vkec, "haskecamatan").getProperties().
                                                           addProperty(new SimpleProperty("haskecamatan.posgre_dbdevgis_indonesiastreet", "haskecamatan"));
                                                   graph.createEdge(vkec, vfulladdress, "kecamatanof").getProperties().
                                                           addProperty(new SimpleProperty("kecamatanof.posgre_dbdevgis_indonesiastreet", "kecamatanof"));
                                                   break;
                                               case "administrative_area_level_2":
//                               logger.info("kab"+longname.get("long_name"));
                                                   vkab = graph.createVertex("kabupaten", longname.get("long_name").toString().toLowerCase().trim());
                                                   vkab.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_indonesiastreet", longname.get("long_name").toString().toLowerCase().trim().trim()));
                                                   graph.createEdge(vfulladdress, vkab, "haskabupaten").getProperties().
                                                           addProperty(new SimpleProperty("haskabupaten.posgre_dbdevgis_indonesiastreet", "haskabupaten"));
                                                   graph.createEdge(vkab, vfulladdress, "kabupatenof").getProperties().
                                                           addProperty(new SimpleProperty("kabupatenof.posgre_dbdevgis_indonesiastreet", "kabupatenof"));
                                                   break;
                                               case "administrative_area_level_1":
//                               logger.info("prov"+longname.get("long_name"));
                                                   vprov = graph.createVertex("provinsi", longname.get("long_name").toString().toLowerCase().trim());
                                                   vprov.getProperties().addProperty(new SimpleProperty("name.posgre_dbdevgis_indonesiastreet", longname.get("long_name").toString().toLowerCase().trim().trim()));
                                                   graph.createEdge(vfulladdress, vprov, "hasprovinsi").getProperties().
                                                           addProperty(new SimpleProperty("hasprovinsi.posgre_dbdevgis_indonesiastreet", "hasprovinsi"));
                                                   graph.createEdge(vprov, vfulladdress, "provinsiof").getProperties().
                                                           addProperty(new SimpleProperty("provinsiof.posgre_dbdevgis_indonesiastreet", "provinsiof"));
                                                   break;
                                               case "country":
//                               logger.info("negara"+longname.get("long_name"));
                                                   break;
                                               case "postal_code":
//                               logger.info("kode pos"+longname.get("long_name"));
                                                   break;
                                               default:
//                               System.out.println("error");
                                                   break;
                                           }

//                       ==========================================================================================
                                       }
                                   } catch (Exception we) {
                                      logger.info("ada error we");
                                   }
                               } catch (Exception qw) {
                                   logger.info("ada error qw");
                               }

                           }

                       }
                   } catch (NullPointerException po) {
                       logger.info("ada error po");
                   }
               }
               j++;
           }

    }

    public static AccumuloLegacyGraph getGraph (String table) {
        AccumuloLegacyStorage2.Builder builder = AccumuloLegacyStorage2.Builder.RajaampatBuilder();
        builder.setTablename(table);
        builder.setUserAuth("riska");
        builder.setPassword("12345678");
        AccumuloLegacyGraph graph = new AccumuloLegacyGraph("match", new AccumuloLegacyStorage2(builder));
        return graph;
    }

}
