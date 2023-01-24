package org.entando.bundle.repository;

import org.entando.bundle.entity.Case;
import org.entando.bundle.entity.enumeration.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CaseRepository extends JpaRepository<Case, Long> {

  @Query(
    value = "select MAX(id) from cases",
    nativeQuery = true
  )
  Optional<Long> getNextId();

  @Modifying
  @Query(value = "update cases a set a.state = :status where a.id = :id")
  void updateState(@Param(value = "id") Long id, @Param(value = "status") State status);

  List<Case> findByOwnerId(String name);

  Optional<Case> findByIdAndOwnerIdIs(Long id, String ownerId);

}
