package matchJalan;

import com.google.common.base.Stopwatch;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1224A on 9/8/2016.
 */
public class kodepos {
    public static final Stopwatch timer = Stopwatch.createStarted();

    public static void main(String[] args) throws IOException {
        String csvFile = "D:/mediatrac/DATA/kodepos.csv";
        String line = "";
        String cvsSplitBy = ",";
        File readto = new File("D:/mediatrac/DATA/kodepos3.csv");
        FileWriter fw = new FileWriter(readto.getAbsoluteFile());

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            try (BufferedWriter op = new BufferedWriter(fw)) {
                while ((line = br.readLine()) != null) {
                String[] sentence = line.split(cvsSplitBy);
                for (String eachsentence : sentence){
                        System.out.print(eachsentence+" == ");
                        op.write(eachsentence+cvsSplitBy);
                        //this is for hasil search
                    String sql = "select distinct(kodepos) from tb_kodepos_all_v1 where kodepos ="+eachsentence;
                    ResultSet rs = getResultSQL(sql);
                    while (rs.next()) {
                        if (rs.getString(1).equals(""))
                            op.write("tidak");
                        else {
                            System.out.println(rs.getString(1)+" --- ");
                            op.write("ada");
                        }
                    }
                }
                    op.write("\n");
                    System.out.println();
            }
                br.close();
            op.close();
            } catch (Exception op) {
                op.getMessage();
            }
        } catch (Exception e) {
            e.getMessage();
        }

        System.out.println("Lama waktu "+timer.elapsedTime(TimeUnit.MILLISECONDS)+ " ms");
    }

    public static ResultSet getResultSQL(String sql) {
        String dbUrl = "jdbc:mysql://192.168.10.11:3139/db_sdn_temp_habibi";
        String username = "ninggar";
        String password = "muninggar123***";
        Connection conn = connectSql(dbUrl,username,password);
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException ae) {
            // TODO Auto-generated catch block

            return null;
        }
    }

    public static Connection connectSql(String dbUrl, String username, String password){
        Connection connection = null;
        try {
            Properties props = new Properties();
            props.setProperty("user",username);
            props.setProperty("password", password);
            connection = DriverManager.getConnection(dbUrl, props);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
