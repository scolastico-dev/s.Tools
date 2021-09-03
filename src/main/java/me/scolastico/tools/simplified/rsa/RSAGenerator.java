package me.scolastico.tools.simplified.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * RSA KeyPair generator
 */
public class RSAGenerator {

  /**
   * Generate an RSA KeyPair with a length of 2048.
   * @return RSA KeyPair with a length of 2048
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   */
  public static KeyPair generate() throws NoSuchAlgorithmException {
    return generate(2048);
  }

  /**
   * Generate an RSA KeyPair
   * @param keyLength The key length in bytes. Should be one of 1024, 2048 or 4096.
   * @return RSA KeyPair
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   */
  public static KeyPair generate(Integer keyLength) throws NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(keyLength);
    return generator.generateKeyPair();
  }

}
