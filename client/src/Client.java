import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Client {
    public static void main(String[] args){
        calculateHashForStudents(4);
    }

    public static void calculateHashForStudents(int studentCount){
        for(int i = 1; i<= studentCount; i++){
            Hashgenerator hashgenerator = new Hashgenerator(100+i);
            Thread generatorThread = new Thread(hashgenerator);
            generatorThread.start();
        }
    }
    public static void registerAndFindHash(String roolno) {
        String output = "";
        try {
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

            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                break;
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(output.equals("end") || output.equals("")){
            return;
        }
        for(int i = 0; i < 4194304; i++) {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md5.update(StandardCharsets.UTF_8.encode(roolno+output+i));
            String hash = String.format("%032x", new BigInteger(1, md5.digest()));

            String s = "00"+ roolno.substring(roolno.length()-3, roolno.length());
            if(hash.substring(0,5).equals(s)){
                sendSuccessRequest(roolno, hash);
                registerAndFindHash(roolno);
            }
        }
        sendFailRequest(roolno, output);
    }


    public static void sendSuccessRequest(String roolno, String hash){
        try {
            URL url = new URL("http://localhost:8080/api/success/" + roolno + "/" + hash);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            conn.disconnect();
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void sendFailRequest(String roolno, String blockNumber){
        try {
            URL url = new URL("http://localhost:8080/api/success" + roolno + "/" + blockNumber);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            conn.disconnect();
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}

class Hashgenerator implements Runnable {
    int roolNo;

    public Hashgenerator(int roolNo) {
        this.roolNo = roolNo;
    }

    @Override
    public void run() {
        Client.registerAndFindHash(Integer.toString(roolNo));
    }
}
