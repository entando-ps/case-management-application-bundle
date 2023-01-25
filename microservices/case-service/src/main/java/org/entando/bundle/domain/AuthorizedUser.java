package org.entando.bundle.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AuthorizedUser {

  public String name;
  public String lastname;
  public LocalDate birthDate;
  public String birthCountry;
  public String birthCity;
  public String birthProvince;
  public String birthRegion;
  public String fiscalCode;
  public String email;
  public String mobile;
  public String role;

}
