package org.entando.bundle.controller;

import org.entando.bundle.domain.AuthorizedUser;
import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Delegation;
import org.entando.bundle.domain.SubscribedUser;
import org.entando.bundle.entity.Process;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.impl.CaseServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseServiceImpl caseService) {
        this.caseService = caseService;
    }

    @GetMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody List<Process> getProcesses() {
        return caseService.getAllProcesses();
    }

    @GetMapping("/process/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody Optional<Process> getProcess(@PathVariable Long id) {
        Optional<Process> process = caseService.getProcess(id);
        return process;
    }

    @PostMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody Process createProcess(@RequestBody Process process) {
        return caseService.createProcess(process);
    }

    @PostMapping(value = "/start", consumes = {"multipart/form-data"})
    public @ResponseBody Process startProcess(@RequestPart("attachments") MultipartFile[] files,
                                              @Valid @RequestPart("case_metadata") CaseMetadata data) {
        Process process = null;

        try {
            process = caseService.startProcess(files, data);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return process;
    }

}