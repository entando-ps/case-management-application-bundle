package org.entando.bundle.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {

  private String key;
  private String url;
  private Long size;

}
