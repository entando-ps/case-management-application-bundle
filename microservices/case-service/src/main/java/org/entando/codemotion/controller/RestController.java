package org.entando.codemotion.controller;

import org.entando.codemotion.entity.Process;
import org.entando.codemotion.entity.enumeration.State;
import org.entando.codemotion.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestController {

    private final ProcessService processService;

    public RestController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody List<Process> getProcesses() {
        return processService.getAllProcesses();
    }

    @GetMapping("/process/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody Optional<Process> getProcess(@PathVariable Long id) {
        Optional<Process> process = processService.getProcess(id);
        return process;
    }

    @PostMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody Process createProcess(@RequestBody Process process) {
        return processService.crateProcess(process);
    }

}