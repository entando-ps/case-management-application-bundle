package org.entando.bundle.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CaseMetadata {

  private AuthorizedUser authorized;
  private SubscribedUser subscriber;
  private List<Resource> resources;
  private Map<String, Object> processData;

}
