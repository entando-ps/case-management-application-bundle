package org.entando.bundle.service.impl.utils;

import lombok.Data;
import org.entando.bundle.domain.AuthorizedUser;
import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Delegation;
import org.entando.bundle.domain.SubscribedUser;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.IntStream;

@Data
public class FakerHelper {

  /**
   * Generate a unique authorized user
   * @return the new user data
   */
  public static AuthorizedUser getAuthorizedUser() {
    AuthorizedUser authUser = new AuthorizedUser();

    authUser.setName(getRandomValue(NAMES));
    authUser.setLastname(getRandomValue(LASTNAMES));
    authUser.setBirthDate(getRandomPastDate(58, 22));
    authUser.setBirthCountry("Italy");
    authUser.setBirthCity(getRandomValue(CITIES));
    authUser.setBirthProvince(getRandomValue(PROV));
    authUser.setBirthRegion(getRandomValue(REGIONS));
    authUser.setFiscalCode(getRandomFiscalCode());
    String email = authUser.getName() + "." + authUser.getLastname().substring(0, 3) + getRandomValue(EMAILS);
    authUser.setEmail(email.toLowerCase());
    authUser.setMobile(getRandomMobile());
    authUser.setRole(getRandomValue(ROLES));
    return authUser;
  }

  /**
   * Generate a unique subscriber user
   * @return the new user data
   */
  public static SubscribedUser getSubscribedUser() {
    SubscribedUser subUser = new SubscribedUser();

    subUser.setName(getRandomValue(NAMES));
    subUser.setLastname(getRandomValue(LASTNAMES));
    subUser.setBirthDate(getRandomPastDate(58, 22));
    subUser.setBirthCountry("Italy");
    subUser.setBirthCity(getRandomValue(CITIES));
    subUser.setBirthProvince(getRandomValue(PROV));
    subUser.setBirthRegion(getRandomValue(REGIONS));
    subUser.setFiscalCode(getRandomFiscalCode());
    String email = subUser.getName() + "." + subUser.getLastname().substring(0, 3) + getRandomValue(EMAILS);
    subUser.setEmail(email.toLowerCase());
    subUser.setLandline(getRandomLandline());
    subUser.setMobile(getRandomMobile());
    subUser.setSector(getRandomValue(SECTORS));

    int i = getRandomNumRange(9, 0);
    if (i <= 4 ) {
      subUser.setDelegation(Delegation.PROCURATORE);
    } else {
      subUser.setDelegation(Delegation.LEGALE_RAPPRESENTANTE);
    }
    return subUser;
  }

  /**
   * Generate random payload
   * @return generated caseMetadata object
   */
  public static CaseMetadata getRandomMetadata(){
    CaseMetadata caseMetadata = new CaseMetadata();

    caseMetadata.setSubscriber(getSubscribedUser());
    caseMetadata.setAuthorized(getAuthorizedUser());
    return caseMetadata;
  }

  /**
   * Return a random element from the given set
   * @param data input array with the value to chose
   * @return a random value from the set
   */
  public static String getRandomValue(String[] data) {
    Random random = new Random();
    int idx = random.nextInt(data.length);
    return data[idx];
  }

  /**
   * Generate a mobile number
   * @return random mobile
   */
  public static String getRandomMobile() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder("+39");
    IntStream.range(0, 10).forEach(n -> sb.append(random.nextInt(10)));
    return sb.toString();
  }

  /**
   * Generate a ransdom landline; the prefix are real ones
   * @return a landline likely number
   */
  public static String getRandomLandline() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder("+39");
    int upperLim = getRandomNumRange(5, 3);
    String prefix = getRandomValue(PHONES);

    while (prefix.length() + upperLim < 9) upperLim++;
    sb.append(prefix);
    IntStream.range(0, upperLim).forEach(n -> getRandomNum(sb, random));
    return sb.toString();
  }

  /**
   * Generate a fiscal code
   * @return the Italian like fiscal code
   */
  public static String getRandomFiscalCode() {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();

    IntStream.range(0, 6).forEach(n -> getRandomChar(sb, random));
    IntStream.range(0, 2).forEach(n -> getRandomNum(sb, random));
    getRandomChar(sb, random);
    IntStream.range(0, 2).forEach(n -> getRandomNum(sb, random));
    getRandomChar(sb, random);
    IntStream.range(0, 3).forEach(n -> getRandomNum(sb, random));
    getRandomChar(sb, random);
    return sb.toString();
  }

  /**
   * Pick a random char
   * @param sb the buffer where to append the character
   * @param random the seed generator
   */
  private static void getRandomChar(StringBuilder sb, Random random) {
    char curChar = (char) (random.nextInt(26) + 'A');
    sb.append(curChar);
  }

  /**
   * Pick a random number
   * @param sb the buffer where to append the digit
   * @param random the seed generator
   */
  private static void getRandomNum(StringBuilder sb, Random random) {
    char curChar = (char) (random.nextInt(10) + '0');
    sb.append(curChar);
  }

  /**
   * Get a random number in the desired range
   * @param max upper limit
   * @param min lower limit
   * @return a random number
   */
  public static int getRandomNumRange(int max, int min) {
    Random random = new Random();
    return random.nextInt(max - min) + min;
  }

  /**
   * Generate a random birthdate for a person
   * @param max upper age limit
   * @param min lower age limit
   * @return a birthdate
   */
  public static LocalDate getRandomPastDate(int max, int min) {
    LocalDate now = LocalDate.now();
    int birthYear = now.getYear() - getRandomNumRange(max, min);
    return LocalDate.ofYearDay(birthYear, getRandomNumRange(365, 1));
  }

  /**
   * Return either a true or a false
   * @return a random boolean
   */
  public static boolean trueFalse() {
    return Boolean.parseBoolean(getRandomValue(new String[] {"TRUE","FALSE"}));
  }

  public final static String[] CITIES = {"Cagliari", "Carbonia", "Roma", "Sassari", "Milano", "Nuoro", "Olbia"};
  public final static String[] PROV = {"CA", "OB", "NU", "RO", "MI"};
  public final static String[] REGIONS = {"Sardegna", "Lazio", "Lombardia"};
  public final static String[] NAMES = {"Matteo", "Emanuele", "Davide", "Simone", "Eugenio", "Alessio", "Alessandro", "Sergio", "Sara", "Giulia", "Federica", "Roberta", "Sabrina", "Renato", "Lucia", "Maria", "Benedetta", "Franco", "Ettore"};
  public final static String[] LASTNAMES = {"Rossi", "Verdi", "Bianchi", "Marrone", "Giusti", "Puddu"};
  public final static String[] EMAILS = {"@cdp.org", "@gmail.it", "@amail.it", "@personal.edu", "@macrohard.it"};
  public final static String[] ROLES = {"Parte del contratto", "AMM.RE", "Supervisore", "Supporto"};
  public final static String[] SECTORS = {"Pubblica Amministrazione", "Immobiliare", "Finanziario", "Bancario", "Educazione", "Altro", "Commercio"};
  public final static String[] PHONES =  {"0781", "070", "06", "02", "0586", "089", "0362", "071", "0721"};

}
