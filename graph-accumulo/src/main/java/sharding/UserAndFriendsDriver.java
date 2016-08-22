package sharding;

import org.apache.accumulo.core.client.*;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.hadoop.io.Text;

import java.util.Iterator;
import java.util.Map;

public class UserAndFriendsDriver {
    
    private void process() throws AccumuloSecurityException, AccumuloException, TableExistsException, TableNotFoundException {
        String instanceName = "accumulo";
        String zooServers = "tanjungbenoa:2181, derawan:2181, giliair:2181, rajaampat:2181, wakatobi:2181";
        String user = "riska";
        String password = "12345678";
        String tableName = "sharding1";

        Instance mock = new ZooKeeperInstance(instanceName, zooServers);
        Connector connector = mock.getConnector(user, new PasswordToken(password));
        connector.tableOperations().create(tableName);
        BatchWriterConfig config = new BatchWriterConfig();
        config.setMaxMemory(10000);

        BatchWriter wr = connector.createBatchWriter (tableName, config);
        Mutation m = new Mutation(new Text("john"));
        m.put("info:name", "", "john henry");
        m.put("info:gender", "", "male");
        m.put("friend:old", "mark", "");
        wr.addMutation(m);
        m = new Mutation(new Text("mary"));
        m.put("info:name", "", "mark wiggins");
        m.put("info:gender", "", "female");
        m.put("friend:new", "mark", "");
        m.put("friend:old", "lucas", "");
        m.put("friend:old", "aaron", "");
        wr.addMutation(m);
        wr.flush();
        wr.close();

        Scanner scanner = connector.createScanner(tableName, new Authorizations());
        scanner.setRange(new Range("a", "z"));
        scanner.fetchColumnFamily(new Text("friend:old"));
        Iterator<Map.Entry<Key, Value>> iterator = scanner.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Key, Value> entry = iterator.next();
            Key key = entry.getKey();
            System.out.println("Old Friends: " + key.getRow() + " -> " + key.getColumnQualifier());
        }
    }

    public static void main(String[] args) throws AccumuloException, AccumuloSecurityException, TableExistsException, TableNotFoundException {
        UserAndFriendsDriver driver = new UserAndFriendsDriver();
        driver.process();
    }
}
