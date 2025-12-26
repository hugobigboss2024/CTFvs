import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
  private static final String KEY_SEED = "happ";
  
  private static final byte[] KEY = Sm4.deriveKeyFromSeed("happ");
  
  private static final String TARGET_CIPHER_HEX = "21c2692a4775c413356a31fc55c38f6218bed9d46c45bd0eb777be9334c999d7";
  
  public static void main(String[] paramArrayOfString) throws Exception {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Input flag: ");
    String str1 = bufferedReader.readLine();
    if (str1 == null)
      return; 
    if (!str1.startsWith("flag{") || !str1.endsWith("}")) {
      System.out.println("Wrong");
      return;
    } 
    byte[] arrayOfByte = Sm4.encrypt(KEY, str1.getBytes(StandardCharsets.UTF_8));
    String str2 = Sm4.toHex(arrayOfByte);
    if (str2.equals("21c2692a4775c413356a31fc55c38f6218bed9d46c45bd0eb777be9334c999d7")) {
      System.out.println("Correct");
    } else {
      System.out.println("Wrong");
    } 
  }
}
