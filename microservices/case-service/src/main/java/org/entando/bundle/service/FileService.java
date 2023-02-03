package org.entando.bundle.service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileService {

  /**
   * Upload a file with the given tags
   * @param file the multipart file to upload
   * @param metadata the S3 metadata
   * @return the eTag of the file uploaded
   */
  String fileUpload(MultipartFile file, Map<String, String> metadata);

  /**
   * Upload the give file
   * @Deprecated
   * @param file file handler
   * @param metadata the attributes of the file
   * @return the eTag of the uploaded object
   */
  String fileUpload(File file, Map<String, String> metadata);

  /**
   * Get the file list in the bucket
   * @return the list of files
   */
  List<S3Object> fileList();

  /**
   * Save a file locally from S3
   * @param keyName the key of the file
   * @return the downloaded file
   */
  File downloadFile(String keyName);

  /**
   * Delete the given file from S3
   * @param keyName the key of the file to delete
   * @return true if the file was deleted, false otherwise
   */
  boolean deleteFile(String keyName);

  /**
   * Delete multiple file from S3
   * @param objectIdentifiers the list of the identifiers of the resources to delete
   * @return true if the file was deleted, false otherwise
   */
  boolean deleteFile(List<ObjectIdentifier> objectIdentifiers);

  /**
   * Get the public URL of the file in S3.
   * @param keyName the key of the resource
   * @return the public URL if the file exists, null otherwise
   */
  String getFilePublicUrl(String keyName);

  /**
   * Get the public URL of the file in S3 without checking for effective existence.
   * @param keyName the key of the resource
   * @return the presumed public URL of the file
   */
  String getFilePublicUrlNoCheck(String keyName);

  /**
   * Upload a resource in S3
   * @param resourceToUpload the resource to upload
   * @param key the file key
   * @param props the tag attribute associated to the resource
   * @return the key of the uploaded file
   */
  String fileUpload(String resourceToUpload, String key, HashMap<String, String> props);
}
