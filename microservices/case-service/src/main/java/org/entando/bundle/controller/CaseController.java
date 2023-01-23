package org.entando.bundle.controller;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.entity.Process;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.impl.CaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.entando.bundle.BundleConstants.CASE_MANAGEMENT_ADMIN;

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaseController {


    private Logger log = LoggerFactory.getLogger(CaseController.class);

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

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('case-management-admin','case-management-entry')")
    public @ResponseBody List<Process> getProcesses(Authentication authentication, Principal principal) {
        try {
            log.info("REST to get the process list");
            if (isRoleAdmin(authentication.getAuthorities())) {
                return caseService.getAllProcesses();
            } else {
                log.debug("returning process list for the user {}", principal.getName());
                return caseService.getProcessesByName(principal.getName());
            }
        } catch (Throwable t) {
            log.error("error getting the list of processes", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected getting the processes list", t);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin','case-management-entry')")
    public @ResponseBody Object getProcess(Authentication authentication, Principal principal, @PathVariable Long id) {
        try {
            Optional<Process> process;

            log.info("REST to get the process {}", id);
            if (isRoleAdmin(authentication.getAuthorities())) {
                process = caseService.getProcess(id);
            } else {
                log.debug("returning process {} of the user {}", id, principal.getName());
                process = caseService.getProcessByIdAndOwner(id, principal.getName());
            }
            if (!process.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return process;
        } catch (Throwable t) {
            log.error("error getting a processes", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error getting a process", t);
        }
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody Process createCase(Principal principal,
                                            @RequestPart("attachments") MultipartFile[] files,
                                            @Valid @RequestPart("case_metadata") CaseMetadata data) {
        Process process;
        final String name = principal.getName();

        log.info("REST to create a process for user {}", name);
        try {
            process = caseService.createProcess(files, data, name);
        } catch (Throwable t) {
            log.error("error creating a new processes", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error creating process", t);
        }
        return process;
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody boolean deleteCase(@PathVariable Long id) {
        boolean process = false;

        log.info("REST to delete the process ID ", id);
        try {
            process = caseService.destroyProcess(id);
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error deleting process", t);
        }
        return process;
    }


}