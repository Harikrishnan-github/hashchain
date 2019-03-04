import java.lang.*;
import java.security.MessageDigest;
import java.io.*;
import org.apache.commons.io.*;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.GsonBuilder;
import static java.lang.System.out;

class StringUtil {

    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexadecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

    }
}

class Firm {

    public String hash;
    public String previousHash;
    private String data;
    public Firm(String data,String previousHash ) {
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256( previousHash + data );
        return calculatedhash;
    }
}
public class FirmHash {
    public static List<Firm> hashchain = new ArrayList<Firm>();
    public static void main(String[] args) throws IndexOutOfBoundsException {
        try{
            File file1 = new File("E://FirmHash//input1.txt");
            File file2 = new File("E://FirmHash//input2.txt");
            File file3 = new File("E://FirmHash//input3.txt");
            String str1 = FileUtils.readFileToString(file1, "UTF-8");
            String str2 = FileUtils.readFileToString(file2, "UTF-8");
            String str3 = FileUtils.readFileToString(file3, "UTF-8");
            Firm obj1 = new Firm(str1, "0");
            obj1.calculateHash();
            hashchain.add(obj1);
            Firm obj2 = new Firm(str2, hashchain.get(0).hash);
            obj2.calculateHash();
            hashchain.add(obj2);
            Firm obj3 = new Firm(str3, hashchain.get(1).hash);
            obj3.calculateHash();
            hashchain.add(obj3);
        }
        catch(IOException e){
            System.err.println("Exception: "+e.getMessage());
            e.printStackTrace();
        }
        catch(IndexOutOfBoundsException ignored){
        }
        String hashchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(hashchain);
        out.println("\nThe hash chain: ");
        out.println(hashchainJson);
    }
}
