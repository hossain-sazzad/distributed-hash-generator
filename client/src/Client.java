import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

public class Client {
    static int[] counts = {1,2,4,8};
    public static void main(String[] args){
        for(int i : counts){
            System.out.println("started running with " + i + " students");
            System.out.println("Please wait, it will take some time");

            File result = null;
            try {
                result = new File("result"+i + ".txt");
                FileWriter myWriter = new FileWriter(result);
                myWriter.write("Client count ====================== "+ i + System.lineSeparator());
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            Instant start = Instant.now();
            start();
            calculateHashForStudents(i, result);
            long seconds = Duration.between(start, Instant.now()).getSeconds();
            try {
                FileWriter myWriter = new FileWriter(result, true);
                myWriter.write("Time ====================== "+ seconds + " secomds" + System.lineSeparator());
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            System.out.println("End running with " + i + " students");
        }
        System.out.println("The end");
    }

    public static void start(){
        try {
            URL url = new URL("http://localhost:9090/api/" + "start");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void calculateHashForStudents(int studentCount, File resultFile){
        Thread[] generatorThreads = new Thread[studentCount];
        for(int i = 1; i<= studentCount; i++){
            Hashgenerator hashgenerator = new Hashgenerator(100+i, resultFile);
            generatorThreads[i-1] = new Thread(hashgenerator);
            generatorThreads[i-1].start();
        }
        for(int i = 1; i<= studentCount; i++){
            try {
                generatorThreads[i-1].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void registerAndFindHash(String roolno, File resultFile) {
        String output = "";
        try {
            URL url = new URL("http://localhost:9090/api/" + roolno);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                break;
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(output.equals("end")){
            return;
        }
        boolean isFound = false;
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
                isFound = true;
                sendSuccessRequest(roolno, hash, resultFile);
                break;
            }
        }
        if (!isFound){
            sendFailRequest(roolno, output);
        }
        registerAndFindHash(roolno, resultFile);
    }


    public static void sendSuccessRequest(String roolno, String hash, File resultFile){
        try {
            URL url = new URL("http://localhost:9090/api/success/" + roolno + "/" + hash);
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
        try {
            FileWriter myWriter = new FileWriter(resultFile, true);
            myWriter.write("client  "+ roolno + " " + hash + System.lineSeparator());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void sendFailRequest(String roolno, String blockNumber){
        try {
            URL url = new URL("http://localhost:9090/api/success/" + roolno + "/" + blockNumber);
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
    File resultFile;

    public Hashgenerator(int roolNo, File resultFile) {
        this.roolNo = roolNo;
        this.resultFile = resultFile;
    }

    @Override
    public void run() {
        Client.registerAndFindHash(Integer.toString(roolNo), resultFile);
    }
}
