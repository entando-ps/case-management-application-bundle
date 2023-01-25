package org.entando.bundle.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CaseMetadata {

  private AuthorizedUser authorized;
  private SubscribedUser subscriber;
  private List<Resource> resources;

}
