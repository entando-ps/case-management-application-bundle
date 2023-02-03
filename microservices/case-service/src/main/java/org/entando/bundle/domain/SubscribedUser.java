package org.entando.bundle.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Data
@NoArgsConstructor
public class SubscribedUser {

  public String name;
  public String lastname;
  public LocalDate birthDate;
  public String birthCountry;
  public String birthCity;
  public String birthProvince;
  public String birthRegion;
  public String fiscalCode;
  public String email;
  public String landline;
  public String mobile;
  public String sector;
  public Delegation delegation;

}
