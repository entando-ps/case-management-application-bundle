package org.entando.bundle.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticElement {

  public StatisticElement(LocalDateTime opened, LocalDateTime closed) {
    this.opened = opened;
    this.closed = closed;
  }

  private LocalDateTime opened;
  private LocalDateTime closed;

}
