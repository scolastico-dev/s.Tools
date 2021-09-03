package me.scolastico.tools.simplified.rsa;

import com.google.common.base.Splitter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Several conversion functions for RSA and PEM keys
 */
public class RSAConverter {

  private static boolean firstRun = true;

  /**
   * Convert a public RSA key to a PEM key.
   * @param publicKey The public key to convert.
   * @return The PEM key string.
   */
  public static String publicToPEM(PublicKey publicKey) {
    StringBuilder ret = new StringBuilder("-----BEGIN PUBLIC KEY-----\n");
    for (String line: Splitter.fixedLength(64).split(Base64.getEncoder().encodeToString(publicKey.getEncoded()))) {
      ret.append(line).append("\n");
    }
    ret.append("-----END PUBLIC KEY-----");
    return ret.toString();
  }

  /**
   * Convert a private RSA key to a PEM key.
   * @param privateKey The private key to convert.
   * @return The PEM key string.
   */
  public static String privateToPEM(PrivateKey privateKey) {
    StringBuilder ret = new StringBuilder("-----BEGIN RSA PRIVATE KEY-----\n");
    for (String line: Splitter.fixedLength(64).split(Base64.getEncoder().encodeToString(privateKey.getEncoded()))) {
      ret.append(line).append("\n");
    }
    ret.append("-----END RSA PRIVATE KEY-----");
    return ret.toString();
  }

  /**
   * Convert a PEM key to a byte array. They key isn't checked before conversion!
   * @param pem The PEM key to convert to a byte array.
   * @return The byte array of the PEM key.
   */
  public static byte[] pemToBytes(String pem) {
    pem = pem.replace("\r", "").replace("\n", "");
    pem = pem.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "");
    pem = pem.replaceAll("-----END RSA PRIVATE KEY-----", "");
    pem = pem.replaceAll("-----BEGIN PUBLIC KEY-----", "");
    pem = pem.replaceAll("-----END PUBLIC KEY-----", "");
    return Base64.getDecoder().decode(pem);
  }

  /**
   * Convert bytes to an RSA public key.
   * @param bytes The bytes to convert.
   * @return The RSA public Key.
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   * @throws InvalidKeySpecException If the bytes can't be parsed to an RSA key.
   */
  public static PublicKey bytesToPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
    return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
  }

  /**
   * Convert bytes to an RSA private key.
   * @param bytes The bytes to convert.
   * @return The RSA private Key.
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   * @throws InvalidKeySpecException If the bytes can't be parsed to an RSA key.
   */
  public static PrivateKey bytesToPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
    return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
  }

  /**
   * Convert a private RSA key to a KeyPair with the public key.
   * @param privateKey The private RSA key.
   * @return The KeyPair with the public RSA key.
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   * @throws InvalidKeySpecException If the private key can't be parsed to a public RSA key.
   */
  public static KeyPair privateKeyToKeyPair(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    RSAPrivateCrtKey privateKeyCrt = (RSAPrivateCrtKey) privateKey;
    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privateKeyCrt.getModulus(), privateKeyCrt.getPublicExponent());
    PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
    return new KeyPair(publicKey, privateKey);
  }

  /**
   * Convert a private RSA key to
   * @param key The private RSA key to convert.
   * @return The bytes of the key.
   */
  public static byte[] getBytes(PrivateKey key) {
    return key.getEncoded();
  }

  /**
   * Convert a public RSA key to
   * @param key The public RSA key to convert.
   * @return The bytes of the key.
   */
  public static byte[] getBytes(PublicKey key) {
    return key.getEncoded();
  }

}
