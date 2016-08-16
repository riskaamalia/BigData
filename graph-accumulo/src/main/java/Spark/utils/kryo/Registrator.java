package Spark.utils.kryo;

import com.esotericsoftware.kryo.Kryo;
import org.apache.spark.serializer.KryoRegistrator;

/**
 * Created by Sami on 7:46 AM - 15-Sep-15.
 */
public class Registrator implements KryoRegistrator {
    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(org.apache.accumulo.core.data.Key.class);
        kryo.register(org.apache.accumulo.core.data.Value.class);
        kryo.register(org.apache.accumulo.core.data.Mutation.class);
    }
}
