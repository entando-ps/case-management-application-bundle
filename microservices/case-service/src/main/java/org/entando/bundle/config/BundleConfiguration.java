package org.entando.bundle.config;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "aws")
public class BundleConfiguration {

  private S3 s3;

  @Data
  @NoArgsConstructor
  public static class Bucket {
    private String name;
  }

  @NoArgsConstructor
  @Data
  public static class  S3 {
    private String secret;
    private String key;
    private Bucket bucket;
    private String region;
  }

}
