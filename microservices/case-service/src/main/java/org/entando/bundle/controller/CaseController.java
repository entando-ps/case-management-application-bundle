package org.entando.bundle.controller;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Statistics;
import org.entando.bundle.entity.Case;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.impl.CaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.entando.bundle.BundleConstants.CASE_MANAGEMENT_ADMIN;
import static org.entando.bundle.BundleConstants.PROCESS_INSTANCE_VARIABLES_APPROVED;

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaseController {

    private final Logger log = LoggerFactory.getLogger(CaseController.class);

    private final CaseService caseService;

    public CaseController(CaseServiceImpl caseService) {
        this.caseService = caseService;
    }

    /**
     * Check whether the current user is also administrator of cases
     * @param authorities the authorities of the currently logged user
     * @return true if the current user has administration rights
     */
    private boolean isRoleAdmin(Collection<? extends GrantedAuthority> authorities) {
        Optional<? extends GrantedAuthority> auth = authorities.stream().filter(a -> a.getAuthority().equals(CASE_MANAGEMENT_ADMIN)).findFirst();
        return auth.isPresent();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('case-management-admin','case-management-entry')")
    public @ResponseBody List<Case> getCases(Authentication authentication, Principal principal) {
        try {
            log.info("REST to get the case list");
            if (isRoleAdmin(authentication.getAuthorities())) {
                return caseService.getAllCases();
            } else {
                log.debug("returning Case list for the user {}", principal.getName());
                return caseService.getCasesByName(principal.getName());
            }
        } catch (Throwable t) {
            log.error("error getting the list of cases", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected getting the Cases list", t);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin','case-management-entry')")
    public @ResponseBody ResponseEntity<Case> getCase(Authentication authentication, Principal principal, @PathVariable Long id) {
        try {
            Optional<Case> optionalCase;

            log.info("REST to get the Case {}", id);
            if (isRoleAdmin(authentication.getAuthorities())) {
                optionalCase = caseService.getCase(id);
            } else {
                log.debug("returning Case {} of the user {}", id, principal.getName());
                optionalCase = caseService.getCaseByIdAndOwner(id, principal.getName());
            }
            return optionalCase.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Throwable t) {
            log.error("error getting a Cases", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error getting a case", t);
        }
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody Case createCase(Principal principal,
                                         @RequestPart("attachments") MultipartFile[] files,
                                         @Valid @RequestPart("case_metadata") CaseMetadata data) {
        Case aCase;
        final String name = principal.getName();

        log.info("REST to create a case for user {}", name);
        try {
            aCase = caseService.createCase(files, data, name);
        } catch (Throwable t) {
            log.error("error creating a new Cases", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error creating a case", t);
        }
        return aCase;
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody ResponseEntity deleteCase(@PathVariable Long id) {

        log.info("REST to delete the case ID {} ", id);
        try {
            if (caseService.destroyCase(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // logical deletion because some resources could not be deleted. Status = DELETED
                return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
            }
        } catch (Throwable t) {
            log.error("error deleting a case", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error deleting Case", t);
        }
    }

    @PostMapping(value = "/{id}/approve")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody ResponseEntity approveCase(@PathVariable Long id) {
        log.info("REST to approve case {}", id);
        return changeRequestState(id, true);
    }

    @PostMapping(value = "/{id}/reject")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody ResponseEntity rejectCase(@PathVariable Long id) {
        log.info("REST to reject case {}", id);
        return changeRequestState(id, false);
    }

    private ResponseEntity<Object> changeRequestState(final Long id, final boolean approved) {
        try {
            if (caseService.completeTaskState(id, new HashMap<>(Map.of(PROCESS_INSTANCE_VARIABLES_APPROVED, approved)))) {
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Throwable t) {
            log.error("error while updating case to " + (approved ? "approved":"rejected"), t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error updating case to " + (approved ? "approved":"rejected"), t);
        }
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody Statistics getCaseStatistics(@RequestParam(required = false)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            return caseService.getStatisticsRange(from, to);
        } catch (Throwable t) {
            log.error("error while getting usage statistics", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error while getting statistics", t);
        }
    }

    @GetMapping("/generate-fake-data")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody ResponseEntity createCaseStatistics(@RequestParam Integer size) {
        try {
            caseService.createFakeData(size);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Throwable t) {
            log.error("error while getting usage statistics", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error while getting statistics", t);
        }
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody ResponseEntity flush() {
        try {
            caseService.flush();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Throwable t) {
            log.error("error while getting usage statistics", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error while getting statistics", t);
        }
    }
}
