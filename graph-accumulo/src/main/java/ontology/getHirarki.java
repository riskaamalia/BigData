package ontology;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1224A on 6/20/2016.
 */
public class getHirarki {

    public Map getDepthNama (int depth, String nama) {
        Map hasil = new HashMap();
        hasil.put(nama,depth);
        return hasil;
    }
}
