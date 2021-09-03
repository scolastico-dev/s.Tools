package me.scolastico.tools.simplified.rsa;

import java.security.KeyPair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RSACryptTest {

  @Test
  void testRSACrypt() throws Exception {
    KeyPair keys = RSAGenerator.generate();
    Assertions.assertEquals(
        "This is a Test!",
        RSACrypt.decryptToString(
            keys.getPrivate(),
            RSACrypt.encrypt(keys.getPublic(), "This is a Test!")
        )
    );
  }

}