package org.entando.bundle;

import com.amazonaws.regions.Regions;
import software.amazon.awssdk.regions.Region;

public interface BundleConstants {

  String S3_BUCKET_NAME = "entando-cdp-poc";
  @Deprecated
  Regions DEFAULT_REGIONS = Regions.EU_WEST_1;
  Region DEFAULT_REGION = Region.EU_WEST_1;

}
