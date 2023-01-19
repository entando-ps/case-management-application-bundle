package org.entando.bundle.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CaseUser {

  private AuthorizedUser authorized;
  private SubscribedUser subscriber;

}
