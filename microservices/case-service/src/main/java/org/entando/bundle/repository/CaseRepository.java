package org.entando.bundle.repository;

import org.entando.bundle.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CaseRepository extends JpaRepository<Process, Long> {

  @Query(
    value = "select MAX(id) from processes",
    nativeQuery = true
  )
  Optional<Long> getNextId();

}
