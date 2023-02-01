package org.entando.bundle.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics {

  @JsonProperty("by_status")
  private StatisticElement byStatus;

  @JsonProperty("by_year")
  private Map<Integer, StatisticElement> byYear;

  /**
   * Return the statistic element of the desired yesr
   * @param year the year of the statistics
   * @return the statistic elements of the desired year
   */
  public StatisticElement getYear(int year) {
    if (byYear == null) {
        byYear = new HashMap<>();
    }
    if (!byYear.containsKey(year)) {
      StatisticElement elem = new StatisticElement();

      byYear.put(year, elem);
      return elem;
    }
    return byYear.get(year);
  }

}
