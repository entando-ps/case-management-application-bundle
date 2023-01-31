package org.entando.bundle.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics {

  private List<StatisticElement> open;
  private List<StatisticElement> rejected;
  private List<StatisticElement> approved;
  private List<StatisticElement> faulty;
  private Period period;

  public void addOpen(StatisticElement elem) {
    if (open == null) {
      open = new ArrayList<>();
    }
    open.add(elem);
  }

  public void addRejected(StatisticElement elem) {
    if (rejected == null) {
      rejected = new ArrayList<>();
    }
    rejected.add(elem);
  }

  public void addApproved(StatisticElement elem) {
    if (approved == null) {
      approved = new ArrayList<>();
    }
    approved.add(elem);
  }

  public void addFaulty(StatisticElement elem) {
    if (faulty == null) {
      faulty = new ArrayList<>();
    }
    faulty.add(elem);
  }

  public Statistics setPeriod(LocalDate from, LocalDate to) {
    if (from !=null && to != null) {
      this.period = Period.between(from, to);
    }
    return this;
  }

}
