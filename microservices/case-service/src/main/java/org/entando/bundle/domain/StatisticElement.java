package org.entando.bundle.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticElement {

 private Integer approved;
 private Integer open;
 private Integer rejected;

 public int addApproved() {
  if (approved == null) {
   approved = 0;
  }
  return ++approved;
 }

 public int addOpen() {
  if (open == null) {
   open = 0;
  }
  return ++open;
 }

 public int addRejected() {
  if (rejected == null) {
   rejected = 0;
  }
  return ++rejected;
 }

}
