package BackEndSearch;

        import com.msk.graph.Vertex;

import java.util.List;

/**
 * Created by 1224A on 5/30/2016.
 */
public class Tes {

    public static void main(String[] args) throws Exception {
        mainApp obj = new mainApp();
        Vertex hasilnya = obj.theGraph ("agung","master_data_administrasi");
        System.out.println(hasilnya.getId()+"=====");
        List<Vertex> hasil2 = obj.findType(hasilnya);
        for (Vertex hh:hasil2) {
            System.out.println(hh.getId()+"-.-.-.-.-");
        }

    }
}
