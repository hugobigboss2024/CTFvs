import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Sm4 {
  private static final byte[] SBOX = new byte[] { 
      -42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 
      20, -62, 40, -5, 44, 5, 43, 103, -102, 118, 
      42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 
      6, -103, -100, 66, 80, -12, -111, -17, -104, 122, 
      51, 84, 11, 67, -19, -49, -84, 98, -28, -77, 
      28, -87, -55, 8, -24, -107, Byte.MIN_VALUE, -33, -108, -6, 
      117, -113, 63, -90, 71, 7, -89, -4, -13, 115, 
      23, -70, -125, 89, 60, 25, -26, -123, 79, -88, 
      104, 107, -127, -78, 113, 100, -38, -117, -8, -21, 
      15, 75, 112, 86, -99, 53, 30, 36, 14, 94, 
      99, 88, -47, -94, 37, 34, 124, 59, 1, 33, 
      120, -121, -44, 0, 70, 87, -97, -45, 39, 82, 
      76, 54, 2, -25, -96, -60, -56, -98, -22, -65, 
      -118, -46, 64, -57, 56, -75, -93, -9, -14, -50, 
      -7, 97, 21, -95, -32, -82, 93, -92, -101, 52, 
      26, 85, -83, -109, 50, 48, -11, -116, -79, -29, 
      29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 
      35, -85, 13, 83, 78, 111, -43, -37, 55, 69, 
      -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 
      91, 81, -115, 27, -81, -110, -69, -35, -68, Byte.MAX_VALUE, 
      17, -39, 92, 65, 31, 16, 90, -40, 10, -63, 
      49, -120, -91, -51, 123, -67, 45, 116, -48, 18, 
      -72, -27, -76, -80, -119, 105, -105, 74, 12, -106, 
      119, 126, 101, -71, -15, 9, -59, 110, -58, -124, 
      24, -16, 125, -20, 58, -36, 77, 32, 121, -18, 
      95, 62, -41, -53, 57, 72 };
  
  private static final int[] FK = new int[] { -1548633402, 1453994832, 1736282519, -1301273892 };
  
  private static final int[] CK = new int[] { 
      462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 
      404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 
      808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 
      1213159005, 1684763257 };
  
  private static final byte[] SBOX_P = new byte[256];
  
  static {
    for (byte b = 0; b < 'A'; b++) {
      int i = (b ^ 0xA7) & 0xFF;
      int j = SBOX[i] & 0xFF;
      int k = b & 0x3;
      int m = rotl8(j, k);
      SBOX_P[b] = (byte)m;
    } 
  }
  
  public static byte[] deriveKeyFromSeed(String paramString) {
    byte[] arrayOfByte1 = paramString.getBytes(StandardCharsets.UTF_8);
    byte[] arrayOfByte2 = new byte[16];
    for (byte b = 0; b < 16; b++) {
      int i = arrayOfByte1[b % arrayOfByte1.length] & 0xFF;
      int j = i + b * 17 + 35 & 0xFF;
      arrayOfByte2[b] = (byte)j;
    } 
    return arrayOfByte2;
  }
  
  public static byte[] encrypt(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    int[] arrayOfInt = expandKey(paramArrayOfbyte1);
    byte[] arrayOfByte1 = pkcs7Pad(paramArrayOfbyte2);
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
    for (byte b = 0; b < arrayOfByte1.length; b += 16)
      encryptBlock(arrayOfByte1, b, arrayOfByte2, b, arrayOfInt); 
    return arrayOfByte2;
  }
  
  public static String toHex(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder(paramArrayOfbyte.length * 2);
    for (byte b : paramArrayOfbyte) {
      String str = Integer.toHexString(b & 0xFF);
      if (str.length() == 1)
        stringBuilder.append('0'); 
      stringBuilder.append(str);
    } 
    return stringBuilder.toString();
  }
  
  private static byte[] pkcs7Pad(byte[] paramArrayOfbyte) {
    int i = 16 - paramArrayOfbyte.length % 16;
    if (i == 0)
      i = 16; 
    byte[] arrayOfByte = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length + i);
    Arrays.fill(arrayOfByte, paramArrayOfbyte.length, arrayOfByte.length, (byte)i);
    return arrayOfByte;
  }
  
  private static int[] expandKey(byte[] paramArrayOfbyte) {
    int[] arrayOfInt1 = new int[4];
    arrayOfInt1[0] = bytesToInt(paramArrayOfbyte, 0);
    arrayOfInt1[1] = bytesToInt(paramArrayOfbyte, 4);
    arrayOfInt1[2] = bytesToInt(paramArrayOfbyte, 8);
    arrayOfInt1[3] = bytesToInt(paramArrayOfbyte, 12);
    int[] arrayOfInt2 = new int[36];
    for (byte b1 = 0; b1 < 4; b1++)
      arrayOfInt2[b1] = arrayOfInt1[b1] ^ FK[b1]; 
    int[] arrayOfInt3 = new int[32];
    for (byte b2 = 0; b2 < 32; b2++) {
      int i = arrayOfInt2[b2 + 1] ^ arrayOfInt2[b2 + 2] ^ arrayOfInt2[b2 + 3] ^ CK[b2];
      i = TPrime(i);
      arrayOfInt2[b2 + 4] = arrayOfInt2[b2] ^ i;
      arrayOfInt3[b2] = arrayOfInt2[b2 + 4];
    } 
    return arrayOfInt3;
  }
  
  private static void encryptBlock(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int[] paramArrayOfint) {
    int[] arrayOfInt = new int[36];
    arrayOfInt[0] = bytesToInt(paramArrayOfbyte1, paramInt1);
    arrayOfInt[1] = bytesToInt(paramArrayOfbyte1, paramInt1 + 4);
    arrayOfInt[2] = bytesToInt(paramArrayOfbyte1, paramInt1 + 8);
    arrayOfInt[3] = bytesToInt(paramArrayOfbyte1, paramInt1 + 12);
    int i;
    for (i = 0; i < 32; i++) {
      int n = arrayOfInt[i + 1] ^ arrayOfInt[i + 2] ^ arrayOfInt[i + 3] ^ paramArrayOfint[i];
      n = T(n);
      arrayOfInt[i + 4] = arrayOfInt[i] ^ n;
    } 
    i = arrayOfInt[35];
    int j = arrayOfInt[34];
    int k = arrayOfInt[33];
    int m = arrayOfInt[32];
    intToBytes(i, paramArrayOfbyte2, paramInt2);
    intToBytes(j, paramArrayOfbyte2, paramInt2 + 4);
    intToBytes(k, paramArrayOfbyte2, paramInt2 + 8);
    intToBytes(m, paramArrayOfbyte2, paramInt2 + 12);
  }
  
  private static int T(int paramInt) {
    int i = tau(paramInt);
    return i ^ rotl(i, 2) ^ rotl(i, 10) ^ rotl(i, 18) ^ rotl(i, 24);
  }
  
  private static int TPrime(int paramInt) {
    int i = tau(paramInt);
    return i ^ rotl(i, 13) ^ rotl(i, 23);
  }
  
  private static int tau(int paramInt) {
    int i = paramInt >>> 24 & 0xFF;
    int j = paramInt >>> 16 & 0xFF;
    int k = paramInt >>> 8 & 0xFF;
    int m = paramInt & 0xFF;
    i = sboxTransform(i);
    j = sboxTransform(j);
    k = sboxTransform(k);
    m = sboxTransform(m);
    return i << 24 | j << 16 | k << 8 | m;
  }
  
  private static int sboxTransform(int paramInt) {
    int i = (paramInt ^ 0x3C) & 0xFF;
    return SBOX_P[i] & 0xFF;
  }
  
  private static int rotl(int paramInt1, int paramInt2) {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }
  
  private static int rotl8(int paramInt1, int paramInt2) {
    paramInt2 &= 0x7;
    return (paramInt1 << paramInt2 | paramInt1 >>> 8 - paramInt2) & 0xFF;
  }
  
  private static int bytesToInt(byte[] paramArrayOfbyte, int paramInt) {
    return (paramArrayOfbyte[paramInt] & 0xFF) << 24 | (paramArrayOfbyte[paramInt + 1] & 0xFF) << 16 | (paramArrayOfbyte[paramInt + 2] & 0xFF) << 8 | paramArrayOfbyte[paramInt + 3] & 0xFF;
  }
  
  private static void intToBytes(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    paramArrayOfbyte[paramInt2] = (byte)(paramInt1 >>> 24);
    paramArrayOfbyte[paramInt2 + 1] = (byte)(paramInt1 >>> 16);
    paramArrayOfbyte[paramInt2 + 2] = (byte)(paramInt1 >>> 8);
    paramArrayOfbyte[paramInt2 + 3] = (byte)paramInt1;
  }
  public static void main(String[] args) {
      String KEY_SEED = "happ";
      byte[] KEY = Sm4.deriveKeyFromSeed("happ");
      Sm4 KEY2 = new Sm4();
      System.out.println(KEY_SEED);
      System.out.println(KEY);
  }
}
  