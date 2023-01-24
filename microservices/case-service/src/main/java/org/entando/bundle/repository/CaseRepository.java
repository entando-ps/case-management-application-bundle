package org.entando.bundle.repository;

import org.entando.bundle.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<Process, Long> {

}
