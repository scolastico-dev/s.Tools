package me.scolastico.tools.simplified.rsa;

/**
 * Validate RSA PEM keys.
 */
public class RSAValidator {

  /**
   * Check if the given string is an RSA public key.
   * @param pem The PEM string to check.
   * @return True if it is an RSA PEM public key.
   */
  public static boolean isPublicKey(String pem) {
    boolean isKey = pem.startsWith("-----BEGIN PUBLIC KEY-----");
    if (!pem.endsWith("-----END PUBLIC KEY-----")) isKey = false;
    return isKey;
  }

  /**
   * Check if the given string is an RSA private key.
   * @param pem The PEM string to check.
   * @return True if it is an RSA PEM private key.
   */
  public static boolean isPrivateKey(String pem) {
    boolean isKey = pem.startsWith("-----BEGIN RSA PRIVATE KEY-----");
    if (!pem.endsWith("-----END RSA PRIVATE KEY-----")) isKey = false;
    return isKey;
  }

}
