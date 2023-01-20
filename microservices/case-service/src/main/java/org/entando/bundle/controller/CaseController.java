package org.entando.bundle.controller;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.entity.Process;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.impl.CaseServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseServiceImpl caseService) {
        this.caseService = caseService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody List<Process> getProcesses() {
        return caseService.getAllProcesses();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody Optional<Process> getProcess(@PathVariable Long id) {
        Optional<Process> process = caseService.getProcess(id);
        return process;
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody Process createCase(@RequestPart("attachments") MultipartFile[] files,
                                              @Valid @RequestPart("case_metadata") CaseMetadata data) {
        // TODO ERROR MANAGEMENT
        Process process = null;
        try {
            process = caseService.createProcess(files, data);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return process;
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody boolean createDelete(@PathVariable Long id) {
        boolean process = false;
        // TODO ERROR MANAGEMENT
        try {
            process = caseService.destroyProcess(id);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return process;
    }


}