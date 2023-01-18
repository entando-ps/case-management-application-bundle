package org.entando.bundle.service.impl;

import org.entando.bundle.BundleConstants;
import org.entando.bundle.service.FileService;
import org.entando.bundle.service.impl.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

  private Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

  private S3Client getClient() {
    // Create the S3Client object.
    Region region = Region.EU_WEST_1;
    S3Client s3 = S3Client.builder()
      .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
      .region(region)
      .build();

    return s3;
  }

  @Override
  public String fileUpload(File file, Map<String, String> metadata) {
    S3Client s3 = getClient();

    try {
//      File file = new File("/home/matteo/Pictures/WTF.png");
      // Set the tags to apply to the object.
//      Map<String, String> metadata = new HashMap<>();
//      metadata.put("x-amz-meta-myVal", "test");
      PutObjectRequest putOb = PutObjectRequest.builder()
        .bucket(BundleConstants.S3_BUCKET_NAME)
        .key(file.getName())
        .metadata(metadata)
        .build();
      PutObjectResponse response = s3.putObject(putOb, RequestBody.fromBytes(getObjectFile(file.getAbsolutePath())));
      return response.eTag();
    } catch (S3Exception t) {
      log.error(t.awsErrorDetails().errorMessage());
      t.printStackTrace();
    }
    return null;
  }

  private static byte[] getObjectFile(String filePath) {
    FileInputStream fileInputStream = null;
    byte[] bytesArray = null;

    try {
      File file = new File(filePath);
      bytesArray = new byte[(int) file.length()];
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytesArray);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return bytesArray;
  }

  @Override
  public List<S3Object> fileList() {
    S3Client s3 = getClient();

    try {
      ListObjectsRequest listObjects = ListObjectsRequest
        .builder()
        .bucket(BundleConstants.S3_BUCKET_NAME)
        .build();

      ListObjectsResponse res = s3.listObjects(listObjects);
      log.debug("File list successfully downloaded from bucket");
      return res.contents();
    } catch (S3Exception t) {
      log.error("Error getting the file list of a bucket", t.awsErrorDetails().errorMessage());
    }
    return null;
  }

  @Override
  public File downloadFile(String keyName) {
    S3Client s3 = getClient();
    File dest = null;

    try {
      GetObjectRequest objectRequest = GetObjectRequest
        .builder()
        .key(keyName)
        .bucket(BundleConstants.S3_BUCKET_NAME)
        .build();

      ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
      byte[] data = objectBytes.asByteArray();

      // Write the data to a local file.
      dest = new File(FileHelper.getTmpPathForFile(keyName));
      try (OutputStream os = new FileOutputStream(dest);) {
        os.write(data);
        log.debug("{} successfully downloaded from bucket", dest.getAbsolutePath());
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (S3Exception t) {
      log.error("Error getting the file list of a bucket", t.awsErrorDetails().errorMessage());
    }
    return dest;
  }

  @Override
  public boolean deleteFile(String keyName) {
    ObjectIdentifier objectId = ObjectIdentifier.builder()
      .key(keyName)
      .build();
    return deleteFile(Arrays.asList(new ObjectIdentifier[] {objectId}));
  }

  public boolean deleteFile(List<ObjectIdentifier> keys) {
    S3Client s3 = getClient();
    boolean deleted = true;
    DeleteObjectsResponse response = null;

    log.debug("{} element(s) to delete", keys.size());
    try {
      Delete del = Delete.builder()
        .objects(keys)
        .build();
      DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
        .bucket(BundleConstants.S3_BUCKET_NAME)
        .delete(del)
        .build();

      response = s3.deleteObjects(multiObjectDeleteRequest);
      log.debug("Multiple objects have been deleted from bucket");
    } catch (S3Exception e) {
      log.error(e.awsErrorDetails().errorMessage());
      deleted = false;
    }
    log.debug("delete overall result: {}\n {}", response.hasErrors(), response);
    log.debug(" has deleted? {}", response.hasDeleted());
    return (response != null && !response.hasErrors());
  }


}
