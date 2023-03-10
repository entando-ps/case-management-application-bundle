package org.entando.bundle.service.impl;

import org.entando.bundle.repository.CaseRepository;
import org.entando.bundle.service.CaseIdentityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CaseIdentityServiceImpl implements CaseIdentityService {

  private final CaseRepository caseRepository;

  public CaseIdentityServiceImpl(CaseRepository caseRepository) {
    this.caseRepository = caseRepository;
  }

  @Override
  public String generateIdentifier() {
    final Optional<Long> progressive = caseRepository.getNextId();
    final LocalDate localDate = LocalDate.now();
    final String year = String.valueOf(localDate.getYear()).substring(2, 4);
    final Long number = progressive.map(num -> num + 1L).orElse(1L);
    return String.format("%07d", number) + "/" + year;
  }

}
