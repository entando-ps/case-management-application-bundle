package org.entando.bundle.service.impl;

import org.entando.bundle.CaseServiceApplication;
import org.entando.bundle.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CaseServiceApplication.class)
public class TestFileServiceImpl {

  @Autowired
  public FileService fileService;

  @Test
  public void testFileUpload() {
    final String KEY = "test-file-2677";

    try {
      if (fileService.fileUpload(TXT_CONTENT, KEY, null) == null) {
        throw new RuntimeException("Error upload file");
      }
      List<S3Object> files = fileService.fileList();
      Optional<S3Object> file = files.stream()
        .filter(s -> s.key().equals(KEY))
        .findFirst();
      assertNotNull(file.orElse(null));
    } catch (Throwable t) {
      t.printStackTrace();
      throw t;
    } finally {
      fileService.deleteFile(KEY);
    }
  }


  public final String TXT_CONTENT = "  private byte[] getObjectFile(MultipartFile file) {\n" +
    "    byte[] bytesArray = null;\n" +
    "    InputStream is = null;\n" +
    "\n" +
    "    try {\n" +
    "      is = file.getInputStream();\n" +
    "      bytesArray = new byte[(int) file.getSize()];\n" +
    "      file.getInputStream().read(bytesArray);\n" +
    "    } catch (IOException e) {\n" +
    "      log.error(\"error building byte array of file\", e);\n" +
    "    } finally {\n" +
    "      if (is != null) {\n" +
    "        try {\n" +
    "          is.close();\n" +
    "        } catch (IOException e) {\n" +
    "          e.printStackTrace();\n" +
    "        }\n" +
    "      }\n" +
    "    }\n" +
    "    return bytesArray;\n" +
    "  }";

}
