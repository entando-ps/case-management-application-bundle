package org.entando.bundle.service.impl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.StringUtils;

import java.io.File;
import java.io.IOException;

import static org.entando.bundle.BundleConstants.PROP_TMP_DIR;

public class FileHelper {

  private static Logger log = LoggerFactory.getLogger(FileHelper.class);


  /**
   * Build a path for the given file in the system temporary path
   * @Deprecated
   * @param fileName the file to be placed in tmp
   * @return the desired path or null
   */
  public static String getTmpPathForFile(String fileName) {
    String path = null;
    
    if (StringUtils.isNotBlank(fileName)) {
      String tempDir = System.getProperty(PROP_TMP_DIR);
      
      path =  tempDir + File.separator + fileName;
    }
    return path;
  }

  /**
   * Build a path for the given file in the system temporary path
   * @Deprecated
   * @param file the multipart file
   * @return the desired path or null
   */
  @Deprecated
  public static String getTmpPathForFile(MultipartFile file) {
    String path = null;

    if (file != null) {
      String tempDir = System.getProperty(PROP_TMP_DIR);

      path =  tempDir + File.separator + file.getOriginalFilename();
    }
    return path;
  }
  
  /**
   * Write a multipart file to a temporary directory preserving the original name
   * @Deprecated
   * @param file multipart file
   * @return the newly created File handle, null otherwise
   */
  @Deprecated
  public static File toFile(MultipartFile file) {
    if (file != null) {
      try {
        File tmpFile = new File(getTmpPathForFile(file));
        if (tmpFile.exists()) {
          if (!tmpFile.delete()) {
              log.error("Could not delete existing file {}", tmpFile.getCanonicalFile());
          }
        }
        file.transferTo(tmpFile);
        log.debug(tmpFile.getAbsolutePath() + " written successfully");
        return tmpFile;
      } catch (IOException e) {
        log.error("Error saving {} file in temp", file.getOriginalFilename());
      }
    }
    return null;
  }

}
