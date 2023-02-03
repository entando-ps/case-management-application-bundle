package org.entando.bundle.service.impl.utils;

import org.entando.bundle.domain.AuthorizedUser;
import org.entando.bundle.domain.SubscribedUser;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class TestFakerHelper {

  @Test
 public void testMobile() {
   String mob = FakerHelper.getRandomMobile();
   assertThat(mob, is(notNullValue()));
   assertThat(mob.length(), is(13));
 }

 @Test
 public void testGetCity() {
   String city = FakerHelper.getRandomValue(FakerHelper.CITIES);
   assertThat(city, is(notNullValue()));
 }

  @Test
  public void testGetFiscalCode() {
    String cf = FakerHelper.getRandomFiscalCode();
    assertThat(cf, is(notNullValue()));
    assertThat(cf.length(), is(16));
  }

  @Test
  public void testGetAge() {
    Integer age = FakerHelper.getRandomNumRange(54, 22);
    assertThat(age, is(both(greaterThan(21)).and(lessThan(55))));
  }

  @Test
  public void testGetPastDate() {
    LocalDate date = FakerHelper.getRandomPastDate(58, 22);
    int age = (LocalDate.now().getYear() - date.getYear());
    assertThat(age, is(both(greaterThan(21)).and(lessThan(59))));
  }

  @Test
  public void testGetAuthorizedUser() {
    AuthorizedUser user = FakerHelper.getAuthorizedUser();
    assertThat(user, is(notNullValue()));
  }
  @Test
  public void testGetSubscribedUser() {
    SubscribedUser user = FakerHelper.getSubscribedUser();
    assertThat(user, is(notNullValue()));
  }

  @Test
  public void testGetLandline() {
    String phone = FakerHelper.getRandomLandline();
    assertThat(phone, is(notNullValue()));
  }

  @Test
  public void testBoolean() {
    boolean result = FakerHelper.trueFalse();
    assertThat(result, is(notNullValue()));
    assertThat(result, is(instanceOf(Boolean.class)));
  }

}
