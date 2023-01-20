package org.entando.bundle.service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileService {

  String fileUpload(MultipartFile file, Map<String, String> metadata);

  String fileUpload(File file, Map<String, String> metadata);

  List<S3Object> fileList();

  File downloadFile(String keyName);

  boolean deleteFile(String keyName);

  boolean deleteFile(List<ObjectIdentifier> keys);

  String getFilePublicUrl(String keyName);

  String getFilePublicUrlNoCheck(String keyName);
}
