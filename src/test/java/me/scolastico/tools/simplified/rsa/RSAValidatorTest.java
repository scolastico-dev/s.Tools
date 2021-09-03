package me.scolastico.tools.simplified.rsa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RSAValidatorTest {

  @Test
  void isPublicKey() {
    Assertions.assertTrue(
        RSAValidator.isPublicKey(
            "-----BEGIN PUBLIC KEY-----\n"
                + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCH49QfQWdFzJV1B8ACCUwXx5Vq\n"
                + "29LgRX5VfpsdRQDfzjYDoN+U+JGNe4JQ6+4Sz/jP4c2wY+xRWValcAZTeMXP5Ggy\n"
                + "A3DRIBkSQTfVJ3uogsxa5MUy83EsfFTQTX2Abg8YQo4y+SX1LTqUO1CakjylFRr1\n"
                + "O22FzUkxQyVDLnBDVQIDAQAB\n"
                + "-----END PUBLIC KEY-----"));
    Assertions.assertFalse(RSAValidator.isPublicKey("something else"));
  }

  @Test
  void isPrivateKey() {
    Assertions.assertTrue(
        RSAValidator.isPrivateKey(
            "-----BEGIN RSA PRIVATE KEY-----\n"
                + "MIICWwIBAAKBgQCH49QfQWdFzJV1B8ACCUwXx5Vq29LgRX5VfpsdRQDfzjYDoN+U\n"
                + "+JGNe4JQ6+4Sz/jP4c2wY+xRWValcAZTeMXP5GgyA3DRIBkSQTfVJ3uogsxa5MUy\n"
                + "83EsfFTQTX2Abg8YQo4y+SX1LTqUO1CakjylFRr1O22FzUkxQyVDLnBDVQIDAQAB\n"
                + "AoGADvfunr7ClHKzp9+Bq/fANQJcGBg8w02EXACFL5UxQnXhh0bKOgujZNVNBoLm\n"
                + "h5X3D1cNJDftiQxxOen8GGe+Sj3l1Ii/oUmYV/k8FyqTWfzWj+nR2DUAjUgaawpc\n"
                + "H8InrVVGJD+4Ucys8NzN+VHrGNMN6MfmglF4tmVcDr2ymMECQQDJUDGP7dLdSgU3\n"
                + "Z65xisJVXmg9Cxz1yPnczzzaENRbIuwvUVlRL8Ud+NLn0G7hktgjy7Ts2nWAYLlE\n"
                + "3YqFnEYFAkEArM3xHHJcOitNhe1/37t5MjKFvOgCGiQ70eyEWhlL/14YHJ5H+7Pk\n"
                + "EJQCBJFvrK0/3phYG9Fu/A2+dCPCYn65EQJAQqXTEsR4uBxmZ7ZisWJv/hVYS0Vf\n"
                + "nBuiZTETA1rpAxcnvG6hNFitWMPlBywhWXrlJcyuJHAa0Jq1G5CA6MUZBQJALXXM\n"
                + "PXiUbLUhot8VKe2u95a0+jpq+yeXGSXIt8854AeH/ldhhoUPtZDKaxs7rsixKcUY\n"
                + "QNE7ClIvTWxu4eyGkQJAM6olvPV4S6XdS1Su7i7KHFTBcPcL/9xKLU/WD/C0SdSQ\n"
                + "skyRC0LTNiX45p3S2bTaxykMtowm9ODfKLNwLPRVmw==\n"
                + "-----END RSA PRIVATE KEY-----"));
    Assertions.assertFalse(RSAValidator.isPrivateKey("something else"));
  }

}