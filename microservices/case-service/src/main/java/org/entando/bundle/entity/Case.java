package org.entando.bundle.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Data;
import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.entity.enumeration.State;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity(name = "cases")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Case {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(nullable = false)
  private String processInstanceId;

  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private State state;

  @Column(name = "created", columnDefinition = "TIMESTAMP")
  private LocalDateTime created;

  @Column(nullable = false, length = 20)
  private String identifier;

  @Column(nullable = false, length = 40)
  private String ownerId;

  @Type(type = "json")
  @Column(columnDefinition = "json", nullable = false)
  private CaseMetadata metadata;

}
