package me.scolastico.tools.simplified.rsa;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Encrypt and decrypt bytes or a string.
 */
public class RSACrypt {

  /**
   * Encrypt a string with an RSA public key.
   * @param publicKey The public key which is used to encrypt the message.
   * @param message The message to encrypt.
   * @return The encrypted base 64 string.
   * @throws NoSuchPaddingException Unknown error.
   * @throws IllegalBlockSizeException If the key isn't long enough to encrypt this message.
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   * @throws BadPaddingException Unknown error.
   * @throws InvalidKeyException If the key can't be used for this process.
   */
  public static String encrypt(PublicKey publicKey, String message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    return encrypt(publicKey,message.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Encrypt a byte array with an RSA public key.
   * @param publicKey The public key which is used to encrypt the bytes.
   * @param bytes The bytes to encrypt.
   * @return The encrypted base 64 string.
   * @throws NoSuchPaddingException Unknown error.
   * @throws IllegalBlockSizeException If the key isn't long enough to encrypt this message.
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   * @throws BadPaddingException Unknown error.
   * @throws InvalidKeyException If the key can't be used for this process.
   */
  public static String encrypt(PublicKey publicKey, byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher encryptCipher = Cipher.getInstance("RSA");
    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedMessageBytes = encryptCipher.doFinal(bytes);
    return Base64.getEncoder().encodeToString(encryptedMessageBytes);
  }

  /**
   * Decrypt a base 64 encrypted message with an RSA private key.
   * @param privateKey The private key which is used to decrypt the message.
   * @param message The base 64 encrypted message to encrypt.
   * @return The encrypted message string.
   * @throws NoSuchPaddingException Unknown error.
   * @throws IllegalBlockSizeException If the key isn't long enough to encrypt this message.
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   * @throws BadPaddingException Unknown error.
   * @throws InvalidKeyException If the key can't be used for this process.
   */
  public static String decryptToString(PrivateKey privateKey, String message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    byte[] bytes = decryptToBytes(privateKey, message);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  /**
   * Decrypt a base 64 encrypted message with an RSA private key.
   * @param privateKey The private key which is used to decrypt the message.
   * @param message The base 64 encrypted message to encrypt.
   * @return The encrypted message bytes.
   * @throws NoSuchPaddingException Unknown error.
   * @throws IllegalBlockSizeException If the key isn't long enough to encrypt this message.
   * @throws NoSuchAlgorithmException If the algorithm can't be found on the system.
   * @throws BadPaddingException Unknown error.
   * @throws InvalidKeyException If the key can't be used for this process.
   */
  public static byte[] decryptToBytes(PrivateKey privateKey, String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher encryptCipher = Cipher.getInstance("RSA");
    encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] bytes = Base64.getDecoder().decode(message);
    return encryptCipher.doFinal(bytes);
  }

}
