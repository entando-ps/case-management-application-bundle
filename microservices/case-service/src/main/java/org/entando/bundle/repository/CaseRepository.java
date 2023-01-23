package org.entando.bundle.repository;

import org.entando.bundle.entity.Process;
import org.entando.bundle.entity.enumeration.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CaseRepository extends JpaRepository<Process, Long> {

  @Query(
    value = "select MAX(id) from processes",
    nativeQuery = true
  )
  Optional<Long> getNextId();

  @Modifying
  @Query(value = "update processes a set a.state = :status where a.id = :id")
  void updateState(@Param(value = "id") Long id, @Param(value = "status") State status);

  List<Process> findByNameIs(String name);

  Optional<Process> findByIdAndNameIs(Long id, String name);

}
