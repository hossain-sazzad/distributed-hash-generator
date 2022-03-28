import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Client {
    public static void main(String[] args){
        try {
            String roolno = "101";
            URL url = new URL("http://localhost:8080/api/" + roolno);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(roolno+output));
            String hash = String.format("%032x", new BigInteger(1, md5.digest()));

            String s = "00"+ roolno.substring(roolno.length()-3, roolno.length());
            if(hash)
            conn.disconnect();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
