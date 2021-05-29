package me.scolastico.tools.simplified;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimplifiedResourceFileReaderTest {

  SimplifiedResourceFileReader resourceFileReader = SimplifiedResourceFileReader.getInstance();

  @Test
  void getByteArrayFromResources() throws Exception {
    Assertions.assertEquals("something",
        IOUtils.toString(
            new ByteArrayInputStream(
                resourceFileReader.getByteArrayFromResources("/folder1/folder2/file2.txt")
            ), StandardCharsets.UTF_8
        )
    );
  }

  @Test
  void getStringFromResources() {
    Assertions.assertEquals("something", resourceFileReader.getStringFromResources("/folder1/folder2/file2.txt"));
    Assertions.assertNull(resourceFileReader.getStringFromResources("/notExisting.404"));
  }

}