package org.entando.bundle.entity;

import lombok.Data;
import org.entando.bundle.entity.enumeration.State;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "processes")
@Data
public class Process {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(nullable = false)
  private Long pid;

  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private State state;

  @Column(name = "created", columnDefinition = "TIMESTAMP")
  private LocalDateTime created;

  @Column(nullable = false)
  private String identifier;

  @Column(nullable = false)
  private String data;

}
