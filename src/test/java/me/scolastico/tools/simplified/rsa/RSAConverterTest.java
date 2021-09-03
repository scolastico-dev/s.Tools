package me.scolastico.tools.simplified.rsa;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class RSAConverterTest {

  final String publicKeyPEM =
      "-----BEGIN PUBLIC KEY-----\n"
          + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAunTSQCZTg777sP76i5yE\n"
          + "0aIUtqal6ZqV9quxyOnrrd+X/IEzrCHiujCHKA6Dxa6cng/Fscv/7N3k7FHy+V7Q\n"
          + "BS+WgkHMPgKCUzm1E/pAnjv19m5YoRTOaZHnRoIDma1kj0/hsHBSnlmyEo4/RuBd\n"
          + "Ssof5piPVFi0jIXcbt7dP/MaW2Yje9kSC1evtrv01OivpbhHuZuD0gNzAwXyz/D2\n"
          + "5WvpeUKowvjYl/NFYyy9E7QosAAzwBWfWt01TJSquH+FHCdh4I4T9yVcHn6koqBL\n"
          + "Df0K5b2KqeYCVGN3O1jzSfj2I1qd4wFfEykMAwIToHTOK7cUUyab5d9AGbcHf2AA\n"
          + "rwIDAQAB\n"
          + "-----END PUBLIC KEY-----";

  final String privateKeyPEM =
      "-----BEGIN RSA PRIVATE KEY-----\n"
          + "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC6dNJAJlODvvuw\n"
          + "/vqLnITRohS2pqXpmpX2q7HI6eut35f8gTOsIeK6MIcoDoPFrpyeD8Wxy//s3eTs\n"
          + "UfL5XtAFL5aCQcw+AoJTObUT+kCeO/X2blihFM5pkedGggOZrWSPT+GwcFKeWbIS\n"
          + "jj9G4F1Kyh/mmI9UWLSMhdxu3t0/8xpbZiN72RILV6+2u/TU6K+luEe5m4PSA3MD\n"
          + "BfLP8Pbla+l5QqjC+NiX80VjLL0TtCiwADPAFZ9a3TVMlKq4f4UcJ2HgjhP3JVwe\n"
          + "fqSioEsN/QrlvYqp5gJUY3c7WPNJ+PYjWp3jAV8TKQwDAhOgdM4rtxRTJpvl30AZ\n"
          + "twd/YACvAgMBAAECggEAZc9uEnsa9QAunInNsrfnSjRPTSCXzYOMSSGdNRCNkOZb\n"
          + "LhQ6a84g5/uhgTzTtPhQKnqAP0Id32+WFXf1zTZ5Lau7aiqVZHKoSNtkxaDK8dvZ\n"
          + "xRKhsGsU/k4d08EgSCSGmT5MBLSAWFivxDQJ1yChsDnHy3kYIj1BcM2S/LMSalzb\n"
          + "Bmr+1SfVGfW6nw1nYhCwrICjVAc55tPhPWfw/NSETT+sI//kSpuQe1tG7oQmIJA6\n"
          + "IyG25pMAB60dq2mkPBJIPFyzvqSPaNeieXHzj4smOmoZyjWklA3AZNfZlf/3Y1OR\n"
          + "lTy8gBBjbjVvNvE3uL6CaQB6TELWl5PINQS/Xwdb6QKBgQDxjA0k86d3og3dDY5d\n"
          + "OJ4O1iW561AgnJRFJ3zBS5odiDaS1MLXij8k1QucqxmvF+XQfvt9AqCD8ZZ5Y0cA\n"
          + "T+NeBm1uUNQqtDE8cSU6XgoLhCXagU2J/dDHkQgWGvjJZZFVPLKVwloGGZJokmPJ\n"
          + "9twQWpugjeYXHrvahdEaZoQE0wKBgQDFnOfiGCybotXE6350wPCp1Jec5x8ZQIKO\n"
          + "8obsP3y0y50aH5wCKiAdqP7V3k3PxqLdmc08UH2uIa3/RnVZus+de79m4PhbTEZV\n"
          + "VmYFJu3euLehyu8/2ZJh+BJiwF/WxQruBU7rFZvl27qyqRvrLMZeFnM98JTzA/jI\n"
          + "bkxEIdxbNQKBgQDGX43f/hdYQgf+toZn+vqcFrWPFjmNPurqTH7BxNRCUr73IbMP\n"
          + "D2PCXRGXTlHeodLEw7TIuyKWmg9JjAYgtHt3mfG/erR0id4x1H6Ni/PV92abKDxF\n"
          + "CuWvMGYiuW3/AOI1zY6hPadvgQrjxO4dTRb4e2F6n28LJ9uBoQrVEsJzrwKBgQCv\n"
          + "RQaKCyTteZSejrxfM42BI/TBDmEP1Z62yLcQrRKfjISv2d9WMCTCCdTUvdJH+gcU\n"
          + "A0j8OOvv2SFpbAXDaySfqlVOGzVMTK+JGxNreGa8BxugHczr1b62s/8ipTub0rJq\n"
          + "AD1J1qWqTqQduE3x3Ymng8P86PhDPn+WzfVI7Lt0DQKBgQDppgCkfd2IXRzhQmVu\n"
          + "wCChCgouBzCR5DGuDRSYfOZCrZN7uS+TuqPu64Rv/5VbPlxJbQhWv+4MJPncOF1A\n"
          + "lGzvRHpVp0PL0VWlHltCfoe90Ra6TYhI7dSq6+0emYAtewcYqasZ7qz+ENADl46I\n"
          + "PVtcyCRblnrTsdAK3ZL+JHrZ/Q==\n"
          + "-----END RSA PRIVATE KEY-----";

  static byte[] privateKeyBytes, publicKeyBytes;
  static PrivateKey privateKey;
  static PublicKey publicKey;
  static KeyPair keyPair;

  @Test
  @Order(1)
  void pemToBytes() {
    privateKeyBytes = RSAConverter.pemToBytes(privateKeyPEM);
    publicKeyBytes = RSAConverter.pemToBytes(publicKeyPEM);
  }

  @Test
  @Order(2)
  void bytesToPublicKey() throws Exception {
    publicKey = RSAConverter.bytesToPublicKey(publicKeyBytes);
  }

  @Test
  @Order(3)
  void bytesToPrivateKey() throws Exception {
    privateKey = RSAConverter.bytesToPrivateKey(privateKeyBytes);
  }

  @Test
  @Order(4)
  void privateKeyToKeyPair() throws Exception {
    keyPair = RSAConverter.privateKeyToKeyPair(privateKey);
  }

  @Test
  @Order(5)
  void publicToPEM() {
    Assertions.assertEquals(publicKeyPEM, RSAConverter.publicToPEM(publicKey));
    Assertions.assertEquals(publicKeyPEM, RSAConverter.publicToPEM(keyPair.getPublic()));
  }

  @Test
  @Order(6)
  void privateToPEM() {
    Assertions.assertEquals(privateKeyPEM, RSAConverter.privateToPEM(privateKey));
    Assertions.assertEquals(privateKeyPEM, RSAConverter.privateToPEM(keyPair.getPrivate()));
  }

}